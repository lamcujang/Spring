package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.PenaltyDeductionClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/penaltyDeductions")
@RequiredArgsConstructor
@Slf4j
public class PenaltyDeductionController {
    private final PenaltyDeductionClientService clientService;

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody PenaltyDeductionDto dto) {
        log.info("save Penalty Deduction in resource");
        return ResponseEntity.ok(clientService.save(dto)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("delete by ID Penalty Deduction in resource");
        return ResponseEntity.ok(clientService.delete(id)).getBody();
    }
}
