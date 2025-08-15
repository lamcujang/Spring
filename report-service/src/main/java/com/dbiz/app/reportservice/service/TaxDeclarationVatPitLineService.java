package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.*;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxDeclarationVatPitLineDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.reportRequest.TaxDeclarationVatPitLineQueryRequest;

public interface TaxDeclarationVatPitLineService extends BaseServiceGeneric<TaxDeclarationVatPitLineDto, Integer, TaxDeclarationVatPitLineQueryRequest> {

    GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
