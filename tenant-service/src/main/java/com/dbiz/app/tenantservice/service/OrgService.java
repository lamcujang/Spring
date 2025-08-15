package com.dbiz.app.tenantservice.service;

import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.OrgEmenuDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.request.intergrationRequest.OrgIntDto;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.hibernate.loader.plan.exec.internal.BasicCollectionLoadQueryDetails;

public interface OrgService extends BaseServiceGeneric<OrgDto, Integer, OrgQueryRequest>{

    GlobalReponse integrationOrg(OrgIntDto param);

    GlobalReponse  findByOrgErp(Integer orgErpId);

    GlobalReponse findByOrgAccess(Integer userId);

    GlobalReponse getOrgEmenu(Integer orgId);

    GlobalReponse saveOrgEmenu(OrgEmenuDto request);


}
