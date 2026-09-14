package com.example.crud.service;

import com.example.crud.dto.*;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Asignatura;
import com.example.crud.model.EstadoTarea;
import com.example.crud.model.Estudiante;
import com.example.crud.model.Profesor;
import com.example.crud.model.Tarea;
import com.example.crud.repository.AsignaturaRepository;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.ProfesorRepository;
import com.example.crud.repository.TareaRepository;
import com.example.crud.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceLayerTest {

    @Mock
    private TareaRepository tareaRepository;
    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private ProfesorRepository profesorRepository;
    @Mock
    private AsignaturaRepository asignaturaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @Spy
    private ModelDtoMapper mapper = new ModelDtoMapper();

    @InjectMocks
    private TareaService tareaService;
    @InjectMocks
    private AsignaturaService asignaturaService;
    @InjectMocks
    private EstudianteService estudianteService;
    @InjectMocks
    private ProfesorService profesorService;
    @InjectMocks
    private UsuarioService usuarioService;

    private Estudiante estudiante;
    private Profesor profesor;
    private Asignatura asignatura;
    private TareaRequestDto tareaDto;

    @BeforeEach
    void setUp() {
        estudiante = Estudiante.builder()
                .idUsuario(1L)
                .nombre("Estudiante 1")
                .documento("11111111")
                .codigo("EST-001")
                .edad(20)
                .semestre(3)
                .tareas(new ArrayList<>())
                .asignaturas(new ArrayList<>())
                .build();

        profesor = Profesor.builder()
                .idUsuario(2L)
                .nombre("Profesor 1")
                .documento("22222222")
                .codigo("DOC-001")
                .edad(40)
                .asignaturas(new ArrayList<>())
                .tareas(new ArrayList<>())
                .build();

        asignatura = Asignatura.builder()
                .idAsignatura(3L)
                .nombre("Estructuras de Datos")
                .codigo("ED-101")
                .estudiantes(new ArrayList<>())
                .tareas(new ArrayList<>())
                .build();

        tareaDto = TareaRequestDto.builder()
                .titulo("Laboratorio 1")
                .descripcion("Grafos y Arboles")
                .fechaEntrega(LocalDateTime.now().plusDays(2))
                .asignaturaId(3L)
                .profesorId(2L)
                .estudianteId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear tarea retornando TareaResponseDto correctamente")
    void testCrearTareaExitoso() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.findById(3L)).thenReturn(Optional.of(asignatura));
        when(tareaRepository.countByProfesor_IdUsuarioAndFechaCreacionBetween(any(), any(), any())).thenReturn(0L);
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(i -> {
            Tarea t = i.getArgument(0);
            t.setIdTarea(10L);
            return t;
        });

        TareaResponseDto response = tareaService.crearTarea(tareaDto, 3L, 2L, 1L);

        assertNotNull(response);
        assertEquals("Laboratorio 1", response.getTitulo());
        assertEquals("Estructuras de Datos", response.getAsignaturaNombre());
        assertEquals("Profesor 1", response.getProfesorNombre());
        assertEquals("Estudiante 1", response.getEstudianteNombre());
        assertEquals(EstadoTarea.PENDIENTE, response.getEstado());
        verify(tareaRepository, times(1)).save(any(Tarea.class));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException si el docente supera 4 tareas en la semana")
    void testCrearTareaLimiteDocenteExcedido() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.findById(3L)).thenReturn(Optional.of(asignatura));
        when(tareaRepository.countByProfesor_IdUsuarioAndFechaCreacionBetween(any(), any(), any())).thenReturn(4L);

        assertThrows(ReglaNegocioException.class, () ->
                tareaService.crearTarea(tareaDto, 3L, 2L, 1L));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException si el estudiante supera 50 tareas")
    void testCrearTareaLimiteEstudianteExcedido() {
        for (int i = 0; i < 50; i++) {
            estudiante.getTareas().add(Tarea.builder().idTarea((long) i).build());
        }
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(asignaturaRepository.findById(3L)).thenReturn(Optional.of(asignatura));

        assertThrows(ReglaNegocioException.class, () ->
                tareaService.crearTarea(tareaDto, 3L, 2L, 1L));
    }

    @Test
    @DisplayName("Debe actualizar Asignatura con AsignaturaRequestDto")
    void testActualizarAsignatura() {
        when(asignaturaRepository.findById(3L)).thenReturn(Optional.of(asignatura));
        when(asignaturaRepository.save(any(Asignatura.class))).thenAnswer(i -> i.getArgument(0));

        AsignaturaRequestDto nuevosDatos = AsignaturaRequestDto.builder()
                .nombre("Algoritmos Avanzados")
                .codigo("ALG-202")
                .build();
        AsignaturaResponseDto actualizada = asignaturaService.actualizar(3L, nuevosDatos);

        assertEquals("Algoritmos Avanzados", actualizada.getNombre());
        assertEquals("ALG-202", actualizada.getCodigo());
    }

    @Test
    @DisplayName("Debe actualizar Estudiante con EstudianteRequestDto")
    void testActualizarEstudiante() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(estudianteRepository.save(any(Estudiante.class))).thenAnswer(i -> i.getArgument(0));

        EstudianteRequestDto nuevosDatos = EstudianteRequestDto.builder()
                .nombre("Nombre Modificado")
                .documento("99999999")
                .codigo("EST-MOD")
                .edad(22)
                .semestre(5)
                .correo("nuevo@correo.com")
                .password("nuevaClave")
                .build();

        EstudianteResponseDto actualizado = estudianteService.actualizar(1L, nuevosDatos);

        assertEquals("Nombre Modificado", actualizado.getNombre());
        assertEquals("99999999", actualizado.getDocumento());
        assertEquals("EST-MOD", actualizado.getCodigo());
        assertEquals(22, actualizado.getEdad());
        assertEquals(5, actualizado.getSemestre());
        assertEquals("nuevo@correo.com", actualizado.getCorreo());
    }

    @Test
    @DisplayName("Debe actualizar Profesor con ProfesorRequestDto")
    void testActualizarProfesor() {
        when(profesorRepository.findById(2L)).thenReturn(Optional.of(profesor));
        when(profesorRepository.save(any(Profesor.class))).thenAnswer(i -> i.getArgument(0));

        ProfesorRequestDto nuevosDatos = ProfesorRequestDto.builder()
                .nombre("Dr. Carlos Ruiz")
                .documento("88888888")
                .codigo("DOC-999")
                .edad(48)
                .correo("carlos.ruiz@universidad.edu.co")
                .password("docentePass")
                .build();

        ProfesorResponseDto actualizado = profesorService.actualizar(2L, nuevosDatos);

        assertEquals("Dr. Carlos Ruiz", actualizado.getNombre());
        assertEquals("88888888", actualizado.getDocumento());
        assertEquals("DOC-999", actualizado.getCodigo());
        assertEquals(48, actualizado.getEdad());
        assertEquals("carlos.ruiz@universidad.edu.co", actualizado.getCorreo());
    }

    @Test
    @DisplayName("Debe consultar Usuario por ID y retornar UsuarioResponseDto")
    void testObtenerUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        UsuarioResponseDto encontrado = usuarioService.obtenerPorId(1L);
        assertEquals(1L, encontrado.getIdUsuario());
        assertEquals("Estudiante 1", encontrado.getNombre());

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.obtenerPorId(99L));
    }
}
