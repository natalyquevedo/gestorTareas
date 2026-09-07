package com.example.crud.controller;

import com.example.crud.model.TareaAcademica;
import com.example.crud.service.TareaAcademicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaAcademicaController {

    private final TareaAcademicaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaAcademica>> listarTodas() {
        return ResponseEntity.ok(tareaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaAcademica> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @GetMapping("/urgentes")
    public ResponseEntity<List<TareaAcademica>> listarUrgentes() {
        return ResponseEntity.ok(tareaService.listarTareasUrgentes());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TareaAcademica>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(tareaService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<TareaAcademica> crearTarea(
            @Valid @RequestBody TareaAcademica tarea,
            @RequestParam Long idAsignatura,
            @RequestParam Long idEstudiante) {
        TareaAcademica nuevaTarea = tareaService.crearTarea(tarea, idAsignatura, idEstudiante);
        return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<TareaAcademica> marcarComoEntregada(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.marcarComoEntregada(id));
    }

    @PutMapping("/{id}/calificar")
    public ResponseEntity<TareaAcademica> calificarTarea(
            @PathVariable Long id,
            @RequestParam Double nota) {
        return ResponseEntity.ok(tareaService.calificarTarea(id, nota));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }
}