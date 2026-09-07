package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Profesor según el diseño UML y especificación del proyecto.
 * Implementa la interfaz Usuario y gestiona la asignación y calificación de tareas.
 */
@Entity
@Table(name = "profesores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"asignaturas", "password"})
@EqualsAndHashCode(of = "idProfesor")
public class Profesor {

    public static final int MAX_ASIGNATURAS = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Long idProfesor;

    @NotBlank(message = "El identificador laboral (id) es obligatorio")
    @Size(min = 3, max = 20, message = "El ID institucional debe tener entre 3 y 20 caracteres")
    @Column(name = "codigo_docente", nullable = false, length = 20, unique = true)
    private String id; // Identificador laboral según UML

    @NotBlank(message = "El nombre del profesor es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima de un docente es 18 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    @Column(nullable = false)
    private Integer edad;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Column(length = 120, unique = true)
    private String correo;

    @Column(length = 255)
    private String password;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String rol = "PROFESOR";

    /**
     * Asignaturas a cargo del profesor (máximo 4 según regla de negocio 2).
     */
    @OneToMany(mappedBy = "profesor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    @JsonIgnoreProperties({"profesor"})
    private List<Asignatura> asignaturas = new ArrayList<>();
}
