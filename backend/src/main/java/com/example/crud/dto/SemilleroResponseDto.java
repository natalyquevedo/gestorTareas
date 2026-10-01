package com.example.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemilleroResponseDto {

    private Long idSemillero;
    private String nombre;
    private String codigo;
    private String lineaInvestigacion;
    private String descripcion;

    private Long profesorId;
    private String profesorNombre;

    private int totalEstudiantes;
    private int totalTareas;

    private List<Long> estudiantesIds;
}
