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
        dto.setAsignaturasIds(estudiante.getAsignaturas() != null
                ? estudiante.getAsignaturas().stream().map(Asignatura::getIdAsignatura).collect(Collectors.toList())
                : Collections.emptyList());
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
        dto.setAsignaturasIds(profesor.getAsignaturas() != null
                ? profesor.getAsignaturas().stream().map(Asignatura::getIdAsignatura).collect(Collectors.toList())
                : Collections.emptyList());
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

    public AsignaturaResponseDto toAsignaturaResponseDto(Asignatura asignatura) {
        if (asignatura == null) return null;

        AsignaturaResponseDto dto = new AsignaturaResponseDto();
        dto.setIdAsignatura(asignatura.getIdAsignatura());
        dto.setNombre(asignatura.getNombre());
        dto.setCodigo(asignatura.getCodigo());
        dto.setProfesorId(asignatura.getProfesor() != null ? asignatura.getProfesor().getIdUsuario() : null);
        dto.setProfesorNombre(asignatura.getProfesor() != null ? asignatura.getProfesor().getNombre() : null);
        dto.setTotalEstudiantes(asignatura.getEstudiantes() != null ? asignatura.getEstudiantes().size() : 0);
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
        dto.setAsignaturaId(tarea.getAsignatura() != null ? tarea.getAsignatura().getIdAsignatura() : null);
        dto.setAsignaturaNombre(tarea.getAsignatura() != null ? tarea.getAsignatura().getNombre() : null);
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

    public Asignatura toEntity(AsignaturaRequestDto dto) {
        if (dto == null) return null;

        Asignatura asignatura = new Asignatura();
        asignatura.setNombre(dto.getNombre());
        asignatura.setCodigo(dto.getCodigo());
        return asignatura;
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
        tarea.setEstado(EstadoTarea.PENDIENTE);
        return tarea;
    }
}
