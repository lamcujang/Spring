package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.define.Reference;
import com.dbiz.app.orderservice.domain.ReservationOrder;
import com.dbiz.app.orderservice.domain.Table;
import com.dbiz.app.orderservice.domain.view.ReservationVAll;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.helper.ReservationOrderMapper;
import com.dbiz.app.orderservice.helper.ReservationViewMapperT;
import com.dbiz.app.orderservice.repository.FloorRepository;
import com.dbiz.app.orderservice.repository.ReservationOrderRepository;
import com.dbiz.app.orderservice.repository.ReservationVAllRepository;
import com.dbiz.app.orderservice.repository.TableRepository;
import com.dbiz.app.orderservice.service.ReservationOrderService;
import com.dbiz.app.orderservice.specification.ReservationOrderSpecification;
import com.dbiz.app.orderservice.specification.ReservationVAllSpecification;
//import com.dbiz.app.helper.ReservationViewMapper;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.dtoView.ReservationVAllDto;
import org.common.dbiz.dto.orderDto.response.UserDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ReservationOrderSerivceImpl implements ReservationOrderService {
    private final ReservationOrderRepository entityRepository;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final ReservationOrderMapper reservationOrderMapper;
    private final EntityManager entityManager;
    private final TableRepository tableRepository;
    private final FloorRepository floorRepository;
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;
    private final ReservationVAllRepository reservationVAllRepository;
    //    private final ReservationViewMapper reservationViewMapper;
    private final ReservationViewMapperT reservationViewMapperT;


    @Override
    public GlobalReponsePagination findAll(ReservationOrderQueryRequest queryReservation) {
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
        log.info("*** ReservationOrder List, service; fetch all ReservationOrder *");
        GlobalReponse serviceDto;
        Pageable pageable = requestParamsUtils.getPageRequest(queryReservation);

        Specification<ReservationOrder> spec = ReservationOrderSpecification.getEntitySpecification(queryReservation);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<ReservationOrder> entityList = entityRepository.findAll(spec, pageable);
        List<ReservationOrderDto> listData = new ArrayList<>();
        for (ReservationOrder item : entityList.getContent()) {

            ReservationOrderDto dto = reservationOrderMapper.reservationOrderDto(item);
            dto.setReservationTime(DateHelper.fromInstantUTC(item.getReservationTime()));
            if (item.getFloorId() != null && item.getFloorId() != 0) {
                dto.setFloor(modelMapper.map(floorRepository.findById(item.getFloorId()).get(), FloorDto.class));
            }
            if (item.getTableId() != null && item.getTableId() != 0) {
                TableDto tableDto = modelMapper.map(tableRepository.findById(item.getTableId()).get(), TableDto.class);
                tableDto.setFloor(null);
                dto.setTable(tableDto);
            }
            if (item.getCustomerId() != null) {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .CUSTOMER_SERVICE_API_URL + "/" + item.getCustomerId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
                dto.setCustomer(modelMapper.map(serviceDto.getData(), CustomerDto.class));
            }
            if (item.getUserId() != null) {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .USER_SERVICE_API_URL + "/" + item.getUserId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
                dto.setUser(modelMapper.map(serviceDto.getData(), UserDto.class));
            }
            serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.REFERENCELIST_SERVICE_API_FIND_REF_VALUE + "?"
                            + "value=" + item.getStatus() + "&domain=" + ReservationOrder.class.getSimpleName() + "&column=status"
                    ,HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
            dto.setReservationStatus(modelMapper.map(serviceDto.getData(), ReservationStatusDto.class));


            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("reservation_order_fetchAll", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(ReservationOrderDto paramDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        log.info("ParamDto: {}", paramDto);
        log.info("*** ReservationOrder, service; save ReservationOrder ***");
        GlobalReponse response = new GlobalReponse();
        GlobalReponse serviceDto = null;
        ReservationOrder entitySave = reservationOrderMapper.reservationOrder(paramDto);
        if (entitySave.getId() != null) // Update

        {
            entitySave = this.entityRepository.findById(paramDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            entitySave = reservationOrderMapper.updateEntity(paramDto, entitySave);
            if (paramDto.getReservationTime() != null) {
                entitySave.setReservationTime(DateHelper.toInstantUTC(paramDto.getReservationTime()));
            }
            this.entityRepository.save(entitySave);

            if (entitySave.getFloorId() != null && entitySave.getFloorId() != 0) {
                paramDto.setFloor(modelMapper.map(floorRepository.findById(entitySave.getFloorId()).get(), FloorDto.class));
            }

            if (entitySave.getTableId() != null && entitySave.getTableId() != 0) {

                if (entitySave.getStatus().equals(Reference.TableReservationStatus.TRC.name())) {

                    String sql1 = "select count(1) from pos.d_pos_order where d_tenant_id = :tenantId " +
                            " and d_table_id = :tableId and order_status = 'DRA' ";

                    Integer rs = ((Number) entityManager.createNativeQuery(sql1)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("tableId", entitySave.getTableId())
                            .getSingleResult()).intValue();

                    if (rs.intValue() > 0) {
                        throw new PosException(messageSource.getMessage("table_in_use", null, LocaleContextHolder.getLocale()));
                    }

                    Table tableUpdate = tableRepository.findById(entitySave.getTableId()).get();
                    tableUpdate.setTableStatus(Reference.TableStatus.TIU.name());
                    tableRepository.saveAndFlush(tableUpdate);
                    paramDto.setTable(modelMapper.map(tableUpdate, TableDto.class));
                }
            }

            if (entitySave.getCustomerId() != null) {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .CUSTOMER_SERVICE_API_URL + "/" + entitySave.getCustomerId(), HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();

                paramDto.setCustomer(modelMapper.map(serviceDto.getData(), CustomerDto.class));
            }
            if (entitySave.getUserId() != null) {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .USER_SERVICE_API_URL + "/" + entitySave.getUserId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
                paramDto.setUser(modelMapper.map(serviceDto.getData(), UserDto.class));

//                List<Map<String, Object>> userDto = entityManager.createNativeQuery("SELECT * FROM d_user WHERE d_user_id = " + entitySave.getUserId())
//                        .unwrap(NativeQuery.class)
//                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                        .getResultList();
//                if(userDto.size() == 0){
//                    throw new ObjectNotFoundException("User not found");
//                }else
//                {
//                    UserDto userDto1 = new UserDto();
//                    userDto1.setUserId(((BigDecimal) userDto.get(0).get("d_user_id")).intValue());
//                    userDto1.setFullName((String) userDto.get(0).get("full_name"));
//                    userDto1.setEmail((String) userDto.get(0).get("email"));
//                    userDto1.setPhone((String) userDto.get(0).get("phone"));
//                    paramDto.setUser(userDto1);
//                }
            }
            serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.REFERENCELIST_SERVICE_API_FIND_NAMEREF_VALUE + "?"
                    + "value=" + entitySave.getStatus() + "&nameReference=Table Reservation Status",HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
            paramDto.setReservationStatus(modelMapper.map(serviceDto.getData(), ReservationStatusDto.class));
            response.setData(paramDto);
            response.setMessage(messageSource.getMessage("reservation_order_update", null, LocaleContextHolder.getLocale()));
        } else { // Create
            if (entitySave.getCustomerId() != null) {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .CUSTOMER_SERVICE_API_URL + "/" + entitySave.getCustomerId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
                paramDto.setCustomer(modelMapper.map(serviceDto.getData(), CustomerDto.class));

                entitySave.setCustomerName(paramDto.getCustomer().getName());
            }
            if (paramDto.getReservationTime() != null) {
                entitySave.setReservationTime(DateHelper.toInstantUTC(paramDto.getReservationTime()));
            }
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave = this.entityRepository.save(entitySave);
            paramDto = reservationOrderMapper.reservationOrderDto(entitySave);
            paramDto.setReservationTime(DateHelper.fromInstantUTC(entitySave.getReservationTime()));
            paramDto.setCustomer(modelMapper.map(serviceDto.getData(), CustomerDto.class));
            if (entitySave.getFloorId() != null && entitySave.getFloorId() != 0) {
                paramDto.setFloor(modelMapper.map(floorRepository.findById(entitySave.getFloorId()).get(), FloorDto.class));
            }
            if (entitySave.getTableId() != null && entitySave.getTableId() != 0) {
                Table tableUpdate = tableRepository.findById(entitySave.getTableId()).get();
                TableDto tableDto = modelMapper.map(tableUpdate, TableDto.class);
                if (paramDto.getStatus().equals(Reference.TableReservationStatus.TBL.name())) {
                    tableDto.setTableStatus(Reference.TableStatus.TBD.name());
                    tableRepository.saveAndFlush(tableUpdate);
                }
//                tableDto.setFloor(null);
                paramDto.setTable(tableDto);

            }
            if (entitySave.getUserId() != null)
            {
                serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                        .USER_SERVICE_API_URL + "/" + entitySave.getUserId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
                paramDto.setUser(modelMapper.map(serviceDto.getData() , UserDto.class));
            }
            serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.REFERENCELIST_SERVICE_API_FIND_NAMEREF_VALUE + "?"
                    + "value=" + entitySave.getStatus() + "&nameReference=Table Reservation Status",HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();

            paramDto.setReservationStatus(modelMapper.map(serviceDto.getData(), ReservationStatusDto.class));
            response.setData(paramDto);
            response.setMessage(messageSource.getMessage("reservation_order_create", null, LocaleContextHolder.getLocale()));
        }
        response.setStatus(HttpStatus.OK.value());
        log.info("ReservationOrder saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete ReservationOrder by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<ReservationOrder> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage(messageSource.getMessage("reservation_order_notFound", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("reservation_order_delete", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
        log.info("*** ReservationOrder, service; fetch ReservationOrder by id *");
        ReservationOrderDto dto;
        ReservationOrder entity = this.entityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

        dto = reservationOrderMapper.reservationOrderDto(entity);
        dto.setReservationTime(DateHelper.fromInstant(entity.getReservationTime()));
        GlobalReponse response = new GlobalReponse();
        GlobalReponse serviceDto;

        if (entity.getTableId() != null && entity.getTableId() != 0) {
            TableDto tableDto = modelMapper.map(tableRepository.findById(entity.getTableId()).get(), TableDto.class);
            tableDto.setFloor(null);
            dto.setTable(tableDto);
        }
        if (entity.getFloorId() != null && entity.getFloorId() != 0)
            dto.setFloor(modelMapper.map(floorRepository.findById(entity.getFloorId()).get(), FloorDto.class));

        if (entity.getCustomerId() != null) {
            serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                    .CUSTOMER_SERVICE_API_URL + "/" + entity.getCustomerId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
            dto.setCustomer(modelMapper.map(serviceDto.getData(), CustomerDto.class));
        }
        if (entity.getUserId() != null) {
            serviceDto = this.restTemplate.exchange(AppConstant.DiscoveredDomainsApi
                    .USER_SERVICE_API_URL + "/" + entity.getUserId(),HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
            dto.setUser(modelMapper.map(serviceDto.getData(), UserDto.class));
        }
        serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.REFERENCELIST_SERVICE_API_FIND_REF_VALUE + "?"
                + "value=" + dto.getStatus() + "&domain=" + ReservationOrder.class.getSimpleName() + "&column=status",HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
        dto.setReservationStatus(modelMapper.map(serviceDto.getData(), ReservationStatusDto.class));
        response.setData(dto);
        response.setMessage(messageSource.getMessage("reservation_order_fetch", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponsePagination findAllV(ReservationOrderQueryRequest queryRequest) {

        Page<ReservationVAll> entityList = this.reservationVAllRepository.findAll(ReservationVAllSpecification.getEntitySpecification(queryRequest), requestParamsUtils.getPageRequest(queryRequest));
       // List<ReservationVAllDto> entityDtoList = reservationViewMapperT.convertToDtoList(entityList.getContent());
        List<ReservationVAllDto> entityDtoList =  new ArrayList<>();

        for (ReservationVAll item : entityList.getContent()) {

            ReservationVAllDto dto = reservationViewMapperT.convertToDto(item);
            dto.setReservationTime(DateHelper.fromInstantUTC(item.getReservationTime()));
            entityDtoList.add(dto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setData(entityDtoList);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());
        response.setMessage(messageSource.getMessage("reservation_order_fetchAll", null, LocaleContextHolder.getLocale()));
        return response;
    }
}
