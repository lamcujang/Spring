package com.dbiz.app.tenantservice.service;

import org.common.dbiz.dto.integrationDto.PosTerminalOrgAccessIntDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.PosTerminalIntDto;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;

import java.util.List;

public interface PosTerminalService extends BaseServiceGeneric<PosTerminalDto, Integer, PosTerminalQueryRequest>{

    GlobalReponse saveInt(PosTerminalIntDto param);

    GlobalReponse findByErpId(Integer erpId);

    GlobalReponse saveIntUser(PosTerminalDto dto);

    GlobalReponse deleteByPosTerminalId(PosTerminalDto posTerminalId);
}
