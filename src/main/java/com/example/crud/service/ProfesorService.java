package com.example.crud.service;

import com.example.crud.model.Asignatura;
import com.example.crud.model.Profesor;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;

    @Transactional(readOnly = true)
    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con el ID: " + id));
    }

    public Profesor guardar(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    public Profesor asignarAsignatura(Long idProfesor, Long idAsignatura) {
        Profesor profesor = obtenerPorId(idProfesor);
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con el ID: " + idAsignatura));

        if (profesor.getAsignaturas() != null && profesor.getAsignaturas().size() >= 4) {
            throw new RuntimeException("El profesor ya tiene el límite máximo de 4 asignaturas asignadas.");
        }

        asignatura.setProfesor(profesor);
        asignaturaRepository.save(asignatura);

        return obtenerPorId(idProfesor);
    }

    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new RuntimeException("Profesor no existe con el ID: " + id);
        }
        profesorRepository.deleteById(id);
    }
}
