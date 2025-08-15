package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.PriceListService;
import com.dbiz.app.productservice.service.PriceListV2Service;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.JsonViewPriceListDto;
import org.common.dbiz.dto.productDto.PriceListIntDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/priceLists")
@Slf4j
@RequiredArgsConstructor
public class PriceListV2Resource {

    private final PriceListV2Service service;

    @PostMapping("/save")
    @JsonView(JsonViewPriceListDto.viewJsonSave.class)
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PricelistDto  DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PutMapping("/update")
    @JsonView(JsonViewPriceListDto.viewJsonSave.class)
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final PricelistDto DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }
}
