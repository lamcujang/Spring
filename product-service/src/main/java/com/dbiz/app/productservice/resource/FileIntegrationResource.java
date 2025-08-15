package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.file.FileIntegrationProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/fileIntegrate"})
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationResource {
    private final FileIntegrationProductService fileIntegrationProductService;

    @PostMapping("/products")
    public ResponseEntity<GlobalReponse> integratePartnerGroup()
    {
        log.info("*** created String, resource; integrate interface table Product ***");
//        return ResponseEntity.ok(this.fileIntegrationService.integrateProduct());
        return null;
    }
}
