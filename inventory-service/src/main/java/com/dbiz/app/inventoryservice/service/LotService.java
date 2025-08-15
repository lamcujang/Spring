package com.dbiz.app.inventoryservice.service;

import org.common.dbiz.dto.inventoryDto.CreateLotDto;
import org.common.dbiz.dto.inventoryDto.LotDto;
import org.common.dbiz.dto.inventoryDto.LotReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface LotService {

    GlobalReponsePagination getLots(LotReqDto dto);

    GlobalReponse create(CreateLotDto dto);

    GlobalReponse deleteById(Integer id);
}
