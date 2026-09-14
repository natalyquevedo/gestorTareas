package com.example.crud.repository;

import com.example.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByDocumento(String documento);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByCodigo(String codigo);
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
    boolean existsByCodigo(String codigo);
}
