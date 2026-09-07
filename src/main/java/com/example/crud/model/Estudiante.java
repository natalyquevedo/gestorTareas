package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Estudiante según el diseño UML y requerimientos del proyecto.
 * Implementa la interfaz Usuario y contiene las tareas académicas asignadas.
 */
@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tareas", "asignaturas", "password"})
@EqualsAndHashCode(of = "idEstudiante")
public class Estudiante {

    public static final int MAX_TAREAS = 50;
    public static final int MAX_ASIGNATURAS = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Long idEstudiante;

    @NotBlank(message = "El nombre del estudiante es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @NotBlank(message = "El código de estudiante es obligatorio")
    @Size(min = 4, max = 20, message = "El código debe tener entre 4 y 20 caracteres")
    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 14, message = "La edad mínima es 14 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    @Column(nullable = false)
    private Integer edad;

    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 12, message = "El semestre máximo es 12")
    @Column(nullable = false)
    private Integer semestre;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Column(length = 120, unique = true)
    private String correo;

    @Column(length = 255)
    private String password;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String rol = "ESTUDIANTE";

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"estudiante"})
    private List<TareaAcademica> tareas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "estudiante_asignaturas",
            joinColumns = @JoinColumn(name = "estudiante_id"),
            inverseJoinColumns = @JoinColumn(name = "asignatura_id")
    )
    @Builder.Default
    @JsonIgnoreProperties({"estudiantes", "tareas"})
    private List<Asignatura> asignaturas = new ArrayList<>();
}
