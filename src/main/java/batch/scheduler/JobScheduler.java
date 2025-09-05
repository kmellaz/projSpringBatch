package batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
//@EnableScheduling
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job chunkJobSaiseCommandes;
    private final Job chunkJobWriteFile;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    @Autowired
    public JobScheduler(JobLauncher jobLauncher, Job chunkJobSaiseCommandes, Job chunkJobWriteFile) {
        this.jobLauncher = jobLauncher;
        this.chunkJobSaiseCommandes = chunkJobSaiseCommandes;
        this.chunkJobWriteFile = chunkJobWriteFile;
    }

    // Planifie job tous les jours à 18h
    @Scheduled(cron = "0 0 18 * * *")
    public void runJobSaiseCommandes() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("time", LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .toJobParameters();

        jobLauncher.run(this.chunkJobSaiseCommandes, params);
    }

    // Planifie job tous les jours à 18h15mn
    //@Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 15 18 * * *")
    public void runJobExtraction() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DATE_TIME_FORMATTER);

        jobLauncher.run(
                this.chunkJobWriteFile,
                new JobParametersBuilder()
                        .addString("time", nowString)
                        .addString("outputFile", "data/ouput/file_" + nowString + ".csv")
                        .toJobParameters()
        );
    }



}

