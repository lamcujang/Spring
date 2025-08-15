package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;

public interface ProductIntService {
    GlobalReponse syncProductInt();

    String saveSigProduct(ProductIntDto productIntDto, Integer priceListId);

    GlobalReponse saveInterfaceProduct(ProductIntDto productIntDto);
}
