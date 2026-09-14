package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asignaturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tareas", "estudiantes", "profesor"})
@EqualsAndHashCode(of = "idAsignatura")
public class Asignatura {

    public static final int MAX_ESTUDIANTES = 25;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Long idAsignatura;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(length = 20)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnoreProperties({"asignaturas", "tareas"})
    private Profesor profesor;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"asignatura"})
    private List<Tarea> tareas = new ArrayList<>();

    @ManyToMany(mappedBy = "asignaturas")
    @Builder.Default
    @JsonIgnoreProperties({"asignaturas", "tareas"})
    private List<Estudiante> estudiantes = new ArrayList<>();
}
