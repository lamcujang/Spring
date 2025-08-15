package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.TimeSheetSummaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.TimeSheetSummaryDto;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/timesheetSummaries"})
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeSheetSummaryResource {
    TimeSheetSummaryService service;

    @GetMapping
    public GlobalReponsePagination findAll(@ModelAttribute TimeSheetSummaryRequest request) {
        log.info("***Timesheet Summary List, resource; fetch allTimesheet Summary *");

        return this.service.findAll(request);
    }

    @PostMapping
    public GlobalReponse save(@RequestBody TimeSheetSummaryDto request) {
        log.info("*** Timesheet Summary List, resource; save Timesheet Summary *");

        return this.service.save(request);
    }

    @GetMapping("/detail")
    public GlobalReponse getTimeSheetDetail(@ModelAttribute TimeSheetSummaryRequest request) {
        log.info("***Timesheet Summary List, resource; fetch allTimesheet Summary *");

        return this.service.getTimeSheetDetail(request);
    }
}
