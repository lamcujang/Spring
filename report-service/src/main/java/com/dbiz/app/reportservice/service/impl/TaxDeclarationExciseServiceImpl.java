

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import com.dbiz.app.reportservice.repository.TaxDeclarationExciseRepository;
import com.dbiz.app.reportservice.service.TaxDeclarationExciseService;
import com.dbiz.app.reportservice.specification.TaxDeclarationExciseSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.reportDto.InventoryCategorySpecialTaxDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationExciseDto;
import org.common.dbiz.dto.reportDto.TaxDeclarationVatPitLineDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxDeclarationExciseQueryRequest;
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
public class TaxDeclarationExciseServiceImpl implements TaxDeclarationExciseService {

    private final TaxDeclarationExciseRepository taxDeclarationExciseRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    @Override
    public GlobalReponsePagination findAll(TaxDeclarationExciseQueryRequest paramRequest) {

        log.info("*** TaxDeclarationExciseDto List, service; fetch all taxDeclarationExcises *");

        paramRequest.setSortBy("id");
        paramRequest.setOrder("asc");

        TaxDeclarationExciseQueryRequest query = (TaxDeclarationExciseQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxDeclarationExcise> spec = TaxDeclarationExciseSpecification.getSpecification(query);
        Page<TaxDeclarationExcise> entityList = taxDeclarationExciseRepository.findAll(spec, pageable);

        List<TaxDeclarationExciseDto> listData = new ArrayList<>();
        for (TaxDeclarationExcise item : entityList.getContent()) {
            TaxDeclarationExciseDto taxDeclarationExciseDto = modelMapper.map(item, TaxDeclarationExciseDto.class);
            listData.add(taxDeclarationExciseDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxDeclarationExcise fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxDeclarationExciseId) {
        log.info("*** TaxDeclarationExciseDto, service; fetch taxDeclarationExcise by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxDeclarationExciseRepository.findById(taxDeclarationExciseId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxDeclarationExcise with id: %d not found", taxDeclarationExciseId))), TaxDeclarationExciseDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findAllByTaxDeclarationIndividualId(Integer taxDeclarationIndividualId) {

        log.info("*** TaxDeclarationExciseDto List, service; fetch all TaxDeclarationExcise *");

        List<TaxDeclarationExcise> entityList = taxDeclarationExciseRepository.findAllByTaxDeclarationIndividualId(taxDeclarationIndividualId);
        List<TaxDeclarationExciseDto> listData = new ArrayList<>();
        if (!entityList.isEmpty()) {

            for (TaxDeclarationExcise item : entityList) {
                TaxDeclarationExciseDto dto = modelMapper.map(item, TaxDeclarationExciseDto.class);
                setAllNecessaryDtoInfo(dto);
                listData.add(dto);
            }
        }

        GlobalReponse response = new GlobalReponse();
        response.setMessage("TaxDeclarationExciseDto fetched successfully");
        response.setData(listData);

        return response;
    }

    public void setAllNecessaryDtoInfo(TaxDeclarationExciseDto dto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        ObjectMapper objectMapper = new ObjectMapper();

        GlobalReponse res = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.REPORT_SERVICE_GET_Inventory_Category_Special_Tax_BY_ID + "/" + dto.getInventoryCategorySpecialTaxId(),
                HttpMethod.GET,
                entityHeader,
                GlobalReponse.class
        ).getBody();

        InventoryCategorySpecialTaxDto result = objectMapper.convertValue(res.getData(), InventoryCategorySpecialTaxDto.class);

        dto.setInventoryCategorySpecialTaxDto(result);
    }

    @Override
    public GlobalReponse save(TaxDeclarationExciseDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









