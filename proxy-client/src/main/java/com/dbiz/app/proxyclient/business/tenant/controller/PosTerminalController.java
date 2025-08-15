package com.dbiz.app.proxyclient.business.tenant.controller;


import com.dbiz.app.proxyclient.business.tenant.service.PosTerminalClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/terminal")
@Slf4j
@RequiredArgsConstructor
public class PosTerminalController {

    private final PosTerminalClientService posTerminalService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap PosTerminalQueryRequest request) {
        log.info("*** Pos Terminal, controller; fetch all Pos Terminal *");
        return ResponseEntity.ok(posTerminalService.getAll(request).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody PosTerminalDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        return ResponseEntity.ok(posTerminalService.save(dto).getBody());
    }

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id) {
        log.info("*** Pos Terminal, controller; fetch Pos Terminal by id *");
        return ResponseEntity.ok(posTerminalService.findById(id).getBody());
    }

    @PostMapping("/delete")
    GlobalReponse deleteByPosTerminalId(@RequestBody PosTerminalDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        return posTerminalService.deleteByPosTerminalId(dto);
    }
}
