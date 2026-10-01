package com.example.crud.controller;

import com.example.crud.dto.EstudianteRequestDto;
import com.example.crud.dto.EstudianteResponseDto;
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
    public ResponseEntity<List<EstudianteResponseDto>> listarTodos() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDto> registrar(@Valid @RequestBody EstudianteRequestDto estudianteDto) {
        EstudianteResponseDto nuevo = estudianteService.guardar(estudianteDto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PostMapping("/{idEstudiante}/inscribir-semillero/{idSemillero}")
    public ResponseEntity<EstudianteResponseDto> inscribirSemillero(
            @PathVariable("idEstudiante") Long idEstudiante,
            @PathVariable("idSemillero") Long idSemillero) {
        EstudianteResponseDto actualizado = estudianteService.inscribirSemillero(idEstudiante, idSemillero);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{idEstudiante}/desvincular-semillero/{idSemillero}")
    public ResponseEntity<EstudianteResponseDto> desvincularSemillero(
            @PathVariable("idEstudiante") Long idEstudiante,
            @PathVariable("idSemillero") Long idSemillero) {
        EstudianteResponseDto actualizado = estudianteService.desvincularSemillero(idEstudiante, idSemillero);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody EstudianteRequestDto estudianteDto) {
        EstudianteResponseDto actualizado = estudianteService.actualizar(id, estudianteDto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
