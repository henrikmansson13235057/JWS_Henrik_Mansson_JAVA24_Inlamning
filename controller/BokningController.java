package com.fulkoping.uthyrning.controller;

import com.fulkoping.uthyrning.model.Bokning;
import com.fulkoping.uthyrning.service.BokningService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bokningar")
public class BokningController {

    private final BokningController bokningController;
    public BokningController(BokningController bokningController) {
        this.bokningController = bokningController;
    }

    @GetMapping
    public List<Bokning> getAll() {
        return bokningController.getAll();

    }

    @PostMapping
    public ResponseEntity<Bokning> create(@Valid @RequestBody Bokning bokning) {
        try {
            return ResponseEntity.ok(BokningService.boka(bokning));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(null);
        }
    }



}
