package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.BusinessSector;
import com.dbiz.app.productservice.helper.Mapper.BusinessSectorMapper;
import com.dbiz.app.productservice.repository.BusinessSectorGroupRepository;
import com.dbiz.app.productservice.repository.ProductRepository;
import com.dbiz.app.productservice.repository.BusinessSectorRepository;
import com.dbiz.app.productservice.service.BusinessSectorService;
import com.dbiz.app.productservice.specification.BusinessSectorSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BusinessSectorDto;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BusinessSectorServiceImpl implements BusinessSectorService {
    private final BusinessSectorRepository entityRepository;
    private final BusinessSectorGroupRepository businessSectorGroupRepository;
    private final BusinessSectorMapper businessSectorMapper;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(BusinessSectorQueryRequest request) {
        log.info("*** BusinessSector List, service; fetch all BusinessSector *");
        request.setOrder("asc");
        request.setSortBy("code");
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<BusinessSector> spec = BusinessSectorSpecification.getEntitySpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<BusinessSector> ResultPage = entityRepository.findAll(spec, pageable);
        List<BusinessSectorDto> listData = new ArrayList<>();
        for(BusinessSector item : ResultPage.getContent()){
            BusinessSectorDto businessSectorDto = businessSectorMapper.toBusinessSectorDto(item);

            if(item.getBusinessSectorGroupId() != null)
            {
                BusinessSectorGroupDto businessSectorCategory = modelMapper.map(businessSectorGroupRepository.findById(item.getBusinessSectorGroupId()).get(), BusinessSectorGroupDto.class);
                businessSectorDto.setBusinessSectorGroupDto(businessSectorCategory);
            }

            listData.add(businessSectorDto);
        }
        response.setMessage(messageSource.getMessage("business.sector.fetch.all",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(ResultPage.getNumber());
        response.setPageSize(ResultPage.getSize());
        response.setTotalPages(ResultPage.getTotalPages());
        response.setTotalItems(ResultPage.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse  findById(Integer id) {
        log.info("*** BusinessSectorCategory service impl, find BusinessSectorCategory by id *");
        GlobalReponse result = new GlobalReponse();
        BusinessSector entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("business.sector.not.found",null, LocaleContextHolder.getLocale()),id)));
        BusinessSectorDto dto = businessSectorMapper.toBusinessSectorDto(entity);
        if(entity.getBusinessSectorGroupId() != null)
        {
            BusinessSectorGroupDto businessSectorCategory = modelMapper.map(businessSectorGroupRepository.findById(entity.getBusinessSectorGroupId()).get(), BusinessSectorGroupDto.class);
            dto.setBusinessSectorGroupDto(businessSectorCategory);
        }
        result.setMessage(messageSource.getMessage("business.sector.fetch.success",null, LocaleContextHolder.getLocale()));
        result.setData(dto);
        return result;
    }

    @Override
    public GlobalReponse save(BusinessSectorDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

}
