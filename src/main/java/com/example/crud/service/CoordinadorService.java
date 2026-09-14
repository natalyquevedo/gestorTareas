package com.example.crud.service;

import com.example.crud.dto.CoordinadorRequestDto;
import com.example.crud.dto.CoordinadorResponseDto;
import com.example.crud.dto.mapper.ModelDtoMapper;
import com.example.crud.exception.RecursoNoEncontradoException;
import com.example.crud.model.Coordinador;
import com.example.crud.repository.CoordinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CoordinadorService {

    private final CoordinadorRepository coordinadorRepository;
    private final ModelDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<CoordinadorResponseDto> listarTodos() {
        return coordinadorRepository.findAll().stream()
                .map(mapper::toCoordinadorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CoordinadorResponseDto obtenerPorId(Long id) {
        return mapper.toCoordinadorResponseDto(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public Coordinador buscarEntidadPorId(Long id) {
        return coordinadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Coordinador no encontrado con el ID: " + id));
    }

    public CoordinadorResponseDto guardar(CoordinadorRequestDto dto) {
        Coordinador coordinador = mapper.toEntity(dto);
        Coordinador guardado = coordinadorRepository.save(coordinador);
        return mapper.toCoordinadorResponseDto(guardado);
    }

    public CoordinadorResponseDto actualizar(Long id, CoordinadorRequestDto dto) {
        Coordinador existente = buscarEntidadPorId(id);

        existente.setNombre(dto.getNombre());
        existente.setDocumento(dto.getDocumento());
        existente.setCodigo(dto.getCodigo());
        existente.setCorreo(dto.getCorreo());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existente.setPassword(dto.getPassword());
        }

        Coordinador actualizado = coordinadorRepository.save(existente);
        return mapper.toCoordinadorResponseDto(actualizado);
    }

    public void eliminar(Long id) {
        if (!coordinadorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Coordinador no existe con el ID: " + id);
        }
        coordinadorRepository.deleteById(id);
    }
}
