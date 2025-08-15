package com.dbiz.app.tenantservice.service;

import com.dbiz.app.tenantservice.service.BaseServiceGeneric;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.OrgBannerQueryRequest;

public interface OrgBannerService extends BaseServiceGeneric<OrgBannerDto,Integer, OrgBannerQueryRequest> {

    GlobalReponse findByOrgId(Integer orgId);
}
