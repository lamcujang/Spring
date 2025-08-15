package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tax")
@Slf4j
@RequiredArgsConstructor
public class TaxResource {

    private final TaxService service;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final TaxQueryRequest  queryRequest) {
            log.info("*** Tax, controller; fetch Tax by id *");
        return ResponseEntity.ok(this.service.findAll(queryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody final TaxDto  dto) {
        log.info("*** Tax, controller; save Tax *");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final TaxDto dto) {
        log.info("*** Tax, controller; update Tax *");
        return ResponseEntity.ok(this.service.save(dto));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id) {
        log.info("*** Tax, controller; delete Tax by id *");
        return ResponseEntity.ok(this.service.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** Tax, controller; fetch Tax by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

}
