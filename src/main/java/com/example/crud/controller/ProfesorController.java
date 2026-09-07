package com.example.crud.controller;

import com.example.crud.model.Profesor;
import com.example.crud.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    public ResponseEntity<List<Profesor>> listarTodos() {
        return ResponseEntity.ok(profesorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Profesor> registrar(@Valid @RequestBody Profesor profesor) {
        Profesor nuevoProfesor = profesorService.guardar(profesor);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    @PostMapping("/{idProfesor}/asignar-materia/{idAsignatura}")
    public ResponseEntity<Profesor> asignarAsignatura(
            @PathVariable Long idProfesor,
            @PathVariable Long idAsignatura) {
        Profesor actualizado = profesorService.asignarAsignatura(idProfesor, idAsignatura);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
