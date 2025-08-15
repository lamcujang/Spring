package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.domain.Icon;
import com.dbiz.app.productservice.repository.IconRepository;
import com.dbiz.app.productservice.service.IconService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.IconDto;
import org.common.dbiz.dto.productDto.request.IconReqDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
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
public class IconServiceImpl implements IconService {

    private final IconRepository iconRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;
    private final DataSourceContextHolder dataSourceContextHolder;

    @Override
    public GlobalReponsePagination getIcons(IconReqDto dto) {

        log.info("IconReqDto: {}", dto);


        Parameter parameter = new Parameter();
        parameter.add("d_icon_id", dto.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", dto.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("name", dto.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("icon_type", dto.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("image_url", dto.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("description", dto.getDescription(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("is_active", dto.getIsActive(), Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);

        dataSourceContextHolder.setCurrentTenantId(null);

        ResultSet rs = queryEngine.getRecords( "d_icon",
                parameter, dto);

        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

        List<IconDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                IconDto icon = IconDto.builder()
                        .id(rs.getInt("d_icon_id"))
                        .name(rs.getString("name"))
                        .type(rs.getString("icon_type"))
                        .imageUrl(rs.getString("image_url"))
                        .description(rs.getString("description"))
                        .isActive(rs.getString("is_active"))
                        .build();
                data.add(icon);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_icon", parameter, dto);
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
    public GlobalReponse getIconById(Integer id) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse createIcon(IconDto dto) {
        log.info("IconDto: {}", dto);
        Icon icon = null;

        boolean isNew = true;
        try{
            if(dto.getId() == null){
                icon = modelMapper.map(dto, Icon.class);
                icon.setTenantId(AuditContext.getAuditInfo().getTenantId());
                icon.setOrgId(0);
                icon = iconRepository.save(icon);
                dto.setId(icon.getId());
            }else{
                icon = iconRepository.findById(dto.getId()).orElseThrow(() ->
                        new PosException(messageSource.getMessage("icon.not.found", null, LocaleContextHolder.getLocale())));
                modelMapper.map(dto, icon);
                icon = iconRepository.save(icon);
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
    public GlobalReponse deleteIcon(Integer id) {
        return null;
    }
}
