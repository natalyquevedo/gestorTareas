package com.example.crud.service;

import com.example.crud.dto.ProfesorRequestDto;
import com.example.crud.dto.ProfesorResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<ProfesorResponseDto> listarTodos() {
        return profesorRepository.findAll().stream()
                .map(mapper::toProfesorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfesorResponseDto obtenerPorId(Long id) {
        return mapper.toProfesorResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Profesor buscarEntidadPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + id));
    }

    public ProfesorResponseDto guardar(ProfesorRequestDto dto) {
        Profesor profesor = mapper.toEntity(dto);
        Profesor guardado = profesorRepository.save(profesor);
        return mapper.toProfesorResponseDto(guardado);
    }

    public ProfesorResponseDto actualizar(Long id, ProfesorRequestDto dto) {
        Profesor existente = buscarEntidadPorId(id);

        existente.setNombre(dto.getNombre());
        existente.setDocumento(dto.getDocumento());
        existente.setCodigo(dto.getCodigo());
        existente.setEdad(dto.getEdad());
        existente.setCorreo(dto.getCorreo());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existente.setPassword(dto.getPassword());
        }

        Profesor actualizado = profesorRepository.save(existente);
        return mapper.toProfesorResponseDto(actualizado);
    }

    public ProfesorResponseDto asignarAsignatura(Long idProfesor, Long idAsignatura) {
        Profesor profesor = buscarEntidadPorId(idProfesor);
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura no encontrada con el ID: " + idAsignatura));

        // Regla 2: Límite máximo de 4 asignaturas por profesor
        if (asignatura.getProfesor() != null && asignatura.getProfesor().getIdUsuario().equals(idProfesor)) { throw new ReglaNegocioException("La asignatura ya se encuentra asignada a este docente."); } if (profesor.getAsignaturas() != null && profesor.getAsignaturas().size() >= Profesor.MAX_ASIGNATURAS) {
            throw new ReglaNegocioException("El profesor ya tiene el límite máximo de " + Profesor.MAX_ASIGNATURAS + " asignaturas asignadas.");
        }

        asignatura.setProfesor(profesor);
        asignaturaRepository.save(asignatura);

        Profesor recargado = buscarEntidadPorId(idProfesor);
        return mapper.toProfesorResponseDto(recargado);
    }

    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Profesor no existe con el ID: " + id);
        }
        profesorRepository.deleteById(id);
    }
}
