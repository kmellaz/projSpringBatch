package batch;

import org.junit.jupiter.api.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@SpringBatchTest
//@ContextConfiguration(classes = { JobSplitFileAndImport.class })
public class SplitFileStepTest {

    //@Autowired
    //private Job importJob;

    @Autowired
    private Step splitFileStep;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    private static final String INPUT_FILE = "src/test/resources/input-test.csv";
    private static final String PARTITION_DIR = "src/test/resources/partitions";
    private static final Long LINES_PER_FILE = 1_000_000L;

    @BeforeEach
    void setUp() throws IOException {
        // Crée un job temporaire avec ta step
        Job job = new JobBuilder("testSplitFileStep", jobRepository)
                .start(splitFileStep)
                .build();


        jobLauncherTestUtils.setJob(job);

        // vider le repertoire des partitions
        //cleanup();
    }

    /*@BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get(PARTITION_DIR));
        // Créer un fichier CSV d’entrée simple (11 lignes = 1 header + 10 data)
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(INPUT_FILE))) {
            writer.write("firstName,lastName\n");
            for (int i = 1; i <= 10; i++) {
                writer.write("First" + i + ",Last" + i + "\n");
            }
        }
    }*/


    private void cleanup() throws IOException {
        Files.walk(Paths.get(PARTITION_DIR))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        //Files.deleteIfExists(Paths.get(INPUT_FILE));
    }

    @Test
    void testSplitFileStep() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("time", String.valueOf(System.currentTimeMillis()))
                .addString("inputFile", INPUT_FILE)
                .addString("partitionDir", PARTITION_DIR)
                .addLong("linesPerFile", LINES_PER_FILE)
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(params); // pas launchStep ici
        assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());

        // Vérifier que des fichiers ont bien été créés
        File dir = new File(PARTITION_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));

        assertThat(files).isNotNull();
        assertThat(files.length).isGreaterThan(0);

        // Vérifier le contenu d’un fichier
        File sample = files[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(sample))) {
            String header = reader.readLine();
            String line1 = reader.readLine();

            assertThat(header).isEqualTo("client;email;produit;quantite;prixUnitaire;prixTotal;dateCommande");

        }
    }
}
