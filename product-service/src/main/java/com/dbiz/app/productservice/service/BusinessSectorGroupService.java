package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.BusinessSectorDto;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;

public interface BusinessSectorGroupService extends BaseServiceGeneric<BusinessSectorGroupDto ,Integer, BusinessSectorGroupQueryRequest> {
}
