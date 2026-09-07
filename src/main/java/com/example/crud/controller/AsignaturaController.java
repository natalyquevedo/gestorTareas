package com.example.crud.controller;

import com.example.crud.model.Asignatura;
import com.example.crud.model.Estudiante;
import com.example.crud.service.AsignaturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @GetMapping
    public ResponseEntity<List<Asignatura>> listarTodas() {
        return ResponseEntity.ok(asignaturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesDeAsignatura(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerEstudiantesDeAsignatura(id));
    }

    @PostMapping
    public ResponseEntity<Asignatura> registrar(@Valid @RequestBody Asignatura asignatura) {
        Asignatura nuevaAsignatura = asignaturaService.guardar(asignatura);
        return new ResponseEntity<>(nuevaAsignatura, HttpStatus.CREATED);
    }

    //falta post por ID esta es la url que debe estar api/asignaturas/{id} (actualizar)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignaturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}