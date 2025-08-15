package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import com.dbiz.app.reportservice.domain.TaxPaymentMethod;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxDeclarationExciseDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.reportRequest.TaxDeclarationExciseQueryRequest;

public interface TaxDeclarationExciseService extends BaseServiceGeneric<TaxDeclarationExciseDto, Integer, TaxDeclarationExciseQueryRequest> {

    GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
