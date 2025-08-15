package com.dbiz.app.productservice.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.dbiz.app.productservice.service.PriceListService;
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
@RequestMapping("/api/v1/priceLists")
@Slf4j
@RequiredArgsConstructor
public class PriceListResource {

    private final PriceListService service;

    @GetMapping("/{priceListId}")
    @JsonView(JsonViewPriceListDto.viewJsonFindByID.class)
    public ResponseEntity<GlobalReponse > findById(@PathVariable("priceListId") final Integer priceListId) {
        log.info("*** PriceDTO, controller; fetch price by id *");
        try {
            return ResponseEntity.ok(this.service.findById(priceListId));
        } catch (Exception e) {
            GlobalReponse response = new GlobalReponse();
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setErrors(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/findAll")
    @JsonView(JsonViewPriceListDto.viewJsonFindAll.class)
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute PriceListQueryRequest  request) {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    @JsonView(JsonViewPriceListDto.viewJsonSave.class)
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final PricelistDto  DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @DeleteMapping("/delete/{priceListId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("priceListId") final Integer priceListId) {
        log.info("*** Delete, controller; delete pricelist  *");
        return ResponseEntity.ok(this.service.deleteById(priceListId));
    }

    @PutMapping("/update")
    @JsonView(JsonViewPriceListDto.viewJsonSave.class)
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final PricelistDto DTO) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.save(DTO));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody final List<PriceListIntDto> param) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.intSave(param));
    }

    @GetMapping("/findErpId/{erpId}")
    public ResponseEntity<GlobalReponse> findByErpId(@PathVariable("erpId") final Integer erpId) {

            return ResponseEntity.ok(this.service.findByErpId(erpId));
    }


    @GetMapping("/cashier")
    public ResponseEntity<GlobalReponsePagination > findAllByCashier(@ModelAttribute PriceListQueryRequest  request) {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAllByCashier(request));
    }
    @GetMapping("/cashiers")
    public ResponseEntity<GlobalReponsePagination > findAllByCashiers(@ModelAttribute PriceListQueryRequest  request) {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.service.findAllByCashier(request));
    }

    @PostMapping("/saveIntPosterminal")
    public ResponseEntity<GlobalReponse> saveIntPosterminal(@RequestBody final PriceListIntDto pricelistDto) {
        log.info("*** Save, controller; save pricelist  *");
        return ResponseEntity.ok(this.service.saveIntPosterminal(pricelistDto));
    }
}
