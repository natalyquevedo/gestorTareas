package com.example.crud.repository;

import com.example.crud.model.Semillero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemilleroRepository extends JpaRepository<Semillero, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByCodigoIgnoreCase(String codigo);

    boolean existsByProfesor_IdUsuario(Long profesorId);

    Optional<Semillero> findByProfesor_IdUsuario(Long profesorId);

    Optional<Semillero> findByEstudiantes_IdUsuario(Long estudianteId);
}
