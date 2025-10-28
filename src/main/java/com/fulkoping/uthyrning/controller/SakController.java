package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.dto.RentalRequest;
import com.fulkoping.uthyrning.model.Sak;
import com.fulkoping.uthyrning.service.SakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saker")
public class SakController {

    private final SakService sakService;

    public SakController(SakService sakService) {
        this.sakService = sakService;
    }

    @Operation(summary = "Hämta alla saker")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Lista med saker returneras"
            ),
        }
    )
    @GetMapping
    public List<Sak> getAll() {
        return sakService.getAll();
    }

    @Operation(summary = "Hämta en sak via ID")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Saken hittades och returneras"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen sak med detta ID"
            ),
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Sak> getById(@PathVariable Long id) {
        return sakService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Visa lagerstatus")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Lista med saker och tillgänglig kvantitet returneras"
            ),
        }
    )
    @GetMapping("/inventory")
    public List<String> showInventory() {
        return sakService.getInventoryStatus();
    }

    @Operation(summary = "Skapa en ny sak")
    @ApiResponses(
        { @ApiResponse(responseCode = "201", description = "Saken skapades") }
    )
    @PostMapping
    public ResponseEntity<Sak> create(@Valid @RequestBody Sak sak) {
        Sak saved = sakService.create(sak);
        return ResponseEntity.created(
            URI.create("/api/saker/" + saved.getId())
        ).body(saved);
    }

    @Operation(summary = "Uppdatera sak")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Saken uppdaterades"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen sak med detta ID"
            ),
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Sak> update(
        @PathVariable Long id,
        @Valid @RequestBody Sak updated
    ) {
        try {
            Sak saved = sakService.update(id, updated);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ta bort en sak")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "204", description = "Saken raderades"),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen sak med detta ID"
            ),
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            sakService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Hyr/boka en sak")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Saken bokades framgångsrikt"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Saken är redan bokad"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Saken hittades inte"
            ),
        }
    )
    @PostMapping("/{id}/rent")
    public ResponseEntity<?> rentItem(
        @PathVariable Long id,
        @Valid @RequestBody RentalRequest request
    ) {
        try {
            Map<String, Object> result = sakService.rentItem(id, request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("message", e.getMessage())
            );
        }
    }
}
