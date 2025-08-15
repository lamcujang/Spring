

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.EnvironmentFee;
import com.dbiz.app.reportservice.repository.EnvironmentFeeRepository;
import com.dbiz.app.reportservice.service.EnvironmentFeeService;

import com.dbiz.app.reportservice.specification.EnvironmentFeeSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;

import org.common.dbiz.dto.reportDto.EnvironmentFeeDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;


import org.common.dbiz.request.reportRequest.EnvironmentFeeQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class EnvironmentFeeServiceImpl implements EnvironmentFeeService {

    private final EnvironmentFeeRepository environmentFeeRepository;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(EnvironmentFeeQueryRequest paramRequest) {

        log.info("*** EnvironmentFeeDto List, service; fetch all environmentFees *");

        paramRequest.setSortBy("itemName");
        paramRequest.setOrder("asc");

        EnvironmentFeeQueryRequest query = (EnvironmentFeeQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<EnvironmentFee> spec = EnvironmentFeeSpecification.getSpecification(query);
        Page<EnvironmentFee> entityList = environmentFeeRepository.findAll(spec, pageable);

        List<EnvironmentFeeDto> listData = new ArrayList<>();
        for (EnvironmentFee item : entityList.getContent()) {
            EnvironmentFeeDto environmentFeeDto = modelMapper.map(item, EnvironmentFeeDto.class);
            listData.add(environmentFeeDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("EnvironmentFee fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer environmentFeeId) {
        log.info("*** EnvironmentFeeDto, service; fetch environmentFee by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.environmentFeeRepository.findById(environmentFeeId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("EnvironmentFee with id: %d not found", environmentFeeId))), EnvironmentFeeDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(EnvironmentFeeDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









