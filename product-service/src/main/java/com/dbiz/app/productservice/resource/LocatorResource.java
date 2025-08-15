package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.LocatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.LocatorQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locators")
@Slf4j
@RequiredArgsConstructor
public class LocatorResource {
    private final LocatorService locatorService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final LocatorQueryRequest  locatorQueryRequest) {
        log.info("*** Locator, controller; fetch locator all *");
        return ResponseEntity.ok(this.locatorService.findAll(locatorQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody LocatorDto  locatorDto)
    {
        log.info("*** locatorDto, controller; save locatorDto *");
        return ResponseEntity.ok(this.locatorService.save(locatorDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody  LocatorDto locatorDto)
    {
        log.info("*** warehouse, controller; update warehouse *");
        return ResponseEntity.ok(this.locatorService.save(locatorDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** warehouse, controller; delete warehouse *");
        return ResponseEntity.ok(this.locatorService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** warehouse, controller; fetch warehouse by id *");
        return ResponseEntity.ok(this.locatorService.findById(id));
    }
}
