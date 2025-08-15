package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "orderIntegrationClientService", path = "/integration-service/api/v1/orderIntegration", decode404 = true
        , configuration = FeignClientConfig.class )
public interface OrderIntegrationClientService {

    @PostMapping
    public ResponseEntity<GlobalReponse> getOrders(@RequestBody SyncIntegrationCredential dto) ;

    @PostMapping("/product")
    public ResponseEntity<GlobalReponse> getOrderMains(@RequestBody SyncIntegrationCredential dto);
}
