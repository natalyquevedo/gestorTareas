package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "semillero", "estudiante", "profesor" })
@EqualsAndHashCode(of = "idTarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTarea;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha_entrega", nullable = false)
    private LocalDateTime fechaEntrega;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String prioridad = "MEDIA";

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_ENTREGADA = "ENTREGADA";
    public static final String ESTADO_CALIFICADA = "CALIFICADA";

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String estado = ESTADO_PENDIENTE;

    @Column
    private Double calificacion;

    @Column(length = 255)
    private String material;

    @Column(length = 1000)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semillero_id")
    @JsonIgnoreProperties({ "tareas", "profesor", "estudiantes" })
    private Semillero semillero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    @JsonIgnoreProperties({ "tareas", "semilleros", "password" })
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnoreProperties({ "semillero", "password", "tareas" })
    private Profesor profesor;
}
