package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Province;
import com.dbiz.app.systemservice.domain.TaxOffice;
import com.dbiz.app.systemservice.repository.ProvinceRepository;
import com.dbiz.app.systemservice.repository.TaxOfficeRepository;
import com.dbiz.app.systemservice.service.TaxOfficeService;
import com.dbiz.app.systemservice.specification.TaxOfficeSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.TaxOfficeDto;
import org.common.dbiz.dto.systemDto.WardDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.TaxOfficeQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaxOfficeServiceImpl implements TaxOfficeService {

    private final DataSourceContextHolder dataSourceContextHolder;
    private final ObjectMapper objectMapper;
    private final QueryEngine queryEngine;
    private final MessageSource messageSource;

    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;

    private final ProvinceRepository provinceRepository;
    private final TaxOfficeRepository taxOfficeRepository;

    @Override
    public GlobalReponse loadTaxOffice(MultipartFile taxOfficeFile, MultipartFile taxRegionFile) {

        dataSourceContextHolder.setCurrentTenantId(null);

        List<TaxOffice> result = new ArrayList<>();
        try {

            String taxOfficeContent = new String(taxOfficeFile.getBytes(), StandardCharsets.UTF_8);
            JsonNode taxOfficeJsonArray = objectMapper.readTree(taxOfficeContent);

            String taxRegionContent = new String(taxRegionFile.getBytes(), StandardCharsets.UTF_8);
            JsonNode taxRegionJsonArray = objectMapper.readTree(taxRegionContent);

            // 2) prefetch provinces and build normalizedName -> id map
            List<Province> provinces = provinceRepository.findAll();

            // 3) iterate taxOffice and resolve
            if (taxOfficeJsonArray != null && taxOfficeJsonArray.isArray()) {
                for (JsonNode officeJson : taxOfficeJsonArray) {
                    String officeCode = officeJson.path("code").asText(null);
                    if (officeCode == null) continue;

                    Integer provinceId = null;

                    String officeName = officeJson.path("name").asText(null);
                    Optional<Province> found = provinces.stream()
                            .filter(province -> {
                                String normalizedProvinceName = normalizeProvinceName(province.getName());
                                return normalizedProvinceName.contains(officeName) || officeName.contains(normalizedProvinceName);
                            })
                            .findFirst();
                    if (found.isPresent()) {
                        provinceId = found.get().getId();
                    } else {
                        log.warn("officeName {} not have province!!!", officeName);
                    }

                    TaxOffice taxOffice = TaxOffice.builder()
                            .orgId(0)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .code(officeCode)
                            .name(officeJson.path("name").asText(null))
                            .provinceId(provinceId)
                            .oldName(officeJson.path("old_name").asText(null))
                            .isParent(officeJson.path("is_parent").asBoolean() ? "Y" : "N")
                            .build();
                    taxOfficeRepository.save(taxOffice);
                    result.add(taxOffice);
                }
            }

        } catch (Exception e) {
            log.error("Error: ", e);
            throw new PosException(e);
        }

        return GlobalReponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(result)
                .build();
    }

    // normalize: remove leading "Tỉnh ", "Thành phố ", "TP.", "Tp." and trim
    private String normalizeProvinceName(String s) {
        if (s == null) return null;
        String t = s.trim();
        // remove common prefixes (case-insensitive)
//        t = t.replaceAll("(?i)^(tỉnh|thành[\\s\\.]?phố|tp\\.?|tp\\s)\\s+", "");
        t = t.replaceAll("(?i)^(tỉnh|thành phố)\\s+", "");
        return t.trim();
    }



    @Override
    public GlobalReponsePagination findAllTaxOffice(TaxOfficeQueryRequest request) {

        dataSourceContextHolder.setCurrentTenantId(null);

        Parameter parameter = new Parameter();
        parameter.add("d_tax_office_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_province_id", request.getProvinceId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("old_name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);

        parameter.add("code", request.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("name", request.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("old_name", request.getOldName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        parameter.add("is_parent", request.getIsParent(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecords("d_tax_office", parameter, request);
        List<TaxOfficeDto> taxOffices = new ArrayList<>();
        try {
            while (rs.next()) {
                TaxOfficeDto taxOfficeDto = TaxOfficeDto.builder()
                        .id(rs.getInt("d_tax_office_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("name"))
                        .provinceId(rs.getInt("d_province_id") != 0 ? rs.getInt("d_province_id") : null)
                        .oldName(rs.getString("old_name"))
                        .isParent(rs.getString("is_parent"))
                        .build();
                taxOffices.add(taxOfficeDto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e);
        }

        Pagination pagination = queryEngine.getPagination("d_tax_office", parameter, request);

        return GlobalReponsePagination.builder()
                .data(taxOffices)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    @Override
    public GlobalReponsePagination findAll(TaxOfficeQueryRequest paramRequest) {
        log.info("*** TaxOfficeDto List, service; fetch all taxOffices *");

        paramRequest.setSortBy("parentCode");
        paramRequest.setOrder("asc");
        paramRequest.setPageSize(1000);

        TaxOfficeQueryRequest query = paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<TaxOffice> spec = TaxOfficeSpecification.getTaxOfficeSpecification(query);
        dataSourceContextHolder.setCurrentTenantId(null);
        Page<TaxOffice> entityList = taxOfficeRepository.findAll(spec, pageable);

        // Map entities to DTOs and tạo hashmap code -> dto
        Map<String, TaxOfficeDto> codeToDto = new HashMap<>();
        List<TaxOfficeDto> allDtos = new ArrayList<>();
        for (TaxOffice item : entityList.getContent()) {
            TaxOfficeDto dto = modelMapper.map(item, TaxOfficeDto.class);
            codeToDto.put(dto.getCode(), dto);
            allDtos.add(dto);
        }

        // Xây dựng cây cha/con dùng HashMap
        List<TaxOfficeDto> roots = new ArrayList<>();
        for (TaxOfficeDto dto : allDtos) {
            String parentCode = dto.getParentCode();
            if ("N".equals(dto.getIsParent()) && parentCode != null && !parentCode.isEmpty() && codeToDto.containsKey(parentCode)) {
                TaxOfficeDto parent = codeToDto.get(parentCode);
                if (parent.getChildDto() == null) parent.setChildDto(new ArrayList<>());
                parent.getChildDto().add(dto);
            } else {
                // Không có parent hoặc parent không nằm trong danh sách -> là node gốc
                roots.add(dto);
            }
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("TaxOffice fetched successfully");
        response.setData(roots); // chỉ trả về các node gốc, children đã lồng sẵn
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(TaxOfficeDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

}
