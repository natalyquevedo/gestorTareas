package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entidad TareaAcademica según el diagrama UML y especificación funcional del proyecto.
 * Representa la actividad académica asignada con fecha límite, prioridad, estado y calificación.
 */
@Entity
@Table(name = "tareas_academicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"asignatura", "estudiante", "profesor"})
@EqualsAndHashCode(of = "idTarea")
public class TareaAcademica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTarea;

    @NotBlank(message = "El título o nombre de la tarea es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "La descripción de la tarea es obligatoria")
    @Size(min = 3, max = 500, message = "La descripción debe tener entre 3 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "La fecha y hora de entrega es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_entrega", nullable = false)
    private LocalDateTime fechaEntrega;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String prioridad = "MEDIA";

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @DecimalMin(value = "0.0", message = "La calificación mínima es 0.0")
    @DecimalMax(value = "5.0", message = "La calificación máxima es 5.0")
    @Column(name = "calificacion")
    private Double calificacion;

    @Size(max = 255, message = "La descripción o enlace del material no puede superar los 255 caracteres")
    @Column(name = "material", length = 255)
    private String material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignatura_id")
    @JsonIgnoreProperties({"tareas", "profesor", "estudiantes"})
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    @JsonIgnoreProperties({"tareas", "asignaturas", "password"})
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnoreProperties({"asignaturas", "password"})
    private Profesor profesor;

    // ==========================================
}
