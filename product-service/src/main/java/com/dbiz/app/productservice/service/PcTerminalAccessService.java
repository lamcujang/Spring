package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.productRequest.PcTerminalAccessQueryRequest;

import java.util.List;

public interface PcTerminalAccessService extends BaseServiceGeneric<PcTerminalAccessDto,Integer, PcTerminalAccessQueryRequest> {
    GlobalReponse intSave(List<PcTerminalAccessDto> entity);
}
