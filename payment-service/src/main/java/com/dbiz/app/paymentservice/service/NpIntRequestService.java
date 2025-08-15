package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.napas.NpIntRequestDto;
import org.common.dbiz.dto.paymentDto.request.BankNpReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.dto.paymentDto.request.NpIntRequestReqDto;

public interface NpIntRequestService {

    GlobalReponsePagination findBank(BankNpReqDto dto);

    GlobalReponsePagination findRequest(NpIntRequestReqDto dto);

    GlobalReponse save(NpIntRequestDto dto);

}
