package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.dto.orderDto.GeneratePOSBillNumberDto;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.common.dbiz.request.orderRequest.ReportPosQueryRequest;

import java.util.List;

public interface PosOrderService extends BaseServiceGeneric<PosOrderDto , Integer, PosOrderQueryRequest > {
    GlobalReponsePagination findAllPosList (final PosOrderListQueryRequest request);

    GlobalReponsePagination getHistory(final PosOrderQueryRequest request);

    GlobalReponse generatePOSBillNumber(GeneratePOSBillNumberDto dto);

    GlobalReponse intSave(List<PosOrderDto> dto);

    GlobalReponse cancelCompletedOrder(Integer productId);

    GlobalReponsePagination reportOrder(ReportPosQueryRequest request);

    GlobalReponse requestOrder(PosOrderDto dto);

    GlobalReponse findPosListById(Integer id);

    GlobalReponsePagination findPosOrdersRetail(PosOrderListQueryRequest request);
    GlobalReponse findPosOrderRetailById(PosOrderReqDto dto);

    GlobalReponse deletePosOrder(Integer id);

    GlobalReponse deleteWithReason(PosOrderDto paramDto);

    GlobalReponse updateOrderHeader(org.common.dbiz.dto.paymentDto.PosOrderDto dto);


}
