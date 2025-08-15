package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.FileIntegrationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("fileIntegrationProductController")
@RequestMapping("/api/v1/fileProduct")
@RequiredArgsConstructor
@Slf4j
public class FileIntegrationController {

    private final FileIntegrationClientService fileIntegrationClientService;

    @PostMapping("/products")
    public ResponseEntity<GlobalReponse> integrateProduct()
    {
        log.info("*** created String, controller; integrate interface table PartnerGroup ***");
        return this.fileIntegrationClientService.integrateProduct();
    }

}
