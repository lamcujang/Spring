package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.TimeKeeping;
import com.dbiz.app.userservice.repository.TimeKeepingRepository;
import com.dbiz.app.userservice.service.TimeKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.*;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.TimeKeepingQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeKeepingServiceImpl implements TimeKeepingService {

    private final TimeKeepingRepository timeKeepingRepository;

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final MessageSource messageSource;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(TimeKeepingQueryRequest request) {
        log.info("Fetching all TimeKeeping records with request: {}", request);
        GlobalReponsePagination response = new GlobalReponsePagination();
        Parameter parameter = new Parameter();
        if(request.getId() != null)
        {
            parameter.add("d_time_keeping_id",request.getId(), Param.Logical.EQUAL,Param.Relational.AND,Param.NONE);
        }
        if(request.getOrgId()!=null)
            parameter.add("d_org_id",request.getOrgId(), Param.Logical.EQUAL,Param.Relational.AND,Param.NONE);
        if(request.getCheckinAddress() != null && !request.getCheckinAddress().isEmpty()) {
            parameter.add("checkin_address", request.getCheckinAddress(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        }
        if(request.getEmployeeCreated()!= null)
            parameter.add("employee_created_by", request.getEmployeeCreated(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        if(request.getIsActive() != null ) {
            parameter.add("is_active", request.getIsActive(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);


        }

        ResultSet rs = queryEngine.getRecords("pos.d_time_keeping_get_v", parameter, request);
        try {
            List<TimeKeepingDto> data = new ArrayList<>();
            while (rs.next()) {
                TimeKeepingDto timeKeepingDto = TimeKeepingDto.builder()
                        .id(rs.getInt("d_time_keeping_id"))
                        .checkinAddress(rs.getString("checkin_address"))
                        .gpsCoordinates(rs.getString("gps_coordinates"))
                        .checkinRadiusMeters( rs.getInt("checkin_radius_meters"))
                        .isActive(rs.getString("is_active"))
                            .updatedQr(rs.getTimestamp("updated_qr") != null ? DateHelper.fromInstantDateAndTime(rs.getTimestamp("updated_qr").toInstant()): "")
                        .employeeCreatedByDto(EmployeeDto.builder()
                                .id(rs.getInt("employee_created_by"))
                                .name(rs.getString("employee_created_by_name"))
                                .build())
                        .orgDto(OrgDto.builder().id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .address(rs.getString("org_address")).build())
                        .build();

                data.add(timeKeepingDto);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_time_keeping_get_v", parameter, request);
            log.info("Load pagination...");
            return GlobalReponsePagination.builder()
                    .data(data)
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(org.springframework.http.HttpStatus.OK.value())
                    .pageSize(pagination.getPageSize())
                    .currentPage(pagination.getPage())
                    .totalPages(pagination.getTotalPage())
                    .totalItems(pagination.getTotalCount())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(TimeKeepingDto entity) {
        modelMapper.typeMap(TimeKeepingDto.class, TimeKeeping.class)
                .addMappings(mapping -> {
                    mapping.skip(TimeKeeping::setEmployeeCreatedBy);
                    mapping.skip(TimeKeeping::setOrgId);
                });

        modelMapper.typeMap(TimeKeeping.class, TimeKeepingDto.class)
                .addMappings(mapping -> {
                    mapping.skip(TimeKeepingDto::setEmployeeCreatedByDto);
                    mapping.skip(TimeKeepingDto::setOrgDto);
                });
        TimeKeeping entitySave = modelMapper.map(entity, TimeKeeping.class);
        GlobalReponse response = new GlobalReponse();
        if(entitySave.getId() != null) {
            TimeKeeping existingEntity = this.timeKeepingRepository.findById(entitySave.getId())
                    .orElseThrow(() -> new RuntimeException("TimeKeeping not found with id: "));
            modelMapper.map(entitySave, existingEntity);
            if(entity.getOrgDto()!= null )
                existingEntity.setOrgId(entity.getOrgDto().getId());
            if(entity.getEmployeeCreatedByDto()!= null)
                entitySave.setEmployeeCreatedBy(entity.getEmployeeCreatedByDto().getId());
            if(entity.getUpdatedQr()!= null)
                entitySave.setUpdatedQr(DateHelper.toInstant2(entity.getUpdatedQr()));
            entitySave = this.timeKeepingRepository.save(existingEntity);
            response.setMessage("TimeKeeping created successfully");
            response.setData(modelMapper.map(entitySave, TimeKeepingDto.class));

        } else {
            entitySave.setTenantId(0);
            if(entity.getOrgDto()!= null )
                entitySave.setOrgId(entity.getOrgDto().getId());
            if(entity.getEmployeeCreatedByDto()!= null)
                entitySave.setEmployeeCreatedBy(entity.getEmployeeCreatedByDto().getId());
            if(entity.getUpdatedQr()!= null)
                entitySave.setUpdatedQr(DateHelper.toInstant2(entity.getUpdatedQr()));
            this.timeKeepingRepository.save(entitySave);
            response.setMessage("TimeKeeping created successfully");
            response.setData(modelMapper.map(entitySave, TimeKeepingDto.class));

        }
        return response;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
