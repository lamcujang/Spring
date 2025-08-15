package com.dbiz.app.proxyclient.business.inventory.controller;


import com.dbiz.app.proxyclient.business.inventory.service.LotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.CreateLotDto;
import org.common.dbiz.dto.inventoryDto.LotDto;
import org.common.dbiz.dto.inventoryDto.LotReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/lot")
@Slf4j
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;


    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getLots(@SpringQueryMap final LotReqDto dto) {
        log.info("*** LotDto List, controller; fetch all lot *");
        return ResponseEntity.ok(this.lotService.getLots(dto)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> create(@RequestBody @Valid final CreateLotDto DTO) {
        log.info("*** LotDto, resource; create Lot *");
        return ResponseEntity.ok(this.lotService.create(DTO)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        log.info("*** Delete, controller; delete lot  *");
        return ResponseEntity.ok(this.lotService.delete(id)).getBody();
    }
}
