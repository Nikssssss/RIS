package task3.xml;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import task3.xml.entities.Node;
import task3.xml.entities.Way;
import task3.xml.entities.Relation;

public class OpenStreetMapProcessor implements AutoCloseable {
    private final XsiTypeReader reader;
    private final JAXBContext nodeContext;
    private final JAXBContext wayContext;
    private final JAXBContext relationContext;

    public OpenStreetMapProcessor(InputStream inputStream) throws XMLStreamException, JAXBException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader eventReader = factory.createXMLStreamReader(inputStream);
        reader = new XsiTypeReader(eventReader, "http://openstreetmap.org/osm/0.6");
        nodeContext = JAXBContext.newInstance(Node.class);
        wayContext = JAXBContext.newInstance(Way.class);
        relationContext = JAXBContext.newInstance(Relation.class);
    }

    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }

    public int next() throws XMLStreamException {
        return reader.next();
    }

    public String getLocalName() {
        return reader.getLocalName();
    }

    public Node unmarshalNode() throws JAXBException {
        return (Node) nodeContext.createUnmarshaller().unmarshal(reader);
    }

    public Way unmarshalWay() throws JAXBException {
        return (Way) wayContext.createUnmarshaller().unmarshal(reader);
    }

    public Relation unmarshalRelation() throws JAXBException {
        return (Relation) relationContext.createUnmarshaller().unmarshal(reader);
    }

    @Override
    public void close() throws XMLStreamException {
        if (reader != null) {
            reader.close();
        }
    }
}
