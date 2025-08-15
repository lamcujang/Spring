

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.TaxAgencyInfo;
import com.dbiz.app.reportservice.domain.TaxDeclarationVatPitLine;
import com.dbiz.app.reportservice.repository.TaxAgencyInfoRepository;
import com.dbiz.app.reportservice.service.TaxAgencyInfoService;
import com.dbiz.app.reportservice.specification.TaxAgencyInfoSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.ConfigForTenantRepository;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.TaxAgencyInfoDto;
import org.common.dbiz.dto.reportDto.TaxAgentInfoDto;
import org.common.dbiz.dto.systemDto.ProvincesDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TaxAgencyInfoServiceImpl implements TaxAgencyInfoService {

    private final TaxAgencyInfoRepository taxAgencyInfoRepository;
    private final ConfigForTenantRepository configRepository;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final ObjectMapper objectMapper;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public GlobalReponsePagination findAll(TaxAgencyInfoQueryRequest paramRequest) {

        log.info("*** TaxAgencyInfoDto List, service; fetch all taxAgencyInfos *");

        paramRequest.setSortBy("taxAgentCode");
        paramRequest.setOrder("asc");

        TaxAgencyInfoQueryRequest query = (TaxAgencyInfoQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<TaxAgencyInfo> spec = TaxAgencyInfoSpecification.getSpecification(query);
        Page<TaxAgencyInfo> entityList = taxAgencyInfoRepository.findAll(spec, pageable);

        List<TaxAgencyInfoDto> listData = new ArrayList<>();
        for (TaxAgencyInfo item : entityList.getContent()) {
            TaxAgencyInfoDto taxAgencyInfoDto = modelMapper.map(item, TaxAgencyInfoDto.class);
            listData.add(taxAgencyInfoDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxAgencyInfo fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer taxAgencyInfoId) {
        log.info("*** TaxAgencyInfoDto, service; fetch taxAgencyInfo by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.taxAgencyInfoRepository.findById(taxAgencyInfoId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("TaxAgencyInfo with id: %d not found", taxAgencyInfoId))), TaxAgencyInfoDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse getCurrentTaxAgencySetup() {
        log.info("*** TaxAgencyInfoDto, service; fetch getCurrentTaxAgencySetup *");

        GlobalReponse response = new GlobalReponse();

        TaxAgencyInfo taxAgencyInfo = taxAgencyInfoRepository.findLatestActiveNative().orElse(new TaxAgencyInfo());

        response.setData(modelMapper.map(taxAgencyInfo, TaxAgencyInfoDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse save(TaxAgencyInfoDto entity) {

        GlobalReponse response = new GlobalReponse();

        entity.setId(null);
        TaxAgencyInfo taxAgencyInfo = modelMapper.map(entity, TaxAgencyInfo.class);

        taxAgencyInfoRepository.updateInactiveAllOldTaxAgencyInfo("N");
        taxAgencyInfo.setIsActive("Y");

        entity = modelMapper.map(taxAgencyInfoRepository.saveAndFlush(taxAgencyInfo), TaxAgencyInfoDto.class);

        response.setData(entity);
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse getTaxAgentInfo(String code) {

        dataSourceContextHolder.setCurrentTenantId(null);

        String url = configRepository.findValueByNameAndTenantId("D_URL_GET_TAX_AGENT_INFO", 0);
        StringBuilder urlGetProvinces = new StringBuilder(url);

        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

        List<TaxAgentInfoDto> listResult = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        if (code != null && !code.isEmpty()) {

            urlGetProvinces.append(code);
            ResponseEntity<String> response = externalRestTemplate.exchange(urlGetProvinces.toString(), HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                try {

                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode dataNode = root.get("data");

                    if (dataNode != null && !dataNode.isNull()) {
                        TaxAgentInfoDto itemResult = new TaxAgentInfoDto();
                        itemResult.setId(dataNode.get("id").asText());
                        itemResult.setName(dataNode.get("name").asText());
                        itemResult.setInternationalName(dataNode.get("internationalName").asText());
                        itemResult.setShortName(dataNode.get("shortName").asText());
                        itemResult.setAddress(dataNode.get("address").asText());
                        listResult.add(itemResult);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
                }

            }
        } else {

            listResult = null;
        }
        GlobalReponse result = GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(listResult).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

        return result;
    }
}









