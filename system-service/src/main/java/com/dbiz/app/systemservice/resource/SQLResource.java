package com.dbiz.app.systemservice.resource;


import com.dbiz.app.systemservice.service.SQLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.POST;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(value = {"/api/v1/sql"})
@Slf4j
@RequiredArgsConstructor
public class SQLResource {

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
