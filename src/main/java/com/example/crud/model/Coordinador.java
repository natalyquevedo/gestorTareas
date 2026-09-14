package com.example.crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "coordinadores")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Coordinador extends Usuario {

    @PrePersist
    public void asignarRolPorDefecto() {
        if (getRol() == null || getRol().isBlank()) {
            setRol("COORDINADOR");
        }
    }

    public Long getIdCoordinador() {
        return getIdUsuario();
    }

    public void setIdCoordinador(Long idCoordinador) {
        setIdUsuario(idCoordinador);
    }
}
