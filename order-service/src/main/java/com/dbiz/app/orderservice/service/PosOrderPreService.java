package com.dbiz.app.orderservice.service;

import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


public interface PosOrderPreService {

    public GlobalReponse createBulkOrder(BulkPosOrderDto dto);
}
