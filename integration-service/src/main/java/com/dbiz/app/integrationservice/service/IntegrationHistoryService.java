package com.dbiz.app.integrationservice.service;

import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IntegrationHistoryService extends BaseServiceGeneric<IntegrationHistoryDto,Integer, IntegrationHistoryQueryRequest> {
        GlobalReponse saveAll(List<IntegrationHistoryDto> dtos);

        void saveIntegrationHistory(IntegrationHistoryDto dto);

        ResponseEntity<GlobalReponse> testInt(String testInt);
}
