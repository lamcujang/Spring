package com.dbiz.app.reportservice.service;


import com.dbiz.app.reportservice.domain.*;
import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.reportDto.TaxPaymentMethodDto;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;

public interface TaxPaymentMethodService extends BaseServiceGeneric<TaxPaymentMethodDto, Integer, TaxPaymentMethodQueryRequest> {
}
