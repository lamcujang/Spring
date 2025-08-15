package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.LeaveApplication;
import com.dbiz.app.userservice.domain.OverTimeLog;
import com.dbiz.app.userservice.repository.LeaveApplicationRepository;
import com.dbiz.app.userservice.service.LeaveApplicationService;
import com.dbiz.app.userservice.specification.LeaveApplicationSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.common.dbiz.dto.orderDto.dtoView.POHeaderVDto;
import org.common.dbiz.dto.userDto.LeaveApplicationDto;
import org.common.dbiz.dto.userDto.request.LeaveApplicationRequest;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApplicationServiceImpl implements LeaveApplicationService {
    LeaveApplicationRepository leaveApplicationRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;
    RequestParamsUtils requestParamsUtils;


    @Override
    public GlobalReponsePagination findAll(LeaveApplicationRequest request) {

        Pageable page = requestParamsUtils.getPageRequest(request);

        Specification<LeaveApplication> spec = LeaveApplicationSpecification.getSpecification(request);

        Page<LeaveApplication> entityList = leaveApplicationRepository.findAll(spec, page);
        List<LeaveApplicationDto> listData = new ArrayList<>();
        if(entityList.hasContent()) {
            entityList.getContent().forEach(entity -> {
                LeaveApplicationDto dto = modelMapper.map(entity, LeaveApplicationDto.class);
                listData.add(dto);
            });
        }

        return GlobalReponsePagination.builder()
                .pageSize(entityList.getNumber())
                .currentPage(entityList.getNumber())
                .data(listData)
                .totalPages(entityList.getTotalPages())
                .totalItems(entityList.getTotalElements())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(LeaveApplicationDto entity) {
        log.info("save leave application request");
        log.info("dto leave application: {}", entity);

        LeaveApplication leaveApplication = null;
        if(entity.getId() == null){
            leaveApplication = modelMapper.map(entity, LeaveApplication.class);
            leaveApplication.setOrgId(0);
            leaveApplication.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(entity.getFromDate() != null){
                leaveApplication.setFromDate(DateHelper.toLocalDate(entity.getFromDate()));
            }

            if(entity.getToDate() != null){
                leaveApplication.setToDate(DateHelper.toLocalDate(entity.getToDate()));
            }

            leaveApplication = leaveApplicationRepository.save(leaveApplication);
        }else{
            leaveApplication = leaveApplicationRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("leave_application_not_exist", null, LocaleContextHolder.getLocale())));

            modelMapper.map(entity, leaveApplication);
            if(entity.getFromDate() != null){
                leaveApplication.setFromDate(DateHelper.toLocalDate(entity.getFromDate()));
            }

            if(entity.getToDate() != null){
                leaveApplication.setToDate(DateHelper.toLocalDate(entity.getToDate()));
            }

            leaveApplication = leaveApplicationRepository.save(leaveApplication);
        }
        return GlobalReponse.builder()
                .status(HttpStatus.SC_CREATED)
                .data(modelMapper.map(leaveApplication, LeaveApplicationDto.class))
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(integer)
                .orElseThrow(() -> new PosException(messageSource.getMessage("leave_application_not_exist", null, LocaleContextHolder.getLocale())));

        leaveApplicationRepository.delete(leaveApplication);
        return GlobalReponse.builder()
                .status(HttpStatus.SC_OK)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
}
