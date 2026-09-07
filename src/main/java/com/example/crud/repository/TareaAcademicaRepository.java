package com.example.crud.repository;

import com.example.crud.model.TareaAcademica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaAcademicaRepository extends JpaRepository<TareaAcademica, Long> {
}
