package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.PriceListIntDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;

import java.util.List;

public interface PriceListV2Service extends BaseServiceGeneric<PricelistDto,Integer, PriceListQueryRequest> {
}
