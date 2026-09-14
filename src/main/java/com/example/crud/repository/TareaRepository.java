package com.example.crud.repository;

import com.example.crud.model.EstadoTarea;
import com.example.crud.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByEstado(EstadoTarea estado);
    long countByProfesor_IdUsuarioAndFechaCreacionBetween(Long idProfesor, LocalDateTime inicio, LocalDateTime fin);
}
