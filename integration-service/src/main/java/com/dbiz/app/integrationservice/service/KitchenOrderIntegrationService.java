package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface KitchenOrderIntegrationService {

    IntegrationHistoryInfoDto syncKitchenOrderIntegrationMain(SyncIntegrationCredential dto);
}
