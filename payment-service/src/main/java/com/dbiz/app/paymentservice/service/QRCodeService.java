package com.dbiz.app.paymentservice.service;


import org.common.dbiz.dto.paymentDto.QRCodeDto;
import org.common.dbiz.dto.paymentDto.request.QRCodeReqDto;
import org.common.dbiz.payload.GlobalReponse;

public interface QRCodeService {

    GlobalReponse  generateQRCodeMBB(Object Dto);

    GlobalReponse  checkQRCodeMBB(Object Dto);

    GlobalReponse generateQRCode(Object Dto);

    GlobalReponse generateQRCodeByBankId(Object Dto);

    GlobalReponse generateQRCodeByNapas(QRCodeReqDto dto);
}
