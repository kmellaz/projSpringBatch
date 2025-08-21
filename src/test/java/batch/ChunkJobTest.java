package batch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;



//(classes = BatchApplication.class)
@SpringBootTest
@Disabled
class ChunkJobTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    //private Job chunkJobWriteConsole;
    private Job chunkJobWriteFile;

    @Test
    void testJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("outputFile", "data/ouput/file.csv")
                .toJobParameters();

        JobExecution execution = jobLauncher.run(chunkJobWriteFile, params);

        assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
    }
}
