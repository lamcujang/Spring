package com.dbiz.app.proxyclient.business.system.controller;


import com.dbiz.app.proxyclient.business.system.service.SQLService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/sql")
@RequiredArgsConstructor
public class SQLController {

    private final SQLService service;

    @PostMapping(path = "/modifyAll",consumes = MULTIPART_FORM_DATA_VALUE)
    public GlobalReponse modifyAll(@RequestPart("file") MultipartFile file) {
        return this.service.modifyAll(file);
    }

    @PostMapping(path ="/grantAccessAll",consumes = MULTIPART_FORM_DATA_VALUE)
    public GlobalReponse grantAccessAll(@RequestPart("file") MultipartFile file) {
        return this.service.grantAccessAll(file);
    }

    @PostMapping(path = "/clearTrialData")
    public GlobalReponse clearTrialData() {
        return this.service.clearTrialData();
    }
}
