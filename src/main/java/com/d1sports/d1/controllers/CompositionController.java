package com.d1sports.d1.controllers;

import com.d1sports.d1.DTOs.CompositionDTO;
import com.d1sports.d1.model.Champion;
import com.d1sports.d1.model.Composition;
import com.d1sports.d1.services.CompositionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compositions")
@CrossOrigin(origins = "*")
public class CompositionController {

    private final CompositionService compositionService;

    public CompositionController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> authRequest) {
        String password = authRequest.get("password");
        boolean isValid = compositionService.validatePassword(password);

        return ResponseEntity.ok().body(Map.of("authenticated", isValid));
    }

    @GetMapping
    public ResponseEntity<Page<Composition>> getCompositions(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Composition> compositions = compositionService.searchCompositions(search, page, size);
        return ResponseEntity.ok(compositions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Composition> updateComposition(
            @PathVariable Long id,
            @RequestBody CompositionDTO compositionDTO,
            @RequestHeader("Authorization") String password) {

        if (!compositionService.validatePassword(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Composition updatedComposition = compositionService.updateComposition(id, compositionDTO);
            return ResponseEntity.ok(updatedComposition);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Composition> createComposition(
            @RequestBody CompositionDTO compositionDTO,
            @RequestHeader("Authorization") String password) {

        if (!compositionService.validatePassword(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Composition composition = compositionService.createComposition(compositionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(composition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComposition(
            @PathVariable Long id,
            @RequestHeader("Authorization") String password) {

        if (!compositionService.validatePassword(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        compositionService.deleteComposition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/champions")
    public ResponseEntity<List<Champion>> searchChampions(
            @RequestParam(required = false) String query) {

        List<Champion> champions = compositionService.searchChampions(query);
        return ResponseEntity.ok(champions);
    }
}
