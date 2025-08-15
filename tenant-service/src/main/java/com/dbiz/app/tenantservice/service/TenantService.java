package com.dbiz.app.tenantservice.service;

import com.dbiz.app.tenantservice.domain.Tenant;
import org.common.dbiz.dto.tenantDto.GetAssetsImageDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.TenantQueryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TenantService extends BaseServiceGeneric<TenantDto, Integer, TenantQueryRequest>{
    Page<TenantDto> findAll(final Pageable pageable);

    GlobalReponse findByDomainURL(final String domainUrl);

    TenantDto updateIndustry(TenantDto dTenantDto);

    Tenant save(final Tenant dTenant);

    GlobalReponse findById(final Integer tenantId);


    TenantDto getById(final Integer tenantId);

    TenantAndOrgDto getByTenantAndOrgCode(final Integer tenantId);


    GlobalReponse createTenant(TenantDto tenantDto);

    GlobalReponse getByIdForIDServerInfo(Integer tenantId);

    GlobalReponse getAssetsImage(GetAssetsImageDto getAssetsImageDto);

    GlobalReponse update(TenantDto tenantDto);

    void changeTenantByMerchantCode(String merchantCode);
}
