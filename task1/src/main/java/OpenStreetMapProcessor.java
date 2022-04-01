import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenStreetMapProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenStreetMapProcessor.class);
    private static final String NODE_TAG = "node";
    private static final String TAG_TAG = "tag";
    private static final QName USER_ATTRIBUTE = new QName("user");
    private static final QName KEY_ATTRIBUTE = new QName("k");

    public OpenStreetMapProcessingResult process(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = null;
        OpenStreetMapProcessingResult result = new OpenStreetMapProcessingResult();
        try {
            eventReader = factory.createXMLEventReader(inputStream);
            processNodes(result, eventReader);
        } catch (Exception ex) {
            LOGGER.error("Error occurred during input processing", ex);
            if (eventReader != null) {
                eventReader.close();
            }
        }
        return result;
    }

    private void processNodes(OpenStreetMapProcessingResult result, XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals(NODE_TAG)) {
                    Attribute userAttribute = startElement.getAttributeByName(USER_ATTRIBUTE);
                    result.incrementUserCount(userAttribute.getValue());
                    processKeys(result, eventReader);
                }
            }
        }
    }

    private void processKeys(OpenStreetMapProcessingResult result, XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.getEventType() == XMLStreamConstants.END_ELEMENT &&
                    event.asEndElement().getName().getLocalPart().equals(NODE_TAG)) {
                return;
            }

            if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals(TAG_TAG)) {
                    Attribute key = startElement.getAttributeByName(KEY_ATTRIBUTE);
                    result.incrementKeyCount(key.getValue());
                }
            }
        }
        throw new XMLStreamException("Unexpected stream");
    }
}
