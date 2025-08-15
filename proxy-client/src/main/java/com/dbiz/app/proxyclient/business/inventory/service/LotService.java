package com.dbiz.app.proxyclient.business.inventory.service;


import org.common.dbiz.dto.inventoryDto.CreateLotDto;
import org.common.dbiz.dto.inventoryDto.LotDto;
import org.common.dbiz.dto.inventoryDto.LotReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "INVENTORY-SERVICE", contextId = "lotService",
        path = "/inventory-service/api/v1/lot")
public interface LotService {

    @PostMapping
    public ResponseEntity<GlobalReponse> create(@RequestBody @Valid final CreateLotDto DTO);

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id);

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getLots(@SpringQueryMap final LotReqDto dto);
}
