package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.integrationDto.voucher.CheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntCheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.CheckInVoucherDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "VoucherIntegrationClientService", path = "/integration-service/api/v1/voucher", decode404 = true
        , configuration = FeignClientConfig.class )
public interface VoucherIntegrationClientSerivce {



    @PostMapping("/checkVoucherInfo")
    public ResponseEntity<GlobalReponse> checkVoucherInfo(@RequestBody CheckVoucherInfoDto dto);

    @GetMapping("/getVoucherServices")
    public ResponseEntity<GlobalReponse> getVoucherServices(@SpringQueryMap VoucherParamDto dto);

    @GetMapping("/getVoucherServiceOrders")
    public ResponseEntity<GlobalReponsePagination> getVoucherServiceOrders(@SpringQueryMap VoucherParamDto dto);

    @PostMapping("/getVoucherInfo")
    public ResponseEntity<GlobalReponse> getVoucherInfo(@RequestBody CheckVoucherInfoDto dto);

    @PostMapping("/checkInVoucher")
    public ResponseEntity<GlobalReponse> checkInVoucher(@RequestBody CheckInVoucherDto dto);

    @PostMapping("/test")
    public ResponseEntity<GlobalReponse> test(@RequestBody SyncIntegrationCredential dto);
}
