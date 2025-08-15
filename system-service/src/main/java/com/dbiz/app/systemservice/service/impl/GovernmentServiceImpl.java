package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Province;
import com.dbiz.app.systemservice.domain.Ward;
import com.dbiz.app.systemservice.repository.ProvinceRepository;
import com.dbiz.app.systemservice.repository.WardRepository;
import com.dbiz.app.systemservice.service.GovernmentService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.systemDto.ProvinceDto;
import org.common.dbiz.dto.systemDto.WardDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ProvinceQueryRequest;
import org.common.dbiz.request.systemRequest.WardQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GovernmentServiceImpl implements GovernmentService {

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final QueryEngine queryEngine;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    private static final String GET_PROVINCE_URL = "https://34tinhthanh.com/api/provinces";
    private static final String GET_WARD_URL = "https://34tinhthanh.com/api/wards?province_code=";

    @Override
    public GlobalReponse loadAdministrativeDivisions() {

        dataSourceContextHolder.setCurrentTenantId(null);

//        ResponseEntity<List<ProvinceDto>> provResponse = restTemplate.exchange(GET_PROVINCE_URL, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});
//        List<ProvinceDto> provinceDtos = Objects.requireNonNullElse(provResponse.getBody(), Collections.emptyList());
        ResponseEntity<List<Map<String, Object>>> provResponse = restTemplate.exchange(GET_PROVINCE_URL, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });
        List<Map<String, Object>> provinces = Objects.requireNonNullElse(provResponse.getBody(), Collections.emptyList());

        try {
            for (Map<String, Object> provinceMap : provinces) {
//                    objectMapper.writeValueAsString(province);

                Province resProvince = Province.builder()
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .orgId(0)
                        .code(ParseHelper.STRING.parse(provinceMap.get("province_code")))
                        .name(ParseHelper.STRING.parse(provinceMap.get("name")))
                        .build();
                Province province;
                Optional<Province> provinceOptional = provinceRepository.findByCode(resProvince.getCode());
                if (provinceOptional.isPresent()) {
                    province = provinceOptional.get();
                    modelMapper.map(resProvince, province);
                } else {
                    province = resProvince;
                }

//                    ResponseEntity<List<WardDto>> wardResponse = restTemplate.exchange(GET_WARD_URL + province.getCode(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});
//                    List<WardDto> wardDtos = Objects.requireNonNullElse(wardResponse.getBody(), Collections.emptyList());
                ResponseEntity<List<Map<String, Object>>> wardResponse = restTemplate.exchange(GET_WARD_URL + province.getCode(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                });
                List<Map<String, Object>> wards = Objects.requireNonNullElse(wardResponse.getBody(), Collections.emptyList());

                if (!wards.isEmpty()) {
                    Map<String, Object> firstWardMap = wards.get(0);
//                        List<String> mergedList = (List<String>) firstWardMap.get("province_merged_with");
//                        String mergedWith = (mergedList == null || mergedList.isEmpty())
//                                ? null
//                                : String.join(", ", mergedList);
                    List<?> mergedListRaw = (List<?>) firstWardMap.get("province_merged_with");
                    String mergedWith = null;
                    if (mergedListRaw != null && !mergedListRaw.isEmpty()) {
                        mergedWith = mergedListRaw.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
                    }

                    province.setShortCode(ParseHelper.STRING.parse(firstWardMap.get("province_code_short")));
                    province.setShortName(ParseHelper.STRING.parse(firstWardMap.get("province_short_name")));
                    province.setPlaceType(ParseHelper.STRING.parse(firstWardMap.get("place_type")));
                    province.setIsMerged(ParseHelper.BOOLEAN.parse(firstWardMap.get("province_is_merged")) ? "Y" : "N");
                    province.setMergedWith(mergedWith);
                }
                province = provinceRepository.save(province);

                for (Map<String, Object> wardMap : wards) {
//                        objectMapper.writeValueAsString(wardDto);
                    Ward resWard = Ward.builder()
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .orgId(0)
                            .code(ParseHelper.STRING.parse(wardMap.get("ward_code")))
                            .name(ParseHelper.STRING.parse(wardMap.get("ward_name")))
                            .provinceId(province.getId())
                            .hasMerger(ParseHelper.BOOLEAN.parse(wardMap.get("has_merger")) ? "Y" : "N")
                            .mergerDetails(ParseHelper.STRING.parse(wardMap.get("merger_details")))
                            .build();
                    Ward ward;
                    Optional<Ward> wardOptional = wardRepository.findByCodeAndProvinceIdAndMergerDetails(resWard.getCode(), province.getId(), resWard.getMergerDetails());
                    if (wardOptional.isPresent()) {
                        ward = wardOptional.get();
                        modelMapper.map(resWard, ward);
                    } else {
                        ward = resWard;
                    }
                    wardRepository.save(ward);
                }
            }
        } catch (Exception e) { //JsonProcessingException if turn on log
            log.info("Error e: ", e);
            throw new PosException(e);
        }

        return GlobalReponse.builder()
//                .data(provinces)
                .build();
    }

    @Override
    public GlobalReponsePagination getProvinceWithWard(ProvinceQueryRequest request) {

        dataSourceContextHolder.setCurrentTenantId(null);

        Parameter parameter = getProvinceParameter(request);

        ResultSet rs = queryEngine.getRecords("d_province", parameter, request);
        List<ProvinceDto> provinces = new ArrayList<>();
        try {
            while (rs.next()) {
                // Province header
                ProvinceDto provinceDto = ProvinceDto.builder()
                        .id(rs.getInt("d_province_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("name"))
                        .shortCode(rs.getString("short_code"))
                        .shortName(rs.getString("short_name"))
                        .placeType(rs.getString("place_type"))
                        .isMerged(rs.getString("is_merged"))
                        .mergedWith(rs.getString("merged_with"))
                        .wards(new ArrayList<>())
                        .build();
                // Ward detail
                String sql = "SELECT * " +
                        "FROM pos.d_ward " +
                        "WHERE d_province_id = :provinceId";
                List<Map<String, Object>> wardResult = entityManager.createNativeQuery(sql)
                        .setParameter("provinceId", provinceDto.getId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                for (Map<String, Object> wardRow : wardResult) {
                    WardDto wardDto = WardDto.builder()
                            .id(ParseHelper.INT.parse(wardRow.get("d_ward_id")))
                            .code(ParseHelper.STRING.parse(wardRow.get("code")))
                            .name(ParseHelper.STRING.parse(wardRow.get("name")))
                            .provinceId(ParseHelper.INT.parse(wardRow.get("d_province_id")))
                            .hasMerger(ParseHelper.STRING.parse(wardRow.get("has_merger")))
                            .mergerDetails(ParseHelper.STRING.parse(wardRow.get("merger_details")))
                            .build();
                    provinceDto.getWards().add(wardDto);
                }

                provinces.add(provinceDto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e);
        }

        Pagination pagination = queryEngine.getPagination("d_province", parameter, request);

        return GlobalReponsePagination.builder()
                .data(provinces)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    @Override
    public GlobalReponsePagination getProvince(ProvinceQueryRequest request) {

        dataSourceContextHolder.setCurrentTenantId(null);

        Parameter parameter = getProvinceParameter(request);

        ResultSet rs = queryEngine.getRecords("d_province", parameter, request);
        List<ProvinceDto> provinces = new ArrayList<>();
        try {
            while (rs.next()) {
                ProvinceDto provinceDto = ProvinceDto.builder()
                        .id(rs.getInt("d_province_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("name"))
                        .shortCode(rs.getString("short_code"))
                        .shortName(rs.getString("short_name"))
                        .placeType(rs.getString("place_type"))
                        .isMerged(rs.getString("is_merged"))
                        .mergedWith(rs.getString("merged_with"))
                        .build();
                provinces.add(provinceDto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e);
        }

        Pagination pagination = queryEngine.getPagination("d_province", parameter, request);

        return GlobalReponsePagination.builder()
                .data(provinces)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    private static Parameter getProvinceParameter(ProvinceQueryRequest request) {
        Parameter parameter = new Parameter();
        parameter.add("d_province_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("short_code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("short_name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);

        parameter.add("code", request.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("name", request.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("short_code", request.getShortCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("short_name", request.getShortName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        parameter.add("place_type", request.getPlaceType(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("is_merged", request.getIsMerged(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("merged_with", request.getMergedWith(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        return parameter;
    }

    @Override
    public GlobalReponsePagination getWard(WardQueryRequest request) {

        dataSourceContextHolder.setCurrentTenantId(null);

        Parameter parameter = new Parameter();
        parameter.add("d_ward_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_province_id", request.getProvinceId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);

        parameter.add("code", request.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("name", request.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        parameter.add("has_merger", request.getHasMerger(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("merger_details", request.getMergerDetails(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecords("d_ward", parameter, request);
        List<WardDto> wards = new ArrayList<>();
        try {
            while (rs.next()) {
                WardDto wardDto = WardDto.builder()
                        .id(rs.getInt("d_ward_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("name"))
                        .provinceId(rs.getInt("d_province_id"))
                        .hasMerger(rs.getString("has_merger"))
                        .mergerDetails(rs.getString("merger_details"))
                        .build();
                wards.add(wardDto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e);
        }

        Pagination pagination = queryEngine.getPagination("d_ward", parameter, request);

        return GlobalReponsePagination.builder()
                .data(wards)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

}
