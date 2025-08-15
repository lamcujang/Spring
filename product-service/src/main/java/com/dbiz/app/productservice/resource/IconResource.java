package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.IconService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.IconDto;
import org.common.dbiz.dto.productDto.request.IconReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/icons")
@Slf4j
@RequiredArgsConstructor
public class IconResource {

    private final IconService service;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getIcons(@ModelAttribute IconReqDto dto) {
        log.info("*** Get, resource; get all icons  *");
        return ResponseEntity.ok(this.service.getIcons(dto));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> createIcon(@RequestBody @Valid final IconDto DTO) {
        log.info("*** Save, resource; save icon  *");
        return ResponseEntity.ok(this.service.createIcon(DTO));
    }
}
