package com.example.crud.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorRequestDto {

    @NotBlank(message = "El nombre del profesor es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    private String documento;

    @NotBlank(message = "El código de docente es obligatorio")
    @Size(min = 3, max = 20, message = "El código debe tener entre 3 y 20 caracteres")
    @JsonAlias({"id", "codigoDocente"})
    private String codigo;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima de un docente es 18 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    private Integer edad;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
