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
@Table(name = "relations")
@Data
@NoArgsConstructor
public class RelationEntity {
    @Id
    @Column()
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "relation_tag",
            joinColumns = { @JoinColumn(name = "relation_id", referencedColumnName = "id") },
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
    private Long changeset;

    public RelationEntity(task3.xml.entities.Relation xmlRelation) {
        id = xmlRelation.getId().longValue();
        user = xmlRelation.getUser();
        uid = xmlRelation.getUid().longValue();
        visible = xmlRelation.isVisible();
        version = xmlRelation.getVersion().longValue();
        changeset = xmlRelation.getChangeset().longValue();
        tags = xmlRelation.getTag()
                .stream()
                .map(TagEntity::new)
                .collect(Collectors.toSet());
    }
}
