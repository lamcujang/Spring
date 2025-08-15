package com.dbiz.app.integrationservice.service;

import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.util.List;

public interface ErpNextIntegrationService {

    GlobalReponse testApi();

    GlobalReponse syncPosInvoice(Integer posOrderId, Integer orgId);

    GlobalReponse syncCustomer(SyncIntegrationCredential credential, ErpIntegration erpIntegration);

    GlobalReponse handleEx(List<UserDto> listUser);

}
