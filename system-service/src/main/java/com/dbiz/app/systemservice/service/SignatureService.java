package com.dbiz.app.systemservice.service;

import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.payload.GlobalReponse;

public interface SignatureService {


    public GlobalReponse verifyNapasData(PayloadNapasDto dto);

    public GlobalReponse signNapasData(PayloadNapasDto dto);

}
