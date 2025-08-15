package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.integrationDto.posOrder.CreateOrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.util.List;

public interface OrderIntegrationService {

    String handleOrderIntegration();

    List<PosOrderDto> getPosOrderList();

    GlobalReponse syncOrderIntegration(SyncIntegrationCredential dto);

    IntegrationHistoryInfoDto syncOrderIntegrationMain(SyncIntegrationCredential dto);

    GlobalReponse syncInvoice(Integer posInvoiceId,Integer orgId);

    public List<CreateOrderDto> getOrders(SyncIntegrationCredential dto);

    public IntegrationHistoryInfoDto getOrdersMainByPosOrder(SyncIntegrationCredential dto, Integer posOrderId);
}
