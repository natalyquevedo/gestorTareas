package com.example.crud.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para validar las entidades de dominio puro (POJOs / JPA Entities)
 * verificando campos, getters, setters, valores por defecto y relaciones.
 */
class ModelDomainTest {

    @Test
    @DisplayName("Debe instanciar y validar campos de Estudiante")
    void testEstudianteEntity() {
        Estudiante estudiante = Estudiante.builder()
                .idEstudiante(1L)
                .nombre("Juan Perez")
                .documento("10203040")
                .codigo("2026101")
                .edad(20)
                .semestre(3)
                .correo("juan@correo.com")
                .build();

        assertEquals(1L, estudiante.getIdEstudiante());
        assertEquals("Juan Perez", estudiante.getNombre());
        assertEquals("10203040", estudiante.getDocumento());
        assertEquals("2026101", estudiante.getCodigo());
        assertEquals(20, estudiante.getEdad());
        assertEquals(3, estudiante.getSemestre());
        assertEquals("ESTUDIANTE", estudiante.getRol());
        assertNotNull(estudiante.getTareas());
        assertNotNull(estudiante.getAsignaturas());
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de Profesor")
    void testProfesorEntity() {
        Profesor profesor = Profesor.builder()
                .idProfesor(10L)
                .id("DOC-101")
                .nombre("Dra. Eva Vasquez")
                .documento("52001122")
                .edad(42)
                .correo("eva@correo.com")
                .build();

        assertEquals(10L, profesor.getIdProfesor());
        assertEquals("DOC-101", profesor.getId());
        assertEquals("Dra. Eva Vasquez", profesor.getNombre());
        assertEquals("52001122", profesor.getDocumento());
        assertEquals(42, profesor.getEdad());
        assertEquals("PROFESOR", profesor.getRol());
        assertNotNull(profesor.getAsignaturas());
    }

    @Test
    @DisplayName("Debe instanciar y validar campos de TareaAcademica")
    void testTareaAcademicaEntity() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(2);

        TareaAcademica tarea = TareaAcademica.builder()
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
        assertEquals("PENDIENTE", tarea.getEstado());
        assertEquals("MEDIA", tarea.getPrioridad());
        assertEquals(4.5, tarea.getCalificacion());
        assertEquals("lectura1.pdf", tarea.getMaterial());
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
    @DisplayName("Debe instanciar y validar campos de Coordinador")
    void testCoordinadorEntity() {
        Coordinador coordinador = Coordinador.builder()
                .idCoordinador(1L)
                .nombre("Admin General")
                .documento("999888777")
                .correo("admin@universidad.edu.co")
                .build();

        assertEquals(1L, coordinador.getIdCoordinador());
        assertEquals("Admin General", coordinador.getNombre());
        assertEquals("999888777", coordinador.getDocumento());
        assertEquals("COORDINADOR", coordinador.getRol());
    }
}
