package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.Allowance;
import com.dbiz.app.userservice.domain.Bonus;
import com.dbiz.app.userservice.repository.AllowanceRepository;
import com.dbiz.app.userservice.service.AllowanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.AllowanceDto;
import org.common.dbiz.dto.userDto.BonusDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.AllowanceQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AllowanceServiceImpl implements AllowanceService {

    private final AllowanceRepository allowanceRepository;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final RequestParamsUtils requestParamsUtils;
    /**
     * 
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(AllowanceQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<Allowance> specification = Specification.where(null);
        Page<Allowance> listReponse = allowanceRepository.findAll(specification, page);
        List<AllowanceDto> data = listReponse.getContent().stream()
                .map(item -> modelMapper.map(item, AllowanceDto.class))
                .collect(Collectors.toList());
        response.setData(data);
        response.setMessage(messageSource.getMessage("customer_fetch_all", null, LocaleContextHolder.getLocale()));
        response.setTotalPages(listReponse.getTotalPages());
        response.setPageSize(listReponse.getSize());
        response.setCurrentPage(listReponse.getNumber());
        response.setTotalItems(listReponse.getTotalElements());
        return response;
    }

    /**
     * 
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        Allowance allowance = allowanceRepository.findById(integer)
                .orElseThrow(() -> new RuntimeException("Allowance not found with id: " + integer));
        AllowanceDto allowanceDto = modelMapper.map(allowance, AllowanceDto.class);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(200);
        response.setMessage(messageSource.getMessage("allowance_fetch_success", null, LocaleContextHolder.getLocale()));
        response.setData(allowanceDto);
        return response;
        }

    /**
     * 
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(AllowanceDto entity) {
        Allowance allowance = modelMapper.map(entity, Allowance.class);
        Allowance  saveAllowance ;
        GlobalReponse response = new GlobalReponse();
        if(allowance.getId() != null) {
            Allowance existingAllowance = allowanceRepository.findById(allowance.getId())
                    .orElseThrow(() -> new RuntimeException("Allowance not found with id: " + allowance.getId()));
            modelMapper.map(allowance,existingAllowance);
            saveAllowance = allowanceRepository.save(existingAllowance);
            response.setMessage(messageSource.getMessage("allowance_update_success", null, LocaleContextHolder.getLocale()));
        } else {
            allowance.setTenantId(0);
            allowance.setOrgId(0);
            saveAllowance = allowanceRepository.saveAndFlush(allowance);
        }
        AllowanceDto allowanceDto = modelMapper.map(saveAllowance, AllowanceDto.class);
    response.setData(allowanceDto);
        return response;
    }

    /**
     * 
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        Allowance allowance = allowanceRepository.findById(integer)
                .orElseThrow(() -> new RuntimeException("Allowance not found with id: " + integer));
        this.allowanceRepository.delete(allowance);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(200);
        response.setMessage(messageSource.getMessage("allowance_delete_success", null, LocaleContextHolder.getLocale()));
        response.setData(null);
        return response;
    }
}
