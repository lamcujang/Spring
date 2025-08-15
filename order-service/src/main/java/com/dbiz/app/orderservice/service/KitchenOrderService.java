package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;

import java.util.List;

public interface KitchenOrderService extends BaseServiceGeneric<KitchenOrderDto , Integer, KitchenOrderRequest > {

    GlobalReponsePagination  findKOrderByStatus(KitchenOrderRequest request);

    GlobalReponsePagination findKOrderGroupByOrder(KitchenOrderRequest request);

    GlobalReponsePagination findAllDocNo(KitchenOrderRequest request);

    GlobalReponse intSave(List<KitchenOrderDto> dto);

    GlobalReponse getLimitedKitchenOrderStatus();

    GlobalReponsePagination  getWDSList(KitchenOrderRequest request);

    GlobalReponse sendWDS(KitchenOrderDto dto);

    GlobalReponse getWDSDetailHistory(GetKolSameProductVRequest dto);


}
