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
@ToString(callSuper = true, exclude = {"tareas", "asignaturas"})
@EqualsAndHashCode(callSuper = true)
public class Estudiante extends Usuario {

    public static final int MAX_TAREAS = 50;
    public static final int MAX_ASIGNATURAS = 8;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private Integer semestre;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"estudiante"})
    private List<Tarea> tareas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "estudiante_asignaturas",
            joinColumns = @JoinColumn(name = "estudiante_id"),
            inverseJoinColumns = @JoinColumn(name = "asignatura_id")
    )
    @Builder.Default
    @JsonIgnoreProperties({"estudiantes", "tareas"})
    private List<Asignatura> asignaturas = new ArrayList<>();

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
