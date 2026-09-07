package com.example.crud.controller;

import com.example.crud.model.Coordinador;
import com.example.crud.service.CoordinadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinadores")
@RequiredArgsConstructor
public class CoordinadorController {

    private final CoordinadorService coordinadorService;

    @GetMapping
    public ResponseEntity<List<Coordinador>> listarTodos() {
        return ResponseEntity.ok(coordinadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coordinador> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(coordinadorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Coordinador> registrar(@Valid @RequestBody Coordinador coordinador) {
        Coordinador nuevoCoordinador = coordinadorService.guardar(coordinador);
        return new ResponseEntity<>(nuevoCoordinador, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coordinador> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Coordinador coordinador) {
        Coordinador actualizado = coordinadorService.actualizar(id, coordinador);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        coordinadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}