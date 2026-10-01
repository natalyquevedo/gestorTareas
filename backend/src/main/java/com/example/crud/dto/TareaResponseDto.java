package com.example.crud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaResponseDto {

    private Long idTarea;
    private String titulo;
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaEntrega;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    private String prioridad;
    private String estado;
    private Double calificacion;
    private String material;
    private String observaciones;

    private Long semilleroId;
    private String semilleroNombre;

    private Long profesorId;
    private String profesorNombre;

    private Long estudianteId;
    private String estudianteNombre;

    public String getNombre() {
        return this.titulo;
    }
}
