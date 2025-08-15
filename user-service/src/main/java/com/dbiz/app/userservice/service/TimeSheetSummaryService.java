package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.TimeSheetSummaryDto;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.payload.GlobalReponse;

public interface TimeSheetSummaryService extends BaseServiceGeneric<TimeSheetSummaryDto, Integer, TimeSheetSummaryRequest>{
    GlobalReponse getTimeSheetDetail(TimeSheetSummaryRequest req);
}
