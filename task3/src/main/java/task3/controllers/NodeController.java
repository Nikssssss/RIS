package task3.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import task3.entities.NodeEntity;
import task3.entities.RelationEntity;
import task3.services.NodeService;

@RestController
@RequestMapping("/api/nodes")
@RequiredArgsConstructor
public class NodeController {
    private final NodeService service;

    @PostMapping()
    public ResponseEntity<NodeEntity> save(@RequestBody NodeEntity nodeEntity) {
        return ResponseEntity.ok(service.save(nodeEntity));
    }

    @GetMapping()
    public ResponseEntity<List<NodeEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/radius")
    public ResponseEntity<List<NodeEntity>> getAllNodesInRadius(
            @RequestParam("lat") Double lat,
            @RequestParam("lon") Double lon,
            @RequestParam("radius") Double radius
    ) {
        return ResponseEntity.ok(service.findAllNodesInRadius(lat, lon, radius));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeEntity> update(@PathVariable Long id, @RequestBody NodeEntity nodeEntity) {
        nodeEntity.setId(id);
        return ResponseEntity.ok(service.update(nodeEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setId(id);
        service.delete(nodeEntity);
        return ResponseEntity.ok().build();
    }
}
