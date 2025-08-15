package com.dbiz.app.systemservice.resource;


import com.dbiz.app.systemservice.service.DoctypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/docType"})
@Slf4j
@RequiredArgsConstructor
public class DoctypeResource {

    private final DoctypeService service;

    @GetMapping("/{code}")
    public GlobalReponse getDoctypeByCode(@PathVariable String code) {
        log.info("*** Doctype, resource; fetch doctype by code: {} ***", code);
        return this.service.getDoctypeByCode(code);
    }
}
