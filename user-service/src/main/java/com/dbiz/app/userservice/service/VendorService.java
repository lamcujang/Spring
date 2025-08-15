package com.dbiz.app.userservice.service;


import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.userRequest.VendorQueryRequest;

import java.util.List;

public interface VendorService extends BaseServiceGeneric<VendorDto, Integer, VendorQueryRequest>{

    GlobalReponse deleteAllIds(VendorDto vendorDto);

    GlobalReponse intSave(List<VendorDto> vendorDto);
}
