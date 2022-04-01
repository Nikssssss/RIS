import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong arguments number");
        }
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(args[0]))) {
            OpenStreetMapProcessor osmProcessor = new OpenStreetMapProcessor();
            LOGGER.info("Started processing input stream");
            OpenStreetMapProcessingResult osmProcessingResult = osmProcessor.process(inputStream);
            LOGGER.info("Printing users edits statistics");
            printStatistics(osmProcessingResult.getUsers());
            LOGGER.info("Printing unique keys statistics");
            printStatistics(osmProcessingResult.getKeys());
            LOGGER.info("Finished processing input stream");
        } catch (Exception ex) {
            LOGGER.error("Error occurred", ex);
        }
    }

    private static void printStatistics(Map<String, Integer> statistics) {
        statistics.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEach(e -> System.out.printf("%s - %d%n", e.getKey(), e.getValue()));
    }
}