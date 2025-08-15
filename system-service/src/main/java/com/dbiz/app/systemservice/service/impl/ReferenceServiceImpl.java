package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.Reference;
import com.dbiz.app.systemservice.helper.ReferenceMapper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import com.dbiz.app.systemservice.repository.ReferenceRepository;
import com.dbiz.app.systemservice.service.ReferenceService;
import com.dbiz.app.systemservice.specification.ReferenceSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ReferenceDto;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {
    private  final ReferenceRepository entityRepository;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final ReferenceMapper referenceMapper;
    @Override
    public GlobalReponsePagination findAll(  ReferenceQueryRequest request) {

        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Reference> spec = ReferenceSpecification.getEntitySpecification(request);
        // lay ra id giao dich  va them dieu kien vao where

        Page<Reference> references = entityRepository.findAll( spec,pageable);
        List<ReferenceDto>listData = new ArrayList<>();
        for(Reference item : references.getContent()){

            ReferenceDto itemDto =modelMapper.map(item, ReferenceDto.class);
            listData.add(itemDto);
        }
        globalReponsePagination.setData(listData);
        globalReponsePagination.setStatus(HttpStatus.OK.value());
        globalReponsePagination.setErrors("");
        globalReponsePagination.setMessage(messageSource.getMessage("reference.findAll", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(references.getTotalPages());
        globalReponsePagination.setPageSize(references.getSize());
        globalReponsePagination.setCurrentPage(references.getNumber());
        globalReponsePagination.setTotalItems(references.getTotalElements());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse save(ReferenceDto entityDto ) {
        log.info("Save Reference: {}", entityDto);
        GlobalReponse response = new GlobalReponse();
        Reference entitySave = null;
        if(entityDto.getId() != null) {
            entitySave = entityRepository.findById(entityDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Vendor not found"));
            entitySave =   referenceMapper.updateEntity(entityDto,entitySave);
            entityRepository.save(entitySave);
            entityDto = referenceMapper.toReferenceDto(entitySave);
            response.setData(entityDto);
            response.setMessage(messageSource.getMessage("reference.updated", null, LocaleContextHolder.getLocale()));
        }
        else {
            entitySave = referenceMapper.toReference(entityDto);
            entityRepository.save(entitySave);
            entityDto = referenceMapper.toReferenceDto(entitySave);
            response.setData(entityDto);
            response.setMessage(messageSource.getMessage("reference.created", null, LocaleContextHolder.getLocale()));
        }
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        Reference entity = entityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Reference not found"));
        entityRepository.delete(entity);
        GlobalReponse response = new GlobalReponse();
        response.setData(null);
        response.setMessage(messageSource.getMessage("reference.deleted", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        Reference entity = entityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Reference not found"));
        GlobalReponse response = new GlobalReponse();
        response.setData(referenceMapper.toReferenceDto(entity));
        response.setMessage(messageSource.getMessage("reference.fetch.success", null, LocaleContextHolder.getLocale()));
        return response;

    }
}
