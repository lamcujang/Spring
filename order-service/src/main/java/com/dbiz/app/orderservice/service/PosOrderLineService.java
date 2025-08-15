package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import org.common.dbiz.dto.orderDto.PosOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.CancelPosOrderRequest;
import org.common.dbiz.request.orderRequest.PosOrderLineQueryRequest;

import java.util.List;

public interface PosOrderLineService extends BaseServiceGeneric<PosOrderlineDto , Integer, PosOrderLineQueryRequest > {
    GlobalReponse CancelPosOrder(CancelPosOrderRequest  request);

    GlobalReponse CancelByRequestOrderId(CancelPosOrderRequest request);

    GlobalReponse deleteWithReason(PosOrderlineDto paramDto);
}
