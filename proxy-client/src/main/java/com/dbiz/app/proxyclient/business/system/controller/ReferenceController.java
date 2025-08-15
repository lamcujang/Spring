package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.ReferenceClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/reference")
@RequiredArgsConstructor
public class ReferenceController {
    private final ReferenceClientService clientService;

    @GetMapping("/{id}")
    public GlobalReponse findById(@PathVariable(name = "id") Integer id, HttpSession session) {
        return this.clientService.findById(id);
    }
    @GetMapping("/findAll")
    public GlobalReponsePagination   findAll(@SpringQueryMap ReferenceQueryRequest request) {
        return this.clientService.findALl(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ReferenceDto  dto) {
        return this.clientService.save(dto);
    }

    @PutMapping("/update")
    public GlobalReponse update(@RequestBody ReferenceDto dto) {
        return this.clientService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalReponse deleteById(@PathVariable(name = "id") Integer id) {
        return this.clientService.delete(id);
    }
}
