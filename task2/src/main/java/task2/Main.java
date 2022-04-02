package task2;

import task2.xml.entities.Node;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task2.database.DatabaseInitializer;
import task2.database.dao.NodeDAO;
import task2.xml.OpenStreetMapProcessor;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenStreetMapProcessor.class);
    private static final int NUMBER_OF_NODES = 2000;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Wrong arguments number");
        }
        try {
            executePlainInsert(args[0]);
            executePreparedInsert(args[0]);
            executeBatchInsert(args[0]);
        } catch (Exception ex) {
            LOGGER.error("Error occurred", ex);
        }
    }

    private static void executePlainInsert(String filePath) throws Exception {
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(filePath))) {
            try (OpenStreetMapProcessor processor = new OpenStreetMapProcessor(inputStream)) {
                double totalTime = 0;
                DatabaseInitializer.initialize();
                NodeDAO nodeDao = new NodeDAO(DatabaseInitializer.getConnection());
                for (int i = 0; i < NUMBER_OF_NODES; i++) {
                    Node node = processor.getNextNode();
                    long startTime = System.currentTimeMillis();
                    nodeDao.save(node);
                    long endTime = System.currentTimeMillis();
                    totalTime += (endTime - startTime);
                }
                totalTime /= 1000;
                System.out.printf("%s plain inserts per second\n", NUMBER_OF_NODES / totalTime);
            }
        }
    }

    private static void executePreparedInsert(String filePath) throws Exception {
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(filePath))) {
            try (OpenStreetMapProcessor processor = new OpenStreetMapProcessor(inputStream)) {
                double totalTime = 0;
                DatabaseInitializer.initialize();
                NodeDAO nodeDao = new NodeDAO(DatabaseInitializer.getConnection());
                for (int i = 0; i < NUMBER_OF_NODES; i++) {
                    Node node = processor.getNextNode();
                    long startTime = System.currentTimeMillis();
                    nodeDao.savePrepared(node);
                    long endTime = System.currentTimeMillis();
                    totalTime += (endTime - startTime);
                }
                totalTime /= 1000;
                System.out.printf("%s prepared inserts per second\n", NUMBER_OF_NODES / totalTime);
            }
        }
    }

    private static void executeBatchInsert(String filePath) throws Exception {
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(filePath))) {
            try (OpenStreetMapProcessor processor = new OpenStreetMapProcessor(inputStream)) {
                double totalTime = 0;
                DatabaseInitializer.initialize();
                NodeDAO nodeDao = new NodeDAO(DatabaseInitializer.getConnection());
                for (int i = 0; i < NUMBER_OF_NODES; i++) {
                    Node node = processor.getNextNode();
                    long startTime = System.currentTimeMillis();
                    nodeDao.saveByBatch(node);
                    long endTime = System.currentTimeMillis();
                    totalTime += (endTime - startTime);
                }
                long startTime = System.currentTimeMillis();
                nodeDao.flush();
                long endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
                totalTime /= 1000;
                System.out.printf("%s batch inserts per second\n", NUMBER_OF_NODES / totalTime);
            }
        }
    }
}
