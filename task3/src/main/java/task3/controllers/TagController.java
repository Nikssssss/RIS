package task3.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task3.entities.TagEntity;
import task3.services.TagService;

import java.util.List;

@RestController
@RequestMapping("api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping()
    public ResponseEntity<TagEntity> save(@RequestBody TagEntity tagEntity) {
        return ResponseEntity.ok(service.save(tagEntity));
    }

    @GetMapping()
    public ResponseEntity<List<TagEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagEntity> update(@PathVariable Long id, @RequestBody TagEntity tagEntity) {
        tagEntity.setId(id);
        return ResponseEntity.ok(service.update(tagEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(id);
        service.delete(tagEntity);
        return ResponseEntity.ok().build();
    }
}
