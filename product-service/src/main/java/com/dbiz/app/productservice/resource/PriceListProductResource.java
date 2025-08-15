package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.PriceListProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.PriceListProductDto;
import org.common.dbiz.dto.productDto.request.PriceListProductReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ApplyNewFormulaPriceRequest;
import org.common.dbiz.request.productRequest.FindAllPriceListAndProductRequest;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/priceListProduct")
@Slf4j
@RequiredArgsConstructor
public class PriceListProductResource {

    private final PriceListProductService service;

    @GetMapping("/{priceListProductId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("priceListProductId") final Integer priceListProductId) {
        log.info("*** PriceProductResource, resource; fetch PriceProductResource by id *");
        return ResponseEntity.ok(this.service.findById(priceListProductId));
    }

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute PriceListProductQueryRequest request) {
        log.info("*** PriceProductResource List, controller; fetch all PriceProductResource *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PriceListProductDto DTO) {
        log.info("*** PriceProductResource resource; save PriceProductResource  *");

        try {
            return ResponseEntity.ok(this.service.save(DTO));
        } catch (Exception e) {
            GlobalReponse response = new GlobalReponse();
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.ok(response);
        }

    }

    @DeleteMapping("/delete/{priceListProductId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListProductId") final Integer priceListProductId) {
        log.info("*** PriceProductResource controller; delete PriceProductResource  *");

        return ResponseEntity.ok(this.service.deleteById(priceListProductId));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final PriceListProductDto DTO) {
        log.info("*** PriceProductResource; save PriceProductResource  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PostMapping("/deleteAllByIds")
    public ResponseEntity<GlobalReponse> deleteAllByIds(@RequestBody PriceListProductDto.IdsRequest ids) {
        log.info("*** PriceProductResource; delete all PriceProductResource by ids  *");
        return ResponseEntity.ok(this.service.deleteAllByIds(ids));
    }

    @PostMapping("/applyAllNewFormulaPrice")
    public ResponseEntity<GlobalReponse> applyAllNewFormulaPrice(@RequestBody final ApplyNewFormulaPriceRequest request) {
        log.info("*** PriceProductResource; apply all new formula price  *");
        return ResponseEntity.ok(this.service.applyAllNewFormulaPrice(request));
    }

    @PostMapping("/deleteAllByPriceListId")
    public ResponseEntity<GlobalReponse> deleteAllByPriceListId(@RequestBody final ApplyNewFormulaPriceRequest request) {
        log.info("*** PriceProductResource; delete all by price list id  *");
        return ResponseEntity.ok(this.service.deleteAllByPriceListId(request));
    }

    @GetMapping("/findAllProductAndPriceList")
    public ResponseEntity<GlobalReponsePagination> findAllProductAndPriceList(@ModelAttribute FindAllPriceListAndProductRequest request) {
        log.info("*** PriceProductResource; fetch all product and price list  *");
        return ResponseEntity.ok(this.service.findAllPriceListAndProduct(request));
    }

    @GetMapping("/findAllPriceListProduct")
    public ResponseEntity<GlobalReponsePagination> findAllPriceListProduct(@ModelAttribute PriceListProductReqDto request) {
        log.info("*** PriceListProductResource; fetch all price list product *");
        return ResponseEntity.ok(this.service.findAllPriceListProduct(request));
    }
}
