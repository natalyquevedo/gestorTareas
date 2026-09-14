package com.example.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EstudianteResponseDto extends UsuarioResponseDto {
    private Integer edad;
    private Integer semestre;
    private List<Long> asignaturasIds;
    private Integer totalTareas;

    public Long getIdEstudiante() {
        return getIdUsuario();
    }
}
