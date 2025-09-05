package batch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Disabled
public class JobLectureCommandesMultiRessourceTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importCommandesJob;

    @Test
    void testJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                //.addString("outputFile", "data/ouput/file.csv")
                //.addString("inputFile", "file:data/input/commande-part-*.csv")
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importCommandesJob, params);

        System.out.println(">>>>>>>> fin de test OK : " + execution.getExitStatus());
        //assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
    }
}
