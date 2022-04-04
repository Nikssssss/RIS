package task3.entities;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nodes")
@Data
@NoArgsConstructor
public class NodeEntity {
    @Id
    @Column()
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "node_tag",
            joinColumns = { @JoinColumn(name = "node_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") }
    )
    private Set<TagEntity> tags;

    @Column()
    private Double lat;

    @Column()
    private Double lon;

    @Column(name = "_user")
    private String user;

    @Column()
    private Long uid;

    @Column()
    private Boolean visible;

    @Column()
    private Long version;

    @Column(name = "changeset")
    private Long changeSet;

    public NodeEntity(task3.xml.entities.Node xmlNode) {
        id = xmlNode.getId().longValue();
        lat = xmlNode.getLat();
        lon = xmlNode.getLon();
        user = xmlNode.getUser();
        uid = xmlNode.getUid().longValue();
        visible = xmlNode.isVisible();
        version = xmlNode.getVersion().longValue();
        changeSet = xmlNode.getChangeset().longValue();
        tags = xmlNode.getTag()
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
    }
}
