package batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.support.TaskExecutorRepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ChunkJobConfig {

    @Bean(name = "chunkJobSaiseCommandes")
    public Job job1(Step chunkStepSaiseCommandes, JobRepository jobRepository) {
        return new JobBuilder("chunkJobSaiseCommandes", jobRepository)
                .start(chunkStepSaiseCommandes)
                .build();
    }


    @Bean(name = "chunkJobWriteFile")
    public Job job2(Step chunkStepWriteFile, JobRepository jobRepository) {
        return new JobBuilder("chunkJobWriteFile", jobRepository)
                .start(chunkStepWriteFile)
                .build();
    }

    @Bean(name="chunkStepSaiseCommandes")
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      Tasklet saisieCommandesClientTasklet) {
        return new StepBuilder("chunkStepSaiseCommandes", jobRepository)
                .tasklet(saisieCommandesClientTasklet, transactionManager)
                .build();
    }

    @Bean(name="chunkStepWriteFile")
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      ItemReader ligneCommandeItemReader,
                      ItemProcessor ligneCommandeItemProcessor,
                      ItemWriter ligneCommandeItemWriter) {
        return new StepBuilder("chunkStepWriteFile", jobRepository)
                .<String, String>chunk(100, transactionManager)
                .reader(ligneCommandeItemReader)
                .processor(ligneCommandeItemProcessor)
                .writer(ligneCommandeItemWriter)
                //.taskExecutor(new SimpleAsyncTaskExecutor()) //paralleliser l'execution des chunk
                //.throttleLimit(3)
                .build();
    }

  /*  @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        ItemReader<String> itemReader = new ListItemReader<>(List.of("item1", "item2", "item3"));
        ItemProcessor<String, String> itemProcessor = str ->  str.toUpperCase();
        ItemWriter<String> itemWriter = items -> items.forEach(System.out::println);

        return new StepBuilder("chunkStep", jobRepository)
                .<String, String>chunk(2, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .build();
                .writer(itemWriter)
    }*/
}
