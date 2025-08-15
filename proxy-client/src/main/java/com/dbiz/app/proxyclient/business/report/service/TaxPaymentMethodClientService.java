package com.dbiz.app.proxyclient.business.report.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "REPORT-SERVICE", contextId = "taxPaymentMethodClientService", path = "/report-service/api/v1/taxPaymentMethod", decode404 = true
, configuration = FeignClientConfig.class )
public interface TaxPaymentMethodClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap TaxPaymentMethodQueryRequest request);

}
