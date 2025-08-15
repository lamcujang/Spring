package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.domain.EmployeeBonusAllowances;
import com.dbiz.app.userservice.service.EmployeeBonusAllowancesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.userDto.EmployeeBonusAllowancesDto;
import org.common.dbiz.dto.userDto.request.EmployeeBonusAllowancesRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/employeeBonusAllowances"})
@Slf4j
@RequiredArgsConstructor
public class EmployeeBonusAllowancesResource {
    private final EmployeeBonusAllowancesService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getAll(@ModelAttribute EmployeeBonusAllowancesRequest req) {
        log.info("fetch all EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(service.findAll(req));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody EmployeeBonusAllowancesDto dto) {
        log.info("save EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        log.info("delete by ID EmployeeBonusAllowancesRequest in resource");
        return ResponseEntity.ok(service.deleteById(id));
    }
}
