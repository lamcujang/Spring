package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.domain.TaxPaymentMethod;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.IndividualIndustryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.common.dbiz.request.reportRequest.IndividualIndustryQueryRequest;

public interface IndividualIndustryService extends BaseServiceGeneric<IndividualIndustryDto, Integer, IndividualIndustryQueryRequest> {

    GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId);
}
