package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.GovernmentClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ProvinceQueryRequest;
import org.common.dbiz.request.systemRequest.WardQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/government"})
@Slf4j
@RequiredArgsConstructor
public class GovernmentController {

    private final GovernmentClientService governmentClientService;

    @PostMapping
    public ResponseEntity<GlobalReponse> loadAdministrativeDivisions() {
        log.info("*** Government - controller : load ***");
        return ResponseEntity.ok(governmentClientService.loadAdministrativeDivisions().getBody());
    }

    @GetMapping
    ResponseEntity<GlobalReponsePagination> getProvinceWithWard(@ModelAttribute final ProvinceQueryRequest request) {
        log.info("*** Government - controller : get Province with Ward ***");
        return ResponseEntity.ok(governmentClientService.getProvinceWithWard(request).getBody());
    }

    @GetMapping("/province")
    ResponseEntity<GlobalReponsePagination> getProvince(@ModelAttribute final ProvinceQueryRequest request) {
        log.info("*** Government - controller : get Province ***");
        return ResponseEntity.ok(governmentClientService.getProvince(request).getBody());
    }

    @GetMapping("/ward")
    ResponseEntity<GlobalReponsePagination> getWard(@ModelAttribute final WardQueryRequest request) {
        log.info("*** Government - controller : get Ward ***");
        return ResponseEntity.ok(governmentClientService.getWard(request).getBody());
    }

}
