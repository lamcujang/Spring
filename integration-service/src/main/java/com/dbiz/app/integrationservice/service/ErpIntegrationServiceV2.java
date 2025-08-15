package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface ErpIntegrationServiceV2 extends BaseServiceGeneric<ErpIntegrationDto,Integer, ErpIntegrationQueryRequest> {
        GlobalReponse syncIntegration(SyncIntegrationCredential credential);


        GlobalReponse activeToggle(ErpIntegrationDto erpIntegrationDto);
}
