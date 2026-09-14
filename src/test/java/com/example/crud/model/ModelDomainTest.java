package com.example.crud.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar las entidades de dominio y herencia
 * verificando campos, getters, setters, valores por defecto y relaciones.
 */
class ModelDomainTest {

    @Test
    @DisplayName("Debe instanciar y validar campos de Estudiante con herencia de Usuario")
    void testEstudianteEntity() {
        Estudiante estudiante = Estudiante.builder()
                .idUsuario(1L)
                .nombre("Juan Perez")
                .documento("10203040")
                .codigo("2026101")
                .edad(20)
                .semestre(3)
                .correo("juan@correo.com")
                .rol("ESTUDIANTE")
                .build();

        assertEquals(1L, estudiante.getIdUsuario());
        assertEquals(1L, estudiante.getIdEstudiante());
        assertEquals("Juan Perez", estudiante.getNombre());
        assertEquals("10203040", estudiante.getDocumento());
        assertEquals("2026101", estudiante.getCodigo());
        assertEquals(20, estudiante.getEdad());
        assertEquals(3, estudiante.getSemestre());
        assertEquals("ESTUDIANTE", estudiante.getRol());
        assertNotNull(estudiante.getTareas());
        assertNotNull(estudiante.getAsignaturas());
        assertTrue(estudiante instanceof Usuario);
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de Profesor con herencia de Usuario")
    void testProfesorEntity() {
        Profesor profesor = Profesor.builder()
                .idUsuario(10L)
                .codigo("DOC-101")
                .nombre("Dra. Eva Vasquez")
                .documento("52001122")
                .edad(42)
                .correo("eva@correo.com")
                .rol("PROFESOR")
                .build();

        assertEquals(10L, profesor.getIdUsuario());
        assertEquals(10L, profesor.getIdProfesor());
        assertEquals("DOC-101", profesor.getCodigo());
        assertEquals("Dra. Eva Vasquez", profesor.getNombre());
        assertEquals("52001122", profesor.getDocumento());
        assertEquals(42, profesor.getEdad());
        assertEquals("PROFESOR", profesor.getRol());
        assertNotNull(profesor.getAsignaturas());
        assertNotNull(profesor.getTareas());
        assertTrue(profesor instanceof Usuario);
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de Tarea")
    void testTareaEntity() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(2);

        Tarea tarea = Tarea.builder()
                .idTarea(100L)
                .titulo("Taller 1")
                .descripcion("Construir capa model")
                .fechaEntrega(fecha)
                .calificacion(4.5)
                .material("lectura1.pdf")
                .build();

        assertEquals(100L, tarea.getIdTarea());
        assertEquals("Taller 1", tarea.getTitulo());
        assertEquals("Construir capa model", tarea.getDescripcion());
        assertEquals(fecha, tarea.getFechaEntrega());
        assertEquals(EstadoTarea.PENDIENTE, tarea.getEstado());
        assertEquals("MEDIA", tarea.getPrioridad());
        assertEquals(4.5, tarea.getCalificacion());
        assertEquals("lectura1.pdf", tarea.getMaterial());
        assertNotNull(tarea.getFechaCreacion());
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de Asignatura")
    void testAsignaturaEntity() {
        Asignatura asignatura = Asignatura.builder()
                .idAsignatura(5L)
                .nombre("Programación Orientada a Objetos")
                .codigo("POO-301")
                .build();

        assertEquals(5L, asignatura.getIdAsignatura());
        assertEquals("Programación Orientada a Objetos", asignatura.getNombre());
        assertEquals("POO-301", asignatura.getCodigo());
        assertNotNull(asignatura.getTareas());
        assertNotNull(asignatura.getEstudiantes());
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de Coordinador con herencia de Usuario")
    void testCoordinadorEntity() {
        Coordinador coordinador = Coordinador.builder()
                .idUsuario(1L)
                .nombre("Admin General")
                .documento("999888777")
                .codigo("COORD-01")
                .correo("admin@universidad.edu.co")
                .rol("COORDINADOR")
                .build();

        assertEquals(1L, coordinador.getIdUsuario());
        assertEquals(1L, coordinador.getIdCoordinador());
        assertEquals("Admin General", coordinador.getNombre());
        assertEquals("999888777", coordinador.getDocumento());
        assertEquals("COORD-01", coordinador.getCodigo());
        assertEquals("COORDINADOR", coordinador.getRol());
        assertTrue(coordinador instanceof Usuario);
    }
}
