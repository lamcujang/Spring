package com.dbiz.app.reportservice.service;


import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxBusinessIndustryDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationIndividualDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxBusinessIndustryQueryRequest;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;

public interface TaxDeclarationIndividualService extends BaseServiceGeneric<TaxDeclarationIndividualDto, Integer, TaxDeclarationIndividualQueryRequest> {
    GlobalReponsePagination findAllHeader(TaxDeclarationIndividualQueryRequest paramRequest);

    GlobalReponse saveAllInfo(TaxDeclarationInforDto taxDeclarationInforDto);

    GlobalReponse init(TaxDeclarationIndividualQueryRequest paramRequest);
}


