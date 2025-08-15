package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.domain.LeaveApplication;
import com.dbiz.app.userservice.service.LeaveApplicationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.LeaveApplicationDto;
import org.common.dbiz.dto.userDto.request.LeaveApplicationRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/leaveApplications"})
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApplicationResource {
    LeaveApplicationService service;

    @GetMapping
    public GlobalReponsePagination findAll(@ModelAttribute LeaveApplicationRequest request) {
        log.info("*** Leave Application List, resource; fetch all Leave Application  *");

        return this.service.findAll(request);
    }

    @PostMapping
    public GlobalReponse save(@RequestBody LeaveApplicationDto request) {
        log.info("*** Leave Application List, resource; fetch all Leave Application  *");

        return this.service.save(request);
    }

    @DeleteMapping("/{id}")
    public GlobalReponse delete(@PathVariable Integer id) {
        log.info("*** Over time log List, resource; delete Overtime log *");
        return this.service.deleteById(id);
    }
}
