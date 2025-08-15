package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.service.PosTerminalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.PosTerminalOrgAccessIntDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.PosTerminalIntDto;
import org.common.dbiz.request.tenantRequest.PosTerminalQueryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/terminal"})
@Slf4j
@RequiredArgsConstructor
public class PosTerminalResource {

    private final PosTerminalService posTerminalService;


    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getAllOrg(@ModelAttribute PosTerminalQueryRequest request) {
        log.info("*** Pos Terminal, controller; fetch all Pos Terminal *");
        return ResponseEntity.ok(posTerminalService.findAll(request));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody PosTerminalDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        return ResponseEntity.ok(posTerminalService.save(dto));
    }

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") Integer id) {
        log.info("*** Pos Terminal, controller; fetch Pos Terminal by id *");
        return ResponseEntity.ok(posTerminalService.findById(id));
    }

    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> saveInt(@RequestBody PosTerminalIntDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        GlobalReponse response = new GlobalReponse();
        try {
            response=   posTerminalService.saveInt(dto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setErrors(e.getMessage());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByErpId/{erpId}")
    public ResponseEntity<GlobalReponse> findByErpId(@PathVariable("erpId") Integer erpId) {
        log.info("*** Pos Terminal, controller; fetch Pos Terminal by erpId *");
        return ResponseEntity.ok(posTerminalService.findByErpId(erpId));
    }

    @PostMapping("/intSaveUser")
    GlobalReponse intSaveUser(@RequestBody PosTerminalDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        return posTerminalService.saveIntUser(dto);
    }

    @PostMapping("/delete")
    GlobalReponse deleteByPosTerminalId(@RequestBody PosTerminalDto dto) {
        log.info("*** Pos Terminal, controller; save Pos Terminal *");
        return posTerminalService.deleteByPosTerminalId(dto);
    }
}
