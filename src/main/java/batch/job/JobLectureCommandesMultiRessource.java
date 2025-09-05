/*
package batch.job;

import batch.dto.LigneCommandeDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
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

import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class JobLectureCommandesMultiRessource {

    @Bean
    public MultiResourcePartitioner partitioner() {
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("file:data/input/commande-part-*.csv");

            // 🔧 injecte les Resource directement
            partitioner.setResources(resources);

            for(Resource ressource : resources){
                System.out.println("file name ressource : " + ressource.getFilename());
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement des fichiers", e);
        }
        return partitioner;
    }





    @Bean
    @StepScope
    public FlatFileItemReader<LigneCommandeDTO> reader(
            @Value("#{stepExecutionContext['fileName']}") Resource resource
    ) {
        System.out.println(">>>>>>>>>>> Lecture du fichier : " + resource.getFilename());

        FlatFileItemReader<LigneCommandeDTO> reader = new FlatFileItemReader<>();
        reader.setResource(resource); // 🔧 PAS besoin de FileSystemResource

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

    @Bean
    public ItemWriter<LigneCommandeDTO> writer() {
        return items -> {
            System.out.println(">>>>>>> Writer reçu " + items.size() + " éléments");


        };



        //return items -> items.forEach(ligneCommande -> System.out.println(Thread.currentThread().getName() + " -> " + ligneCommande));
        //return items -> items.forEach(ligneCommande -> System.out.println(" -> " + ligneCommande.toString()));
    }


    @Bean
    public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          //ItemReader<LigneCommande> reader,
                          FlatFileItemReader<LigneCommandeDTO> reader,
                          ItemWriter<LigneCommandeDTO> writer) {

        return new StepBuilder("slaveStep", jobRepository)
                .<LigneCommandeDTO, LigneCommandeDTO>chunk(100, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Step masterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           MultiResourcePartitioner partitioner,
                           Step slaveStep,
                           TaskExecutor taskExecutor) {

        return new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", partitioner)
                .step(slaveStep)
                .gridSize(4) // 4 fichiers = 4 partitions
                .taskExecutor(taskExecutor)
                .build();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // un thread par fichier
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("csv-worker-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Job importCommandesJob(Step masterStep, JobRepository jobRepository) {
        return new JobBuilder("importCommandesJob", jobRepository)
                .start(masterStep)
                .build();
    }


}
*/
