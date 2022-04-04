package task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task3.entities.NodeEntity;
import task3.entities.RelationEntity;
import task3.entities.WayEntity;
import task3.services.NodeService;
import task3.services.RelationService;
import task3.services.WayService;
import task3.xml.OpenStreetMapProcessor;

@Component
@RequiredArgsConstructor
public class Parser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);
    private static final String FILE_PATH = "src/main/resources/RU-NVS.osm.bz2";
    private static final int MAX_EVENTS_COUNT = 5000;

    private final NodeService nodeService;
    private final WayService wayService;
    private final RelationService relationService;

    @EventListener(ApplicationReadyEvent.class)
    public void parse() throws IOException, XMLStreamException, JAXBException {
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(FILE_PATH))) {
            OpenStreetMapProcessor processor = new OpenStreetMapProcessor(inputStream);

            int currentEvent = 0;
            LOGGER.info("Started parsing " + MAX_EVENTS_COUNT + " xml events");
            while (processor.hasNext() && currentEvent < MAX_EVENTS_COUNT) {
                if (processor.next() == XMLStreamConstants.START_ELEMENT) {
                    switch (processor.getLocalName()) {
                        case "node":
                            NodeEntity nodeEntity = new NodeEntity(processor.unmarshalNode());
                            nodeService.save(nodeEntity);
                            currentEvent++;
                            break;
                        case "way":
                            WayEntity wayEntity = new WayEntity(processor.unmarshalWay());
                            wayService.save(wayEntity);
                            currentEvent++;
                            break;
                        case "relation":
                            RelationEntity relationEntity = new RelationEntity(processor.unmarshalRelation());
                            relationService.save(relationEntity);
                            currentEvent++;
                            break;
                    }
                }
            }
            LOGGER.info("Finished parsing " + MAX_EVENTS_COUNT + " xml events");
        }
    }
}
