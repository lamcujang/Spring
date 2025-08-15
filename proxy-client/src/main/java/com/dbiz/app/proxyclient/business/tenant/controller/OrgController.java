package com.dbiz.app.proxyclient.business.tenant.controller;


import com.dbiz.app.proxyclient.business.tenant.service.OrgClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.OrgEmenuDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/v1/org")
@RequiredArgsConstructor
@Slf4j
public class OrgController {
    private final OrgClientService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap OrgQueryRequest orgQueryRequest) {
        GlobalReponsePagination response = this.service.getAllOrg(orgQueryRequest).getBody();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody OrgDto dto) {
        GlobalReponse response = this.service.save(dto).getBody();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody OrgDto dto) {
        GlobalReponse response = this.service.update(dto).getBody();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id) {
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        log.info("Begin proxy fetch ORG at {}", startTime.format(formatter));
        GlobalReponse response = this.service.findById(id).getBody();

        LocalDateTime endTime = LocalDateTime.now();
        log.info("End proxy fetch ORG at {}", endTime.format(formatter));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") Integer id) {
        GlobalReponse response = this.service.deleteById(id).getBody();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOrgAccess/{userId}")
    public ResponseEntity<GlobalReponse> getOrgAccess(@PathVariable("userId") Integer userId) {
        log.info("*** Org, controller; fetch Org by userId *");

        return ResponseEntity.ok(this.service.getOrgAccess(userId).getBody());
    }
    @GetMapping("/getOrgEmenu")
    public ResponseEntity<GlobalReponse> getOrgEmenu(@RequestParam("orgId") Integer orgId) {
        log.info("*** Org, controller; fetch Org by userId *");
        return service.getOrgEmenu(orgId);
    }

    @PostMapping("/saveOrgEmenu")
    public ResponseEntity<GlobalReponse> saveOrgEmenu(@RequestBody OrgEmenuDto dto) {
        log.info("*** Org, controller; save Org *");
        return  service.saveOrgEmenu(dto);
    }


}
