package com.example.crud.controller;

import com.example.crud.dto.TareaRequestDto;
import com.example.crud.dto.TareaResponseDto;
import com.example.crud.service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaResponseDto>> listarTodas() {
        return ResponseEntity.ok(tareaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDto> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<TareaResponseDto>> listarPorEstudiante(@PathVariable("idEstudiante") Long idEstudiante) {
        return ResponseEntity.ok(tareaService.listarPorEstudiante(idEstudiante));
    }

    @GetMapping("/semillero/{idSemillero}")
    public ResponseEntity<List<TareaResponseDto>> listarPorSemillero(@PathVariable("idSemillero") Long idSemillero) {
        return ResponseEntity.ok(tareaService.listarPorSemillero(idSemillero));
    }

    @GetMapping("/profesor/{idProfesor}")
    public ResponseEntity<List<TareaResponseDto>> listarPorProfesor(@PathVariable("idProfesor") Long idProfesor) {
        return ResponseEntity.ok(tareaService.listarPorProfesor(idProfesor));
    }

    @GetMapping("/urgentes")
    public ResponseEntity<List<TareaResponseDto>> listarUrgentes() {
        return ResponseEntity.ok(tareaService.listarTareasUrgentes());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TareaResponseDto>> listarPorEstado(@PathVariable("estado") String estado) {
        return ResponseEntity.ok(tareaService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<TareaResponseDto> crearTarea(
            @Valid @RequestBody TareaRequestDto tareaDto,
            @RequestParam(name = "semilleroId", required = false) Long semilleroId,
            @RequestParam(name = "idSemillero", required = false) Long idSemillero,
            @RequestParam(name = "profesorId", required = false) Long profesorId,
            @RequestParam(name = "idProfesor", required = false) Long idProfesor,
            @RequestParam(name = "estudianteId", required = false) Long estudianteId,
            @RequestParam(name = "idEstudiante", required = false) Long idEstudiante) {

        Long finalSemilleroId = semilleroId != null ? semilleroId
                : (idSemillero != null ? idSemillero : tareaDto.getSemilleroId());
        Long finalProfesorId = profesorId != null ? profesorId : idProfesor;
        Long finalEstudianteId = estudianteId != null ? estudianteId : idEstudiante;

        TareaResponseDto nuevaTarea = tareaService.crearTarea(
                tareaDto, finalSemilleroId, finalProfesorId, finalEstudianteId);
        return new ResponseEntity<>(nuevaTarea, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<TareaResponseDto> marcarComoEntregada(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tareaService.marcarComoEntregada(id));
    }

    @PutMapping("/{id}/calificar")
    public ResponseEntity<TareaResponseDto> calificarTarea(
            @PathVariable("id") Long id,
            @RequestParam(name = "observaciones", required = false) String observaciones,
            @RequestParam(name = "nota", required = false) Double nota,
            @RequestParam(name = "profesorId", required = false) Long profesorId,
            @RequestParam(name = "idProfesor", required = false) Long idProfesor) {
        Long finalProfesorId = profesorId != null ? profesorId : idProfesor;
        return ResponseEntity.ok(tareaService.calificarTarea(id, observaciones, nota, finalProfesorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable("id") Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }
}
