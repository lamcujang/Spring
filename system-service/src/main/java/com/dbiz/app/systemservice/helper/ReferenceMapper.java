package com.dbiz.app.systemservice.helper;

import com.dbiz.app.systemservice.domain.Reference;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferenceMapper {
    private final ModelMapper modelMapper;
    public Reference toReference(final ReferenceDto dto)
    {
        return modelMapper.map(dto, Reference.class);
    }


    public Reference updateEntity(ReferenceDto dto, Reference entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public ReferenceDto toReferenceDto(final Reference entity)
    {
        return modelMapper.map(entity, ReferenceDto.class);
    }
}
