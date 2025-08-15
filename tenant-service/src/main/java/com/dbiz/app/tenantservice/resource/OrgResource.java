package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.service.OrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.OrgEmenuDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.request.intergrationRequest.OrgIntDto;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = {"/api/v1/org"})
@Slf4j
@RequiredArgsConstructor
public class OrgResource {

    private final OrgService service;


    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination>  getAllOrg(@ModelAttribute OrgQueryRequest request) {
        log.info("*** Org List, controller; fetch all Org *");
        GlobalReponsePagination response = service.findAll(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody OrgDto dto) {
        log.info("*** Org, controller; save Org *");
        GlobalReponse response = service.save(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody OrgDto dto) {
        log.info("*** Org, controller; update Org *");
        GlobalReponse response = service.save(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id) {
        log.info("*** Org, controller; fetch Org by id *");
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        log.info("Begin fetch ORG at {}", startTime.format(formatter));
        GlobalReponse response = service.findById(id);

        LocalDateTime endTime = LocalDateTime.now();
        log.info("END fetch ORG at {}", endTime.format(formatter));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable("id") Integer id)
    {
        log.info("*** Org, controller; delete Org by id *");
        GlobalReponse response = service.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody OrgIntDto dto) {
        log.info("*** Org, controller; save Org *");
        GlobalReponse response = service.integrationOrg(dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByErpId/{erpId}")
    public ResponseEntity<GlobalReponse> getByErpId(@PathVariable("erpId") Integer erpId) {
        log.info("*** Org, controller; fetch Org by erpId *");
        GlobalReponse response = service.findByOrgErp(erpId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getOrgAccess/{userId}")
    public ResponseEntity<GlobalReponse> getOrgAccess(@PathVariable("userId") Integer userId ) {
        log.info("*** Org, controller; fetch Org by userId *");
        GlobalReponse response = service.findByOrgAccess(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOrgEmenu")
    public ResponseEntity<GlobalReponse> getOrgEmenu(@RequestParam("orgId") Integer orgId) {
        log.info("*** Org, controller; fetch Org by userId *");
        GlobalReponse response = service.getOrgEmenu(orgId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveOrgEmenu")
    public ResponseEntity<GlobalReponse> saveOrgEmenu(@RequestBody OrgEmenuDto dto) {
        log.info("*** Org, controller; save Org *");
        GlobalReponse response = service.saveOrgEmenu(dto);
        return ResponseEntity.ok(response);
    }


}
