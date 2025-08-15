package com.dbiz.app.systemservice.resource;

import com.dbiz.app.systemservice.service.GovernmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ProvinceQueryRequest;
import org.common.dbiz.request.systemRequest.WardQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/government"})
@Slf4j
@RequiredArgsConstructor
public class GovernmentResource {

    private final GovernmentService governmentService;

    @PostMapping
    public ResponseEntity<GlobalReponse> loadAdministrativeDivisions() {
        log.info("*** Government - resource : load ***");
        return ResponseEntity.ok(governmentService.loadAdministrativeDivisions());
    }

    @GetMapping
    ResponseEntity<GlobalReponsePagination> getProvinceWithWard(@ModelAttribute final ProvinceQueryRequest request) {
        log.info("*** Government - resource : get Province with Ward ***");
        return ResponseEntity.ok(governmentService.getProvinceWithWard(request));
    }

    @GetMapping("/province")
    ResponseEntity<GlobalReponsePagination> getProvince(@ModelAttribute final ProvinceQueryRequest request) {
        log.info("*** Government - resource : get Province ***");
        return ResponseEntity.ok(governmentService.getProvince(request));
    }

    @GetMapping("/ward")
    ResponseEntity<GlobalReponsePagination> getWard(@ModelAttribute final WardQueryRequest request) {
        log.info("*** Government - resource : get Ward ***");
        return ResponseEntity.ok(governmentService.getWard(request));
    }

}
