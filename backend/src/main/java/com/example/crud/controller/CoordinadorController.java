package com.example.crud.controller;

import com.example.crud.dto.CoordinadorRequestDto;
import com.example.crud.dto.CoordinadorResponseDto;
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
    public ResponseEntity<List<CoordinadorResponseDto>> listarTodos() {
        return ResponseEntity.ok(coordinadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinadorResponseDto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(coordinadorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CoordinadorResponseDto> registrar(@Valid @RequestBody CoordinadorRequestDto coordinadorDto) {
        CoordinadorResponseDto nuevo = coordinadorService.guardar(coordinadorDto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoordinadorResponseDto> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody CoordinadorRequestDto coordinadorDto) {
        CoordinadorResponseDto actualizado = coordinadorService.actualizar(id, coordinadorDto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        coordinadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}