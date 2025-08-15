package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.ConfigShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/configShift"})
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftResource {
    private final ConfigShiftService configShiftService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute ConfigShiftQueryRequest request) {
        log.info("*** ConfigShift List, resource; fetch all config shifts ***");
        return this.configShiftService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ConfigShiftDto request) {
        log.info("*** ConfigShift, resource; save config shift ***");
        return this.configShiftService.save(request);
    }
}
