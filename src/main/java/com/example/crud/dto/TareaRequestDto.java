package com.example.crud.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaRequestDto {

    @NotBlank(message = "El nombre o título de la tarea es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @JsonAlias({"nombre"})
    private String titulo;

    @NotBlank(message = "La descripción de la tarea es obligatoria")
    @Size(min = 3, max = 500, message = "La descripción debe tener entre 3 y 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha y hora de entrega es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEntrega;

    private String prioridad;

    @Size(max = 255, message = "El material o enlace no puede superar los 255 caracteres")
    private String material;

    private Long asignaturaId;
    private Long profesorId;
    private Long estudianteId;
}
