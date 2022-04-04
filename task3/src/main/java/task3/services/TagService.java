package task3.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task3.entities.TagEntity;
import task3.repositories.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    @Transactional
    public TagEntity save(TagEntity tagEntity) {
        return repository.save(tagEntity);
    }

    public List<TagEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public TagEntity update(TagEntity tagEntity) {
        return repository.save(tagEntity);
    }

    @Transactional
    public void delete(TagEntity tagEntity) {
        repository.delete(tagEntity);
    }
}
