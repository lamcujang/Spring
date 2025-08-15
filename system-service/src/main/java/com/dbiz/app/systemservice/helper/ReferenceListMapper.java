package com.dbiz.app.systemservice.helper;

import com.dbiz.app.systemservice.domain.ReferenceList;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.systemDto.ReferenceListDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferenceListMapper {
    private final ModelMapper modelMapper;
    public ReferenceList toReferenceList(final ReferenceListDto dto)
    {
        return modelMapper.map(dto, ReferenceList.class);
    }


    public ReferenceList updateEntity(ReferenceListDto dto, ReferenceList entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public ReferenceListDto toReferenceDto(final ReferenceList entity)
    {
        return modelMapper.map(entity, ReferenceListDto.class);
    }
}
