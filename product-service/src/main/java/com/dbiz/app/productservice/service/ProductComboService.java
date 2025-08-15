package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.ProductComboDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;

public interface ProductComboService extends BaseServiceGeneric<ProductComboDto,Integer, ProductComboQueryRequest> {
    GlobalReponse findByProductId(final Integer id);

    GlobalReponse findByProductIsComponentById(final Integer id);

    GlobalReponsePagination findAllComponentByProductId(ProductComboQueryRequest query);
}
