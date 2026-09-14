package com.example.crud.service;

import com.example.crud.dto.TareaRequestDto;
import com.example.crud.dto.TareaResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.*;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.ProfesorRepository;
import com.example.crud.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TareaService {

    private final TareaRepository tareaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;
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

    public TareaResponseDto crearTarea(TareaRequestDto dto, Long idAsignatura, Long idProfesor, Long idEstudiante) {
        Long finalAsignaturaId = idAsignatura != null ? idAsignatura : dto.getAsignaturaId();
        Long finalEstudianteId = idEstudiante != null ? idEstudiante : dto.getEstudianteId();
        Long finalProfesorId = idProfesor != null ? idProfesor : dto.getProfesorId();

        if (finalAsignaturaId == null) {
            throw new ReglaNegocioException("El ID de la asignatura es obligatorio.");
        }
        if (finalEstudianteId == null) {
            throw new ReglaNegocioException("El ID del estudiante es obligatorio.");
        }

        Asignatura asignatura = asignaturaRepository.findById(finalAsignaturaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura no encontrada con el ID: " + finalAsignaturaId));

        if (finalProfesorId == null && asignatura.getProfesor() != null) {
            finalProfesorId = asignatura.getProfesor().getIdUsuario();
        }

        if (finalProfesorId == null) {
            throw new ReglaNegocioException("Debe especificar un profesor para la tarea.");
        }

        Long docenteId = finalProfesorId;
        Profesor profesor = profesorRepository.findById(docenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + docenteId));

        Estudiante estudiante = estudianteRepository.findById(finalEstudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + finalEstudianteId));

        // Regla 5: Límite de 50 tareas para estudiante
        if (estudiante.getTareas() != null && estudiante.getTareas().size() >= Estudiante.MAX_TAREAS) {
            throw new ReglaNegocioException("El estudiante ya ha alcanzado el límite máximo de " + Estudiante.MAX_TAREAS + " tareas.");
        }

        // Regla 1: Límite de 4 tareas por semana para el docente
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioSemana = ahora.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime finSemana = ahora.with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
        long tareasSemanaDocente = tareaRepository.countByProfesor_IdUsuarioAndFechaCreacionBetween(
                profesor.getIdUsuario(), inicioSemana, finSemana);

        if (tareasSemanaDocente >= Profesor.MAX_TAREAS_SEMANA) {
            throw new ReglaNegocioException("El docente ya ha alcanzado el límite máximo de "
                    + Profesor.MAX_TAREAS_SEMANA + " tareas registradas para esta semana.");
        }

        Tarea tarea = mapper.toEntity(dto);

        // Regla 6: Prioridad ALTA para tareas que vencen en menos de 24 horas
        if (tarea.getFechaEntrega() != null && tarea.getFechaEntrega().isBefore(ahora.plusHours(24))) {
            tarea.setPrioridad("ALTA");
        }

        tarea.setEstudiante(estudiante);
        tarea.setProfesor(profesor);
        tarea.setAsignatura(asignatura);
        tarea.setEstado(EstadoTarea.PENDIENTE);

        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    public TareaResponseDto marcarComoEntregada(Long idTarea) {
        Tarea tarea = buscarEntidadPorId(idTarea);
        tarea.setEstado(EstadoTarea.ENTREGADA);
        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    public TareaResponseDto calificarTarea(Long idTarea, Double nota) {
        return calificarTarea(idTarea, nota, null);
    }

    public TareaResponseDto calificarTarea(Long idTarea, Double nota, Long idProfesor) {
        // Regla 7: Calificación entre 0.0 y 5.0
        if (nota == null || nota < 0.0 || nota > 5.0) {
            throw new ReglaNegocioException("La calificación debe estar entre 0.0 y 5.0.");
        }

        if (idProfesor != null && !profesorRepository.existsById(idProfesor)) {
            throw new RecursoNoEncontradoException("Profesor calificador no encontrado con el ID: " + idProfesor);
        }

        Tarea tarea = buscarEntidadPorId(idTarea);
        tarea.setCalificacion(nota);
        tarea.setEstado(EstadoTarea.CALIFICADA);
        Tarea guardada = tareaRepository.save(tarea);
        return mapper.toTareaResponseDto(guardada);
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarTareasUrgentes() {
        LocalDateTime limiteUrgencia = LocalDateTime.now().plusHours(24);
        return tareaRepository.findAll().stream()
                .filter(t -> t.getEstado() == EstadoTarea.PENDIENTE)
                .filter(t -> t.getFechaEntrega() != null && t.getFechaEntrega().isBefore(limiteUrgencia))
                .map(mapper::toTareaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDto> listarPorEstado(String estadoStr) {
        EstadoTarea estado;
        try {
            estado = EstadoTarea.valueOf(estadoStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado no válido: " + estadoStr + ". Estados válidos: PENDIENTE, ENTREGADA, CALIFICADA");
        }
        return tareaRepository.findByEstado(estado).stream()
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
