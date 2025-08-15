package com.dbiz.app.orderservice.helper;


import com.dbiz.app.orderservice.domain.PosOrderline;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PosOrderLineMapper extends BaseMapper<PosOrderline, PosOrderLineVAllDto> {
    @Autowired
    private  ModelMapper modelMapper;

    public PosOrderLineMapper() {
        super(PosOrderline.class, PosOrderLineVAllDto.class);
    }

    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<PosOrderLineVAllDto, PosOrderline>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());

            }
        });

//        modelMapper.addMappings(new PropertyMap<PosOrderline, PosOrderLineVAllDto>() {
//            @Override
//            protected void configure() {
//                map(source.getId(), destination.getId());
//            }
//        });
        modelMapper.addMappings(new PropertyMap<PosOrderLineVAllDto, PosOrderline>() {
            @Override
            protected void configure() {
                map(source.getKitchenOrderLineId(), destination.getKitchenOrderLineId());

            }
        });

        modelMapper.addMappings(new PropertyMap<PosOrderLineVAllDto, PosOrderline>() {
            @Override
            protected void configure() {
                map(source.getRequestOrderLineId(), destination.getRequestOrderLineId());

            }
        });
    }
    public PosOrderline toEntity(PosOrderLineVAllDto dto) {
        return modelMapper.map(dto, PosOrderline.class);
    }

    public PosOrderLineVAllDto toDto(PosOrderline entity) {
        return modelMapper.map(entity, PosOrderLineVAllDto.class);
    }

    public PosOrderline updateEntity(PosOrderLineVAllDto dto, PosOrderline entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
