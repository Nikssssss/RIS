package task3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import task3.entities.RelationEntity;

public interface RelationRepository extends JpaRepository<RelationEntity, Long> {
}
