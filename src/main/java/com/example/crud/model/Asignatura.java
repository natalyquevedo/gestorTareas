package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Asignatura según el diseño UML y especificación del proyecto.
 * Representa una materia académica a la que se asocian profesores, estudiantes y tareas.
 */
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

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Size(max = 20, message = "El código de la asignatura no puede superar los 20 caracteres")
    @Column(name = "codigo", length = 20)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnoreProperties({"asignaturas", "tareas"})
    private Profesor profesor;

    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"asignatura"})
    private List<TareaAcademica> tareas = new ArrayList<>();

    @ManyToMany(mappedBy = "asignaturas")
    @Builder.Default
    @JsonIgnoreProperties({"asignaturas", "tareas"})
    private List<Estudiante> estudiantes = new ArrayList<>();
}
