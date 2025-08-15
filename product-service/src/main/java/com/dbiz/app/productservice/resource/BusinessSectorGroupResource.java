package com.dbiz.app.productservice.resource;


import com.dbiz.app.productservice.service.BusinessSectorGroupService;
import com.dbiz.app.productservice.service.BusinessSectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/businessSectorGroup")
@Slf4j
@RequiredArgsConstructor
public class BusinessSectorGroupResource {

    private final BusinessSectorGroupService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final BusinessSectorGroupQueryRequest queryRequest) {
            log.info("*** BusinessSectorGroup, controller; fetch BusinessSectorGroup by id *");
        return ResponseEntity.ok(this.service.findAll(queryRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** BusinessSectorGroup, controller; fetch BusinessSectorGroup by id *");
        return ResponseEntity.ok(this.service.findById(id));
    }

}
