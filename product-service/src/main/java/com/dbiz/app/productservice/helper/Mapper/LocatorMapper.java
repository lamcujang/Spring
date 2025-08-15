package com.dbiz.app.productservice.helper.Mapper;

import com.dbiz.app.productservice.domain.Warehouse;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocatorMapper {
    private final ModelMapper modelMapper;
    public Warehouse toWarehouse(final WarehouseDto warehouseDto)
    {
        return modelMapper.map(warehouseDto, Warehouse.class);
    }


    public Warehouse updateEntity(WarehouseDto dto, Warehouse entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public WarehouseDto toWarehouseDto(final Warehouse warehouse)
    {
        return modelMapper.map(warehouse, WarehouseDto.class);
    }
}
