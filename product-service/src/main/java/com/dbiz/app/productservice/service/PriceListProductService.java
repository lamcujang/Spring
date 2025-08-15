package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.dto.productDto.request.PriceListProductReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ApplyNewFormulaPriceRequest;
import org.common.dbiz.request.productRequest.FindAllPriceListAndProductRequest;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;

public interface PriceListProductService extends BaseServiceGeneric<PriceListProductDto,Integer, PriceListProductQueryRequest> {
    GlobalReponse deleteAllByIds(PriceListProductDto.IdsRequest  ids);

    GlobalReponse applyAllNewFormulaPrice(ApplyNewFormulaPriceRequest request);

    GlobalReponse deleteAllByPriceListId(ApplyNewFormulaPriceRequest request);

    GlobalReponse findPriceListProductByProductId(Integer productId);

    GlobalReponsePagination findAllPriceListAndProduct(FindAllPriceListAndProductRequest request);

    GlobalReponsePagination findAllPriceListProduct(PriceListProductReqDto request);
}
