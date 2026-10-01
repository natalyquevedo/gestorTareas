package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profesores")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"semillero", "tareas"})
@EqualsAndHashCode(callSuper = true)
public class Profesor extends Usuario {

    public static final int MAX_SEMILLEROS = 1;

    @Column(nullable = false)
    private Integer edad;

    @OneToOne(mappedBy = "profesor")
    @JsonIgnoreProperties({"profesor", "tareas"})
    private Semillero semillero;

    @OneToMany(mappedBy = "profesor")
    @Builder.Default
    @JsonIgnoreProperties({"profesor"})
    private List<Tarea> tareas = new ArrayList<>();

    @PrePersist
    public void asignarRolPorDefecto() {
        if (getRol() == null || getRol().isBlank()) {
            setRol("PROFESOR");
        }
    }

    public Long getIdProfesor() {
        return getIdUsuario();
    }

    public void setIdProfesor(Long idProfesor) {
        setIdUsuario(idProfesor);
    }
}
