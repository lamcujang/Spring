package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.request.PaymentReqDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface PaymentService extends BaseService{
	
    GlobalReponsePagination getPayments(PaymentReqDto rq);

    public GlobalReponse getMethodForPayment();

    GlobalReponse getPaymentSummary(ReportReqDto dto);
	
}
