package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = { "tareas", "semillero" })
@EqualsAndHashCode(callSuper = true)
public class Estudiante extends Usuario {

    public static final int MAX_SEMILLEROS = 1;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private Integer semestre;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({ "estudiante" })
    private List<Tarea> tareas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semillero_id")
    @JsonIgnoreProperties({ "estudiantes", "tareas", "profesor" })
    private Semillero semillero;

    @PrePersist
    public void asignarRolPorDefecto() {
        if (getRol() == null || getRol().isBlank()) {
            setRol("ESTUDIANTE");
        }
    }

    public Long getIdEstudiante() {
        return getIdUsuario();
    }

    public void setIdEstudiante(Long idEstudiante) {
        setIdUsuario(idEstudiante);
    }
}
