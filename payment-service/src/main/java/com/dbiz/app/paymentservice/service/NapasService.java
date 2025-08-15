package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.paymentDto.napas.*;
import org.common.dbiz.payload.GlobalReponse;

public interface NapasService {

    GlobalReponse createNapasTransaction(NotificationDto dto);

    NapasResponseDto createNapasReconciliation(PayloadNapasDto req);

    GlobalReponse checkNapasQRCode(CheckNapasQRCodeDto dto);
}
