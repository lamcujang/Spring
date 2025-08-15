package com.dbiz.app.proxyclient.business.report.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.reportDto.TaxAgencyInfoDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "REPORT-SERVICE", contextId = "taxAgencyInfoClientService", path = "/report-service/api/v1/taxAgencyInfo", decode404 = true
, configuration = FeignClientConfig.class )
public interface TaxAgencyInfoClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/current")
    GlobalReponse getCurrentTaxAgencySetup();

    @GetMapping("/getTaxAgentInfo/{code}")
    GlobalReponse getTaxAgentInfo(@PathVariable(name = "code") String code);

    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap TaxAgencyInfoQueryRequest request);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TaxAgencyInfoDto entityDto);
}
