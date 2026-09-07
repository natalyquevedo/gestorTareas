package com.example.crud.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidad Coordinador según el diseño del sistema y roles de acceso.
 * Implementa la interfaz Usuario y representa la administración académica.
 */
@Entity
@Table(name = "coordinadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password"})
@EqualsAndHashCode(of = "idCoordinador")
public class Coordinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordinador")
    private Long idCoordinador;

    @NotBlank(message = "El nombre del coordinador es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20, message = "El documento debe tener entre 5 y 20 caracteres")
    @Column(nullable = false, length = 20, unique = true)
    private String documento;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Column(length = 120, unique = true)
    private String correo;

    @Column(length = 255)
    private String password;

    @Builder.Default
    @Column(nullable = false, length = 20)
    private String rol = "COORDINADOR";
}
