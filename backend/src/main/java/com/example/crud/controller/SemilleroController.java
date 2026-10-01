package com.example.crud.controller;

import com.example.crud.dto.SemilleroRequestDto;
import com.example.crud.dto.SemilleroResponseDto;
import com.example.crud.service.SemilleroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semilleros")
@RequiredArgsConstructor
public class SemilleroController {

    private final SemilleroService semilleroService;

    @GetMapping
    public ResponseEntity<List<SemilleroResponseDto>> listarTodos() {
        return ResponseEntity.ok(semilleroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemilleroResponseDto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(semilleroService.obtenerPorId(id));
    }

    @GetMapping("/tutor/{idProfesor}")
    public ResponseEntity<SemilleroResponseDto> obtenerPorTutor(@PathVariable("idProfesor") Long idProfesor) {
        return ResponseEntity.ok(semilleroService.obtenerPorTutor(idProfesor));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<SemilleroResponseDto> obtenerPorEstudiante(@PathVariable("idEstudiante") Long idEstudiante) {
        return ResponseEntity.ok(semilleroService.obtenerPorEstudiante(idEstudiante));
    }

    @PostMapping
    public ResponseEntity<SemilleroResponseDto> crear(@Valid @RequestBody SemilleroRequestDto dto) {
        SemilleroResponseDto nuevo = semilleroService.crear(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemilleroResponseDto> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody SemilleroRequestDto dto) {
        return ResponseEntity.ok(semilleroService.actualizar(id, dto));
    }

    @PutMapping("/{id}/asignar-tutor/{idProfesor}")
    public ResponseEntity<SemilleroResponseDto> asignarTutor(
            @PathVariable("id") Long id,
            @PathVariable("idProfesor") Long idProfesor) {
        return ResponseEntity.ok(semilleroService.asignarTutor(id, idProfesor));
    }

    @PostMapping("/{id}/inscribir/{idEstudiante}")
    public ResponseEntity<SemilleroResponseDto> inscribirSemillerista(
            @PathVariable("id") Long id,
            @PathVariable("idEstudiante") Long idEstudiante) {
        return ResponseEntity.ok(semilleroService.inscribirSemillerista(id, idEstudiante));
    }

    @DeleteMapping("/{id}/desvincular/{idEstudiante}")
    public ResponseEntity<SemilleroResponseDto> desvincularSemillerista(
            @PathVariable("id") Long id,
            @PathVariable("idEstudiante") Long idEstudiante) {
        return ResponseEntity.ok(semilleroService.desvincularSemillerista(id, idEstudiante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        semilleroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
