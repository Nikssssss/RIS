package task3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import task3.entities.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
