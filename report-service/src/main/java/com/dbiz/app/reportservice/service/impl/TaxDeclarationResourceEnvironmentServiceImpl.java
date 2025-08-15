

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import com.dbiz.app.reportservice.domain.TaxDeclarationResourceEnvironment;
import com.dbiz.app.reportservice.repository.TaxDeclarationResourceEnvironmentRepository;
import com.dbiz.app.reportservice.service.TaxDeclarationResourceEnvironmentService;
import com.dbiz.app.reportservice.specification.TaxDeclarationResourceEnvironmentSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.reportDto.EnvironmentFeeDto;
import org.common.dbiz.dto.reportDto.InventoryCategorySpecialTaxDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationExciseDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationResourceEnvironmentDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationResourceEnvironmentQueryRequest;
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


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TaxDeclarationResourceEnvironmentServiceImpl implements TaxDeclarationResourceEnvironmentService {

    private final TaxDeclarationResourceEnvironmentRepository taxDeclarationResourceEnvironmentRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    @Override
    public GlobalReponsePagination findAll(TaxDeclarationResourceEnvironmentQueryRequest paramRequest) {

        log.info("*** TaxDeclarationResourceEnvironmentDto List, service; fetch all taxDeclarationResourceEnvironments *");

        paramRequest.setSortBy("id");
        paramRequest.setOrder("asc");

        TaxDeclarationResourceEnvironmentQueryRequest query = (TaxDeclarationResourceEnvironmentQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxDeclarationResourceEnvironment> spec = TaxDeclarationResourceEnvironmentSpecification.getSpecification(query);
        Page<TaxDeclarationResourceEnvironment> entityList = taxDeclarationResourceEnvironmentRepository.findAll(spec, pageable);

        List<TaxDeclarationResourceEnvironmentDto> listData = new ArrayList<>();
        for (TaxDeclarationResourceEnvironment item : entityList.getContent()) {
            TaxDeclarationResourceEnvironmentDto taxDeclarationResourceEnvironmentDto = modelMapper.map(item, TaxDeclarationResourceEnvironmentDto.class);
            listData.add(taxDeclarationResourceEnvironmentDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxDeclarationResourceEnvironment fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxDeclarationResourceEnvironmentId) {
        log.info("*** TaxDeclarationResourceEnvironmentDto, service; fetch taxDeclarationResourceEnvironment by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxDeclarationResourceEnvironmentRepository.findById(taxDeclarationResourceEnvironmentId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxDeclarationResourceEnvironment with id: %d not found", taxDeclarationResourceEnvironmentId))), TaxDeclarationResourceEnvironmentDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId) {

        log.info("*** TaxDeclarationResourceEnvironmentDto List, service; fetch all TaxDeclarationResourceEnvironment *");

        List<TaxDeclarationResourceEnvironment> entityList = taxDeclarationResourceEnvironmentRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationResourceEnvironmentDto> listData = new ArrayList<>();
        if (!entityList.isEmpty()) {

            for (TaxDeclarationResourceEnvironment item : entityList) {
                TaxDeclarationResourceEnvironmentDto dto = modelMapper.map(item, TaxDeclarationResourceEnvironmentDto.class);
                setAllNecessaryDtoInfo(dto);
                listData.add(dto);
            }
        }

        GlobalReponse response = new GlobalReponse();
        response.setMessage("TaxDeclarationResourceEnvironmentDto fetched successfully");
        response.setData(listData);

        return response;
    }

    public void setAllNecessaryDtoInfo(TaxDeclarationResourceEnvironmentDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Environment_Fee_BY_ID + "/" + dto.getEnvironmentFeeId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        EnvironmentFeeDto result = objectMapper.convertValue(res.getData(), EnvironmentFeeDto.class);

        dto.setEnvironmentFeeDto(result);
    }

    @Override
    public GlobalReponse save(TaxDeclarationResourceEnvironmentDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









