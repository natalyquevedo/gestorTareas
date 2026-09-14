package com.example.crud.controller;

import com.example.crud.dto.TareaRequestDto;
import com.example.crud.dto.TareaResponseDto;
import com.example.crud.exception.GlobalExceptionHandler;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.EstadoTarea;
import com.example.crud.service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionTest {

    private MockMvc mockMvc;

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tareaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Debe responder 404 con estructura ErrorResponse cuando el recurso no existe")
    void testRecursoNoEncontrado() throws Exception {
        when(tareaService.obtenerPorId(999L))
                .thenThrow(new RecursoNoEncontradoException("Tarea no encontrada con el ID: 999"));

        mockMvc.perform(get("/api/tareas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Tarea no encontrada con el ID: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Debe responder 400 cuando se violan validaciones @Valid de TareaRequestDto")
    void testValidacionCamposFallida() throws Exception {
        String jsonInvalido = """
                {
                    "titulo": "",
                    "descripcion": "",
                    "fechaEntrega": null
                }
                """;

        mockMvc.perform(post("/api/tareas")
                        .param("asignaturaId", "1")
                        .param("profesorId", "1")
                        .param("estudianteId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Error de Validación de Datos"))
                .andExpect(jsonPath("$.errors").isMap());
    }

    @Test
    @DisplayName("Debe responder 400 con ReglaNegocioException cuando se excede límite")
    void testReglaNegocioException() throws Exception {
        String jsonValido = """
                {
                    "titulo": "Tarea Nueva",
                    "descripcion": "Descripcion de tarea valida",
                    "fechaEntrega": "2026-10-10 12:00:00"
                }
                """;

        when(tareaService.crearTarea(org.mockito.ArgumentMatchers.any(TareaRequestDto.class), org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.eq(1L)))
                .thenThrow(new ReglaNegocioException("El docente ya ha alcanzado el límite máximo de 4 tareas por semana."));

        mockMvc.perform(post("/api/tareas")
                        .param("asignaturaId", "1")
                        .param("profesorId", "1")
                        .param("estudianteId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Violación de Regla de Negocio"))
                .andExpect(jsonPath("$.message").value("El docente ya ha alcanzado el límite máximo de 4 tareas por semana."));
    }
}
