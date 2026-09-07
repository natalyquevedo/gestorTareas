package com.example.crud.service;

import com.example.crud.model.Asignatura;
import com.example.crud.model.Estudiante;
import com.example.crud.model.TareaAcademica;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.TareaAcademicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TareaAcademicaService {

    private final TareaAcademicaRepository tareaRepository;
    private final EstudianteRepository estudianteRepository;
    private final AsignaturaRepository asignaturaRepository;

    @Transactional(readOnly = true)
    public List<TareaAcademica> listarTodas() {
        return tareaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TareaAcademica obtenerPorId(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con el ID: " + id));
    }

    public TareaAcademica crearTarea(TareaAcademica tarea, Long idAsignatura, Long idEstudiante) {
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con el ID: " + idEstudiante));

        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con el ID: " + idAsignatura));

        if (estudiante.getTareas() != null && estudiante.getTareas().size() >= 50) {
            throw new RuntimeException("El estudiante ya ha alcanzado el límite máximo de 50 tareas.");
        }

        tarea.setEstudiante(estudiante);
        tarea.setAsignatura(asignatura);
        tarea.setEstado("PENDIENTE");

        return tareaRepository.save(tarea);
    }

    public TareaAcademica marcarComoEntregada(Long idTarea) {
        TareaAcademica tarea = obtenerPorId(idTarea);
        tarea.setEstado("ENTREGADA");
        return tareaRepository.save(tarea);
    }

    public TareaAcademica calificarTarea(Long idTarea, Double nota) {
        if (nota < 0.0 || nota > 5.0) {
            throw new RuntimeException("La calificación debe estar entre 0.0 y 5.0.");
        }

        TareaAcademica tarea = obtenerPorId(idTarea);
        tarea.setCalificacion(nota);
        tarea.setEstado("CALIFICADA");
        return tareaRepository.save(tarea);
    }

    @Transactional(readOnly = true)
    public List<TareaAcademica> listarTareasUrgentes() {
        LocalDateTime limiteUrgencia = LocalDateTime.now().plusHours(24);
        return tareaRepository.findAll().stream()
                .filter(t -> "PENDIENTE".equalsIgnoreCase(t.getEstado()))
                .filter(t -> t.getFechaEntrega() != null && t.getFechaEntrega().isBefore(limiteUrgencia))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TareaAcademica> listarPorEstado(String estado) {
        return tareaRepository.findAll().stream()
                .filter(t -> estado.equalsIgnoreCase(t.getEstado()))
                .collect(Collectors.toList());
    }

    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no existe con el ID: " + id);
        }
        tareaRepository.deleteById(id);
    }
}