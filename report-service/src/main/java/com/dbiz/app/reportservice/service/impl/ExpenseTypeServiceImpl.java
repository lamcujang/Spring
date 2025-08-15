package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.ExpenseType;
import com.dbiz.app.reportservice.repository.ExpenseTypeRepository;
import com.dbiz.app.reportservice.service.ExpenseTypeService;
import com.dbiz.app.reportservice.specification.ExpenseTypeSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.common.dbiz.dto.reportDto.TaxDeclarationExciseDto;
import org.common.dbiz.dto.reportDto.request.ExpenseTypeRequest;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExpenseTypeServiceImpl implements ExpenseTypeService {
    ExpenseTypeRepository expenseTypeRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;
    RequestParamsUtils requestParamsUtils;

    @Override
    public GlobalReponsePagination findAll(ExpenseTypeRequest request) {
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<ExpenseType> spec = ExpenseTypeSpecification.getEntitySpecification(request);

        Page<ExpenseType> pages = expenseTypeRepository.findAll(spec, page);
        // get user org access
        List<ExpenseTypeDto> listData = new ArrayList<>();

        for (ExpenseType item : pages.getContent()) {
            ExpenseTypeDto expenseTypeDto = this.modelMapper.map(item, ExpenseTypeDto.class);
            listData.add(expenseTypeDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(pages.getTotalPages());
        globalReponsePagination.setPageSize(pages.getSize());
        globalReponsePagination.setCurrentPage(pages.getNumber());
        globalReponsePagination.setTotalItems(pages.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse findById(Integer expenseTypeId) {
        log.info("*** expenseType, service; fetch expenseType by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.expenseTypeRepository.findById(expenseTypeId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("expenseType with id: %d not found", expenseTypeId))), ExpenseTypeDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(ExpenseTypeDto entity) {
        ExpenseType expenseType = null;
        if(entity.getId()==null){
            expenseType = modelMapper.map(entity, ExpenseType.class);
            expenseType.setTenantId(AuditContext.getAuditInfo().getTenantId());

            expenseType = expenseTypeRepository.save(expenseType);
        }else{
            expenseType = expenseTypeRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale())));
        }

        expenseType = expenseTypeRepository.save(expenseType);

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(modelMapper.map(expenseType, ExpenseTypeDto.class))
                .status(HttpStatus.SC_CREATED)
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        ExpenseType expenseType = expenseTypeRepository.findById(integer)
                .orElseThrow(() -> new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale())));

        if(expenseType != null){
            expenseType.setIsActive("N");
            expenseTypeRepository.save(expenseType);
        }
        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.SC_OK)
                .build();
    }
}
