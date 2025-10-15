package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.service.BokningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Hämta alla bokningar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista med bokningar returneras")
    })
    @GetMapping
    public List<Bokning> getAll() {
        return bokningService.getAll();
    }

    @Operation(summary = "Hämta en bokning via ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bokningen hittades och returneras"),
            @ApiResponse(responseCode = "404", description = "Ingen bokning med detta ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Bokning> getById(@PathVariable Long id) {
        Optional<Bokning> b = bokningService.findById(id);
        return b.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Skapa en ny bokning")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bokningen skapades"),
            @ApiResponse(responseCode = "400", description = "Fel i bokningsdata eller sak redan bokad")
    })
    @PostMapping
    public ResponseEntity<Bokning> create(@Valid @RequestBody Bokning bokning) {
        try {
            Bokning ny = bokningService.boka(bokning);
            return ResponseEntity.ok(ny);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Uppdatera en befintlig bokning")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bokningen uppdaterades"),
            @ApiResponse(responseCode = "400", description = "Fel i bokningsdata eller sak redan bokad"),
            @ApiResponse(responseCode = "404", description = "Ingen bokning med detta ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Bokning> update(@PathVariable Long id, @Valid @RequestBody Bokning bokning) {
        try {
            Bokning updated = bokningService.update(id, bokning);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Ta bort en bokning")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bokningen raderades"),
            @ApiResponse(responseCode = "404", description = "Ingen bokning med detta ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (bokningService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bokningService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
