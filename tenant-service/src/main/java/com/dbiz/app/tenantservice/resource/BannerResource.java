package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.service.OrgBannerService;
import com.dbiz.app.tenantservice.service.OrgService;
import com.dbiz.app.tenantservice.service.TenantBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/banner"})
@Slf4j
@RequiredArgsConstructor
public class BannerResource {

    private final OrgBannerService orgBannerService;

    private final TenantBannerService tenantBannerService;


    @GetMapping("/findByOrgId/{orgId}")
    public ResponseEntity<GlobalReponse>  getOrgBanner(@PathVariable Integer orgId) {
        log.info("*** Org List, controller; fetch all Org *");
        GlobalReponse response = orgBannerService.findByOrgId(orgId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByTenantId/{tenantId}")
    public ResponseEntity<GlobalReponse>  getTenantBanner(@PathVariable Integer tenantId) {
        log.info("*** Tenant List, controller; fetch all Tenant *");
        GlobalReponse response = tenantBannerService.findByTenantId(tenantId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveTenantBanner")
    public ResponseEntity<GlobalReponse> saveTenant(@RequestBody TenantBannerDto tenantBannerDto) {
        log.info("*** Org List, controller; fetch all Org *");
        GlobalReponse response = tenantBannerService.save(tenantBannerDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveOrgBanner")
    public ResponseEntity<GlobalReponse> saveOrg(@RequestBody OrgBannerDto orgBannerDto) {
        log.info("*** Org List, controller; fetch all Org *");
        GlobalReponse response = orgBannerService.save(orgBannerDto);

        return ResponseEntity.ok(response);
    }

}
