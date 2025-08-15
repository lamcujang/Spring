package com.dbiz.app.orderservice.service.impl;


import com.dbiz.app.orderservice.domain.Floor;
import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.repository.FloorRepository;
import com.dbiz.app.orderservice.repository.PosOrderRepository;
import com.dbiz.app.orderservice.repository.RequestOrderRepository;
import com.dbiz.app.orderservice.repository.TableRepository;
import com.dbiz.app.orderservice.service.FloorService;
import com.dbiz.app.orderservice.specification.FloorSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.domain.PosTerminal;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.FloorKafkaDto;
import org.common.dbiz.dto.integrationDto.ProductCategoryKafkaDto;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.FloorQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
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
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class FloorServiceImpl implements FloorService {
    private final RequestParamsUtils requestParamsUtils;

    private final FloorRepository floorRepository;

    private final ModelMapper modelMapper;

    private final TableRepository tableRepository;

    private final EntityManager entityManager;

    private final MessageSource messageSource;

    private final PosOrderRepository posOrderRepository;
    private final DataSourceConfigService dataSourceConfigService;


    @Override
    public GlobalReponsePagination findAll(FloorQueryRequest paramRequest) {
        log.info("*** Floor List, service; fetch all Floor *");

        paramRequest.setOrder("asc");
        paramRequest.setSortBy("displayIndex");
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<Floor> spec = FloorSpecification.getFloorSpecification(paramRequest);
        if (paramRequest.getPosTerminalId() != null) {

            List<Tuple> rsCheckOrg = entityManager.createNativeQuery("select is_pos_mng from d_org where d_org_id = :orgId and d_tenant_id = :tenantId", Tuple.class)
                    .setParameter("orgId", paramRequest.getOrgId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            if(!rsCheckOrg.isEmpty())
            {
                if("Y".equals(ParseHelper.STRING.parse(rsCheckOrg.get(0).get("is_pos_mng") != null ? rsCheckOrg.get(0).get("is_pos_mng") : "N")))
                {
                    spec = spec.and(FloorSpecification.equalPosTerminalId(paramRequest.getPosTerminalId()));
                }
            }

//            StringBuilder slqCheck = new StringBuilder("select d_pos_terminal_id,is_default from d_pos_terminal where d_pos_terminal_id = :posterminalId   and  d_tenant_id = :tenantId");
//            List<Map<String, Object>> resultList = entityManager.createNativeQuery(slqCheck.toString())
//                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                    .setParameter("posterminalId", paramRequest.getPosTerminalId())
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                    .getResultList();
//
//            for (Map<String, Object> row : resultList) {
//                if (ParseHelper.STRING.parse(row.get("is_default")).equals("N"))
//                    spec = spec.and(FloorSpecification.equalPosTerminalId(paramRequest.getPosTerminalId()));
//            }
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        log.info("tenantId: " + AuditContext.getAuditInfo().getTenantId());
        Page<Floor> floors = floorRepository.findAll(spec, pageable);
        List<FloorDto> listData = new ArrayList<>();
        for (Floor item : floors.getContent()) {
            Integer countTable = tableRepository.countByFloorId(item.getId());
            FloorDto dto = modelMapper.map(item, FloorDto.class);
            dto.setQtyTables(countTable);
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(floors.getNumber());
        response.setPageSize(floors.getSize());
        response.setTotalPages(floors.getTotalPages());
        response.setTotalItems(floors.getTotalElements());
        return response;
    }
    @Transactional
    @Override
    public GlobalReponse save(FloorDto Dto) {
        log.info("*** Floor, service; save Floor ***");
        GlobalReponse response = new GlobalReponse();
        Floor floorSave = modelMapper.map(Dto, Floor.class);

        if (floorSave.getId() != null) // update
        {
            floorSave = this.floorRepository.findById(Dto.getId()).orElseThrow(() -> new ObjectNotFoundException("Floor not found"));

            modelMapper.map(Dto, floorSave);
            if (Dto.getIsActive() != null && "N".equals(Dto.getIsActive()) &&
                    posOrderRepository.existsByFloorIdAndOrderStatus(Dto.getId(), "DRA")) {
                throw new PosException(messageSource.getMessage("floor.in.use", null, LocaleContextHolder.getLocale()));
            }

            this.floorRepository.save(floorSave);
            response.setMessage(messageSource.getMessage("floor.update.success", null, LocaleContextHolder.getLocale()));
        } else {
            //validate floor name and floor no
            Floor floorCheck = this.floorRepository.findByNameAndOrgId(Dto.getName(), Dto.getOrgId());
            if (floorCheck != null) {
                throw new RuntimeException(messageSource.getMessage("floor_name_exits", null, LocaleContextHolder.getLocale()));
            } else {
                if (Dto.getFloorNo() != null)
                    floorCheck = this.floorRepository.findByFloorNoAndOrgId(Dto.getFloorNo(), Dto.getOrgId());
                if (floorCheck != null) {
                    throw new RuntimeException(messageSource.getMessage("floor_no_exits", null, LocaleContextHolder.getLocale()));
                }
            }


            if (Dto.getFloorNo() == null) {
                int maxId = this.floorRepository.getMaxId() + 1;
                String docNo = "FL" + maxId;
                floorSave.setFloorNo(docNo);
            }
            if (Dto.getPosTerminalId() == null)
                floorSave.setPosTerminalId(0);
            floorSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            this.floorRepository.save(floorSave);
            response.setMessage(messageSource.getMessage("floor.create.success", null, LocaleContextHolder.getLocale()));

        }

        response.setData(modelMapper.map(floorSave, FloorDto.class));
        response.setMessage("Floor saved successfully");
        response.setStatus(HttpStatus.OK.value());
        log.info("Floor saved successfully with ID: {}", floorSave.getId());
        return response;

    }
    @Transactional
    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete Floor by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<Floor> entityDelete = this.floorRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage("floor not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        if(this.posOrderRepository.existsByFloorId(id))
            throw new PosException( messageSource.getMessage("floor.used.posorder",null,LocaleContextHolder.getLocale()));
        if(this.tableRepository.existsByFloorId(id))
            throw new PosException( messageSource.getMessage("floor.used.table",null,LocaleContextHolder.getLocale()));

        this.floorRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }
    @Transactional
    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** Floor, service; fetch Floor by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.floorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id))), FloorDto.class));
        response.setMessage("Product fetched successfully");
        return response;
    }
    @Transactional
    @Override
    public GlobalReponse intSaveAll(List<FloorDto> floorDtos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        log.info("*** Floor, service;int  save all Floor ***");
        floorDtos.forEach(item -> {

            StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
            List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                    .setParameter("orgId", item.getOrgId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            if (resultList.size() == 0)
                throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), item.getOrgId()));
            Integer orgId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

            if (item.getPosTerminalId() != null && item.getPosTerminalId() != 0) {
                sqlGetEntity = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where erp_pos_id = :posTerminalId and d_tenant_id = :tenantId");
                resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                        .setParameter("posTerminalId", item.getPosTerminalId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                if (resultList.size() == 0)
                    throw new PosException(String.format(messageSource.getMessage("posterminal.not.sync", null, LocaleContextHolder.getLocale()), item.getPosTerminalId()));
                Integer posTerminalId =  ParseHelper.INT.parse(resultList.get(0).get("d_pos_terminal_id"));


                Floor floorErp = floorRepository.findByErpFloorId(item.getErpFloorId());
                if (floorErp == null) {
                    floorErp = modelMapper.map(item, Floor.class);
                    floorErp.setOrgId(orgId);
                    floorErp.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    floorErp.setPosTerminalId(posTerminalId);
                    this.floorRepository.save(floorErp);
                } else {
                    modelMapper.map(item, floorErp);
                    floorErp.setOrgId(orgId);
                    floorErp.setPosTerminalId(posTerminalId);
                    this.floorRepository.save(floorErp);
                }
            }

        });

        return GlobalReponse.builder()
                .message(messageSource.getMessage("floor.synched",null,LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .data(floorDtos)
                .build();
    }

    public int getTenantNumbers()
    {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @Transactional
    public FloorKafkaDto intSaveKafka(List<FloorDto> floorDtos)
    {
        FloorKafkaDto result = new FloorKafkaDto();
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        result.setStatus("COM");
        result.setError("");
        log.info("tenantId: "+AuditContext.getAuditInfo().getTenantId());

        try {
            log.info("*** Floor, service;int  save all Floor ***");
            floorDtos.forEach(item -> {
                StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                        .setParameter("orgId", item.getOrgId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                if (resultList.size() == 0)
                    throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), item.getOrgId()));
                Integer orgId = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

                if (item.getPosTerminalId() != null && item.getPosTerminalId() != 0) {
                    sqlGetEntity = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where erp_pos_id = :posTerminalId and d_tenant_id = :tenantId");
                    resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                            .setParameter("posTerminalId", item.getPosTerminalId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    if (resultList.size() == 0)
                        throw new PosException(String.format(messageSource.getMessage("posterminal.not.sync", null, LocaleContextHolder.getLocale()), item.getPosTerminalId()));
                    Integer posTerminalId =  ParseHelper.INT.parse(resultList.get(0).get("d_pos_terminal_id"));

                    Floor floorErp = floorRepository.findByErpFloorId(item.getErpFloorId());
                    if (floorErp == null) {
                        floorErp = modelMapper.map(item, Floor.class);
                        floorErp.setOrgId(orgId);
                        floorErp.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        floorErp.setPosTerminalId(posTerminalId);
                        this.floorRepository.save(floorErp);
                    } else {
                        modelMapper.map(item, floorErp);
                        floorErp.setOrgId(orgId);
                        floorErp.setPosTerminalId(posTerminalId);
                        this.floorRepository.save(floorErp);
                    }
                }

            });
        } catch (Exception e) {
            result.setStatus("FAI");
            result.setError(e.getMessage());
            result.setLastPage("Y");
        }
        log.info("finish save floor kafka");
        return result;
    }
}
