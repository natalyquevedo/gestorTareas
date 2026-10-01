package com.example.crud.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteRequestDto {

    @NotBlank(message = "El nombre del estudiante es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    private String documento;

    @NotBlank(message = "El código de estudiante es obligatorio")
    @Size(min = 4, max = 20, message = "El código debe tener entre 4 y 20 caracteres")
    private String codigo;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 14, message = "La edad mínima es 14 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    private Integer edad;

    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 12, message = "El semestre máximo es 12")
    private Integer semestre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
