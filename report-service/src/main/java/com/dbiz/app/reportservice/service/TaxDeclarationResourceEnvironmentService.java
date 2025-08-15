package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.TaxDeclarationResourceEnvironment;
import com.dbiz.app.reportservice.domain.TaxPaymentMethod;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxDeclarationResourceEnvironmentDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.reportRequest.TaxDeclarationResourceEnvironmentQueryRequest;

public interface TaxDeclarationResourceEnvironmentService extends BaseServiceGeneric<TaxDeclarationResourceEnvironmentDto, Integer, TaxDeclarationResourceEnvironmentQueryRequest> {

    GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
