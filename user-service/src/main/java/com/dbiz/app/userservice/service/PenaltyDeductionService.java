package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.payload.GlobalReponse;

public interface PenaltyDeductionService {
    GlobalReponse save(PenaltyDeductionDto dto);

    GlobalReponse delete(Integer id);
}
