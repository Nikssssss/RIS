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
@Table(name = "ways")
@Data
@NoArgsConstructor
public class WayEntity {
    @Id
    @Column()
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "way_tag",
            joinColumns = { @JoinColumn(name = "way_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") }
    )
    private Set<TagEntity> tags;

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

    public WayEntity(task3.xml.entities.Way xmlWay) {
        id = xmlWay.getId().longValue();
        user = xmlWay.getUser();
        uid = xmlWay.getUid().longValue();
        visible = xmlWay.isVisible();
        version = xmlWay.getVersion().longValue();
        changeSet = xmlWay.getChangeset().longValue();
        tags = xmlWay.getTag()
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
    }
    
    public static WayEntity makeFromXml() {
        WayEntity wayEntity = new WayEntity();

        return wayEntity;
    }
}
