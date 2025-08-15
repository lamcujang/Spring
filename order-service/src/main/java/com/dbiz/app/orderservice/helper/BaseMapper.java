package com.dbiz.app.orderservice.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<E, D> {

    @Autowired
    protected ModelMapper modelMapper; // Sử dụng ModelMapper được tiêm vào

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    protected BaseMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public E convertToEntity(D dto, Object... args) {
        return modelMapper.map(dto, entityClass);
    }

    public D convertToDto(E entity, Object... args) {
        return modelMapper.map(entity, dtoClass);
    }

    public Collection<E> convertToEntity(Collection<D> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).collect(Collectors.toList());
    }

    public Collection<D> convertToDto(Collection<E> entities, Object... args) {
        return entities.stream().map(entity -> convertToDto(entity, args)).collect(Collectors.toList());
    }

    public List<E> convertToEntityList(Collection<D> dto, Object... args) {
        return convertToEntity(dto, args).stream().collect(Collectors.toList());
    }

    public List<D> convertToDtoList(Collection<E> entities, Object... args) {
        return convertToDto(entities, args).stream().collect(Collectors.toList());
    }
}
