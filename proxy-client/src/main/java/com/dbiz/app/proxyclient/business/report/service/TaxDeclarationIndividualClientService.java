package com.dbiz.app.proxyclient.business.report.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationIndividualDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationInforDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "REPORT-SERVICE", contextId = "taxDeclarationIndividualClientService", path = "/report-service/api/v1/taxDeclarationIndividual", decode404 = true
, configuration = FeignClientConfig.class )
public interface TaxDeclarationIndividualClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap TaxDeclarationIndividualQueryRequest request);

    @GetMapping("/init")
    GlobalReponse init(@SpringQueryMap TaxDeclarationIndividualQueryRequest request);

    @GetMapping("/findAllHeader")
    GlobalReponsePagination findAllHeader(@SpringQueryMap TaxDeclarationIndividualQueryRequest request);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody @Valid final TaxDeclarationInforDto entityDto);

    @PostMapping("/update")
    ResponseEntity<GlobalReponse> update(@RequestBody @Valid final TaxDeclarationInforDto entityDto);
}
