package com.dbiz.app.systemservice.service;

import org.common.dbiz.dto.systemDto.ConfigDto;
import org.common.dbiz.dto.systemDto.TaxOfficeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ConfigQueryRequest;
import org.common.dbiz.request.systemRequest.TaxOfficeQueryRequest;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface TaxOfficeService extends BaseServiceGeneric<TaxOfficeDto, Integer, TaxOfficeQueryRequest>{

    GlobalReponse loadTaxOffice(MultipartFile taxOffice, MultipartFile taxRegion);

    GlobalReponsePagination findAllTaxOffice(TaxOfficeQueryRequest request);

}
