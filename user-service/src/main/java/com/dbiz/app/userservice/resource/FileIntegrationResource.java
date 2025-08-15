package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.file.FileIntegrationUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/fileIntegrate"})
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationResource {

    private final FileIntegrationUserService fileIntegrationUserService;

    @PostMapping("/partnerGroup")
    public ResponseEntity<GlobalReponse> integratePartnerGroup(@RequestParam final String created)
    {
//        log.info("*** created String, resource; integrate interface table PartnerGroup ***");
//        return ResponseEntity.ok(this.fileIntegrationService.integratePartnerGroup(created));
        return null;
    }

    @PostMapping("/customer")
    public ResponseEntity<GlobalReponse> integrateCustomer(@RequestParam final String created)
    {
//        log.info("*** created String, resource; integrate interface table Customer ***");
//        return ResponseEntity.ok(this.fileIntegrationService.integrateCustomer(created));
        return null;
    }

}
