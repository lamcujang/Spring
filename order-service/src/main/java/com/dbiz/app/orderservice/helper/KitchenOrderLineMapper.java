package com.dbiz.app.orderservice.helper;

import com.dbiz.app.orderservice.domain.KitchenOrderLine;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KitchenOrderLineMapper {
    private final ModelMapper modelMapper;
    public KitchenOrderLine toKitchenOrderLine(KitchenOrderlineDto dto) {
        return modelMapper.map(dto, KitchenOrderLine.class);
    }

    public KitchenOrderlineDto toKitchenOrderLineDto(KitchenOrderLine entity) {
        return modelMapper.map(entity, KitchenOrderlineDto.class);
    }

    public KitchenOrderLine updateEntity(KitchenOrderlineDto dto, KitchenOrderLine entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
