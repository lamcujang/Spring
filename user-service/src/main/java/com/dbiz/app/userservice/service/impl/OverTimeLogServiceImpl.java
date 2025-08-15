package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.LeaveApplication;
import com.dbiz.app.userservice.domain.OverTimeLog;
import com.dbiz.app.userservice.helper.DateHelper;
import com.dbiz.app.userservice.repository.OverTimeLogRepository;
import com.dbiz.app.userservice.service.OverTimeLogService;
import com.dbiz.app.userservice.specification.LeaveApplicationSpecification;
import com.dbiz.app.userservice.specification.OverTimeLogSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.common.dbiz.dto.userDto.LeaveApplicationDto;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.request.OverTimeLogRequest;
import org.common.dbiz.exception.PosException;
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
public class OverTimeLogServiceImpl implements OverTimeLogService {
    OverTimeLogRepository overTimeLogRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;
    RequestParamsUtils requestParamsUtils;

    @Override
    public GlobalReponsePagination findAll(OverTimeLogRequest request) {
        Pageable page = requestParamsUtils.getPageRequest(request);

        Specification<OverTimeLog> spec = OverTimeLogSpecification.getSpecification(request);

        Page<OverTimeLog> entityList = overTimeLogRepository.findAll(spec, page);
        List<OverTimeLogDto> listData = new ArrayList<>();

        if(entityList.hasContent()) {
            entityList.getContent().forEach(entity -> {
                OverTimeLogDto dto = modelMapper.map(entity, OverTimeLogDto.class);
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
    public GlobalReponse save(OverTimeLogDto entity) {
        log.info("save overtime ");
        log.info("dto overtime log: {}", entity);

        OverTimeLog overTimeLog = null;
        if(entity.getId() == null){
            overTimeLog = modelMapper.map(entity, OverTimeLog.class);
            overTimeLog.setOrgId(0);
            overTimeLog.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if(entity.getOvertimeDate() != null){
                overTimeLog.setOvertimeDate(DateHelper.toLocalDate(entity.getOvertimeDate()));
            }

            overTimeLog = overTimeLogRepository.save(overTimeLog);
        }else{
            overTimeLog = overTimeLogRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("overtime_log_not_exist", null, LocaleContextHolder.getLocale())));

            if(entity.getOvertimeDate() != null){
                overTimeLog.setOvertimeDate(DateHelper.toLocalDate(entity.getOvertimeDate()));
            }

            modelMapper.map(entity, overTimeLog);
            overTimeLog = overTimeLogRepository.save(overTimeLog);
        }
        return GlobalReponse.builder()
                .status(HttpStatus.SC_CREATED)
                .data(modelMapper.map(overTimeLog, LeaveApplication.class))
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        OverTimeLog overTimeLog = overTimeLogRepository.findById(integer)
                .orElseThrow(() -> new PosException(messageSource.getMessage("overtime_log_not_exist", null, LocaleContextHolder.getLocale())));

        overTimeLogRepository.delete(overTimeLog);
        return GlobalReponse.builder()
                .status(HttpStatus.SC_OK)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
}
