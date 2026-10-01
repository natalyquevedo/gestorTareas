package com.example.crud.service;

import com.example.crud.dto.SemilleroRequestDto;
import com.example.crud.dto.SemilleroResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Estudiante;
import com.example.crud.model.Profesor;
import com.example.crud.model.Semillero;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.ProfesorRepository;
import com.example.crud.repository.SemilleroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SemilleroService {

    private final SemilleroRepository semilleroRepository;
    private final ProfesorRepository profesorRepository;
    private final EstudianteRepository estudianteRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<SemilleroResponseDto> listarTodos() {
        return semilleroRepository.findAll().stream()
                .map(mapper::toSemilleroResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SemilleroResponseDto obtenerPorId(Long id) {
        return mapper.toSemilleroResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Semillero buscarEntidadPorId(Long id) {
        return semilleroRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Semillero no encontrado con el ID: " + id));
    }

    @Transactional(readOnly = true)
    public SemilleroResponseDto obtenerPorTutor(Long idProfesor) {
        return semilleroRepository.findByProfesor_IdUsuario(idProfesor)
                .map(mapper::toSemilleroResponseDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró semillero asignado al docente con ID: " + idProfesor));
    }

    @Transactional(readOnly = true)
    public List<SemilleroResponseDto> listarPorTutor(Long idProfesor) {
        return semilleroRepository.findByProfesor_IdUsuario(idProfesor).stream()
                .map(mapper::toSemilleroResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SemilleroResponseDto obtenerPorEstudiante(Long idEstudiante) {
        return semilleroRepository.findByEstudiantes_IdUsuario(idEstudiante)
                .map(mapper::toSemilleroResponseDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("El estudiante no se encuentra vinculado a ningún semillero."));
    }

    @Transactional(readOnly = true)
    public List<SemilleroResponseDto> listarPorEstudiante(Long idEstudiante) {
        return semilleroRepository.findByEstudiantes_IdUsuario(idEstudiante).stream()
                .map(mapper::toSemilleroResponseDto)
                .collect(Collectors.toList());
    }

    public SemilleroResponseDto crear(SemilleroRequestDto dto) {
        if (semilleroRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ReglaNegocioException("Ya existe un semillero con el nombre: " + dto.getNombre());
        }

        if (dto.getCodigo() != null && !dto.getCodigo().isBlank()
                && semilleroRepository.existsByCodigoIgnoreCase(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un semillero con el código o sigla: " + dto.getCodigo());
        }

        Semillero semillero = mapper.toEntity(dto);

        if (dto.getProfesorId() != null) {
            Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + dto.getProfesorId()));

            // Regla: 1 docente = 1 semillero máximo
            if (semilleroRepository.existsByProfesor_IdUsuario(dto.getProfesorId())) {
                throw new ReglaNegocioException("El profesor ya tiene un semillero asignado. Solo se permite un semillero por docente.");
            }

            semillero.setProfesor(profesor);
        }

        Semillero guardado = semilleroRepository.save(semillero);
        return mapper.toSemilleroResponseDto(guardado);
    }

    public SemilleroResponseDto actualizar(Long id, SemilleroRequestDto dto) {
        Semillero existente = buscarEntidadPorId(id);

        if (!existente.getNombre().equalsIgnoreCase(dto.getNombre())
                && semilleroRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ReglaNegocioException("Ya existe un semillero con el nombre: " + dto.getNombre());
        }

        if (dto.getCodigo() != null && !dto.getCodigo().isBlank()
                && (existente.getCodigo() == null || !existente.getCodigo().equalsIgnoreCase(dto.getCodigo()))
                && semilleroRepository.existsByCodigoIgnoreCase(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un semillero con el código: " + dto.getCodigo());
        }

        existente.setNombre(dto.getNombre());
        existente.setCodigo(dto.getCodigo());
        existente.setLineaInvestigacion(dto.getLineaInvestigacion());
        existente.setDescripcion(dto.getDescripcion());

        if (dto.getProfesorId() != null) {
            if (existente.getProfesor() == null || !existente.getProfesor().getIdUsuario().equals(dto.getProfesorId())) {
                Profesor nuevoProfesor = profesorRepository.findById(dto.getProfesorId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + dto.getProfesorId()));

                // Regla: 1 docente = 1 semillero máximo
                if (semilleroRepository.existsByProfesor_IdUsuario(dto.getProfesorId())) {
                    throw new ReglaNegocioException("El profesor ya tiene un semillero asignado. Solo se permite un semillero por docente.");
                }

                existente.setProfesor(nuevoProfesor);
            }
        }

        Semillero actualizado = semilleroRepository.save(existente);
        return mapper.toSemilleroResponseDto(actualizado);
    }

    public SemilleroResponseDto asignarTutor(Long idSemillero, Long idProfesor) {
        Semillero semillero = buscarEntidadPorId(idSemillero);
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con el ID: " + idProfesor));

        if (semillero.getProfesor() != null && semillero.getProfesor().getIdUsuario().equals(idProfesor)) {
            throw new ReglaNegocioException("El docente ya es el tutor asignado a este semillero.");
        }

        // Regla: 1 docente = 1 semillero máximo
        if (semilleroRepository.existsByProfesor_IdUsuario(idProfesor)) {
            throw new ReglaNegocioException("El profesor ya tiene un semillero asignado. Solo se permite un semillero por docente.");
        }

        semillero.setProfesor(profesor);
        Semillero guardado = semilleroRepository.save(semillero);
        return mapper.toSemilleroResponseDto(guardado);
    }

    public SemilleroResponseDto inscribirSemillerista(Long idSemillero, Long idEstudiante) {
        Semillero semillero = buscarEntidadPorId(idSemillero);
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + idEstudiante));

        // Regla: Máximo 1 semillero por estudiante
        if (estudiante.getSemillero() != null) {
            if (estudiante.getSemillero().getIdSemillero().equals(idSemillero)) {
                throw new ReglaNegocioException("El estudiante ya se encuentra vinculado a este semillero.");
            } else {
                throw new ReglaNegocioException("El estudiante ya pertenece al semillero '"
                        + estudiante.getSemillero().getNombre() + "'. Solo puede pertenecer a un semillero como máximo.");
            }
        }

        if (semillero.getEstudiantes() != null && semillero.getEstudiantes().size() >= Semillero.MAX_ESTUDIANTES) {
            throw new ReglaNegocioException("El semillero ya alcanzó el límite máximo de " + Semillero.MAX_ESTUDIANTES + " semilleristas.");
        }

        estudiante.setSemillero(semillero);
        semillero.getEstudiantes().add(estudiante);

        estudianteRepository.save(estudiante);
        Semillero guardado = semilleroRepository.save(semillero);

        return mapper.toSemilleroResponseDto(guardado);
    }

    public SemilleroResponseDto desvincularSemillerista(Long idSemillero, Long idEstudiante) {
        Semillero semillero = buscarEntidadPorId(idSemillero);
        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con el ID: " + idEstudiante));

        if (estudiante.getSemillero() != null && estudiante.getSemillero().getIdSemillero().equals(idSemillero)) {
            estudiante.setSemillero(null);
            estudianteRepository.save(estudiante);
        }
        semillero.getEstudiantes().remove(estudiante);
        Semillero guardado = semilleroRepository.save(semillero);

        return mapper.toSemilleroResponseDto(guardado);
    }

    public void eliminar(Long id) {
        if (!semilleroRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Semillero no existe con el ID: " + id);
        }
        semilleroRepository.deleteById(id);
    }
}
