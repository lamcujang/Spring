package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.IconService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.IconDto;
import org.common.dbiz.dto.productDto.request.IconReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/icons")
@Slf4j
@RequiredArgsConstructor
public class IconController {

    private final IconService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getIcons(@SpringQueryMap IconReqDto dto) {
        log.info("*** Get, resource; get all icons  *");
        return ResponseEntity.ok(this.service.getIcons(dto)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> createIcon(@RequestBody @Valid final IconDto DTO) {
        log.info("*** Save, resource; save icon  *");
        return ResponseEntity.ok(this.service.createIcon(DTO)).getBody();
    }
}
