package com.dbiz.app.integrationservice.helper.mapper;


import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.integrationDto.posOrder.CreateOrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class OrderIntegrationMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {

    }

//    public CreateOrderDto toDto(PosOrderDto entity) {
//        CreateOrderDto createOrderDto = CreateOrderDto.builder()
//                .orderDate(entity.getOrderDate())
//                .orderNumber(entity.getOrderNumber())
//                .orderType(entity.getOrderType())
//                .orderStatus(entity.getOrderStatus())
//                .orderTotal(entity.getOrderTotal())
//                .build();
//        return modelMapper.map(entity, CreateOrderDto.class);
//    }
}
