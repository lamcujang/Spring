package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface PosTerminalIntService {
    public IntegrationHistory posTerminalIntegration(SyncIntegrationCredential credential) ;
}
