package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.PaymentDetailDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.payload.GlobalReponse;

import java.util.List;

public interface VoucherService extends BaseService{


    public GlobalReponse saveVoucherTransaction(VoucherTransactionDto Dto);

    public String saveVoucherCouponPayment(List<PaymentDetailDto> Dto, Integer orgId,Integer posPaymentId,String paymentMethod);

}
