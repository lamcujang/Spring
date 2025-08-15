

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.constant.AppConstant;
import com.dbiz.app.reportservice.domain.HouseholdIndustry;
import com.dbiz.app.reportservice.domain.IndividualIndustry;
import com.dbiz.app.reportservice.domain.TaxDeclarationIndividual;
import com.dbiz.app.reportservice.domain.TaxHouseholdProfile;
import com.dbiz.app.reportservice.repository.HouseholdIndustryRepository;
import com.dbiz.app.reportservice.repository.TaxHouseholdProfileRepository;
import com.dbiz.app.reportservice.service.TaxHouseholdProfileService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.*;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
public class TaxHouseholdProfileServiceImpl implements TaxHouseholdProfileService {

    private final TaxHouseholdProfileRepository taxHouseholdProfileRepository;
    private final HouseholdIndustryRepository householdIndustryRepository;

    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(TaxHouseholdProfileDto request) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer taxHouseholdProfileId) {
        log.info("*** TaxHouseholdProfileDto, service; fetch TaxHouseholdProfile by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxHouseholdProfileRepository.findById(taxHouseholdProfileId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxHouseholdProfile with id: %d not found", taxHouseholdProfileId))), TaxHouseholdProfileDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(TaxHouseholdProfileDto entity) {

        GlobalReponse response = new GlobalReponse();
        TaxHouseholdProfile result = null;

        if (entity.getId() != null) {

            GlobalReponse exRes = null;
            result = this.taxHouseholdProfileRepository.findById(entity.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(entity, result);
        } else {

            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

        result = this.taxHouseholdProfileRepository.saveAndFlush(result);
        List<HouseholdIndustryDto> householdIndustryDto = null;

        if (entity.getHouseholdIndustryDto() != null && !entity.getHouseholdIndustryDto().isEmpty()) {

            householdIndustryRepository.updateInactiveAllOldHouseholdIndustry();
            householdIndustryDto = processHouseholdIndustry(entity, result.getId());
        }

        entity = modelMapper.map(result, TaxHouseholdProfileDto.class);
        entity.setHouseholdIndustryDto(householdIndustryDto);

        response.setData(entity);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    public void setAllNecessaryBusinessIndustryInfo(HouseholdIndustryDto dto) {

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

    public List<HouseholdIndustryDto> processHouseholdIndustry(TaxHouseholdProfileDto paramDto, Integer id) {

        List<HouseholdIndustryDto> result = new ArrayList<>();
        for (HouseholdIndustryDto element : paramDto.getHouseholdIndustryDto()) {

            HouseholdIndustry householdIndustry = saveHouseholdIndustry(element, id);
            element.setId(householdIndustry.getId());
            HouseholdIndustryDto mappedDto = modelMapper.map(householdIndustry, HouseholdIndustryDto.class);
            setAllNecessaryBusinessIndustryInfo(mappedDto);
            result.add(mappedDto);
        }

        return result;
    }

    private HouseholdIndustry saveHouseholdIndustry(HouseholdIndustryDto element, Integer id) {

        HouseholdIndustry entity = modelMapper.map(element, HouseholdIndustry.class);
        entity.setTaxHouseholdProfileId(id);
        return householdIndustryRepository.saveAndFlush(entity);
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









