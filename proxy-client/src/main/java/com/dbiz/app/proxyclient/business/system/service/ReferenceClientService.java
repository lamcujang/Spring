package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "SYSTEM-SERVICE", contextId = "referenceClientService", path = "/system-service/api/v1/reference", decode404 = true
, configuration = FeignClientConfig.class )
public interface ReferenceClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/findAll")
    GlobalReponsePagination findALl
            (
//            @NotBlank(message = "*Param must not blank!**")
//            @Valid @ModelAttribute CustomerQueryRequest requesta
                    @SpringQueryMap ReferenceQueryRequest  request
                    );

    @PostMapping("/save")
    GlobalReponse save(@RequestBody ReferenceDto  dto);

    @PutMapping("/update")
    GlobalReponse update(@RequestBody ReferenceDto dto);

    @DeleteMapping("/delete/{id}")
    GlobalReponse delete(@PathVariable(name = "id") Integer id);

}
