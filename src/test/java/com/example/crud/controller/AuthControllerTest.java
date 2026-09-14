package com.example.crud.controller;

import com.example.crud.dto.AuthResponseDto;
import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.RegisterRequestDto;
import com.example.crud.dto.UsuarioResponseDto;
import com.example.crud.exception.GlobalExceptionHandler;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Debe responder 200 OK en login exitoso")
    void testLoginExitoso() throws Exception {
        AuthResponseDto response = AuthResponseDto.builder()
                .mensaje("Inicio de sesión exitoso.")
                .token("token-xyz")
                .usuario(UsuarioResponseDto.builder()
                        .idUsuario(1L)
                        .nombre("Ana Lopez")
                        .correo("ana@correo.com")
                        .rol("ESTUDIANTE")
                        .build())
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(response);

        String json = """
                {
                    "correo": "ana@correo.com",
                    "password": "secretPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Inicio de sesión exitoso."))
                .andExpect(jsonPath("$.usuario.nombre").value("Ana Lopez"))
                .andExpect(jsonPath("$.usuario.password").doesNotExist());
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request si login tiene correo vacío")
    void testLoginInvalido() throws Exception {
        String json = """
                {
                    "correo": "",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Debe responder 201 Created en registro exitoso")
    void testRegisterExitoso() throws Exception {
        AuthResponseDto response = AuthResponseDto.builder()
                .mensaje("Usuario registrado exitosamente con rol PROFESOR.")
                .token("token-prof")
                .usuario(UsuarioResponseDto.builder()
                        .idUsuario(2L)
                        .nombre("Profesor Gomez")
                        .correo("gomez@correo.com")
                        .rol("PROFESOR")
                        .build())
                .build();

        when(authService.register(any(RegisterRequestDto.class))).thenReturn(response);

        String json = """
                {
                    "nombre": "Profesor Gomez",
                    "documento": "88776655",
                    "codigo": "DOC-77",
                    "correo": "gomez@correo.com",
                    "password": "profePassword",
                    "rol": "PROFESOR",
                    "edad": 38
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.correo").value("gomez@correo.com"))
                .andExpect(jsonPath("$.usuario.password").doesNotExist());
    }
}
