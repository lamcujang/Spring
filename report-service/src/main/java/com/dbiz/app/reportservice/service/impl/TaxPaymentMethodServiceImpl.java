

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.TaxPaymentMethod;
import com.dbiz.app.reportservice.repository.TaxPaymentMethodRepository;
import com.dbiz.app.reportservice.service.TaxPaymentMethodService;
import com.dbiz.app.reportservice.specification.TaxPaymentMethodSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.reportDto.TaxPaymentMethodDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxPaymentMethodQueryRequest;
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
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TaxPaymentMethodServiceImpl implements TaxPaymentMethodService {

    private final TaxPaymentMethodRepository taxPaymentMethodRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(TaxPaymentMethodQueryRequest paramRequest) {

        log.info("*** TaxPaymentMethodDto List, service; fetch all taxPaymentMethods *");

        paramRequest.setSortBy("name");
        paramRequest.setOrder("asc");

        TaxPaymentMethodQueryRequest query = (TaxPaymentMethodQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxPaymentMethod> spec = TaxPaymentMethodSpecification.getSpecification(query);
        Page<TaxPaymentMethod> entityList = taxPaymentMethodRepository.findAll(spec, pageable);

        List<TaxPaymentMethodDto> listData = new ArrayList<>();
        for (TaxPaymentMethod item : entityList.getContent()) {
            TaxPaymentMethodDto taxPaymentMethodDto = modelMapper.map(item, TaxPaymentMethodDto.class);
            listData.add(taxPaymentMethodDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxPaymentMethod fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxPaymentMethodId) {
        log.info("*** TaxPaymentMethodDto, service; fetch taxPaymentMethod by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxPaymentMethodRepository.findById(taxPaymentMethodId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxPaymentMethod with id: %d not found", taxPaymentMethodId))), TaxPaymentMethodDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(TaxPaymentMethodDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









