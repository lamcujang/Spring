package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.orderDto.external.ReferenceListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
    import org.springframework.cloud.openfeign.SpringQueryMap;
    import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "referenceListClientService", path = "/system-service/api/v1/referenceList", decode404 = true
, configuration = FeignClientConfig.class )
public interface ReferenceListClientService {

    @GetMapping("/{id}")
    GlobalReponse findById(@PathVariable(name = "id") Integer id);

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findALl
            (
//            @NotBlank(message = "*Param must not blank!**")
//            @Valid @ModelAttribute CustomerQueryRequest requesta
                    @SpringQueryMap ReferenceListQueryRequest
                            request
                    );

    @PostMapping("/save")
    GlobalReponse save(@RequestBody ReferenceListDto  dto);

    @PutMapping("/update")
    GlobalReponse update(@RequestBody ReferenceListDto dto);

    @DeleteMapping("/delete/{id}")
    GlobalReponse delete(@PathVariable(name = "id") Integer id);

    @GetMapping("/findByReferenceName")
//    @Cacheable(value = "vendors", key = "#vendorId")
    GlobalReponse findByReferenceName(@RequestParam(value = "nameReference") String nameReference);

}
