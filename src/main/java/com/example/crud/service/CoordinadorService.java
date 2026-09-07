package com.example.crud.service;

import com.example.crud.model.Coordinador;
import com.example.crud.repository.CoordinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CoordinadorService {

    private final CoordinadorRepository coordinadorRepository;

    @Transactional(readOnly = true)
    public List<Coordinador> listarTodos() {
        return coordinadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Coordinador obtenerPorId(Long id) {
        return coordinadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coordinador no encontrado con el ID: " + id));
    }

    public Coordinador guardar(Coordinador coordinador) {
        return coordinadorRepository.save(coordinador);
    }

    public Coordinador actualizar(Long id, Coordinador coordinadorDetalles) {
        Coordinador coordinadorExistente = obtenerPorId(id);

        coordinadorExistente.setNombre(coordinadorDetalles.getNombre());
        coordinadorExistente.setDocumento(coordinadorDetalles.getDocumento());
        coordinadorExistente.setCorreo(coordinadorDetalles.getCorreo());

        if (coordinadorDetalles.getPassword() != null && !coordinadorDetalles.getPassword().isBlank()) {
            coordinadorExistente.setPassword(coordinadorDetalles.getPassword());
        }

        return coordinadorRepository.save(coordinadorExistente);
    }

    public void eliminar(Long id) {
        if (!coordinadorRepository.existsById(id)) {
            throw new RuntimeException("Coordinador no existe con el ID: " + id);
        }
        coordinadorRepository.deleteById(id);
    }
}
