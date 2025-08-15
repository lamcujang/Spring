package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.service.OrderService;
import com.dbiz.app.orderservice.service.PosOrderPreService;
import com.dbiz.app.orderservice.service.PosOrderService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.orderDto.BulkPosOrderDto;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PosOrderPreServiceImpl implements PosOrderPreService {

    private final String GROUP_ID = "order-service";
    private final String SEND_BULK_ORDER_TOPIC = "SEND_BULK_ORDER_TOPIC";
    private final DataSourceContextHolder dataSourceContextHolder;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final PosOrderService posOrderService;
    private final OrderService orderService;


    @Override
    public GlobalReponse createBulkOrder(BulkPosOrderDto dto) {

        log.info("BulkPosOrderDto");

        try {
            dto.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
            kafkaTemplate.send(SEND_BULK_ORDER_TOPIC, dto);
        }catch (Exception e){
            log.error("Error sending message to Kafka: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @KafkaListener(groupId = GROUP_ID, topics = SEND_BULK_ORDER_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void createBulkOrderKafka(ConsumerRecord<String, BulkPosOrderDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        BulkPosOrderDto dto = consumerRecord.value();
        try {
            log.info("Topic: " + SEND_BULK_ORDER_TOPIC);
            log.info("Received message with key: " + key);
            acknowledgment.acknowledge();
            dataSourceContextHolder.setCurrentTenantId(new Long(dto.getTenantId().toString()));
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "en", dto.getTenantId()));
            List<PosOrderDto> posOrderDtos = dto.getData();
            OrderDto orderDto = null;
            PosOrderDto resultOrder = null;
            if(posOrderDtos != null && !posOrderDtos.isEmpty()){
                for(PosOrderDto posOrderDto : posOrderDtos){
                    GlobalReponse dtoAfterSaved = posOrderService.save(posOrderDto);
                    resultOrder = modelMapper.map(dtoAfterSaved.getData(), PosOrderDto.class);
                    log.info("PosOrderDto ID: " + posOrderDto.getId());
                    orderDto = orderDto.builder().posOrderId(resultOrder.getId())
                            .payments(posOrderDto.getPayments())
                            .build();
                    GlobalReponse orderDtoAfterSaved = orderService.save(orderDto);
                }
            }

        } catch (Exception e) {
            log.error("Caught error in updateImportFile(): ", e);
        }

    }
}
