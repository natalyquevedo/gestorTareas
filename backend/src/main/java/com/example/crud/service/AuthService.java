package com.example.crud.service;

import com.example.crud.dto.AuthResponseDto;
import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.RegisterRequestDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.ReglaNegocioException;
import com.example.crud.model.Coordinador;
import com.example.crud.model.Estudiante;
import com.example.crud.model.Profesor;
import com.example.crud.model.Usuario;
import com.example.crud.repository.CoordinadorRepository;
import com.example.crud.repository.EstudianteRepository;
import com.example.crud.repository.ProfesorRepository;
import com.example.crud.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ReglaNegocioException("Credenciales inválidas: correo no registrado."));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new ReglaNegocioException("Credenciales inválidas: contraseña incorrecta.");
        }

        return AuthResponseDto.builder()
                .mensaje("Inicio de sesión exitoso.")
                .token("demo-token-user-" + usuario.getIdUsuario())
                .usuario(mapper.toUsuarioResponseDto(usuario))
                .build();
    }

    public AuthResponseDto register(RegisterRequestDto request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ReglaNegocioException("Ya existe un usuario con el correo: " + request.getCorreo());
        }
        if (usuarioRepository.existsByDocumento(request.getDocumento())) {
            throw new ReglaNegocioException("Ya existe un usuario con el documento: " + request.getDocumento());
        }
        if (request.getCodigo() != null && usuarioRepository.existsByCodigo(request.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un usuario con el código: " + request.getCodigo());
        }

        String rol = request.getRol() != null ? request.getRol().trim().toUpperCase() : "ESTUDIANTE";
        Usuario nuevoUsuario;

        switch (rol) {
            case "ESTUDIANTE" -> {
                int edad = request.getEdad() != null ? request.getEdad() : 18;
                int semestre = request.getSemestre() != null ? request.getSemestre() : 1;
                if (edad < 14 || edad > 100) {
                    throw new ReglaNegocioException("La edad del estudiante debe estar entre 14 y 100 años.");
                }
                if (semestre < 1 || semestre > 12) {
                    throw new ReglaNegocioException("El semestre debe estar entre 1 y 12.");
                }
                Estudiante est = Estudiante.builder()
                        .nombre(request.getNombre())
                        .documento(request.getDocumento())
                        .codigo(request.getCodigo())
                        .correo(request.getCorreo())
                        .password(request.getPassword())
                        .rol("ESTUDIANTE")
                        .edad(edad)
                        .semestre(semestre)
                        .build();
                nuevoUsuario = estudianteRepository.save(est);
            }
            case "PROFESOR" -> {
                int edad = request.getEdad() != null ? request.getEdad() : 30;
                if (edad < 18 || edad > 100) {
                    throw new ReglaNegocioException("La edad mínima del docente es 18 años.");
                }
                Profesor prof = Profesor.builder()
                        .nombre(request.getNombre())
                        .documento(request.getDocumento())
                        .codigo(request.getCodigo())
                        .correo(request.getCorreo())
                        .password(request.getPassword())
                        .rol("PROFESOR")
                        .edad(edad)
                        .build();
                nuevoUsuario = profesorRepository.save(prof);
            }
            case "COORDINADOR" -> {
                Coordinador coord = Coordinador.builder()
                        .nombre(request.getNombre())
                        .documento(request.getDocumento())
                        .codigo(request.getCodigo())
                        .correo(request.getCorreo())
                        .password(request.getPassword())
                        .rol("COORDINADOR")
                        .build();
                nuevoUsuario = coordinadorRepository.save(coord);
            }
            default -> throw new ReglaNegocioException("Rol inválido: " + rol + ". Roles permitidos: ESTUDIANTE, PROFESOR, COORDINADOR.");
        }

        return AuthResponseDto.builder()
                .mensaje("Usuario registrado exitosamente con rol " + rol + ".")
                .token("demo-token-user-" + nuevoUsuario.getIdUsuario())
                .usuario(mapper.toUsuarioResponseDto(nuevoUsuario))
                .build();
    }
}
