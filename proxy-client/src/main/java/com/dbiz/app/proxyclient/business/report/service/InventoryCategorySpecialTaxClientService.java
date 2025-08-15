package com.dbiz.app.proxyclient.business.report.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.InventoryCategorySpecialTaxQueryRequest;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "REPORT-SERVICE", contextId = "inventoryCategorySpecialTaxClientService", path = "/report-service/api/v1/inventoryCategorySpecialTax", decode404 = true
, configuration = FeignClientConfig.class )
public interface InventoryCategorySpecialTaxClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap InventoryCategorySpecialTaxQueryRequest request);
}
