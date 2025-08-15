package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.EmployeeBonusAllowances;
import com.dbiz.app.userservice.domain.OverTimeLog;
import com.dbiz.app.userservice.repository.EmployeeBonusAllowancesRepository;
import com.dbiz.app.userservice.service.EmployeeBonusAllowancesService;
import com.dbiz.app.userservice.specification.EmployeeBonusAllowancesSpecification;
import com.dbiz.app.userservice.specification.OverTimeLogSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.common.dbiz.dto.userDto.EmployeeBonusAllowancesDto;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.request.EmployeeBonusAllowancesRequest;
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
public class EmployeeBonusAllowancesServiceImpl implements EmployeeBonusAllowancesService {
    EmployeeBonusAllowancesRepository employeeBonusAllowancesRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;
    RequestParamsUtils requestParamsUtils;

    @Override
    public GlobalReponsePagination findAll(EmployeeBonusAllowancesRequest request) {
        Pageable page = requestParamsUtils.getPageRequest(request);

        Specification<EmployeeBonusAllowances> spec = EmployeeBonusAllowancesSpecification.getSpecification(request);

        Page<EmployeeBonusAllowances> entityList = employeeBonusAllowancesRepository.findAll(spec, page);
        List<EmployeeBonusAllowancesDto> listData = new ArrayList<>();

        if(entityList.hasContent()) {
            entityList.getContent().forEach(entity -> {
                EmployeeBonusAllowancesDto dto = modelMapper.map(entity, EmployeeBonusAllowancesDto.class);
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
    public GlobalReponse save(EmployeeBonusAllowancesDto entity) {
        log.info("save EmployeeBonusAllowancesDto");

        EmployeeBonusAllowances employeeBonusAllowances = null;
        if(entity.getId()==null){
            employeeBonusAllowances = modelMapper.map(entity, EmployeeBonusAllowances.class);
            employeeBonusAllowances.setOrgId(0);
            employeeBonusAllowances.setTenantId(AuditContext.getAuditInfo().getTenantId());
            employeeBonusAllowances = employeeBonusAllowancesRepository.save(employeeBonusAllowances);
        }else{
            employeeBonusAllowances = employeeBonusAllowancesRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale())));
            modelMapper.map(entity, employeeBonusAllowances);
            employeeBonusAllowances = employeeBonusAllowancesRepository.save(employeeBonusAllowances);
        }

        return GlobalReponse.builder()
                .status(HttpStatus.SC_CREATED)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(modelMapper.map(employeeBonusAllowances, EmployeeBonusAllowancesDto.class))
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        EmployeeBonusAllowances employeeBonusAllowances = employeeBonusAllowancesRepository.findById(integer)
                .orElseThrow(() -> new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale())));

        if(employeeBonusAllowances != null){
            employeeBonusAllowances.setIsActive("Y");
            employeeBonusAllowancesRepository.save(employeeBonusAllowances);
        }

        return GlobalReponse.builder()
                .status(HttpStatus.SC_OK)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
}
