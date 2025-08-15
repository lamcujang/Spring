package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.AttributeValue;
import com.dbiz.app.productservice.repository.AttributeValueRepository;
import com.dbiz.app.productservice.service.AttributeValueService;
import com.dbiz.app.productservice.specification.AttributeValueSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.dto.productDto.AttributeValueDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeValueQueryRequest;
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
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository entityRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;
    @Override
    public GlobalReponsePagination findAll(AttributeValueQueryRequest queryRequest) {
        log.info("*** AttributeValue List, service; fetch all AttributeValue ***");

        Pageable pageable = requestParamsUtils.getPageRequest(queryRequest);


        Specification<AttributeValue> spec = AttributeValueSpecification.getEntitySpecification(queryRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<AttributeValue> entityList = entityRepository.findAll( spec,pageable);
        List<AttributeDto> listData = new ArrayList<>();
        for(AttributeValue item : entityList.getContent()){
            listData.add(modelMapper.map(item,AttributeDto.class));
        }
        response.setMessage("Attribute fetched successfully");
        response.setData(listData);
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
    public GlobalReponse save(AttributeValueDto Dto) {
        log.info("*** AttributeValue, service; save AttributeValue ***");
        GlobalReponse response = new GlobalReponse();
        if(Dto.getId()!=null)
        {
            AttributeValue attrUpdate = entityRepository.findById(Dto.getId()).orElseThrow(()-> new RuntimeException("Attribute not found"));
            modelMapper.map(Dto,attrUpdate);
            entityRepository.save(attrUpdate);
            response.setMessage(messageSource.getMessage("attribute.updated",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            response.setData(modelMapper.map(attrUpdate,AttributeValueDto.class));
        }
        else{
            AttributeValue entitySave = modelMapper.map(Dto, AttributeValue.class);
            entityRepository.save(entitySave);
            response.setMessage(messageSource.getMessage("attribute.created",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());

            response.setData(modelMapper.map(entitySave,AttributeValueDto.class));
        }
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }


}
