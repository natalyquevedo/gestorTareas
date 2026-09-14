package com.example.crud.service;

import com.example.crud.dto.EstudianteRequestDto;
import com.example.crud.dto.EstudianteResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Asignatura;
import com.example.crud.model.Estudiante;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<EstudianteResponseDto> listarTodos() {
        return estudianteRepository.findAll().stream()
                .map(mapper::toEstudianteResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDto obtenerPorId(Long id) {
        return mapper.toEstudianteResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Estudiante buscarEntidadPorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + id));
    }

    public EstudianteResponseDto guardar(EstudianteRequestDto dto) {
        if (usuarioRepository.existsByDocumento(dto.getDocumento())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el documento: " + dto.getDocumento());
        }
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el correo: " + dto.getCorreo());
        }
        if (dto.getCodigo() != null && usuarioRepository.existsByCodigo(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el código: " + dto.getCodigo());
        }

        Estudiante estudiante = mapper.toEntity(dto);
        Estudiante guardado = estudianteRepository.save(estudiante);
        return mapper.toEstudianteResponseDto(guardado);
    }

    public EstudianteResponseDto actualizar(Long id, EstudianteRequestDto dto) {
        Estudiante existente = buscarEntidadPorId(id);

        if (!existente.getDocumento().equals(dto.getDocumento()) && usuarioRepository.existsByDocumento(dto.getDocumento())) {
            throw new ReglaNegocioException("Ya existe otro usuario con el documento: " + dto.getDocumento());
        }
        if (!existente.getCorreo().equals(dto.getCorreo()) && usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ReglaNegocioException("Ya existe otro usuario con el correo: " + dto.getCorreo());
        }
        if (dto.getCodigo() != null && !dto.getCodigo().equals(existente.getCodigo()) && usuarioRepository.existsByCodigo(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe otro usuario con el código: " + dto.getCodigo());
        }

        existente.setNombre(dto.getNombre());
        existente.setDocumento(dto.getDocumento());
        existente.setCodigo(dto.getCodigo());
        existente.setEdad(dto.getEdad());
        existente.setSemestre(dto.getSemestre());
        existente.setCorreo(dto.getCorreo());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existente.setPassword(dto.getPassword());
        }

        Estudiante actualizado = estudianteRepository.save(existente);
        return mapper.toEstudianteResponseDto(actualizado);
    }

    public EstudianteResponseDto inscribirAsignatura(Long idEstudiante, Long idAsignatura) {
        Estudiante estudiante = buscarEntidadPorId(idEstudiante);
        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura no encontrada con el ID: " + idAsignatura));

        if (estudiante.getAsignaturas() != null && estudiante.getAsignaturas().contains(asignatura)) {
            throw new ReglaNegocioException("El estudiante ya se encuentra matriculado en esta asignatura.");
        }

        if (estudiante.getAsignaturas() != null && estudiante.getAsignaturas().size() >= Estudiante.MAX_ASIGNATURAS) {
            throw new ReglaNegocioException("El estudiante no puede matricular más de " + Estudiante.MAX_ASIGNATURAS + " asignaturas.");
        }

        if (asignatura.getEstudiantes() != null && asignatura.getEstudiantes().size() >= Asignatura.MAX_ESTUDIANTES) {
            throw new ReglaNegocioException("La asignatura ya alcanzó el límite máximo de " + Asignatura.MAX_ESTUDIANTES + " estudiantes.");
        }

        estudiante.getAsignaturas().add(asignatura);
        Estudiante guardado = estudianteRepository.save(estudiante);
        return mapper.toEstudianteResponseDto(guardado);
    }

    public void eliminar(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estudiante no existe con el ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}
