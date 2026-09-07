package com.example.crud.service;

import com.example.crud.model.Asignatura;
import com.example.crud.model.Estudiante;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final AsignaturaRepository asignaturaRepository;

    @Transactional(readOnly = true)
    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con el ID: " + id));
    }

    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public Estudiante inscribirAsignatura(Long idEstudiante, Long idAsignatura) {
        Estudiante estudiante = obtenerPorId(idEstudiante);
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con el ID: " + idAsignatura));

        if (estudiante.getAsignaturas() != null && estudiante.getAsignaturas().size() >= 8) {
            throw new RuntimeException("El estudiante no puede matricular más de 8 asignaturas.");
        }

        if (asignatura.getEstudiantes() != null && asignatura.getEstudiantes().size() >= 25) {
            throw new RuntimeException("La asignatura ya alcanzó el límite máximo de 25 estudiantes.");
        }

        estudiante.getAsignaturas().add(asignatura);
        return estudianteRepository.save(estudiante);
    }

    public void eliminar(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RuntimeException("Estudiante no existe con el ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}