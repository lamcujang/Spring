package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.ReservationOrderDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;

public interface ReservationOrderService extends BaseServiceGeneric<ReservationOrderDto ,Integer, ReservationOrderQueryRequest > {


    GlobalReponsePagination  findAllV(ReservationOrderQueryRequest queryRequest);
}
