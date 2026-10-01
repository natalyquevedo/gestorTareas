package com.example.crud.repository;

import com.example.crud.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByEstado(String estado);
    List<Tarea> findByEstadoIgnoreCase(String estado);
    List<Tarea> findBySemillero_IdSemillero(Long idSemillero);
    List<Tarea> findByEstudiante_IdUsuario(Long idEstudiante);
    List<Tarea> findByProfesor_IdUsuario(Long idProfesor);
    long countByProfesor_IdUsuarioAndFechaCreacionBetween(Long idProfesor, LocalDateTime inicio, LocalDateTime fin);
}
