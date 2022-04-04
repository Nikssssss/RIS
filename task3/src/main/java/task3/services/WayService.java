package task3.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task3.entities.WayEntity;
import task3.repositories.WayRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WayService {
    private final WayRepository repository;

    @Transactional
    public WayEntity save(WayEntity wayEntity) {
        return repository.save(wayEntity);
    }

    public List<WayEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public WayEntity update(WayEntity wayEntity) {
        return repository.save(wayEntity);
    }

    @Transactional
    public void delete(WayEntity wayEntity) {
        repository.delete(wayEntity);
    }
}
