package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.constant.AppConstant;
import com.dbiz.app.systemservice.domain.Country;
import com.dbiz.app.systemservice.repository.ConfigRepository;
import com.dbiz.app.systemservice.repository.CountryRepository;
import com.dbiz.app.systemservice.service.ProvincesService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.CountryDto;
import org.common.dbiz.dto.systemDto.ProvincesDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.external.GetTokenCredential;
import org.common.dbiz.request.systemRequest.CountryRequest;
import org.common.dbiz.request.systemRequest.ProvincesRequest;
import org.common.dbiz.payload.GlobalReponsePagination;
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
public class ProvincesServiceImpl implements ProvincesService {

    private final ConfigRepository configRepository;

    private final DataSourceContextHolder dataSourceContextHolder;

    private final MessageSource messageSource;

    private final ObjectMapper objectMapper;

    private final CountryRepository countryRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    /**
     * khu vuc = tinh ( p ) + huyen ( d )
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getArea(ProvincesRequest request) {

        int count = 0;
        dataSourceContextHolder.setCurrentTenantId(null);
        String url = configRepository.findValueByNameAndTenantId("D_URL_GET_PROVINCES", 0);
        StringBuilder urlGetProvinces = new StringBuilder(url);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

        List<ProvincesDto> listResult = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        urlGetProvinces.append("p");
        if(request.getName() !=null && !request.getName().isEmpty())
        {
            urlGetProvinces.append("/search/?q=").append(request.getName());
        }
        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetProvinces.toString(), HttpMethod.GET, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                JsonNode root = objectMapper.readTree(responseBody);
                for (JsonNode item : root) {
                    String ProvincesName = item.get("name").asText();
                    String provincesCode= item.get("code").asText();

                    StringBuilder urlGetDistricts = new StringBuilder(url + "p/" + item.get("code") + "?depth=2");
                    ResponseEntity<String> responseDis = externalRestTemplate.exchange(urlGetDistricts.toString(), HttpMethod.GET, requestEntity, String.class);
                    if (responseDis.getStatusCode().is2xxSuccessful()) {
                        String responseBodyDis = responseDis.getBody();
                        JsonNode rootDisData = objectMapper.readTree(responseBodyDis);
                        JsonNode rootDis = rootDisData.get("districts");
                        for (JsonNode itemDis : rootDis) {
                            ProvincesDto itemResult = new ProvincesDto();
                            itemResult.setName(ProvincesName + " - " + itemDis.path("name").asText());
                            itemResult.setCodeName(itemDis.path("codename").asText());
                            itemResult.setCodeDistricts(itemDis.path("code").asText());
                            itemResult.setCodeProvinces(provincesCode);
                            listResult.add(itemResult);
                            count++;
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
            }

        }
        GlobalReponse result = GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(listResult).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

        return result;
    }

    /**
     * phuong xa ( w )
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getWard(ProvincesRequest request) {
        dataSourceContextHolder.setCurrentTenantId(null);
        StringBuilder urlGetProvinces = new StringBuilder(configRepository.findValueByNameAndTenantId("D_URL_GET_PROVINCES", 0));
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        if(request.getName() != null && !request.getName().isEmpty())
            urlGetProvinces.append("w/search/").append("p=").append(request.getCodeDistricts()).append("&q=").append(request.getName());
        else
            urlGetProvinces.append("d/").append(request.getCodeDistricts()).append("?depth=2");
        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetProvinces.toString(), HttpMethod.GET, requestEntity, String.class);
        List<ProvincesDto> listResult = new ArrayList<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode rootWards = root.get("wards");
                for (JsonNode item : rootWards) {
                    ProvincesDto itemResult = new ProvincesDto();
                    itemResult.setName(item.path("name").asText());
                    listResult.add(itemResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return GlobalReponse.builder().data(listResult).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).status(HttpStatus.OK.value()).build();
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getProvinces(ProvincesRequest request) {
        int count = 0;
        dataSourceContextHolder.setCurrentTenantId(null);
        String url = configRepository.findValueByNameAndTenantId("D_URL_GET_PROVINCES", 0);
        StringBuilder urlGetProvinces = new StringBuilder(url);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

        List<ProvincesDto> listResult = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        urlGetProvinces.append("p");
        if(request.getName() !=null && !request.getName().isEmpty())
        {
            urlGetProvinces.append("/search/?q=").append(request.getName());
        }
        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetProvinces.toString(), HttpMethod.GET, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                JsonNode root = objectMapper.readTree(responseBody);
                for (JsonNode item : root) {
                    ProvincesDto itemResult = new ProvincesDto();
                    itemResult.setName( item.get("name").asText());
                    itemResult.setCodeProvinces( item.get("code").asText());
                    listResult.add(itemResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
            }

        }
        GlobalReponse result = GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(listResult).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

        return result;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getDistrict(ProvincesRequest request) {
        dataSourceContextHolder.setCurrentTenantId(null);
        String url = configRepository.findValueByNameAndTenantId("D_URL_GET_PROVINCES", 0);
        StringBuilder urlGetProvinces = new StringBuilder(url);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        List<ProvincesDto> listResult = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        urlGetProvinces.append("p").append("/").append(request.getCodeProvinces()).append("?depth=2");
        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetProvinces.toString(), HttpMethod.GET, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode rootDis = root.get("districts");
                for (JsonNode item : rootDis) {
                    ProvincesDto itemResult = new ProvincesDto();
                    itemResult.setName(item.path("name").asText());
                    itemResult.setCodeDistricts(item.path("code").asText());
                    listResult.add(itemResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GlobalReponse result = GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(listResult).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

        return result;
    }

    /**
     *
     * @param countryRequest
     * @return
     */
    @Override
    public GlobalReponsePagination getCountry(CountryRequest countryRequest) {
        Pageable pageable = requestParamsUtils.getPageRequest(countryRequest);
        Specification<Country> spec = (root, query, cb) -> {
            List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (countryRequest.getCode() != null) {
                predicates.add(cb.equal(root.get("isoCountryCode"), countryRequest.getCode()));
            }
            if (countryRequest.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + countryRequest.getName().toLowerCase() + "%"));
            }
            if (countryRequest.getIsDefault() != null) {
                predicates.add(cb.equal(root.get("isDefault"), countryRequest.getIsDefault()));
            }
            return cb.and(predicates.toArray(new javax.persistence.criteria.Predicate[0]));
        };
        dataSourceContextHolder.setCurrentTenantId(null);
        Page<Country> result = countryRepository.findAll(spec, pageable);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));
        List<CountryDto> rs = result.map((element) -> modelMapper.map(element, CountryDto.class)).getContent();
        GlobalReponsePagination response = GlobalReponsePagination.builder()
                .data(rs)
                .status(HttpStatus.OK.value())
                .errors("")
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .totalPages(result.getTotalPages())
                .pageSize(result.getSize())
                .currentPage(result.getNumber())
                .totalItems(result.getTotalElements())
                .build();
        return response;
    }
}
