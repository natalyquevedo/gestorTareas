package com.example.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsuarioResponseDto {
    private Long idUsuario;
    private String nombre;
    private String documento;
    private String codigo;
    private String correo;
    private String rol;
}
