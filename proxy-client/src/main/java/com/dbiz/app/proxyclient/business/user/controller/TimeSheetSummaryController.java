package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.TimeSheetSummaryClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.TimeSheetSummaryDto;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/timesheetSummaries")
@RequiredArgsConstructor
public class TimeSheetSummaryController {
    private final TimeSheetSummaryClientService clientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap TimeSheetSummaryRequest request) {
            return ResponseEntity.ok(clientService.findAll(request)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody TimeSheetSummaryDto request) {
        return ResponseEntity.ok(clientService.save(request)).getBody();
    }

    @GetMapping("/detail")
    ResponseEntity<GlobalReponse> getTimeSheetDetail(@ModelAttribute TimeSheetSummaryRequest request){
        return ResponseEntity.ok(clientService.getTimeSheetDetail(request)).getBody();
    }
}
