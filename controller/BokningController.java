package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.service.BokningService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bokningar")
public class BokningController {

    private final BokningService bokningService;


    public BokningController(BokningService bokningService) {
        this.bokningService = bokningService;
    }


    @GetMapping
    public List<Bokning> getAll() {
        return bokningService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bokning> getById(@PathVariable Long id) {
        Optional<Bokning> b = bokningService.findById(id);
        return b.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Bokning> create(@Valid @RequestBody Bokning bokning) {
        try {
            Bokning ny = bokningService.boka(bokning);
            return ResponseEntity.ok(ny);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bokning> update(@PathVariable Long id, @Valid @RequestBody Bokning bokning) {
        try {
            Bokning updated = bokningService.update(id, bokning);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (bokningService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bokningService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
