package com.dbiz.app.userservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.request.userRequest.DeletePartnerGroupByIds;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;

import java.util.List;

public interface PartnerGroupService  extends BaseServiceGeneric<PartnerGroupDto, Integer, PartnerGroupQuery>{
    
    GlobalReponse removeChildPartner(PartnerGroupDto param);

    GlobalReponse deleteAllByIds(DeletePartnerGroupByIds request);

    GlobalReponse intSave(List<PartnerGroupDto> listInt);

    GlobalReponse intSaveERPNext(List<PartnerGroupDto> listInt);
}
