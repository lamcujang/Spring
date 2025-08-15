package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.RequestOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;

/*
    * interface: RequestOrderService
 */
public interface RequestOrderService extends BaseServiceGeneric<RequestOrderDto,Integer, RequestOrderGetALlVQueryRequest> {

    GlobalReponse saveAll(RequestOrderDto requestOrderDto);

    GlobalReponse saveV2(RequestOrderDto requestOrderDto);

    GlobalReponse getHistoryRequestOrder(RequestOrderDto param);

    GlobalReponse sendNotify(SendNotifycationRq request);

}
