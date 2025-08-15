package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;

public interface TaxCategoryService  extends BaseServiceGeneric<TaxCategoryDto ,Integer, TaxCategoryQueryRequest > {

    GlobalReponse intSave(TaxCategoryDto dto);
}
