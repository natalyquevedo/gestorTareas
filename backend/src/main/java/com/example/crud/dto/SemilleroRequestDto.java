package com.example.crud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemilleroRequestDto {

    @NotBlank(message = "El nombre del semillero es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @Size(max = 30, message = "El código o sigla no puede superar los 30 caracteres")
    private String codigo;

    @Size(max = 200, message = "La línea de investigación no puede superar los 200 caracteres")
    private String lineaInvestigacion;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    private Long profesorId;
}
