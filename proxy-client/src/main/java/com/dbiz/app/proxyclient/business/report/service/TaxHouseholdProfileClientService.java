package com.dbiz.app.proxyclient.business.report.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.dto.reportDto.TaxHouseholdProfileDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "REPORT-SERVICE", contextId = "taxHouseholdProfileClientService", path = "/report-service/api/v1/taxHouseholdProfile", decode404 = true
, configuration = FeignClientConfig.class )
public interface TaxHouseholdProfileClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @PostMapping("/update")
    ResponseEntity<GlobalReponse> update(@RequestBody @Valid final TaxHouseholdProfileDto entityDto);
}
