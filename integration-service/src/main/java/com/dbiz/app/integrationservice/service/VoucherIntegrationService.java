package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.voucher.CheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntCheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.CheckInVoucherDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface VoucherIntegrationService {

    GlobalReponse checkVoucherInfo(CheckVoucherInfoDto dto);

    GlobalReponse getVoucherServices(VoucherParamDto dto);

    GlobalReponsePagination getVoucherServiceOrders(VoucherParamDto dto);

    GlobalReponse getVoucherInfo(CheckVoucherInfoDto dto);

    GlobalReponse checkInVoucher(CheckInVoucherDto dto);
}
