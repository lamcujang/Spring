package com.dbiz.app.proxyclient.business.system.controller;


import com.dbiz.app.proxyclient.business.system.service.DoctypeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/docType")
@Slf4j
@RequiredArgsConstructor
public class DoctypeController {

    private final DoctypeClientService service;

    @GetMapping("/{code}")
    public GlobalReponse getDoctypeByCode(@PathVariable String code) {
        log.info("*** Doctype, resource; fetch doctype by code: {} ***", code);
        return this.service.getDoctypeByCode(code);
    }
}
