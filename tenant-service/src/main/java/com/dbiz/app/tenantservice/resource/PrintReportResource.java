package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.service.PrintReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.PrintReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/printReports")
@Slf4j
@RequiredArgsConstructor
public class PrintReportResource {

    private final PrintReportService entityService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute final PrintReportQueryRequest entityQueryRequest) {
        log.info("*** PrintReport, controller; fetch PrintReport all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody PrintReportDto entityDto)
    {
        log.info("*** Floor, controller; save floor *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @GetMapping("/findAllByTenant")
    public ResponseEntity<GlobalReponsePagination> findAllByTenant(@ModelAttribute final PrintReportQueryRequest entityQueryRequest) {
        log.info("*** PrintReport, controller; fetch PrintReport all *");
        return ResponseEntity.ok(this.entityService.findAllByTenant(entityQueryRequest));
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody List<Map<String, Object>> rawList)
    {
        log.info("*** Floor, controller; save floor *");
        return ResponseEntity.ok(this.entityService.saveAll(rawList));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody PrintReportDto entityDto)
    {
        log.info("*** floor, controller; update entity *");
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        log.info("*** PrintReport, controller; delete PrintReport *");
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        log.info("*** PrintReport, controller; fetch PrintReport by id *");
        return ResponseEntity.ok(this.entityService.findById(id));
    }
}
