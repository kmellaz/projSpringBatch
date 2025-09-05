package batch;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@SpringBatchTest
/*
@TestPropertySource(properties = {
        "input.file=src/test/resources/input-test.csv",
        "partition.dir=src/test/resources/partitions"
})
*/

public class SplitFileAndImportJobTest {

    private static final String INPUT_FILE = "src/test/resources/input-test.csv";
    private static final String PARTITION_DIR = "src/test/resources/partitions";
    private static final Long LINES_PER_FILE = 1_000_000L;


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importJob;

    @BeforeEach
    void setUp() throws IOException {
        // vider le repertoire des partitions
        //cleanup();
    }


    private void cleanup() throws IOException {
        Files.walk(Paths.get(PARTITION_DIR))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        System.out.println(">>>> delete fichiers partitions OK");
        //Files.deleteIfExists(Paths.get(INPUT_FILE));
    }

    @Test
    void testJobSplitFileAndImport() throws Exception{
        JobParameters params = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("inputFile", INPUT_FILE)
                .addString("partitionDir", PARTITION_DIR)
                .addLong("linesPerFile", LINES_PER_FILE)
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importJob, params);

        assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
    }

}
