package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.util.List;

public interface ProductIntegrationService {
   IntegrationHistoryInfoDto getProductErp(SyncIntegrationCredential credential );


   IntegrationHistory productCategoryIntegration(SyncIntegrationCredential credential) ;


   GlobalReponse productIntegration(SyncIntegrationCredential credential) ;
}
