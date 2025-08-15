package com.dbiz.app.tenantservice.service;

import org.common.dbiz.dto.tenantDto.PrintReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;

import java.util.List;
import java.util.Map;

public interface PrintReportService extends BaseServiceGeneric<PrintReportDto, Integer, PrintReportQueryRequest>{

    GlobalReponse saveAll(List<Map<String, Object>> rawList);

    GlobalReponsePagination findAllByTenant(PrintReportQueryRequest paramRequest);
}
