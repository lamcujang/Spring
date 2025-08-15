package com.dbiz.app.proxyclient.business.tenant.service;


import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TENANT-SERVICE", contextId = "bannerClientService", path = "/tenant-service/api/v1/banner", decode404 = true)
public interface BannerClientService {


    @GetMapping("/findByOrgId/{orgId}")
    public ResponseEntity<GlobalReponse> getOrgBanner(@PathVariable Integer orgId);

    @GetMapping("/findByTenantId/{tenantId}")
    public ResponseEntity<GlobalReponse> getTenantBanner(@PathVariable Integer tenantId);

    @PostMapping("/saveTenantBanner")
    public ResponseEntity<GlobalReponse> saveTenant(@RequestBody TenantBannerDto tenantBannerDto);

    @PostMapping("/saveOrgBanner")
    public ResponseEntity<GlobalReponse> saveOrg(@RequestBody OrgBannerDto orgBannerDto);
}










