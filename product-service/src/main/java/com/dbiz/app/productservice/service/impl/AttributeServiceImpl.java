package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Attribute;
import com.dbiz.app.productservice.domain.AttributeValue;
import com.dbiz.app.productservice.repository.AttributeRepository;
import com.dbiz.app.productservice.repository.AttributeValueRepository;
import com.dbiz.app.productservice.service.AttributeService;
import com.dbiz.app.productservice.specification.AttributeSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.AttributeDto;
import org.common.dbiz.dto.productDto.AttributeValueDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.AttributeQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    /*
     * inject AttributeRepository
     *
     * inject RequestParamsUtils
     *
     * inject ModelMapper
     *
     * inject MessageSource
     *
     */
    private final AttributeRepository entityRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final AttributeValueRepository attributeValueRepository;

    /**
     * @param attributeQueryRequest
     * @return response
     */
    @Override
    public GlobalReponsePagination findAll(AttributeQueryRequest attributeQueryRequest) {
        log.info("*** Attribute List, service; fetch all Attribute ***");
        Pageable pageable = requestParamsUtils.getPageRequest(attributeQueryRequest);


        Specification<Attribute> spec = AttributeSpecification.getEntitySpecification(attributeQueryRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Attribute> entityList = entityRepository.findAll(spec, pageable);
        List<AttributeDto> listData = new ArrayList<>();
        for (Attribute item : entityList.getContent()) {
            AttributeDto dtoResponse = modelMapper.map(item, AttributeDto.class);
            List<AttributeValue> attributeValues = attributeValueRepository.findByAttributeId(item.getId());
            List<AttributeValueDto> attributeValueDtos = attributeValues.stream().map(attributeValue -> modelMapper.map(attributeValue, AttributeValueDto.class)).collect(Collectors.toList());
            dtoResponse.setAttributeValues(attributeValueDtos);
            listData.add(dtoResponse);
        }
        response.setMessage("Attribute fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    /**
     * @param Dto
     * @return response
     */
    @Override
    public GlobalReponse save(AttributeDto Dto) {
        log.info("*** Attribute, service; save Attribute ***");
        GlobalReponse response = new GlobalReponse();
        if (Dto.getId() != null) {
            Attribute attrUpdate = entityRepository.findById(Dto.getId()).orElseThrow(() -> new RuntimeException("Attribute not found"));
            modelMapper.map(Dto, attrUpdate);
            entityRepository.save(attrUpdate);
            response.setMessage(messageSource.getMessage("attribute.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            response.setData(modelMapper.map(attrUpdate, AttributeDto.class));
        } else {

            Attribute entitySave = entityRepository.findByCodeAndTenantId(Dto.getCode(), AuditContext.getAuditInfo().getTenantId()).orElse(null);
//                    modelMapper.map(Dto, Attribute.class);
            if(entitySave == null)
            {
                entitySave = modelMapper.map(Dto, Attribute.class);
                entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entitySave.setOrgId(0);
                entitySave=  entityRepository.save(entitySave);
            }
            response.setMessage(messageSource.getMessage("attribute.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());

            response.setData(modelMapper.map(entitySave, AttributeDto.class));
        }
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer attributeId) {
        GlobalReponse response = new GlobalReponse();

        Attribute attribute = entityRepository.findById(attributeId).orElseThrow(() -> new RuntimeException("Attribute not found"));
        AttributeDto attributeDto = modelMapper.map(attribute, AttributeDto.class);
        List<AttributeValueDto> attributeValueDtos = attributeValueRepository.findByAttributeId(attributeId).stream().map(attributeValue -> modelMapper.map(attributeValue, AttributeValueDto.class)).collect(Collectors.toList());
        attributeDto.setAttributeValues(attributeValueDtos);

        response.setMessage("Attribute fetched successfully");
        response.setData(attributeDto);
        response.setStatus(HttpStatus.OK.value());

        return response;
    }

    /**
     * @param attributeDto
     * @return
     */
    @Override
    public GlobalReponse saveAll(AttributeDto attributeDto) {
        Attribute attrCheck = entityRepository.findByCodeAndTenantId(attributeDto.getCode(), AuditContext.getAuditInfo().getTenantId()).orElse(null);
        if (attrCheck == null) {
            Attribute attrSave  = modelMapper.map(attributeDto, Attribute.class);
            attrSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            attrSave.setOrgId(0);
            attrCheck = entityRepository.save(attrSave);
        }
        Integer attributeId = attrCheck.getId();

        List<AttributeValueDto> attributeValueDto = attributeDto.getAttributeValues().stream()
                .map(element -> {
                    AttributeValue attributeValue = attributeValueRepository.findByValueAndAttributeId(element.getValue(), attributeId);
                    if(attributeValue == null){
                        element.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        element.setOrgId(0);
                        attributeValue = attributeValueRepository.save(modelMapper.map(element, AttributeValue.class));
                    }
                    return modelMapper.map(attributeValue, AttributeValueDto.class);
                }).collect(Collectors.toList());

        attributeDto = modelMapper.map(attrCheck, AttributeDto.class);
        attributeDto.setAttributeValues(attributeValueDto);
        return GlobalReponse.builder()
                .data(attributeDto)
                .message(messageSource.getMessage("attribute.created", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }
}
