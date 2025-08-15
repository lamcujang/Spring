package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.domain.Tenant;
import com.dbiz.app.tenantservice.service.TenantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.GetAssetsImageDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.dto.tenantDto.reponse.collection.DtoCollectionResponse;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = {"/api/v1/tenant"})
@Slf4j
@RequiredArgsConstructor
public class TenantResource {

    private final TenantService tenantService;
    private final MessageSource messageSource;

    @GetMapping("/getAll")
    public ResponseEntity<DtoCollectionResponse<TenantDto>> getAllTenants(@RequestParam int page, @RequestParam int size) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        Page<TenantDto> tenantPage = tenantService.findAll(PageRequest.of(page, size));
        DtoCollectionResponse<TenantDto> response = new DtoCollectionResponse<>(tenantPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getbyDomain")
    public ResponseEntity<GlobalReponse> getByDomainURL(@RequestParam("domainUrl") String domainUrl) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        return ResponseEntity.ok(tenantService.findByDomainURL(domainUrl));
    }

    @PutMapping("/updateDomain")
    public ResponseEntity<GlobalReponse> updatedomain(@RequestBody TenantDto dTenantDto, HttpServletRequest request) {
        log.info("Content-Type: " + request.getContentType());
        log.info("*** TenantDto update, controller; *");
        TenantDto dataResponse = tenantService.updateIndustry(dTenantDto);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(dataResponse);
        response.setMessage("TenantDto update industry successfully!");
        response.setErrors("");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registerDomain")
    public ResponseEntity<GlobalReponse> insertDomain(@RequestBody Tenant dTenant, HttpServletRequest request) {
        log.info("Content-Type: " + request.getContentType());
        log.info("*** TenantDto update, controller; *");
        Tenant dataResponse = tenantService.save(dTenant);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(dataResponse);
        response.setMessage("TenantDto insert industry successfully!");
        response.setErrors("");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{getbyId}")
    public ResponseEntity<TenantDto> getById(@PathVariable("getbyId") Integer tenantId) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        TenantDto dataResponse = tenantService.getById(tenantId);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/findById")
    public ResponseEntity<GlobalReponse> findById(@RequestParam("tenantId") Integer tenantId) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        TenantDto dataResponse = tenantService.getById(tenantId);
        GlobalReponse response = GlobalReponse.builder().status(HttpStatus.OK.value()).data(dataResponse).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).errors("").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/idserver/{getbyId}")
    public ResponseEntity<GlobalReponse> getByIdForIDServerInfo(@PathVariable("getbyId") Integer tenantId) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        return ResponseEntity.ok(tenantService.getByIdForIDServerInfo(tenantId));
    }

    @GetMapping("/getInforTenantOrg/{tenantId}/{orgId}")
    public ResponseEntity<GlobalReponse> getInforTenantOrg(@PathVariable("tenantId") Integer tenantId, @PathVariable("orgId") Integer orgId) {
        log.info("*** TenantDto List, controller; fetch all tenants *");
        TenantAndOrgDto dataResponse = tenantService.getByTenantAndOrgCode(tenantId);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(dataResponse);
        response.setMessage("TenantDto fetched successfully!");
        response.setErrors("");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/createTenant")
    public ResponseEntity<GlobalReponse> save(@RequestBody TenantDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantService.createTenant(dto));
    }

    @PostMapping("/getImageAsset")
    public ResponseEntity<GlobalReponse> getImageAsset(@RequestBody GetAssetsImageDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantService.getAssetsImage(dto));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody TenantDto dto) {
        log.info("*** TENANT, controller; create new Tenant *");
        return ResponseEntity.ok(tenantService.save(dto));
    }


}
