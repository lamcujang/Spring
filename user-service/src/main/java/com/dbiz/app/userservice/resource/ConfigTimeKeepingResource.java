package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.ConfigTimeKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigTimeKeepingQueryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/configTimeKeeping"})
@Slf4j
@RequiredArgsConstructor
public class ConfigTimeKeepingResource {
    private final ConfigTimeKeepingService configTimeKeepingService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute ConfigTimeKeepingQueryRequest request) {
        log.info("*** ConfigTimeKeeping List, resource; fetch all config time keepings ***");
        return this.configTimeKeepingService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ConfigTimeKeepingDto request) {
        log.info("*** ConfigTimeKeeping, resource; save config time keeping ***");
        return this.configTimeKeepingService.save(request);
    }
}
