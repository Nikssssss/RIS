package task3.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task3.entities.RelationEntity;
import task3.services.RelationService;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@RequiredArgsConstructor
public class RelationController {
    private final RelationService service;

    @PostMapping()
    public ResponseEntity<RelationEntity> save(@RequestBody RelationEntity relationEntity) {
        return ResponseEntity.ok(service.save(relationEntity));
    }

    @GetMapping()
    public ResponseEntity<List<RelationEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelationEntity> update(@PathVariable Long id, @RequestBody RelationEntity relationEntity) {
        relationEntity.setId(id);
        return ResponseEntity.ok(service.update(relationEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setId(id);
        service.delete(relationEntity);
        return ResponseEntity.ok().build();
    }
}
