package batch.job;

import batch.component.CountingItemWriter;
import batch.component.SplitFileTasklet;
import batch.dto.LigneCommandeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class JobSplitFileAndImport {
    private static final Logger log = LogManager.getLogger(JobSplitFileAndImport.class);

    /*@Value("${input.file}")
    private String inputFile;

    @Value("${partition.dir}")
    private String partitionDir;
*/
    @Bean
    public Job importJob(Step splitFileStep, Step masterStep, JobRepository jobRepository) {
                return new JobBuilder("importJob", jobRepository)
                .start(splitFileStep)
                .next(masterStep)
                .build();
    }

    @Bean
    public Step splitFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              Tasklet splitFileTasklet) throws IOException {
         Step splitStep = new StepBuilder("splitFileStep", jobRepository)
                .tasklet(splitFileTasklet, transactionManager)
                .build();
        return splitStep;
    }


    @Bean
    @StepScope
    public MultiResourcePartitioner partitioner(@Value("#{jobParameters['inputFile']}") String inputFile,
                                                @Value("#{jobParameters['partitionDir']}") String  partitionDir) throws Exception {

        log.info(">>>>> Partitionner ressources démarre, fichiers visibles : ");
        Files.list(Paths.get(partitionDir)).sorted().forEach(log::info);

        //System.out.println(">>>>> 📂 Partitionner ressources démarre, fichiers visibles : ");
        //Files.list(Paths.get(partitionDir)).sorted().forEach(System.out::println);

        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("file:" + partitionDir + "/*.csv");
        partitioner.setResources(resources);

        return partitioner;
    }



    @Bean
    public Step masterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           MultiResourcePartitioner partitioner,
                           Step slaveStep,
                           TaskExecutor taskExecutor) {

        return new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", partitioner)
                .step(slaveStep)
                .gridSize(10) // définit combien de partitions sont distribuées en parallèle. Il devrait être égal ou inférieur à corePoolSize pour éviter des attentes inutiles.
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2*5); // // Nombre de threads de base (optimum : 2 * nombre de coeur)
        executor.setMaxPoolSize(20); // Nombre de threads max (optimum : 2 * corePoolSize)
        //executor.setQueueCapacity(0);  // pas de file d’attente : exécution directe
        executor.setWaitForTasksToCompleteOnShutdown(true);  // pour attendre la fin des tâches
        executor.setThreadNamePrefix("core-worker-");
        executor.initialize();
        return executor;
    }


    @Bean
    public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          FlatFileItemReader<LigneCommandeDTO> readerFile,
                          CountingItemWriter countingItemWriter) {

        return new StepBuilder("slaveStep", jobRepository)
                .<LigneCommandeDTO, LigneCommandeDTO>chunk(10_000, transactionManager)
                .reader(readerFile)
                .writer(countingItemWriter)
                .build();
    }



    @Bean
    @StepScope
    public FlatFileItemReader<LigneCommandeDTO> readerFile(@Value("#{stepExecutionContext['fileName']}") Resource resource) {
        //System.out.println(">>>>>>>>>>> Lecture du fichier : " + resource.getFilename());
        log.info(">>>>> readerFile > Lecture du fichier : " + resource.getFilename());
        FlatFileItemReader<LigneCommandeDTO> reader = new FlatFileItemReader<>();
        reader.setResource(resource); // PAS besoin de FileSystemResource
        reader.setLinesToSkip(1); // si tu as un header
        reader.setLineMapper(lineMapper());
        return reader;
    }


    @Bean
    public LineMapper<LigneCommandeDTO> lineMapper() {
        DefaultLineMapper<LigneCommandeDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(";");
        tokenizer.setNames("client", "email", "produit",
                "quantite", "prixUnitaire",
                "prixTotal" ,"dateCommande");

        BeanWrapperFieldSetMapper<LigneCommandeDTO> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(LigneCommandeDTO.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);


        return lineMapper;
    }



}
