package com.dbiz.app.tenantservice.service;

import org.common.dbiz.dto.tenantDto.IndustryDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.IndustryQueryRequest;

public interface IndustryService extends BaseServiceGeneric<IndustryDto, Integer, IndustryQueryRequest>{

    GlobalReponsePagination getAllGroupByType();
}
