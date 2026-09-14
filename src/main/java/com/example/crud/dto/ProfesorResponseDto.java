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
public class ProfesorResponseDto extends UsuarioResponseDto {
    private Integer edad;
    private List<Long> asignaturasIds;
    private Integer totalTareasAsignadas;

    public Long getIdProfesor() {
        return getIdUsuario();
    }
}
