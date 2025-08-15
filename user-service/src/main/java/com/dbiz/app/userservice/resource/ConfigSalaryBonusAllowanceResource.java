package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.ConfigSalaryBonusAllowanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigSalaryBonusAllowanceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/configSalaryBonusAllowance"})
@Slf4j
@RequiredArgsConstructor
public class ConfigSalaryBonusAllowanceResource {
    private final ConfigSalaryBonusAllowanceService service;

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody ConfigSalaryBonusAllowanceDto dto) {
        log.info("ConfigSalaryBonusAllowanceService Resource save");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("ConfigSalaryBonusAllowanceService Resource delete");
        return ResponseEntity.ok(this.service.deleteById(id));
    }
}
