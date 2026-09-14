package com.example.crud.service;

import com.example.crud.dto.AsignaturaRequestDto;
import com.example.crud.dto.AsignaturaResponseDto;
import com.example.crud.dto.EstudianteResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Asignatura;
import com.example.crud.model.Profesor;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final ProfesorRepository profesorRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<AsignaturaResponseDto> listarTodas() {
        return asignaturaRepository.findAll().stream()
                .map(mapper::toAsignaturaResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsignaturaResponseDto obtenerPorId(Long id) {
        return mapper.toAsignaturaResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Asignatura buscarEntidadPorId(Long id) {
        return asignaturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura no encontrada con el ID: " + id));
    }

    public AsignaturaResponseDto guardar(AsignaturaRequestDto dto) {
        Asignatura asignatura = mapper.toEntity(dto);
        if (dto.getProfesorId() != null) {
            Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + dto.getProfesorId()));
            if (profesor.getAsignaturas() != null && profesor.getAsignaturas().size() >= Profesor.MAX_ASIGNATURAS) {
                throw new ReglaNegocioException("El profesor ya tiene el límite máximo de " + Profesor.MAX_ASIGNATURAS + " asignaturas asignadas.");
            }
            asignatura.setProfesor(profesor);
        }
        Asignatura guardada = asignaturaRepository.save(asignatura);
        return mapper.toAsignaturaResponseDto(guardada);
    }

    public AsignaturaResponseDto actualizar(Long id, AsignaturaRequestDto dto) {
        Asignatura existente = buscarEntidadPorId(id);
        existente.setNombre(dto.getNombre());
        if (dto.getCodigo() != null) {
            existente.setCodigo(dto.getCodigo());
        }
        if (dto.getProfesorId() != null) {
            Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + dto.getProfesorId()));
            if (existente.getProfesor() == null || !existente.getProfesor().getIdUsuario().equals(dto.getProfesorId())) {
                if (profesor.getAsignaturas() != null && profesor.getAsignaturas().size() >= Profesor.MAX_ASIGNATURAS) {
                    throw new ReglaNegocioException("El profesor ya tiene el límite máximo de " + Profesor.MAX_ASIGNATURAS + " asignaturas asignadas.");
                }
            }
            existente.setProfesor(profesor);
        }
        Asignatura actualizada = asignaturaRepository.save(existente);
        return mapper.toAsignaturaResponseDto(actualizada);
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponseDto> obtenerEstudiantesDeAsignatura(Long idAsignatura) {
        Asignatura asignatura = buscarEntidadPorId(idAsignatura);
        if (asignatura.getEstudiantes() == null) {
            return Collections.emptyList();
        }
        return asignatura.getEstudiantes().stream()
                .map(mapper::toEstudianteResponseDto)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        if (!asignaturaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Asignatura no existe con el ID: " + id);
        }
        asignaturaRepository.deleteById(id);
    }
}
