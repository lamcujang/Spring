package com.dbiz.app.systemservice.resource;


import com.dbiz.app.systemservice.service.SignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/signature"})
@Slf4j
@RequiredArgsConstructor
public class SignatureResource {

     private final SignatureService service;
     @PostMapping("/napas/verify")
     public GlobalReponse verifyNapasData(@RequestBody PayloadNapasDto dto) {
         log.info("*** Get Napas config *");
         return this.service.verifyNapasData(dto);
     }

     @PostMapping("/napas/sign")
     public GlobalReponse signNapasData(@RequestBody PayloadNapasDto dto) {
         log.info("*** Get Napas config *");
         return this.service.signNapasData(dto);
     }
}
