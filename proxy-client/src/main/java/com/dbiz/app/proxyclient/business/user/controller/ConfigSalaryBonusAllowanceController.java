package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.ConfigSalaryBonusAllowanceClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigSalaryBonusAllowanceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configShift")
@Slf4j
@RequiredArgsConstructor
public class ConfigSalaryBonusAllowanceController {
    ConfigSalaryBonusAllowanceClientService clientService;

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody ConfigSalaryBonusAllowanceDto dto) {
        log.info("ConfigSalaryBonusAllowanceService Controller save");
        return ResponseEntity.ok(this.clientService.save(dto)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("ConfigSalaryBonusAllowanceService Controller delete");
        return ResponseEntity.ok(this.clientService.delete(id)).getBody();
    }
}
