package com.dbiz.app.systemservice.resource;

import com.dbiz.app.systemservice.service.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.VersionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v1/version"})
@Slf4j
@RequiredArgsConstructor
public class VersionResource {
    private final VersionService service;

    @GetMapping("/check")
    GlobalReponsePagination getAll(@ModelAttribute VersionRequest req){
        return this.service.findAll(req);
    }
}
