package com.dbiz.app.tenantservice.service;

import com.dbiz.app.tenantservice.domain.TenantBanner;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.TenantBannerQueryRequest;

public interface TenantBannerService extends BaseServiceGeneric<TenantBannerDto, Integer, TenantBannerQueryRequest>{
    GlobalReponse findByTenantId(Integer tenantId);
}
