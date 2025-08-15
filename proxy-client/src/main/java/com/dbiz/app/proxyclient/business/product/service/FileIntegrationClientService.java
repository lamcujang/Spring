package com.dbiz.app.proxyclient.business.product.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "PRODUCT-SERVICE",
        contextId = "FileIntegrationProductClientService",
        path = "/product-service/api/v1/fileIntegrate",
        decode404 = true,
        configuration = FeignClientConfig.class)
public interface FileIntegrationClientService {
    @PostMapping("/products")
    ResponseEntity<GlobalReponse> integrateProduct();
}
