package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.util.List;

public interface ShiftIntegrationService {

    IntegrationHistoryInfoDto syncShiftIntegration(SyncIntegrationCredential dto);

    GlobalReponse syncApi(ShiftIntDto dto);
}
