package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.domain.ReferenceList;
import com.dbiz.app.systemservice.domain.view.ReferenceGetV;
import com.dbiz.app.systemservice.helper.ReferenceListMapper;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import com.dbiz.app.systemservice.repository.ReferenceGetVRepository;
import com.dbiz.app.systemservice.repository.ReferenceListRepository;
import com.dbiz.app.systemservice.service.ReferenceListService;
import com.dbiz.app.systemservice.specification.ReferenceListSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.systemDto.ReferenceListDto;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RefercenceListServiceImpl implements ReferenceListService {
    private  final ReferenceListRepository entityRepository;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final ReferenceListMapper referenceListMapper;
    private final ReferenceGetVRepository referenceGetVRepository;
    private final EntityManager entityManager;
    @Override
    public GlobalReponsePagination findAll(  ReferenceListQueryRequest request ) {
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        GlobalReponse response = new GlobalReponse();
        List<ReferenceGetV> entity = referenceGetVRepository.findByNameReference(request.getName());
        response.setData(entity);
        response.setMessage("ReferenceList fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        globalReponsePagination.setData(response.getData());
        globalReponsePagination.setTotalPages(0);
        globalReponsePagination.setPageSize(0);
        globalReponsePagination.setCurrentPage(0);
        globalReponsePagination.setTotalItems(BigDecimal.ZERO.longValue());
        return globalReponsePagination;
    }

    @Override
    public GlobalReponse save( ReferenceListDto entityDto) {
        log.info("Save VendorDto: {}", entityDto);

        GlobalReponse response = new GlobalReponse();
        ReferenceList entitySave = null;

        if(entityDto.getId() != null) {
            entitySave = entityRepository.findById(entityDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Vendor not found"));
            entitySave =   referenceListMapper.updateEntity(entityDto,entitySave);
            entityRepository.save(entitySave);
            entityDto = referenceListMapper.toReferenceDto(entitySave);
            response.setData(entityDto);
            response.setMessage(messageSource.getMessage("reference.list.updated",null, LocaleContextHolder.getLocale()));
        }
        else {
            entitySave = referenceListMapper.toReferenceList(entityDto);
            entityRepository.save(entitySave);
            entityDto = referenceListMapper.toReferenceDto(entitySave);
            response.setData(entityDto);
            response.setMessage(messageSource.getMessage("reference.list.created",null, LocaleContextHolder.getLocale()));
        }
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {

        ReferenceList entity = entityRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("ReferenceList not found"));
       entityRepository.delete(entity);
        GlobalReponse response = new GlobalReponse();
        response.setData(null);
        response.setMessage(messageSource.getMessage("reference.list.deleted",null, LocaleContextHolder.getLocale()));
        return response;

    }

    @Override
    public GlobalReponse findById(Integer id) {
        ReferenceList entity = entityRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("ReferenceList not found"));
        GlobalReponse response = new GlobalReponse();
        response.setMessage(messageSource.getMessage("reference.list.not.found",null, LocaleContextHolder.getLocale()));
        response.setData(referenceListMapper.toReferenceDto(entity));
        return response;
    }

    @Override
    public GlobalReponse findByValue(String value) {
        GlobalReponse response = new GlobalReponse();
        ReferenceList entity = entityRepository.findByValue(value);
        response.setData(referenceListMapper.toReferenceDto(entity));
        response.setMessage(messageSource.getMessage("reference.list.fetch.success",null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findRefValue(String value, String domain,String column) {
        GlobalReponse response = new GlobalReponse();
        ReferenceList entity = entityRepository.findByValueAndDomain(value,domain,column);
        response.setData(referenceListMapper.toReferenceDto(entity));
        response.setMessage(messageSource.getMessage("reference.list.fetch.success",null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findByReferenceNameAndValue(String nameReference, String value) {
        GlobalReponse response = new GlobalReponse();
        ReferenceGetV entity = referenceGetVRepository.findByNameReferenceAndValue(nameReference,value);
        response.setData(entity);
        response.setMessage(messageSource.getMessage("reference.list.fetch.success",null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findByReferenceName(String nameReference) {
        String sql =   " select d_reference_id, name_reference,name,value from pos.d_reference_get_v where name_reference = :nameReference)";
        List<ReferenceGetV>resultDto  = new ArrayList<>();

        List<Map<String, Object>> results = getDynamicResults(nameReference);

        for (Map<String, Object> row : results) {
            ReferenceGetV item = ReferenceGetV.builder()
                    .dReferenceId(ParseHelper.INT.parse(row.get("d_reference_id")))
                    .name(ParseHelper.STRING.parse(row.get("name")))
                    .nameReference(ParseHelper.STRING.parse(row.get("name_reference")))
                    .value(ParseHelper.STRING.parse(row.get("value")))
                    .build();
            resultDto.add(item);
        }
        GlobalReponse response = new GlobalReponse();
//        List<ReferenceGetV> entity = referenceGetVRepository.findByNameReference(nameReference);
        response.setData(resultDto);
        response.setMessage(messageSource.getMessage("reference.list.fetch.success",null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }


    public List<Map<String, Object>> getDynamicResults(String nameReference) {
        String sql = "SELECT d_reference_id, name_reference, name, value FROM pos.d_reference_get_v WHERE name_reference = :nameReference order by lineno asc";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("nameReference", nameReference)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        return results;
    }
}
