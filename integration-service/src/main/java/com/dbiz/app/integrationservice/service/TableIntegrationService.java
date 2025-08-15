package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface TableIntegrationService {
    public IntegrationHistory tableIntegration(SyncIntegrationCredential credential) ;
}
