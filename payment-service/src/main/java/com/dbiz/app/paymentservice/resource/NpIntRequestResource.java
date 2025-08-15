package com.dbiz.app.paymentservice.resource;

import com.dbiz.app.paymentservice.service.NpIntRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NpIntRequestDto;
import org.common.dbiz.dto.paymentDto.request.BankNpReqDto;
import org.common.dbiz.dto.paymentDto.request.NpIntRequestReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/napas/int")
@Slf4j
@RequiredArgsConstructor
public class NpIntRequestResource {

    private final NpIntRequestService service;

    @GetMapping("/bank")
    public ResponseEntity<GlobalReponsePagination> findBank(@ModelAttribute @Valid final BankNpReqDto reqDto) {
        log.info("*** NapasInt resource; findBank ***");
        return ResponseEntity.ok(this.service.findBank(reqDto));
    }

    @GetMapping("/request")
    public ResponseEntity<GlobalReponsePagination> findRequest(@ModelAttribute @Valid final NpIntRequestReqDto request) {
        log.info("*** NapasInt, resource; findRequest NpIntRequest ***");
        return ResponseEntity.ok(this.service.findRequest(request));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final NpIntRequestDto dto) {
        log.info("*** NapasInt, resource; save NpIntRequest ***");
        return ResponseEntity.ok(this.service.save(dto));

    }
}
