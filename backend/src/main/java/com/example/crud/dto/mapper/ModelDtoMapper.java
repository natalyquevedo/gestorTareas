package com.example.crud.dto.mapper;

import com.example.crud.dto.*;
import com.example.crud.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ModelDtoMapper {

    public UsuarioResponseDto toUsuarioResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        if (usuario instanceof Estudiante estudiante) {
            return toEstudianteResponseDto(estudiante);
        } else if (usuario instanceof Profesor profesor) {
            return toProfesorResponseDto(profesor);
        } else if (usuario instanceof Coordinador coordinador) {
            return toCoordinadorResponseDto(coordinador);
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setDocumento(usuario.getDocumento());
        dto.setCodigo(usuario.getCodigo());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol());
        return dto;
    }

    public EstudianteResponseDto toEstudianteResponseDto(Estudiante estudiante) {
        if (estudiante == null) return null;

        EstudianteResponseDto dto = new EstudianteResponseDto();
        dto.setIdUsuario(estudiante.getIdUsuario());
        dto.setNombre(estudiante.getNombre());
        dto.setDocumento(estudiante.getDocumento());
        dto.setCodigo(estudiante.getCodigo());
        dto.setCorreo(estudiante.getCorreo());
        dto.setRol(estudiante.getRol());
        dto.setEdad(estudiante.getEdad());
        dto.setSemestre(estudiante.getSemestre());
        if (estudiante.getSemillero() != null) {
            dto.setSemilleroId(estudiante.getSemillero().getIdSemillero());
            dto.setSemilleroNombre(estudiante.getSemillero().getNombre());
        }
        dto.setTotalTareas(estudiante.getTareas() != null ? estudiante.getTareas().size() : 0);
        return dto;
    }

    public ProfesorResponseDto toProfesorResponseDto(Profesor profesor) {
        if (profesor == null) return null;

        ProfesorResponseDto dto = new ProfesorResponseDto();
        dto.setIdUsuario(profesor.getIdUsuario());
        dto.setNombre(profesor.getNombre());
        dto.setDocumento(profesor.getDocumento());
        dto.setCodigo(profesor.getCodigo());
        dto.setCorreo(profesor.getCorreo());
        dto.setRol(profesor.getRol());
        dto.setEdad(profesor.getEdad());
        if (profesor.getSemillero() != null) {
            dto.setSemilleroId(profesor.getSemillero().getIdSemillero());
            dto.setSemilleroNombre(profesor.getSemillero().getNombre());
        }
        dto.setTotalTareasAsignadas(profesor.getTareas() != null ? profesor.getTareas().size() : 0);
        return dto;
    }

    public CoordinadorResponseDto toCoordinadorResponseDto(Coordinador coordinador) {
        if (coordinador == null) return null;

        CoordinadorResponseDto dto = new CoordinadorResponseDto();
        dto.setIdUsuario(coordinador.getIdUsuario());
        dto.setNombre(coordinador.getNombre());
        dto.setDocumento(coordinador.getDocumento());
        dto.setCodigo(coordinador.getCodigo());
        dto.setCorreo(coordinador.getCorreo());
        dto.setRol(coordinador.getRol());
        return dto;
    }

    public SemilleroResponseDto toSemilleroResponseDto(Semillero semillero) {
        if (semillero == null) return null;

        SemilleroResponseDto dto = new SemilleroResponseDto();
        dto.setIdSemillero(semillero.getIdSemillero());
        dto.setNombre(semillero.getNombre());
        dto.setCodigo(semillero.getCodigo());
        dto.setLineaInvestigacion(semillero.getLineaInvestigacion());
        dto.setDescripcion(semillero.getDescripcion());
        dto.setProfesorId(semillero.getProfesor() != null ? semillero.getProfesor().getIdUsuario() : null);
        dto.setProfesorNombre(semillero.getProfesor() != null ? semillero.getProfesor().getNombre() : null);
        dto.setTotalEstudiantes(semillero.getEstudiantes() != null ? semillero.getEstudiantes().size() : 0);
        dto.setTotalTareas(semillero.getTareas() != null ? semillero.getTareas().size() : 0);
        dto.setEstudiantesIds(semillero.getEstudiantes() != null
                ? semillero.getEstudiantes().stream().map(e -> e.getIdUsuario()).collect(Collectors.toList())
                : Collections.emptyList());
        return dto;
    }

    public TareaResponseDto toTareaResponseDto(Tarea tarea) {
        if (tarea == null) return null;

        TareaResponseDto dto = new TareaResponseDto();
        dto.setIdTarea(tarea.getIdTarea());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setFechaEntrega(tarea.getFechaEntrega());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setEstado(tarea.getEstado());
        dto.setCalificacion(tarea.getCalificacion());
        dto.setMaterial(tarea.getMaterial());
        dto.setObservaciones(tarea.getObservaciones());
        dto.setSemilleroId(tarea.getSemillero() != null ? tarea.getSemillero().getIdSemillero() : null);
        dto.setSemilleroNombre(tarea.getSemillero() != null ? tarea.getSemillero().getNombre() : null);
        dto.setProfesorId(tarea.getProfesor() != null ? tarea.getProfesor().getIdUsuario() : null);
        dto.setProfesorNombre(tarea.getProfesor() != null ? tarea.getProfesor().getNombre() : null);
        dto.setEstudianteId(tarea.getEstudiante() != null ? tarea.getEstudiante().getIdUsuario() : null);
        dto.setEstudianteNombre(tarea.getEstudiante() != null ? tarea.getEstudiante().getNombre() : null);
        return dto;
    }

    public Estudiante toEntity(EstudianteRequestDto dto) {
        if (dto == null) return null;

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(dto.getNombre());
        estudiante.setDocumento(dto.getDocumento());
        estudiante.setCodigo(dto.getCodigo());
        estudiante.setEdad(dto.getEdad());
        estudiante.setSemestre(dto.getSemestre());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setPassword(dto.getPassword());
        estudiante.setRol("ESTUDIANTE");
        return estudiante;
    }

    public Profesor toEntity(ProfesorRequestDto dto) {
        if (dto == null) return null;

        Profesor profesor = new Profesor();
        profesor.setNombre(dto.getNombre());
        profesor.setDocumento(dto.getDocumento());
        profesor.setCodigo(dto.getCodigo());
        profesor.setEdad(dto.getEdad());
        profesor.setCorreo(dto.getCorreo());
        profesor.setPassword(dto.getPassword());
        profesor.setRol("PROFESOR");
        return profesor;
    }

    public Coordinador toEntity(CoordinadorRequestDto dto) {
        if (dto == null) return null;

        Coordinador coordinador = new Coordinador();
        coordinador.setNombre(dto.getNombre());
        coordinador.setDocumento(dto.getDocumento());
        coordinador.setCodigo(dto.getCodigo());
        coordinador.setCorreo(dto.getCorreo());
        coordinador.setPassword(dto.getPassword());
        coordinador.setRol("COORDINADOR");
        return coordinador;
    }

    public Semillero toEntity(SemilleroRequestDto dto) {
        if (dto == null) return null;

        Semillero semillero = new Semillero();
        semillero.setNombre(dto.getNombre());
        semillero.setCodigo(dto.getCodigo());
        semillero.setLineaInvestigacion(dto.getLineaInvestigacion());
        semillero.setDescripcion(dto.getDescripcion());
        return semillero;
    }

    public Tarea toEntity(TareaRequestDto dto) {
        if (dto == null) return null;

        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFechaEntrega(dto.getFechaEntrega());
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setPrioridad(dto.getPrioridad() != null && !dto.getPrioridad().isBlank() ? dto.getPrioridad() : "MEDIA");
        tarea.setMaterial(dto.getMaterial());
        tarea.setEstado(Tarea.ESTADO_PENDIENTE);
        return tarea;
    }
}
