package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "erpIntegrationClientService", path = "/integration-service/api/v1/erpIntegration", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ErpIntegrationClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ErpIntegrationQueryRequest request) ;
    @PostMapping("/save")
    public ResponseEntity<GlobalReponse>  save(@RequestBody ErpIntegrationDto  erpIntegrationDto);


    @PostMapping("/syncIntegration")
    public ResponseEntity<GlobalReponse> syncIntegration(@RequestBody SyncIntegrationCredential credential) ;

    @GetMapping("/testApi")
    public GlobalReponse testApi();


    @GetMapping("/syncPosInvoice")
    public ResponseEntity<GlobalReponse> syncPosInvoice(@RequestParam("posOrderId")Integer posInteger, @RequestParam("orgId")Integer orgId);

    @PostMapping("/syncShiftIntegration")
    public ResponseEntity<GlobalReponse> syncShiftIntegration(@RequestBody ShiftIntDto credential);

    @PostMapping("/syncImage")
    public ResponseEntity<GlobalReponse> syncImageProduct(@RequestBody SyncIntegrationCredential credential);

    @PostMapping("/handLeEx")
    public ResponseEntity<GlobalReponse> handleEx(@RequestBody List<UserDto> credential);

    @PostMapping("/activeToggle")
    public ResponseEntity<GlobalReponse> activeToggle(@RequestBody ErpIntegrationDto  erpIntegrationDto);
}
