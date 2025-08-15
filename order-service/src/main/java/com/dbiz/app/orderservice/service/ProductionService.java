package com.dbiz.app.orderservice.service;

import com.dbiz.app.orderservice.domain.Production;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.orderRequest.ProductionQueryRequest;

public interface ProductionService extends BaseServiceGeneric<ProductionDto,Integer, ProductionQueryRequest>{

    GlobalReponse deleteProductionLineById(Integer id);
}
