package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.BusinessSectorGroup;
import com.dbiz.app.productservice.helper.Mapper.BusinessSectorGroupMapper;
import com.dbiz.app.productservice.repository.BusinessSectorGroupRepository;
import com.dbiz.app.productservice.repository.BusinessSectorGroupRepository;
import com.dbiz.app.productservice.service.BusinessSectorGroupService;
import com.dbiz.app.productservice.specification.BusinessSectorGroupSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.dto.productDto.BusinessSectorGroupDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BusinessSectorGroupServiceImpl implements BusinessSectorGroupService {
    private final BusinessSectorGroupRepository entityRepository;
    private final BusinessSectorGroupRepository businessSectorGroupRepository;
    private final BusinessSectorGroupMapper businessSectorGroupMapper;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(BusinessSectorGroupQueryRequest request) {
        log.info("*** BusinessSectorGroup List, service; fetch all BusinessSectorGroup *");
        request.setOrder("asc");
        request.setSortBy("code");
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<BusinessSectorGroup> spec = BusinessSectorGroupSpecification.getEntitySpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<BusinessSectorGroup> ResultPage = entityRepository.findAll(spec, pageable);
        List<BusinessSectorGroupDto> listData = new ArrayList<>();
        
        for(BusinessSectorGroup item : ResultPage.getContent()){
            
            BusinessSectorGroupDto businessSectorGroupDto = businessSectorGroupMapper.toBusinessSectorGroupDto(item);
            listData.add(businessSectorGroupDto);
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
        log.info("*** BusinessSectorGroupCategory service impl, find BusinessSectorGroupCategory by id *");
        GlobalReponse result = new GlobalReponse();
        BusinessSectorGroup entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("business.sector.not.found",null, LocaleContextHolder.getLocale()),id)));
        BusinessSectorGroupDto dto = businessSectorGroupMapper.toBusinessSectorGroupDto(entity);
        result.setMessage(messageSource.getMessage("business.sector.fetch.success",null, LocaleContextHolder.getLocale()));
        result.setData(dto);
        return result;
    }

    @Override
    public GlobalReponse save(BusinessSectorGroupDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

}
