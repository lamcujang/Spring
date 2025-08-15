package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.BusinessSectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BusinessSectorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/businessSector")
@Slf4j
@RequiredArgsConstructor
public class BusinessSectorResource {

    private final BusinessSectorService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final BusinessSectorQueryRequest  queryRequest) {
            log.info("*** BusinessSector, controller; fetch BusinessSector by id *");
        return ResponseEntity.ok(this.service.findAll(queryRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** BusinessSector, controller; fetch BusinessSector by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

}
