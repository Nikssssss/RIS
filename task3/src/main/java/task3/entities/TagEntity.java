package task3.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;
    
    @Column(name = "_key")
    private String key;
    
    @Column()
    private String value;
    
    public TagEntity(task3.xml.entities.Tag xmlTag) {
        key = xmlTag.getK();
        value = xmlTag.getV();
    }
}
