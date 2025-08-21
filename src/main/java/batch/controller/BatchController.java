package batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/spring-batch-api")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job chunkJobSaiseCommandes;
    private final Job chunkJobWriteFile;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    @Autowired
    public BatchController(JobLauncher jobLauncher, Job chunkJobSaiseCommandes, Job chunkJobWriteFile) {
        this.jobLauncher = jobLauncher;
        this.chunkJobSaiseCommandes = chunkJobSaiseCommandes;
        this.chunkJobWriteFile = chunkJobWriteFile;
    }

    @GetMapping("/public/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @GetMapping("/jobReportCommandes")
    ResponseEntity<String> lancerJobReportCommandes(){
        try {
            LocalDateTime now = LocalDateTime.now();
            String nowString = now.format(DATE_TIME_FORMATTER);

            jobLauncher.run(
                    this.chunkJobWriteFile,
                    new JobParametersBuilder()
                            .addString("time", nowString)
                            .addString("outputFile", "data/ouput/file_" + nowString + ".csv")
                            .toJobParameters()
            );

            return ResponseEntity.status(HttpStatus.OK).body("Job report commandes lancé avec succés");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors du lancement du job report commandes: " + e.getMessage());
        }

    }


}
