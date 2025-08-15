package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.request.BankAccountReqDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;

public interface BankAccountService extends BaseServiceGeneric<BankAccountDto, Integer, BankAccountQueryRequest>{

    GlobalReponsePagination findByBank(BankAccountReqDto reqDto);
}
