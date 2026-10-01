package com.example.crud.service;

import com.example.crud.dto.TareaRequestDto;
import com.example.crud.dto.TareaResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.*;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.ProfesorRepository;
import com.example.crud.repository.SemilleroRepository;
import com.example.crud.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TareaService {

    private final TareaRepository tareaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final SemilleroRepository semilleroRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarTodas() {
        return tareaRepository.findAll().stream()
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TareaResponseDto obtenerPorId(Long id) {
        return mapper.toTareaResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Tarea buscarEntidadPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada con el ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarPorEstudiante(Long idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + idEstudiante);
        }
        return tareaRepository.findByEstudiante_IdUsuario(idEstudiante).stream()
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarPorSemillero(Long idSemillero) {
        if (!semilleroRepository.existsById(idSemillero)) {
            throw new RecursoNoEncontradoException("Semillero no encontrado con el ID: " + idSemillero);
        }
        return tareaRepository.findBySemillero_IdSemillero(idSemillero).stream()
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarPorProfesor(Long idProfesor) {
        if (!profesorRepository.existsById(idProfesor)) {
            throw new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + idProfesor);
        }
        return tareaRepository.findByProfesor_IdUsuario(idProfesor).stream()
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    public TareaResponseDto crearTarea(TareaRequestDto dto, Long idSemillero, Long idProfesor, Long idEstudiante) {
        Long finalSemilleroId = idSemillero != null ? idSemillero : dto.getSemilleroId();
        Long finalEstudianteId = idEstudiante != null ? idEstudiante : dto.getEstudianteId();
        Long finalProfesorId = idProfesor != null ? idProfesor : dto.getProfesorId();

        if (finalSemilleroId == null) {
            throw new ReglaNegocioException("El ID del semillero es obligatorio.");
        }
        if (finalEstudianteId == null) {
            throw new ReglaNegocioException("El ID del estudiante es obligatorio.");
        }

        Semillero semillero = semilleroRepository.findById(finalSemilleroId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Semillero no encontrado con el ID: " + finalSemilleroId));

        if (finalProfesorId == null && semillero.getProfesor() != null) {
            finalProfesorId = semillero.getProfesor().getIdUsuario();
        }

        if (finalProfesorId == null) {
            throw new ReglaNegocioException("Debe especificar un profesor tutor para la actividad.");
        }

        Long docenteId = finalProfesorId;
        Profesor profesor = profesorRepository.findById(docenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor tutor no encontrado con el ID: " + docenteId));

        Estudiante estudiante = estudianteRepository.findById(finalEstudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + finalEstudianteId));

        LocalDateTime ahora = LocalDateTime.now();
        Tarea tarea = mapper.toEntity(dto);

        // Prioridad ALTA para tareas que vencen en menos de 24 horas
        if (tarea.getFechaEntrega() != null && tarea.getFechaEntrega().isBefore(ahora.plusHours(24))) {
            tarea.setPrioridad("ALTA");
        }

        tarea.setEstudiante(estudiante);
        tarea.setProfesor(profesor);
        tarea.setSemillero(semillero);
        tarea.setEstado(Tarea.ESTADO_PENDIENTE);

        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    public TareaResponseDto marcarComoEntregada(Long idTarea) {
        Tarea tarea = buscarEntidadPorId(idTarea);
        if (Tarea.ESTADO_CALIFICADA.equalsIgnoreCase(tarea.getEstado())) {
            throw new ReglaNegocioException("No se puede entregar una actividad que ya ha sido calificada.");
        }
        tarea.setEstado(Tarea.ESTADO_ENTREGADA);
        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    public TareaResponseDto calificarTarea(Long idTarea, String observaciones, Double nota, Long idProfesor) {
        if (idProfesor != null && !profesorRepository.existsById(idProfesor)) {
            throw new RecursoNoEncontradoException("Profesor tutor no encontrado con el ID: " + idProfesor);
        }

        if (nota != null && (nota < 0.0 || nota > 5.0)) {
            throw new ReglaNegocioException("La calificación debe estar entre 0.0 y 5.0.");
        }

        Tarea tarea = buscarEntidadPorId(idTarea);
        tarea.setObservaciones(observaciones);
        if (nota != null) {
            tarea.setCalificacion(nota);
        }
        tarea.setEstado(Tarea.ESTADO_CALIFICADA);
        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarTareasUrgentes() {
        LocalDateTime limiteUrgencia = LocalDateTime.now().plusHours(24);
        return tareaRepository.findAll().stream()
                .filter(t -> Tarea.ESTADO_PENDIENTE.equalsIgnoreCase(t.getEstado()))
                .filter(t -> t.getFechaEntrega() != null && t.getFechaEntrega().isBefore(limiteUrgencia))
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarPorEstado(String estadoStr) {
        if (estadoStr == null || estadoStr.isBlank()) {
            throw new ReglaNegocioException("Debe especificar un estado: PENDIENTE, ENTREGADA, CALIFICADA");
        }
        String estadoUpper = estadoStr.trim().toUpperCase();
        if (!Tarea.ESTADO_PENDIENTE.equals(estadoUpper)
                && !Tarea.ESTADO_ENTREGADA.equals(estadoUpper)
                && !Tarea.ESTADO_CALIFICADA.equals(estadoUpper)) {
            throw new ReglaNegocioException("Estado no válido: " + estadoStr + ". Estados válidos: PENDIENTE, ENTREGADA, CALIFICADA");
        }
        return tareaRepository.findByEstadoIgnoreCase(estadoUpper).stream()
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Tarea no existe con el ID: " + id);
        }
        tareaRepository.deleteById(id);
    }
}
