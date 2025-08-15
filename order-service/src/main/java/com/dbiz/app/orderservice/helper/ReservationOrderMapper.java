package com.dbiz.app.orderservice.helper;

import com.dbiz.app.orderservice.domain.ReservationOrder;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ReservationOrderMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<ReservationOrderDto , ReservationOrder>() {
            @Override
            protected void configure() {
                map(source.getCustomerId(), destination.getCustomerId());
                map(source.getTableId(), destination.getTableId());
                map(source.getFloorId(),destination.getFloorId());
                map(source.getCustomerName(),destination.getCustomerName());

            }
        });

        modelMapper.addMappings(new PropertyMap<ReservationOrder, ReservationOrderDto>() {
            @Override
            protected void configure() {
                map(source.getCustomerId(), destination.getCustomerId());
                map(source.getTableId(), destination.getTableId());
                map(source.getFloorId(),destination.getFloorId());
                map(source.getCustomerName(),destination.getCustomerName());
            }
        });
    }
    public ReservationOrder reservationOrder(ReservationOrderDto dto) {
        return modelMapper.map(dto, ReservationOrder.class);
    }

    public ReservationOrderDto reservationOrderDto(ReservationOrder entity) {
        return modelMapper.map(entity, ReservationOrderDto.class);
    }

    public ReservationOrder updateEntity(ReservationOrderDto dto, ReservationOrder entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
}
