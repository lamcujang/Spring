package com.dbiz.app.paymentservice.service;

import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.CalculateAmountRODto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.PosReceiptOtherDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.TotalPosOrderCostDto;
import org.common.dbiz.dto.paymentDto.request.BillInfoReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReceiptOtherQueryRequest;

import java.util.List;

public interface ReceiptOtherService extends BaseServiceGeneric<ReceiptOtherDto,Integer, ReceiptOtherQueryRequest>{

    GlobalReponse calculateAmountROByOrder(CalculateAmountRODto dto);

    GlobalReponse getPosCost(CalculateAmountRODto dto);

    GlobalReponse getBillInfo(BillInfoReqDto dto);

    GlobalReponse getROSelection(CalculateAmountRODto dto);

    GlobalReponse getOrgAccess(ReceiptOtherQueryRequest receiptOtherQueryRequest);

    GlobalReponsePagination getOrgAssign(ReceiptOtherQueryRequest receiptOtherQueryRequest);

    GlobalReponse createPosReceiptOther(TotalPosOrderCostDto receiptOtherDto);

    GlobalReponse getROSelection2(CalculateAmountRODto dto);
}
