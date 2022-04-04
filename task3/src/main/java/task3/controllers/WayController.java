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
import task3.entities.WayEntity;
import task3.services.WayService;

import java.util.List;

@RestController
@RequestMapping("api/ways")
@RequiredArgsConstructor
public class WayController {
    private final WayService service;

    @PostMapping()
    public ResponseEntity<WayEntity> save(@RequestBody WayEntity wayEntity) {
        return ResponseEntity.ok(service.save(wayEntity));
    }

    @GetMapping()
    public ResponseEntity<List<WayEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WayEntity> update(@PathVariable Long id, @RequestBody WayEntity wayEntity) {
        wayEntity.setId(id);
        return ResponseEntity.ok(service.update(wayEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        WayEntity wayEntity = new WayEntity();
        wayEntity.setId(id);
        service.delete(wayEntity);
        return ResponseEntity.ok().build();
    }
}
