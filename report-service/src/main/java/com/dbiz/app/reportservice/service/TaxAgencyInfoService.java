package com.dbiz.app.reportservice.service;


import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxAgencyInfoDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;

public interface TaxAgencyInfoService extends BaseServiceGeneric<TaxAgencyInfoDto, Integer, TaxAgencyInfoQueryRequest> {

    GlobalReponse getCurrentTaxAgencySetup();
    GlobalReponse getTaxAgentInfo(String code);
}
