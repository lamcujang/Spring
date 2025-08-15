package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface CouPonService {
    IntegrationHistory syncCounponIntegration(SyncIntegrationCredential syncIntegrationCredential);
}
