package com.dbiz.app.systemservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ProvinceQueryRequest;
import org.common.dbiz.request.systemRequest.WardQueryRequest;

public interface GovernmentService {

    GlobalReponse loadAdministrativeDivisions();

    GlobalReponsePagination getProvinceWithWard(ProvinceQueryRequest request);

    GlobalReponsePagination getProvince(ProvinceQueryRequest request);

    GlobalReponsePagination getWard(WardQueryRequest request);

}
