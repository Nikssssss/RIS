package task3.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task3.entities.RelationEntity;
import task3.repositories.RelationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationService {
    private final RelationRepository repository;

    @Transactional
    public RelationEntity save(RelationEntity relationEntity) {
        return repository.save(relationEntity);
    }

    public List<RelationEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public RelationEntity update(RelationEntity relationEntity) {
        return repository.save(relationEntity);
    }

    @Transactional
    public void delete(RelationEntity relationEntity) {
        repository.delete(relationEntity);
    }
}
