package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;

public interface ReturnReasonService extends BaseServiceGeneric<ReturnReasonDto,Integer, ReturnReasonQueryRequest>{
}
