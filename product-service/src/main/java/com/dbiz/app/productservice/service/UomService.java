package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.productRequest.UomQueryRequest;

public interface UomService extends BaseServiceGeneric<UomDto , Integer, UomQueryRequest >{

    GlobalReponse intSave(UomDto uomDto);



}
