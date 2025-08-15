package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.integrationDto.ProductCatIntDto;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.request.ProductCategoryReq;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;

import java.util.List;

public interface ProductCategoryService extends BaseServiceGeneric<ProductCategoryDto, Integer, ProductCategoryQueryRequest>{
    GlobalReponse intSave (ProductCatIntDto param);

    GlobalReponse  getOrgAccess (ProductCategoryQueryRequest request);

    GlobalReponse updatePcE_request(List<PcTerminalAccessDto> request);

    GlobalReponsePagination getPcTerminalAccess(ProductCategoryReq req );

    GlobalReponsePagination getAllPcAccess(ProductCategoryQueryRequest request);

    GlobalReponsePagination getAllSimple(ProductCategoryQueryRequest request);
}
