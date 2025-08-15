package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.domain.Brand;
import com.dbiz.app.productservice.repository.BrandRepository;
import com.dbiz.app.productservice.service.BrandService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.request.BrandReqDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination getBrands(BrandReqDto dto) {

        log.info("BrandReqDto: {}", dto);


        Parameter parameter = new Parameter();
        parameter.add("d_brand_id", dto.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", dto.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("name", dto.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("description", dto.getDescription(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("is_active", dto.getIsActive(), Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);

        ResultSet rs = queryEngine.getRecords( "d_brand",
                parameter, dto);

        List<BrandDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                BrandDto brand = BrandDto.builder()
                        .id(rs.getInt("d_brand_id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .isActive(rs.getString("is_active"))
                        .build();
                data.add(brand);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_brand", parameter, dto);
        log.info("Load pagination...");
        return GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();

    }

    @Override
    public GlobalReponse getBrandById(Integer id) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse createBrand(BrandDto dto) {
        log.info("BrandDto: {}", dto);
        Brand brand = null;

        boolean isNew = true;
        try{
            if(dto.getId() == null){
                brand = modelMapper.map(dto, Brand.class);
                brand.setTenantId(AuditContext.getAuditInfo().getTenantId());
                brand.setOrgId(0);
                brand = brandRepository.save(brand);
                dto.setId(brand.getId());
            }else{
                brand = brandRepository.findById(dto.getId()).orElseThrow(() ->
                        new PosException(messageSource.getMessage("brand.not.found", null, LocaleContextHolder.getLocale())));
                modelMapper.map(dto, brand);
                brand = brandRepository.save(brand);
                isNew = false;
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        GlobalReponse response = new GlobalReponse();
        response.setData(dto);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setStatus(isNew == false ? HttpStatus.OK.value() : HttpStatus.CREATED.value());
        return response;
    }

    @Override
    public GlobalReponse deleteBrand(Integer id) {
        return null;
    }
}
