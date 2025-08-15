package com.dbiz.app.productservice.service;


import com.dbiz.app.productservice.domain.PriceList;
import org.common.dbiz.dto.productDto.PriceListIntDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;

import java.util.List;

public interface PriceListService extends BaseServiceGeneric<PricelistDto,Integer, PriceListQueryRequest> {
        GlobalReponse intSave(List<PriceListIntDto> param);

        GlobalReponse findByErpId(Integer erpId);

        GlobalReponsePagination findAllByCashier(PriceListQueryRequest request);

        GlobalReponse getOrgAccess(PriceListQueryRequest request);

        GlobalReponse saveIntPosterminal(PriceListIntDto pricelistDto);
}
