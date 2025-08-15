package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.request.BrandReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface BrandService {

    GlobalReponsePagination getBrands(BrandReqDto dto);

    GlobalReponse getBrandById(Integer id);

    GlobalReponse createBrand(BrandDto dto);

    GlobalReponse deleteBrand(Integer id);
}
