package com.example.crud.service;

import com.example.crud.model.Asignatura;
import com.example.crud.model.Estudiante;
import com.example.crud.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    @Transactional(readOnly = true)
    public List<Asignatura> listarTodas() {
        return asignaturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Asignatura obtenerPorId(Long id) {
        return asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con el ID: " + id));
    }

    public Asignatura guardar(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }

    @Transactional(readOnly = true)
    public List<Estudiante> obtenerEstudiantesDeAsignatura(Long idAsignatura) {
        Asignatura asignatura = obtenerPorId(idAsignatura);
        return asignatura.getEstudiantes();
    }

    public void eliminar(Long id) {
        if (!asignaturaRepository.existsById(id)) {
            throw new RuntimeException("Asignatura no existe con el ID: " + id);
        }
        asignaturaRepository.deleteById(id);
    }
}
