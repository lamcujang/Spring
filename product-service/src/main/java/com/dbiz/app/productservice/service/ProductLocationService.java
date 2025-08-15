package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.SaveProductLocationDto;
import org.common.dbiz.payload.GlobalReponse;

public interface ProductLocationService {


    GlobalReponse deleteById(Integer productLocationId);

    GlobalReponse saveProductLocation(SaveProductLocationDto request);

    GlobalReponse saveInternalProductLocation(SaveProductLocationDto request);
}
