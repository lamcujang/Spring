package com.dbiz.app.systemservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.systemRequest.CountryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface ProvincesService {
    GlobalReponse getArea (ProvincesRequest request);

    GlobalReponse getWard(ProvincesRequest request);

    GlobalReponse getProvinces(ProvincesRequest request);

    GlobalReponse getDistrict(ProvincesRequest request);

    GlobalReponsePagination getCountry(CountryRequest countryRequest);
}
