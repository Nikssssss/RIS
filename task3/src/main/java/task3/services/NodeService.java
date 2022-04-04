package task3.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task3.entities.NodeEntity;
import task3.repositories.NodeRepository;

@Service
@RequiredArgsConstructor
public class NodeService {
    private final NodeRepository repository;

    @Transactional
    public NodeEntity save(NodeEntity nodeEntity) {
        return repository.save(nodeEntity);
    }

    public List<NodeEntity> findAll() {
        return repository.findAll();
    }

    public List<NodeEntity> findAllNodesInRadius(Double lat, Double lon, Double radius) {
        return repository.findAllNodesInRadius(lat, lon, radius);
    }

    @Transactional
    public NodeEntity update(NodeEntity nodeEntity) {
        return repository.save(nodeEntity);
    }

    @Transactional
    public void delete(NodeEntity nodeEntity) {
        repository.delete(nodeEntity);
    }
}
