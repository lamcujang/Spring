package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.ReturnOrderDto;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.*;

public interface ReturnOrderService extends BaseServiceGeneric<ReturnOrderDto, Integer, ReturnOrderRequest> {
    GlobalReponse  findAllDetail(RODetailVRequest request);

    GlobalReponsePagination findPosOrdersRetail(PosOrderListQueryRequest request);

    GlobalReponse findPosOrderRetailById(PosOrderReqDto dto);

    GlobalReponsePagination findPOAll(POHeaderVRequest request);
    GlobalReponse findPODetail(PODetailVRequest request);

}
