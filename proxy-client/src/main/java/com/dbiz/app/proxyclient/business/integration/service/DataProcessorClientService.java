package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "FileClientService", path = "/integration-service/api/v1/data", decode404 = true, configuration = FeignClientConfig.class)
public interface DataProcessorClientService {

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalReponse> importData(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("code") String code,
                                                    @RequestParam("orgId") Integer orgId);

    @PostMapping(value = "/export")
    public ResponseEntity<GlobalReponse> exportFormData(@RequestParam("code") String code);

}
