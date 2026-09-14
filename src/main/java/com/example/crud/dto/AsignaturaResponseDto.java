package com.example.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignaturaResponseDto {
    private Long idAsignatura;
    private String nombre;
    private String codigo;
    private Long profesorId;
    private String profesorNombre;
    private Integer totalEstudiantes;
}
