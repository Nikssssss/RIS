package task2.xml;

import task2.xml.entities.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class OpenStreetMapProcessor implements AutoCloseable {
    private final XsiTypeReader reader;
    private final JAXBContext context;

    public OpenStreetMapProcessor(InputStream inputStream) throws XMLStreamException, JAXBException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader eventReader = factory.createXMLStreamReader(inputStream);
        reader = new XsiTypeReader(eventReader, "http://openstreetmap.org/osm/0.6");
        context = JAXBContext.newInstance(Node.class);
    }

    public Node getNextNode() throws JAXBException, XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT &&
                    reader.getLocalName().equals("node")) {
                return (Node) context.createUnmarshaller().unmarshal(reader);
            }
        }
        return null;
    }

    @Override
    public void close() throws XMLStreamException {
        if (reader != null) {
            reader.close();
        }
    }
}
