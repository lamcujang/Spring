package com.dbiz.app.paymentservice.resource;


import org.common.dbiz.dto.paymentDto.PayMethodOrgDto;
import com.dbiz.app.paymentservice.service.PayMethodOrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payMethodOrg")
@Slf4j
@RequiredArgsConstructor
public class PayMethodOrgResource {

    private final PayMethodOrgService service;

    @GetMapping("/save")
    public ResponseEntity<GlobalReponse > save(@ModelAttribute @Valid final PayMethodOrgDto request) {
        log.info("*** Bank, resource; Find All Bank  ***");
        return ResponseEntity.ok(this.service.save(request));
    }
}
