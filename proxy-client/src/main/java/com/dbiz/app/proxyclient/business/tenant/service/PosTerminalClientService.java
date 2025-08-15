package com.dbiz.app.proxyclient.business.tenant.service;


import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TENANT-SERVICE", contextId = "posTerminalService", path = "/tenant-service/api/v1/terminal")
public interface PosTerminalClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap PosTerminalQueryRequest request);

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody PosTerminalDto dto);

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id);

    @PostMapping("/delete")
    GlobalReponse deleteByPosTerminalId(@RequestBody PosTerminalDto dto);
}
