package batch.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import java.io.*;
import java.nio.file.*;

@Component("splitFileTasklet")
@StepScope
public class SplitFileTasklet implements Tasklet {

    private static final Logger log = LogManager.getLogger(SplitFileTasklet.class);
    private final String inputFilePath;
    private final String outputDirPath;
    private final Long linesPerFile ;

    public SplitFileTasklet(@Value("#{jobParameters['inputFile']}") String inputFile,
                            @Value("#{jobParameters['partitionDir']}") String  partitionDir,
                            @Value("#{jobParameters['linesPerFile']}") Long  linesPerFile) {
        this.inputFilePath = inputFile;
        this.outputDirPath = partitionDir;
        this.linesPerFile = linesPerFile;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path inputPath = Paths.get(inputFilePath);
        Path outputDir = Paths.get(outputDirPath);

        // Clean & recreate output partition directory
        FileSystemUtils.deleteRecursively(outputDir.toFile());
        Files.createDirectories(outputDir);

        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String header = reader.readLine(); // first line is header
            int fileIndex = 0;
            int lineCount = 0;
            BufferedWriter writer = null;

            String line;
            while ((line = reader.readLine()) != null) {
                if (lineCount % linesPerFile == 0) {
                    if (writer != null) {
                        writer.close();
                    }
                    Path partFile = outputDir.resolve("partition_" + (fileIndex++) + ".csv");
                    writer = Files.newBufferedWriter(partFile);
                    writer.write(header);
                    writer.newLine();
                }

                writer.write(line);
                writer.newLine();
                lineCount++;

            }

            if (writer != null) {
                writer.close();
            }
        }

        log.info(">>>>> Split step done - all files should now exist.");
        log.info("🔍 Listing des fichiers écrits :");
        Files.list(Paths.get(this.outputDirPath)).sorted().forEach(log::info);

        return RepeatStatus.FINISHED;
    }
}
