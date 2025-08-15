package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.PenaltyDeductionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeBonusAllowancesDto;
import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/penaltyDeductions"})
@Slf4j
@RequiredArgsConstructor
public class PenaltyDeductionResource {
    private final PenaltyDeductionService service;

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody PenaltyDeductionDto dto) {
        log.info("save Penalty Deduction in resource");
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("delete by ID Penalty Deduction in resource");
        return ResponseEntity.ok(service.delete(id));
    }
}
