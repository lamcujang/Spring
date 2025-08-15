package com.dbiz.app.proxyclient.business.order.service;

import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.GetKolSameProductVRequest;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORDER-SERVICE", contextId = "wdsClientService", path = "/order-service/api/v1/wds")
public interface WDSService {

    @GetMapping("/fetch/detail")
    public ResponseEntity<GlobalReponsePagination> getWDSList(@SpringQueryMap final KitchenOrderRequest entityQueryRequest);

    @PostMapping("/send")
    public ResponseEntity<GlobalReponse> sendWDS(@RequestBody final KitchenOrderDto dto);

    @GetMapping("/fetch/detail/history")
    public ResponseEntity<GlobalReponse> getWDSDetailHistory(@SpringQueryMap final GetKolSameProductVRequest entityQueryRequest);
}
