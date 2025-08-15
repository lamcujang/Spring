package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.IconDto;
import org.common.dbiz.dto.productDto.request.IconReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface IconService {

    GlobalReponsePagination getIcons(IconReqDto dto);

    GlobalReponse getIconById(Integer id);

    GlobalReponse createIcon(IconDto dto);

    GlobalReponse deleteIcon(Integer id);
}
