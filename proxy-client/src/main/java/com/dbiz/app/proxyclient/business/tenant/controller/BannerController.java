package com.dbiz.app.proxyclient.business.tenant.controller;


import com.dbiz.app.proxyclient.business.tenant.service.BannerClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
@Slf4j
public class BannerController {
    private final BannerClientService service;

    @GetMapping("/findByOrgId/{orgId}")
    public ResponseEntity<GlobalReponse> getOrgBanner(@PathVariable Integer orgId) {
        log.info("*** Org List, controller; fetch all Org *");
        return service.getOrgBanner(orgId);
    }

    @GetMapping("/findByTenantId/{tenantId}")
    public ResponseEntity<GlobalReponse> getTenatBanner(@PathVariable Integer tenantId) {
        log.info("*** Tenant List, controller; fetch all Tenant *");
        return service.getTenantBanner(tenantId);
    }

    @PostMapping("/saveTenantBanner")
    public ResponseEntity<GlobalReponse> saveTenant(@RequestBody TenantBannerDto tenantBannerDto){
        log.info("*** Org List, controller; fetch all Org *");
        return service.saveTenant(tenantBannerDto);
    }

    @PostMapping("/saveOrgBanner")
    public ResponseEntity<GlobalReponse> saveOrg(@RequestBody OrgBannerDto orgBannerDto)
    {
        log.info("*** Org List, controller; fetch all Org *");
        return service.saveOrg(orgBannerDto);
    }

}
