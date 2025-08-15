

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.BusinessSectorGroup;
import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.repository.IndividualIndustryRepository;
import com.dbiz.app.reportservice.service.IndividualIndustryService;
import com.dbiz.app.reportservice.specification.IndividualIndustrySpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.InvoiceViewDto;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.dto.reportDto.*;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.IndividualIndustryQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class IndividualIndustryServiceImpl implements IndividualIndustryService {

    private final IndividualIndustryRepository individualIndustryRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    @Override
    public GlobalReponsePagination findAll(IndividualIndustryQueryRequest query) {

        log.info("*** IndividualIndustryDto List, service; fetch all individualIndustrys *");

        query.setSortBy("taxBusinessIndustryId");
        query.setOrder("asc");

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<IndividualIndustry> spec = IndividualIndustrySpecification.getSpecification(query);
        Page<IndividualIndustry> entityList = individualIndustryRepository.findAll(spec, pageable);

        List<IndividualIndustryDto> listData = new ArrayList<>();
        for (IndividualIndustry item : entityList.getContent()) {
            IndividualIndustryDto individualIndustryDto = modelMapper.map(item, IndividualIndustryDto.class);
            listData.add(individualIndustryDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("IndividualIndustry fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer individualIndustryId) {
        log.info("*** IndividualIndustryDto, service; fetch individualIndustry by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.individualIndustryRepository.findById(individualIndustryId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("IndividualIndustry with id: %d not found", individualIndustryId))), IndividualIndustryDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId) {

        log.info("*** IndividualIndustryDto List, service; fetch all individualIndustrys *");

        List<IndividualIndustry> entityList = individualIndustryRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<IndividualIndustryDto> listData = new ArrayList<>();
        if (!entityList.isEmpty()) {

            for (IndividualIndustry item : entityList) {
                IndividualIndustryDto individualIndustryDto = modelMapper.map(item, IndividualIndustryDto.class);
                setAllNecessaryDtoInfo(individualIndustryDto);
                listData.add(individualIndustryDto);
            }
        }

        GlobalReponse response = new GlobalReponse();
        response.setMessage("IndividualIndustry fetched successfully");
        response.setData(listData);

        return response;
    }

    public void setAllNecessaryDtoInfo(IndividualIndustryDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Tax_Business_Industry_BY_ID + "/" + dto.getTaxBusinessIndustryId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        TaxBusinessIndustryDto result = objectMapper.convertValue(res.getData(), TaxBusinessIndustryDto.class);

        dto.setTaxBusinessIndustryDto(result);
    }

    @Override
    public GlobalReponse save(IndividualIndustryDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









