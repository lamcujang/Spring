package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.service.IndustryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.IndustryQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/industry"})
@Slf4j
@RequiredArgsConstructor
public class IndustryResource {
    private final IndustryService industryService;

    @GetMapping("/getAll")
    public ResponseEntity<GlobalReponsePagination> getAllIndustries(@ModelAttribute IndustryQueryRequest request){
        log.info("*** IndustryDto List, controller; fetch all industries *");

        return ResponseEntity.ok(industryService.findAll(request));
    }

    @GetMapping("/getAllGroupByType")
    public ResponseEntity<GlobalReponsePagination> getAllGroupByType(){
        log.info("*** IndustryDto List, controller; fetch all industries *");

        return ResponseEntity.ok(industryService.getAllGroupByType());
    }
}
