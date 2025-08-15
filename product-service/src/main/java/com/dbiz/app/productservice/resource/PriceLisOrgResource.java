package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.PriceListOrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.PriceListOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListOrgQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/priceListOrg")
@Slf4j
@RequiredArgsConstructor
public class PriceLisOrgResource {

    private final PriceListOrgService service;

    @GetMapping("/{priceListOrgId}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable("priceListOrgId") final Integer priceListOrgId) {
        log.info("*** priceList, controller; fetch price by id *");
        return ResponseEntity.ok(this.service.findById(priceListOrgId));
    }
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute PriceListOrgQueryRequest request)
    {
        log.info("*** priceListOrg List, controller; fetch all priceListOrg *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PriceListOrgDto DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{priceListOrgId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListOrgId") final Integer priceListOrgId) {
        log.info("*** Delete, controller; delete pricelist  *");
        return ResponseEntity.ok(this.service.deleteById(priceListOrgId));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final PriceListOrgDto DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }
}
