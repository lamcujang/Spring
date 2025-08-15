

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.TaxBusinessIndustry;
import com.dbiz.app.reportservice.repository.TaxBusinessIndustryRepository;
import com.dbiz.app.reportservice.service.TaxBusinessIndustryService;
import com.dbiz.app.reportservice.specification.TaxBusinessIndustrySpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.reportDto.TaxBusinessIndustryDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxBusinessIndustryQueryRequest;
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
public class TaxBusinessIndustryServiceImpl implements TaxBusinessIndustryService {

    private final TaxBusinessIndustryRepository taxBusinessIndustryRepository;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(TaxBusinessIndustryQueryRequest paramRequest) {

        log.info("*** TaxBusinessIndustryDto List, service; fetch all taxBusinessIndustrys *");

        paramRequest.setSortBy("industryName");
        paramRequest.setOrder("asc");

        TaxBusinessIndustryQueryRequest query = (TaxBusinessIndustryQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxBusinessIndustry> spec = TaxBusinessIndustrySpecification.getSpecification(query);
        Page<TaxBusinessIndustry> entityList = taxBusinessIndustryRepository.findAll(spec, pageable);

        List<TaxBusinessIndustryDto> listData = new ArrayList<>();
        for (TaxBusinessIndustry item : entityList.getContent()) {
            TaxBusinessIndustryDto taxBusinessIndustryDto = modelMapper.map(item, TaxBusinessIndustryDto.class);
            listData.add(taxBusinessIndustryDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxBusinessIndustry fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxBusinessIndustryId) {
        log.info("*** TaxBusinessIndustryDto, service; fetch taxBusinessIndustry by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxBusinessIndustryRepository.findById(taxBusinessIndustryId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxBusinessIndustry with id: %d not found", taxBusinessIndustryId))), TaxBusinessIndustryDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(TaxBusinessIndustryDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









