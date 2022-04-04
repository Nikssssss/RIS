package task3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import task3.entities.NodeEntity;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    @Query(
            value = "SELECT * FROM nodes"
                    + " WHERE gc_to_sec(earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.lat, nodes.lon))) < ?3"
                    + " ORDER BY gc_to_sec(earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.lat, nodes.lon))) ASC",
            nativeQuery = true
    )
    List<NodeEntity> findAllNodesInRadius(Double latitude, Double longitude, Double radius);
}
