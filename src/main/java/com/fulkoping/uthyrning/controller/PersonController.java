package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.model.Person;
import com.fulkoping.uthyrning.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personer")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Hämta alla personer")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Lista med personer returneras"
            ),
        }
    )
    @GetMapping
    public List<Person> getAll() {
        return personService.getAll();
    }

    @Operation(summary = "Hämta en person via ID")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Personen hittades och returneras"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen person med detta ID"
            ),
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        return personService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Skapa en ny person")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "201",
                description = "Personen skapades"
            ),
        }
    )
    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        Person saved = personService.create(person);
        return ResponseEntity.created(
            URI.create("/api/personer/" + saved.getId())
        ).body(saved);
    }

    @Operation(summary = "Uppdatera person")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Personen uppdaterades"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen person med detta ID"
            ),
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Person> update(
        @PathVariable Long id,
        @Valid @RequestBody Person updated
    ) {
        try {
            Person saved = personService.update(id, updated);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ta bort en person")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "204",
                description = "Personen raderades"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Ingen person med detta ID"
            ),
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            personService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
