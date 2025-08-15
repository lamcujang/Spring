package com.dbiz.app.proxyclient.business.integration.controller;

import com.dbiz.app.proxyclient.business.integration.service.DataProcessorClientService;
import com.dbiz.app.proxyclient.business.integration.service.ErpIntegrationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.common.dbiz.payload.GlobalReponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
@Slf4j
@RequiredArgsConstructor
public class DataProcessorController {

    private final DataProcessorClientService dataProcessorClientService;

    @PostMapping(value = "/import")
    public ResponseEntity<GlobalReponse> importData(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("code") String code,
                                                    @RequestParam("orgId") Integer orgId) {
        log.info("*** DataProcessor Controller: import Data ***");
        return ResponseEntity.ok(dataProcessorClientService.importData(file, code, orgId)).getBody();
    }

    @PostMapping("/export")
    public ResponseEntity<GlobalReponse> exportFormData(@RequestParam("code") String code) {
        log.info("*** DataProcessor Controller: export Data ***");
        return ResponseEntity.ok(dataProcessorClientService.exportFormData(code)).getBody();
    }

}
