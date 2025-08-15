package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.EmployeeBonusAllowancesClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeBonusAllowancesDto;
import org.common.dbiz.dto.userDto.request.EmployeeBonusAllowancesRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employeeBonusAllowances")
@RequiredArgsConstructor
@Slf4j
public class EmployeeBonusAllowancesController {
    private final EmployeeBonusAllowancesClientService clientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap EmployeeBonusAllowancesRequest req) {
        log.info("fetch all EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(clientService.getAll(req)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody EmployeeBonusAllowancesDto dto) {
        log.info("save EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(clientService.save(dto)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("delete by ID EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(clientService.delete(id)).getBody();
    }
}
