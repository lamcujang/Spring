package com.dbiz.app.tenantservice.controller;


import com.dbiz.app.tenantservice.domain.db.Tenant1;

import com.dbiz.app.tenantservice.dto.request.CreateTenantRequestDto;
import com.dbiz.app.tenantservice.dto.request.RenameTenantRequestDto;
import com.dbiz.app.tenantservice.mapper.TenantMapper;
import com.dbiz.app.tenantservice.service.db.Tenant1Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import org.common.dbiz.dto.tenantDto.reponse.TenantResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tenants")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TenantController {

    Tenant1Service tenantService;

    @GetMapping("/v2")
    @Operation(summary = "Get all tenants")
    public ResponseEntity<List<TenantResponseDto>> getAllTenants() {

//        List<Tenant1> tenants = tenantService.findAll();
//
//        List<TenantResponseDto> responseDtos
//            = TenantMapper.INSTANCE.toResponseDtoList(tenants);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/v2")
    @Operation(summary = "Create a new tenant")
    public ResponseEntity<TenantResponseDto> createTenant(@RequestBody CreateTenantRequestDto tenantDto) {

        TenantResponseDto tenant = tenantService.create(tenantDto);

        return new ResponseEntity<>(tenant, HttpStatus.CREATED);
    }

    @PatchMapping("/v2")
    @Operation(summary = "Rename tenant")
    public ResponseEntity<?> renameTenant(@RequestBody RenameTenantRequestDto params) {

        tenantService.rename(params);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/v2")
    @Operation(summary = "Delete tenant")
    public ResponseEntity<?> deleteTenant(@RequestParam @NonNull Long id) {

        tenantService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
