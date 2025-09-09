package batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/jobs")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job chunkJobSaiseCommandes;
    private final Job chunkJobWriteFile;
    private final Job splitFileAndImportJob;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    @Value("${input.file}")
    private String inputFile;

    @Value("${partition.dir}")
    private String partitionDir;

    @Value("${linesPerFile}")
    private Long linesPerFile;



    @Autowired
    public BatchController(JobLauncher jobLauncher, Job chunkJobSaiseCommandes, Job chunkJobWriteFile, Job splitFileAndImportJob) {
        this.jobLauncher = jobLauncher;
        this.chunkJobSaiseCommandes = chunkJobSaiseCommandes;
        this.chunkJobWriteFile = chunkJobWriteFile;
        this.splitFileAndImportJob = splitFileAndImportJob;
    }

    @GetMapping("/public/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @GetMapping("/jobReportCommandes/start")
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

    @GetMapping("/jobSplitFileAndImport/start")
    ResponseEntity<String> lancerJobSplitFileAndImport(){
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DATE_TIME_FORMATTER);
        try {
        jobLauncher.run(
                this.splitFileAndImportJob,
                new JobParametersBuilder()
                        .addString("time", nowString)
                        .addString("inputFile", this.inputFile)
                        .addString("partitionDir", this.partitionDir)
                        .addLong("linesPerFile", this.linesPerFile)
                        .toJobParameters()

        );

        return ResponseEntity.status(HttpStatus.OK).body("Job split and import file lancé avec succés");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erreur lors du lancement du job split and import file : " + e.getMessage());
    }
    }


}
