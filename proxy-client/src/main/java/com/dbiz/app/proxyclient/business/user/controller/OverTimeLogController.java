package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.OverTimeLogClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.request.OverTimeLogRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/overTimeLogs")
@RequiredArgsConstructor
public class OverTimeLogController {
    private final OverTimeLogClientService clientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap OverTimeLogRequest request) {
        return ResponseEntity.ok(clientService.findAll(request)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody OverTimeLogDto request) {
        return ResponseEntity.ok(clientService.save(request)).getBody();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.delete(id)).getBody();
    }
}
