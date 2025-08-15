package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PODetailVRequest;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;

public interface PurchaseOrderService extends BaseServiceGeneric<PurchaseOrderDto , Integer, POHeaderVRequest > {

    public GlobalReponsePagination  findAllDetail(PODetailVRequest  request);

}
