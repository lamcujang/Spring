package com.dbiz.app.orderservice.service.impl;


import com.dbiz.app.orderservice.domain.Floor;
import com.dbiz.app.orderservice.domain.PosOrder;
import com.dbiz.app.orderservice.domain.ReservationOrder;
import com.dbiz.app.orderservice.domain.Table;
import com.dbiz.app.orderservice.domain.view.TablePosV;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.FloorKafkaDto;
import org.common.dbiz.dto.integrationDto.TableKafkaDto;
import org.common.dbiz.dto.orderDto.TablePosVDto;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.specification.TablePosVSpecification;
import com.dbiz.app.orderservice.constant.AppConstant;

import com.dbiz.app.orderservice.service.TableService;
import com.dbiz.app.orderservice.specification.TableSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.ReservationTablePosVDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TableServiceImpl implements TableService {
    private final RequestParamsUtils requestParamsUtils;

    private final TableRepository entityRepository;

    private final ModelMapper modelMapper;

    private final FloorRepository floorRepository;

    private final MessageSource messageSource;

    private final ReservationOrderRepository reservationOrderRepository;
    private final PosOrderRepository posOrderRepository;

    private final TablePosVRepository tablePosVRepository;
    private final EntityManager entityManager;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;


    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public GlobalReponsePagination findAll(TableQueryRequest entityQueryRequest) {
        log.info("*** Table List, service; fetch all Table *");

        entityQueryRequest.setOrder("asc");
        entityQueryRequest.setSortBy("displayIndex");
        Pageable pageable = requestParamsUtils.getPageRequest(entityQueryRequest);

        Specification<Table> spec = TableSpecification.getTableSpecification(entityQueryRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Table> tables = entityRepository.findAll(spec, pageable);
        List<TableDto> listData = new ArrayList<>();
        for (Table item : tables.getContent()) {
            TableDto dtoItem = modelMapper.map(item, TableDto.class);
            Floor floor = floorRepository.findById(item.getFloorId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));
            FloorDto floorDto = new FloorDto();
            floorDto.setId(floor.getId());
            floorDto.setName(floor.getName());
            floorDto.setIsActive(floor.getIsActive());
            dtoItem.setFloor(floorDto);
            if(item.getTableStatus().equals(AppConstant.TableStatus.TABLE_STATUS_TABLE_IN_USE))
            {
                String sql = "select dc.name from d_pos_order o join d_customer dc on o.d_customer_id = dc.d_customer_id where o.d_table_id = ? and o.order_status ='DRA' limit 1";
                List<Map<String,Object>> resultList = entityManager.createNativeQuery(sql)
                        .setParameter(1, item.getId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                if(resultList.size()>0)
                {
                    dtoItem.setCustomerName(ParseHelper.STRING.parse(resultList.get(0).get("name")));
                }
            }
            listData.add(dtoItem);
        }
        response.setMessage(messageSource.getMessage("table.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(tables.getNumber());
        response.setPageSize(tables.getSize());
        response.setTotalPages(tables.getTotalPages());
        response.setTotalItems(tables.getTotalElements());
        return response;
    }
    @Transactional
    @Override
    public GlobalReponse save(TableDto Dto) {
        log.info("table {}", Dto);
        log.info("*** Table, service; save Table ***");
        GlobalReponse response = new GlobalReponse();
        Table tableSave = null;

        if (Dto.getId() != null) // update
        {
            tableSave = this.entityRepository.findById(Dto.getId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));
            if (Dto.getFloor() != null) {
                tableSave.setFloorId(Dto.getFloor().getId());
            }
            modelMapper.map(Dto, tableSave);
            if(Dto.getIsActive()!= null && Dto.getIsActive().equals("Y") && posOrderRepository.existsByTableIdAndOrderStatus(Dto.getId(), "DRA"))
            {
                throw new PosException(messageSource.getMessage("table_in_use", null, LocaleContextHolder.getLocale()));
            }
            this.entityRepository.save(tableSave);
            response.setMessage(messageSource.getMessage("table_update", null, LocaleContextHolder.getLocale()));
        } else {
            Table tableCheck = this.entityRepository.findByNameAndOrgIdAndFloorId(Dto.getName(), Dto.getOrgId(),Dto.getFloor().getId());
            if (tableCheck != null) {
                throw new PosException(messageSource.getMessage("table_name_exits", null, LocaleContextHolder.getLocale()));
            } else {
                if (Dto.getTableNo() != null && !( Dto.getTableNo().isEmpty()))
                    tableCheck = this.entityRepository.findByTableNoAndOrgId(Dto.getTableNo(), Dto.getOrgId());
                if (tableCheck != null) {
                    throw new PosException(messageSource.getMessage("table_no_exits", null, LocaleContextHolder.getLocale()));
                }
            }

            tableSave = modelMapper.map(Dto, Table.class);
            if(Dto.getIsDefault() == null){
                tableSave.setIsDefault("N");
            }
            if (Dto.getFloor() != null) {
                tableSave.setFloorId(Dto.getFloor().getId());
            }

            if (tableSave.getTableNo() == null || tableSave.getTableNo().isEmpty())
                tableSave.setTableNo("TBL" + (this.entityRepository.getMaxId() + 1));
            tableSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            tableSave = this.entityRepository.save(tableSave);
            response.setMessage(messageSource.getMessage("table_create", null, LocaleContextHolder.getLocale()));
        }


        response.setData(modelMapper.map(tableSave, TableDto.class));

        response.setStatus(HttpStatus.OK.value());
        log.info("Table saved successfully with ID: {}", tableSave.getId());
        return response;

    }
    @Transactional
    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete Table by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<Table> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            throw new PosException(messageSource.getMessage("table_notFound", null, LocaleContextHolder.getLocale()));
        }
        if("Y".equals(entityDelete.get().getIsDefault())){ // If table is default

            throw new PosException(messageSource.getMessage("table_is_default", null, LocaleContextHolder.getLocale()));
        }

        if("Y".equals(entityDelete.get().getIsDefault())){ // If table is default
            response.setMessage(messageSource.getMessage("table_is_default", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("table_delete", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** Table, service; fetch table by id *");

        GlobalReponse response = new GlobalReponse();
        Table table = this.entityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Table with id: %d not found", id)));
        Floor floor = floorRepository.findById(table.getFloorId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));
        FloorDto floorDto = FloorDto.builder().id(floor.getId())
                .name(floor.getName())
                .isActive(floor.getIsActive())
                .build();
        TableDto tableDto = modelMapper.map(table, TableDto.class);
        tableDto.setFloor(floorDto);
        response.setData(tableDto);
        response.setMessage("Table fetched successfully");
        return response;
    }

    @Override
    public GlobalReponsePagination findAllTableAndReservationByDate(TableQueryRequest entityQueryRequest) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        entityQueryRequest.setOrder("asc");
//        entityQueryRequest.setSortBy("displayIndex");
//        entityQueryRequest.setSortBy("name");
        entityQueryRequest.setOrder(null);
        entityQueryRequest.setSortBy(null);
        log.info("*** Table List, service; fetch all Table by reservation date *");
        Pageable pageable =  PageRequest.of(
                entityQueryRequest.getPage(),
                entityQueryRequest.getPageSize(),
                Sort.by(
                        Sort.Order.asc("displayIndex"),
                        Sort.Order.asc("name")
                )
        );

        Specification<TablePosV> spec = TablePosVSpecification.getTableSpecification(entityQueryRequest);

        if(entityQueryRequest.getPosTerminalId()!=null)
        {
            List<Tuple> rsCheckOrg = entityManager.createNativeQuery("select is_pos_mng from d_org where d_org_id = :orgId and d_tenant_id = :tenantId", Tuple.class)
                    .setParameter("orgId", entityQueryRequest.getOrgId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            if(!rsCheckOrg.isEmpty())
            {
                if("Y".equals(ParseHelper.STRING.parse(rsCheckOrg.get(0).get("is_pos_mng") != null ? rsCheckOrg.get(0).get("is_pos_mng") : "N")))
                {
                    spec = spec.and(TablePosVSpecification.hasFloorByPosterminalId(entityQueryRequest.getPosTerminalId()));
                }
            }
//            StringBuilder slqCheck = new StringBuilder("select d_pos_terminal_id,is_default from d_pos_terminal where d_pos_terminal_id = :posterminalId   and  d_tenant_id = :tenantId");
//            List<Map<String,Object>> resultList = entityManager.createNativeQuery(slqCheck.toString())
//                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                    .setParameter("posterminalId", entityQueryRequest.getPosTerminalId())
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                    .getResultList();
//            if(ParseHelper.STRING.parse(resultList.get(0).get("is_default")).equals("N"))
//            {
//                spec = spec.and(TablePosVSpecification.hasFloorByPosterminalId(entityQueryRequest.getPosTerminalId()));
//            }
        }

        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<TablePosV> tables = tablePosVRepository.findAll(spec, pageable);

        List<TablePosVDto> listData = tables.stream().map(item -> {
            TablePosVDto dtoItem = modelMapper.map(item, TablePosVDto.class);
//            Floor floor = floorRepository.findById(item.getFloorId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));
            dtoItem.setFloor(FloorDto.builder().id(item.getFloorId())
                            .name(item.getFloorName())
                    .build());
            dtoItem.setOrderGuest(item.getOrderGuest().toString());
//            ReservationOrder resOrder = reservationOrderRepository.findTableByIdAndDate(
//                    item.getId(), entityQueryRequest.getReservationDate(), item.getTenantId()
//            );
//            ReservationTablePosVDto resOrderDto = new ReservationTablePosVDto();
//            if (resOrder != null) {
//                resOrderDto.setTimeTocome(resOrder.getTimeTocome());
//                resOrderDto.setReservationTime(DateHelper.fromInstant(resOrder.getReservationTime()));
//                resOrderDto.setReservationOrderId(resOrder.getId());
//                dtoItem.setReservation(resOrderDto);
//            } else {
//                dtoItem.setReservation(null);
//            }
//
//            Integer posOrderId = posOrderRepository.findByOrderStatusAndTableIdLimit("DRA", item.getId());
//
//            dtoItem.setPosOrderId(posOrderId);
//
//            if(item.getTableStatus().equals(AppConstant.TableStatus.TABLE_STATUS_TABLE_IN_USE))
//            {
//                String sql = "select dc.name,o.order_guests from d_pos_order o join d_customer dc on o.d_customer_id = dc.d_customer_id where o.d_table_id = ? and o.order_status ='DRA' limit 1";
//                List<Map<String,Object>> resultList = entityManager.createNativeQuery(sql)
//                        .setParameter(1, item.getId())
//                        .unwrap(NativeQuery.class)
//                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                        .getResultList();
//                if(resultList.size()>0)
//                {
//                    dtoItem.setCustomerName(ParseHelper.STRING.parse(resultList.get(0).get("name")));
//                    dtoItem.setOrderGuest(ParseHelper.STRING.parse(resultList.get(0).get("order_guests")));
//                }
//            }
            return dtoItem;
        }).collect(Collectors.toList());


        response.setMessage(messageSource.getMessage("table.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(tables.getNumber());
        response.setPageSize(tables.getSize());
        response.setTotalPages(tables.getTotalPages());
        response.setTotalItems(tables.getTotalElements());
        return response;
    }
    @Transactional
    @Override
    public GlobalReponse intSave(List<TableDto> entityDto) {

        log.info("*** Table, service; integration save *");
        entityDto.forEach(item -> {
            Table tableSave = entityRepository.findByErpTableId(item.getErpTableId());
            Floor floorRef = floorRepository.findByErpFloorId(item.getFloor().getErpFloorId());
            if (floorRef == null)
                throw new PosException(messageSource.getMessage("not_sync_floor_erp", null, LocaleContextHolder.getLocale()));

//            OrgDto orgDto = modelMapper.map(restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_BY_ORGERP + "/" + item.getOrgId(), HttpMethod.GET,entityHeader, GlobalReponse.class).getBody().getData(), OrgDto.class);
//            if (orgDto == null)
//                throw new PosException(messageSource.getMessage("not_sync_org_erp", null, LocaleContextHolder.getLocale()));
            StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
            List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                    .setParameter("orgId", item.getOrgId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            if (resultList.isEmpty())
                throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
            Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

            if (tableSave == null) {
                tableSave = modelMapper.map(item, Table.class);
                tableSave.setFloorId(floorRef.getId());
                tableSave.setTableStatus("ETB");
                tableSave.setOrgId(orgDtoId);
                tableSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entityRepository.save(tableSave);
            } else {
                modelMapper.map(item, tableSave);
                tableSave.setFloorId(floorRef.getId());
                tableSave.setOrgId(orgDtoId);
                tableSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entityRepository.save(tableSave);
            }
        });
        return GlobalReponse.builder()
                .message(messageSource.getMessage("table_synched",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .data(entityDto)
                .build();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public GlobalReponse getByIdFromCashier(Integer id) {
        TablePosV table = tablePosVRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Table with id: %d not found", id)));
        TablePosVDto tableDto = modelMapper.map(table, TablePosVDto.class);
        Floor floor = floorRepository.findById(table.getFloorId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));
        FloorDto floorDto = FloorDto.builder().id(floor.getId())
                .name(floor.getName())
                .isActive(floor.getIsActive())
                .build();
        tableDto.setFloor(floorDto);


        return GlobalReponse.builder()
                .data(tableDto)
                .message(messageSource.getMessage("successfully",null,LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }


    //    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_SEND_TBL, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, TableKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
////        log.info("Received message: " + consumerRecord.value());
//        try {
//            String key = consumerRecord.key(); // could be null
//            TableKafkaDto value = consumerRecord.value();
//
//            try {
//                int tenantNumbers = getTenantNumbers();
//                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//                            .configureDataSources();
//
//                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//                }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (value.getTenantId() != 0) {
//                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//            } else {
//                dataSourceContextHolder.setCurrentTenantId(null);
//
//            }
//            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                    "0", 0, "vi", value.getTenantId()));
//
//
//            log.info("Received message:");
//            log.info("Key: " + key);
////                log.info("Value: " + value);
//            TableKafkaDto result = this.intSaveKafka(value.getTableDtos());
//            result.setLastPage(value.getLastPage());
//            kafkaTemplate.send(TOPIC_RECEIVE_TBL, result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        acknowledgment.acknowledge();
//    }
    @Transactional
    public TableKafkaDto intSaveKafka(List<TableDto> entityDto) {
        TableKafkaDto result = new TableKafkaDto();
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        result.setError("");
        result.setStatus("COM");
        log.info("*** table save kafka *");
       try {
           entityDto.forEach(item -> {
               Table tableSave = entityRepository.findByErpTableId(item.getErpTableId());
               Floor floorRef = floorRepository.findByErpFloorId(item.getFloor().getErpFloorId());
               if (floorRef == null)
                   throw new PosException(messageSource.getMessage("not_sync_floor_erp", null, LocaleContextHolder.getLocale()));

//            OrgDto orgDto = modelMapper.map(restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_BY_ORGERP + "/" + item.getOrgId(), HttpMethod.GET,entityHeader, GlobalReponse.class).getBody().getData(), OrgDto.class);
//            if (orgDto == null)
//                throw new PosException(messageSource.getMessage("not_sync_org_erp", null, LocaleContextHolder.getLocale()));
               StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
               List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                       .setParameter("orgId", item.getOrgId())
                       .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                       .unwrap(NativeQuery.class)
                       .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                       .getResultList();
               if (resultList.isEmpty())
                   throw new PosException(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()));
               Integer orgDtoId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

               if (tableSave == null) {
                   tableSave = modelMapper.map(item, Table.class);
                   tableSave.setFloorId(floorRef.getId());
                   tableSave.setTableStatus("ETB");
                   tableSave.setOrgId(orgDtoId);
                   tableSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                   entityRepository.save(tableSave);
               } else {
                   modelMapper.map(item, tableSave);
                   tableSave.setFloorId(floorRef.getId());
                   tableSave.setOrgId(orgDtoId);
                   tableSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                   entityRepository.save(tableSave);
               }
           });
       }catch (Exception e){
           e.printStackTrace();
           result.setError(e.getMessage());
           result.setStatus("FAI");
           result.setLastPage("Y");
       }
        return result;

    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

}
