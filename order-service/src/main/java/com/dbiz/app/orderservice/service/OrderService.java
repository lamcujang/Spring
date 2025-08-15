package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;

public interface OrderService extends BaseServiceGeneric<OrderDto , Integer, OrderQueryRequest > {

    public String updateOrderMBB(Object Dto);


    public NapasResponseDto updateNapasOrder(PayloadNapasDto dto);
}
