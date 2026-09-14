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
@ToString(callSuper = true, exclude = {"asignaturas", "tareas"})
@EqualsAndHashCode(callSuper = true)
public class Profesor extends Usuario {

    public static final int MAX_ASIGNATURAS = 4;
    public static final int MAX_TAREAS_SEMANA = 4;

    @Column(nullable = false)
    private Integer edad;

    @OneToMany(mappedBy = "profesor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    @JsonIgnoreProperties({"profesor"})
    private List<Asignatura> asignaturas = new ArrayList<>();

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
