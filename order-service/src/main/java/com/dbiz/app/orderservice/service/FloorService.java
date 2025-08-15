package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;

import java.util.List;

public interface FloorService extends BaseServiceGeneric<FloorDto , Integer, FloorQueryRequest > {
     GlobalReponse intSaveAll(List<FloorDto> floorDtos);
}
