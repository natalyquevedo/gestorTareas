package com.example.crud.controller;

import com.example.crud.dto.AsignaturaRequestDto;
import com.example.crud.dto.AsignaturaResponseDto;
import com.example.crud.dto.EstudianteResponseDto;
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
    public ResponseEntity<List<AsignaturaResponseDto>> listarTodas() {
        return ResponseEntity.ok(asignaturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<EstudianteResponseDto>> obtenerEstudiantesDeAsignatura(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.obtenerEstudiantesDeAsignatura(id));
    }

    @PostMapping
    public ResponseEntity<AsignaturaResponseDto> registrar(@Valid @RequestBody AsignaturaRequestDto asignaturaDto) {
        AsignaturaResponseDto nueva = asignaturaService.guardar(asignaturaDto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AsignaturaRequestDto asignaturaDto) {
        AsignaturaResponseDto actualizada = asignaturaService.actualizar(id, asignaturaDto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignaturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}