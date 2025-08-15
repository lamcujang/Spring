package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

public interface PartnerIntegrationService {

    IntegrationHistory partnerIntegration(SyncIntegrationCredential credential);

    GlobalReponse getPartnerERP(SyncIntegrationCredential credential, ErpIntegration erpIntegration);

}
