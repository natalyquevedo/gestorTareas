package com.example.crud.controller;

import com.example.crud.model.Estudiante;
import com.example.crud.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodos() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Estudiante> registrar(@Valid @RequestBody Estudiante estudiante) {
        Estudiante nuevoEstudiante = estudianteService.guardar(estudiante);
        return new ResponseEntity<>(nuevoEstudiante, HttpStatus.CREATED);
    }

    @PostMapping("/{idEstudiante}/inscribir/{idAsignatura}")
    public ResponseEntity<Estudiante> inscribirAsignatura(
            @PathVariable Long idEstudiante,
            @PathVariable Long idAsignatura) {
        Estudiante actualizado = estudianteService.inscribirAsignatura(idEstudiante, idAsignatura);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
