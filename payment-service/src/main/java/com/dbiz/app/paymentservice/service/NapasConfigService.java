package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.BankIntegrationInfoDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;

public interface NapasConfigService {


    public BankIntegrationInfoDto getBankIntegrationInfo(Integer orgId, Integer posTerminalId);
    public NapasConfigResDto getNapasConfig(String type);
}
