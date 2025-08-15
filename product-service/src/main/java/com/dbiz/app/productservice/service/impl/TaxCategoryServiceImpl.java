package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Tax;
import com.dbiz.app.productservice.domain.TaxCategory;
import com.dbiz.app.productservice.repository.TaxCategoryRepository;
import com.dbiz.app.productservice.repository.TaxRepository;
import com.dbiz.app.productservice.service.TaxCategoryService;
import com.dbiz.app.productservice.specification.TaxCategorySpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxCategoryQueryRequest;
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
public class TaxCategoryServiceImpl implements TaxCategoryService{
    private final TaxCategoryRepository entityRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final TaxRepository taxRepository;
    @Override
    public GlobalReponsePagination findAll(TaxCategoryQueryRequest request) {
        log.info("*** TaxCategory List, service; fetch all TaxCategory *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<TaxCategory> spec = TaxCategorySpecification.getEntitySpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<TaxCategory> ResultPage = entityRepository.findAll( spec,pageable);
        List<TaxCategoryDto> listData = new ArrayList<>();
        for(TaxCategory item : ResultPage.getContent()){
            listData.add(modelMapper.map(item, TaxCategoryDto.class));
        }
        response.setMessage(messageSource.getMessage("tax.category.fetch.all",null, LocaleContextHolder.getLocale() ));
        response.setData(listData);
        response.setCurrentPage(ResultPage.getNumber());
        response.setPageSize(ResultPage.getSize());
        response.setTotalPages(ResultPage.getTotalPages());
        response.setTotalItems(ResultPage.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(TaxCategoryDto entityDto) {
        log.info("*** TaxCategory service impl, save TaxCategory dto *");
        GlobalReponse result = new GlobalReponse();
        TaxCategory entitySave = null;
        if(entityDto.getId() != null){
            entitySave = entityRepository.findById(entityDto.getId()).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("tax.category.not.found",null, LocaleContextHolder.getLocale()), entityDto.getId())));
            modelMapper.map(entityDto,entitySave);
            entityRepository.save(entitySave);
            result.setMessage(messageSource.getMessage("tax.category.updated",null, LocaleContextHolder.getLocale() ));
            result.setStatus(HttpStatus.OK.value());
            result.setData(modelMapper.map(entitySave, TaxCategoryDto.class));
        }else {
            entitySave = modelMapper.map(entityDto, TaxCategory.class);
            entityRepository.save(entitySave);
            result.setStatus(HttpStatus.CREATED.value());
            result.setMessage(messageSource.getMessage("tax.category.created",null, LocaleContextHolder.getLocale() ));
            result.setData(modelMapper.map(entitySave, TaxCategoryDto.class));
        }
        return result;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** TaxCategory service impl, update TaxCategory dto *");
        GlobalReponse result = new GlobalReponse();
        TaxCategory entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("tax.category.not.found",null, LocaleContextHolder.getLocale()),id)));

        entityRepository.delete(entity);
        result.setMessage(messageSource.getMessage("tax.category.deleted",null, LocaleContextHolder.getLocale() ));
        result.setData("");

        return result;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** TaxCategory service impl, find TaxCategory by id *");
        GlobalReponse result = new GlobalReponse();
        TaxCategory entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("tax.category.not.found",null, LocaleContextHolder.getLocale()),id)));
        result.setMessage(messageSource.getMessage("tax.category.fetch.success",null, LocaleContextHolder.getLocale() ));
        result.setData(modelMapper.map(entity, TaxCategoryDto.class));
        return result;
    }

    /**
     * @param dto
     * @return
     * bo
     */
    @Override
    public GlobalReponse intSave(TaxCategoryDto dto) {

        TaxCategory taxCategory = entityRepository.findByErpTaxCategoryId(dto.getErpTaxCategoryId()).orElse(null);
        if(taxCategory == null){
            taxCategory = modelMapper.map(dto, TaxCategory.class);
            taxCategory.setTenantId(AuditContext.getAuditInfo().getTenantId());
            taxCategory.setOrgId(0);
            entityRepository.save(taxCategory);
            Tax tax = taxRepository.findByErpTaxId(dto.getTaxDto().getErpTaxId()).orElse(null);
            if(tax == null){
                tax = modelMapper.map(dto.getTaxDto(), Tax.class);
                tax.setTenantId(AuditContext.getAuditInfo().getTenantId());
                tax.setOrgId(0);
                tax.setTaxCategoryId(taxCategory.getId());
                taxRepository.save(tax);
            }
        }

        return null;
    }
}
