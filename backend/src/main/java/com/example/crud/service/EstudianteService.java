package com.example.crud.service;

import com.example.crud.dto.EstudianteRequestDto;
import com.example.crud.dto.EstudianteResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Estudiante;
import com.example.crud.model.Semillero;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.SemilleroRepository;
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
    private final SemilleroRepository semilleroRepository;
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

    public EstudianteResponseDto inscribirSemillero(Long idEstudiante, Long idSemillero) {
        Estudiante estudiante = buscarEntidadPorId(idEstudiante);
        Semillero semillero = semilleroRepository.findById(idSemillero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Semillero no encontrado con el ID: " + idSemillero));

        // Regla: 1 semillero máximo por estudiante
        if (estudiante.getSemillero() != null) {
            if (estudiante.getSemillero().getIdSemillero().equals(idSemillero)) {
                throw new ReglaNegocioException("El estudiante ya se encuentra vinculado a este semillero.");
            } else {
                throw new ReglaNegocioException("El estudiante ya pertenece al semillero '"
                        + estudiante.getSemillero().getNombre() + "'. Solo puede pertenecer a un semillero como máximo.");
            }
        }

        if (semillero.getEstudiantes() != null && semillero.getEstudiantes().size() >= Semillero.MAX_ESTUDIANTES) {
            throw new ReglaNegocioException("El semillero ya alcanzó el límite máximo de " + Semillero.MAX_ESTUDIANTES + " estudiantes.");
        }

        estudiante.setSemillero(semillero);
        semillero.getEstudiantes().add(estudiante);

        Estudiante guardado = estudianteRepository.save(estudiante);
        semilleroRepository.save(semillero);
        return mapper.toEstudianteResponseDto(guardado);
    }

    public EstudianteResponseDto desvincularSemillero(Long idEstudiante, Long idSemillero) {
        Estudiante estudiante = buscarEntidadPorId(idEstudiante);
        Semillero semillero = semilleroRepository.findById(idSemillero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Semillero no encontrado con el ID: " + idSemillero));

        if (estudiante.getSemillero() != null && estudiante.getSemillero().getIdSemillero().equals(idSemillero)) {
            estudiante.setSemillero(null);
        }
        semillero.getEstudiantes().remove(estudiante);

        Estudiante guardado = estudianteRepository.save(estudiante);
        semilleroRepository.save(semillero);
        return mapper.toEstudianteResponseDto(guardado);
    }

    public void eliminar(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estudiante no existe con el ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}
