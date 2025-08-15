package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPriceListCredential;

public interface PriceListIntegrationService {
   IntegrationHistoryInfoDto syncPriceListErp(SyncIntegrationCredential credential );
}
