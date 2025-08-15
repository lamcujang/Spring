package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.OverTimeLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.LeaveApplicationDto;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.request.LeaveApplicationRequest;
import org.common.dbiz.dto.userDto.request.OverTimeLogRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/overTimeLogs"})
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OverTimeLogResource {
    OverTimeLogService service;

    @GetMapping
    public GlobalReponsePagination findAll(@ModelAttribute OverTimeLogRequest request) {
        log.info("*** Over time log List, resource; fetch all Overtime log *");

        return this.service.findAll(request);
    }

    @PostMapping
    public GlobalReponse save(@RequestBody OverTimeLogDto request) {
        log.info("*** Over time log List, resource; save Overtime log *");

        return this.service.save(request);
    }

    @DeleteMapping("/{id}")
    public GlobalReponse delete(@PathVariable Integer id) {
        log.info("*** Over time log List, resource; delete Overtime log *");
        return this.service.deleteById(id);
    }
}
