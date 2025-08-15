package com.dbiz.app.orderservice.helper;

import com.dbiz.app.orderservice.domain.PosOrder;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.hibernate.collection.internal.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PosOrderMapper {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        Converter<PersistentBag, List> persistentBagToListConverter = new Converter<PersistentBag, List>() {
            @Override
            public List convert(MappingContext<PersistentBag, List> context) {
                return new ArrayList<>(context.getSource());
            }
        };
        modelMapper.addConverter(persistentBagToListConverter);

//        modelMapper.addMappings(new PropertyMap<PosOrder, PosOrderDto>() {
//            @Override
//            protected void configure() {
//                map(source.getListPosOrderLines(), destination.getListPosOrderLines());
//            }
//        });
        modelMapper.addMappings(new PropertyMap<PosOrder, PosOrderDto >() {
            @Override
            protected void configure() {
//                map(source.getPosOrderLines(), destination.getPosOrderLines());
                map(source.getOrderStatus(),destination.getOrderStatus());
            }
        });
        modelMapper.addMappings(new PropertyMap<PosOrderDto, PosOrder>() {
            @Override
            protected void configure() {
//                map(source.getPosOrderLines(), destination.getPosOrderLines());
                map(source.getOrderStatus(),destination.getOrderStatus());
            }
        });
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    }
    public PosOrderDto toPosOrderDto(PosOrder entity) {
        return modelMapper.map(entity, PosOrderDto.class);
    }
    public PosOrder toPosOrder(PosOrderDto dto) {
        return modelMapper.map(dto, PosOrder.class);
    }
    public PosOrder updatePosOrder(PosOrderDto dto, PosOrder entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
