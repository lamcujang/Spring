package com.dbiz.app.orderservice.helper;


import com.dbiz.app.orderservice.domain.KitchenOrder;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.dto.orderDto.response.KitchenOrderDtoV;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class KitchenOrderMapper {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<KitchenOrderDto , KitchenOrder>() {
            @Override
            protected void configure() {
                map(source.getFloorId(), destination.getFloorId());
                map(source.getTableId(),destination.getTableId());
            }
        });

        modelMapper.addMappings(new PropertyMap<KitchenOrder,KitchenOrderDto>() {
            @Override
            protected void configure() {
                map(source.getFloorId(), destination.getFloorId());
                map(source.getTableId(),destination.getTableId());
            }
        });
    }

    public KitchenOrder toKitchenOrder(KitchenOrderDto dto) {
        return modelMapper.map(dto, KitchenOrder.class);
    }

    public KitchenOrderDto toKitchenOrderDto(KitchenOrder entity) {
        return modelMapper.map(entity, KitchenOrderDto.class);
    }

    public KitchenOrder updateEntity(KitchenOrderDto dto, KitchenOrder entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public KitchenOrderDtoV  toKitchenOrderDtoV(KitchenOrder entity) {
        return modelMapper.map(entity, KitchenOrderDtoV.class);
    }

}
