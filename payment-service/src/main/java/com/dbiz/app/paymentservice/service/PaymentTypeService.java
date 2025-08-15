package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.PaymentTypeDto;
import org.common.dbiz.payload.GlobalReponse;

import java.util.List;

public interface PaymentTypeService {

    GlobalReponse getAllPaymentTypes();


    GlobalReponse save(List<PaymentTypeDto> paymentTypeDto);
}
