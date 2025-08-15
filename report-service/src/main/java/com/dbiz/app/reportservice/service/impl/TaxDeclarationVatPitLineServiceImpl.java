

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import com.dbiz.app.reportservice.repository.TaxDeclarationVatPitLineRepository;
import com.dbiz.app.reportservice.service.TaxDeclarationVatPitLineService;
import com.dbiz.app.reportservice.specification.TaxDeclarationVatPitLineSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.dto.reportDto.IndividualIndustryDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationVatPitLineDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationVatPitLineQueryRequest;
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
public class TaxDeclarationVatPitLineServiceImpl implements TaxDeclarationVatPitLineService {

    private final TaxDeclarationVatPitLineRepository taxDeclarationVatPitLineRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    @Override
    public GlobalReponsePagination findAll(TaxDeclarationVatPitLineQueryRequest paramRequest) {

        log.info("*** TaxDeclarationVatPitLineDto List, service; fetch all taxDeclarationVatPitLines *");

        paramRequest.setSortBy("itemCode");
        paramRequest.setOrder("asc");

        TaxDeclarationVatPitLineQueryRequest query = (TaxDeclarationVatPitLineQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxDeclarationVatPitLine> spec = TaxDeclarationVatPitLineSpecification.getSpecification(query);
        Page<TaxDeclarationVatPitLine> entityList = taxDeclarationVatPitLineRepository.findAll(spec, pageable);

        List<TaxDeclarationVatPitLineDto> listData = new ArrayList<>();
        for (TaxDeclarationVatPitLine item : entityList.getContent()) {
            TaxDeclarationVatPitLineDto taxDeclarationVatPitLineDto = modelMapper.map(item, TaxDeclarationVatPitLineDto.class);
            listData.add(taxDeclarationVatPitLineDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxDeclarationVatPitLine fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxDeclarationVatPitLineId) {
        log.info("*** TaxDeclarationVatPitLineDto, service; fetch taxDeclarationVatPitLine by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxDeclarationVatPitLineRepository.findById(taxDeclarationVatPitLineId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxDeclarationVatPitLine with id: %d not found", taxDeclarationVatPitLineId))), TaxDeclarationVatPitLineDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId) {

        log.info("*** TaxDeclarationVatPitLineDto List, service; fetch all TaxDeclarationVatPitLine *");

        List<TaxDeclarationVatPitLine> entityList = taxDeclarationVatPitLineRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationVatPitLineDto> listData = new ArrayList<>();
        if (!entityList.isEmpty()) {

            for (TaxDeclarationVatPitLine item : entityList) {
                TaxDeclarationVatPitLineDto dto = modelMapper.map(item, TaxDeclarationVatPitLineDto.class);
                setAllNecessaryDtoInfo(dto);
                listData.add(dto);
            }
        }

        GlobalReponse response = new GlobalReponse();
        response.setMessage("TaxDeclarationVatPitLineDto fetched successfully");
        response.setData(listData);

        return response;
    }

    public void setAllNecessaryDtoInfo(TaxDeclarationVatPitLineDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_GET_Business_Sector_Group_BY_ID + "/" + dto.getBusinessSectorGroupId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        BusinessSectorGroupDto result = objectMapper.convertValue(res.getData(), BusinessSectorGroupDto.class);

        dto.setBusinessSectorGroupDto(result);
    }

    @Override
    public GlobalReponse save(TaxDeclarationVatPitLineDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









