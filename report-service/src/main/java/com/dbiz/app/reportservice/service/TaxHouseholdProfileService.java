package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.TaxHouseholdProfile;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxHouseholdProfileDto;
import org.common.dbiz.dto.reportDto.TaxPaymentMethodDto;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;

public interface TaxHouseholdProfileService extends BaseServiceGeneric<TaxHouseholdProfileDto, Integer, TaxHouseholdProfileDto> {
}
