package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.PayMethodAndOrgDto;
import org.common.dbiz.dto.paymentDto.PayMethodDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;

public interface PayMethodService extends BaseServiceGeneric<PayMethodDto,Integer, PayMethodQueryRequest> {
    GlobalReponse  savePayMethod(PayMethodAndOrgDto dto);
}
