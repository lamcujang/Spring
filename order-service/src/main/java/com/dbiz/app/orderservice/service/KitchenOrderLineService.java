package com.dbiz.app.orderservice.service;


import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.*;

public interface KitchenOrderLineService extends BaseServiceGeneric<KitchenOrderlineDto , Integer, KitchenOrderLineRequest > {

    GlobalReponse  cancelQty(KitchenOrderLineCancelRq  rq);

    GlobalReponse updateAllById(UpdateAllKitchenLineByIdRequest  request);

    GlobalReponse findAllProductSameSelected(GetKolSameProductVRequest request);

    GlobalReponse getHistoryKichen(KitchenOrderRequest request);

    GlobalReponse sendNotify(SendNotifycationRq request);

    GlobalReponse getProductComboTest(Integer productId );
}
