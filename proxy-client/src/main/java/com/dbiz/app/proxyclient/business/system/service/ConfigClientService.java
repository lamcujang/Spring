package com.dbiz.app.proxyclient.business.system.service;

 ;
import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
 import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
 import org.common.dbiz.payload.GlobalReponse;
 import org.common.dbiz.payload.GlobalReponsePagination;
 import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
 import org.common.dbiz.request.systemRequest.SendNotification;
 import org.springframework.cloud.openfeign.FeignClient;
 import org.springframework.cloud.openfeign.SpringQueryMap;
 import org.springframework.web.bind.annotation.*;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "configClientService", path = "/system-service/api/v1/config", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ConfigClientService {

    @GetMapping("/findByName/{name}")
    GlobalReponse findValueByName(@PathVariable(value = "name") String name);


    @GetMapping("/getEMenuConfig")
    GlobalReponsePagination getEMenuConfig(@SpringQueryMap EMenuGetUrlQueryRequest request);

    @PostMapping("/sendNotify")
    GlobalReponse sendNotify(@RequestBody SendNotification request) ;

    @GetMapping("/getParam")
    GlobalReponse getParam(@RequestParam String param);

    @PostMapping("/napas")
    GlobalReponse getNapasConfig(@RequestBody NapasConfigReqDto dto);
}
