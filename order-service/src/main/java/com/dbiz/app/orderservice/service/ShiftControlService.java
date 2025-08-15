package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.ShiftControlDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;

public interface ShiftControlService extends BaseServiceGeneric<ShiftControlDto,Integer, ShiftControlQueryRequest> {
    GlobalReponse findAllOrderByShiftId(final Integer shiftId);

    GlobalReponse findAllPaymentByShiftId(final Integer shiftId);


    GlobalReponse findAllPurchaseByShiftId(final Integer shiftId);

    GlobalReponse findAllReturnByShiftId(final Integer shiftId);

    GlobalReponse getReportClosingShift(final Integer shiftId);

    GlobalReponse getShiftInfoById(final Integer shiftId);

    GlobalReponse getNewestShiftInfo(Integer orgId, Integer posTerminalId);
}
