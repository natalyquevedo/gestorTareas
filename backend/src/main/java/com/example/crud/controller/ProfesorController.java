package com.example.crud.controller;

import com.example.crud.dto.ProfesorRequestDto;
import com.example.crud.dto.ProfesorResponseDto;
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
    public ResponseEntity<List<ProfesorResponseDto>> listarTodos() {
        return ResponseEntity.ok(profesorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(profesorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfesorResponseDto> registrar(@Valid @RequestBody ProfesorRequestDto profesorDto) {
        ProfesorResponseDto nuevo = profesorService.guardar(profesorDto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PostMapping("/{idProfesor}/asignar-semillero/{idSemillero}")
    public ResponseEntity<ProfesorResponseDto> asignarSemillero(
            @PathVariable("idProfesor") Long idProfesor,
            @PathVariable("idSemillero") Long idSemillero) {
        ProfesorResponseDto actualizado = profesorService.asignarSemillero(idProfesor, idSemillero);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDto> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProfesorRequestDto profesorDto) {
        ProfesorResponseDto actualizado = profesorService.actualizar(id, profesorDto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
