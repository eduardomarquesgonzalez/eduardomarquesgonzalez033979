package br.gov.mt.seplag.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface base para Mappers
 *
 * @param <E> Entity
 * @param <Q> Request DTO
 * @param <S> Response DTO
 */
public interface BaseMapper<E, Q, S> {
    E toEntity(Q requestDTO);
    S toResponseDTO(E entity);
    void updateEntityFromDTO(Q requestDTO, E entity);
    default List<S> toResponseDTOList(List<E> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}