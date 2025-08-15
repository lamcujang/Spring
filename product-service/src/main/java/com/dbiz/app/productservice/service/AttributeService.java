package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;

public interface AttributeService extends BaseServiceGeneric<AttributeDto,Integer, AttributeQueryRequest> {
        GlobalReponse saveAll(AttributeDto attributeDto);

}
