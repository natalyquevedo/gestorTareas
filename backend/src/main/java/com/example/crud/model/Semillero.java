package com.example.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "semilleros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tareas", "estudiantes", "profesor"})
@EqualsAndHashCode(of = "idSemillero")
public class Semillero {

    public static final int MAX_ESTUDIANTES = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semillero")
    private Long idSemillero;

    @Column(nullable = false, length = 150, unique = true)
    private String nombre;

    @Column(length = 30)
    private String codigo;

    @Column(name = "linea_investigacion", length = 200)
    private String lineaInvestigacion;

    @Column(length = 1000)
    private String descripcion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", unique = true)
    @JsonIgnoreProperties({"semillero", "tareas"})
    private Profesor profesor;

    @OneToMany(mappedBy = "semillero", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"semillero"})
    private List<Tarea> tareas = new ArrayList<>();

    @OneToMany(mappedBy = "semillero")
    @Builder.Default
    @JsonIgnoreProperties({"semillero", "tareas"})
    private List<Estudiante> estudiantes = new ArrayList<>();
}
