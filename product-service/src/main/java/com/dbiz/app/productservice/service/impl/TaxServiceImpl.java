package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Tax;
import com.dbiz.app.productservice.domain.TaxCategory;
import com.dbiz.app.productservice.helper.Mapper.TaxMapper;
import com.dbiz.app.productservice.repository.ProductRepository;
import com.dbiz.app.productservice.repository.TaxCategoryRepository;
import com.dbiz.app.productservice.repository.TaxRepository;
import com.dbiz.app.productservice.service.TaxService;
import com.dbiz.app.productservice.specification.TaxSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.TaxCategoryDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.TaxQueryRequest;
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
public class TaxServiceImpl implements TaxService {
    private final TaxRepository entityRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final TaxMapper taxMapper;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    private final ProductRepository productRepository;

    @Override
    public GlobalReponsePagination  findAll(TaxQueryRequest request) {
        log.info("*** Tax List, service; fetch all Tax *");
        request.setOrder("asc");
        request.setSortBy("taxRate");
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<Tax> spec = TaxSpecification.getEntitySpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Tax> ResultPage = entityRepository.findAll( spec,pageable);
        List<TaxDto> listData = new ArrayList<>();
        for(Tax item : ResultPage.getContent()){
            TaxDto taxDto = taxMapper.toTaxDto(item);

            if(item.getTaxCategoryId() != null)
            {
                TaxCategoryDto taxCategory = modelMapper.map(taxCategoryRepository.findById(item.getTaxCategoryId()).get(), TaxCategoryDto.class);
                taxDto.setTaxCategoryDto(taxCategory);
            }

            listData.add(taxDto);
        }
        response.setMessage(messageSource.getMessage("tax.fetch.all",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(ResultPage.getNumber());
        response.setPageSize(ResultPage.getSize());
        response.setTotalPages(ResultPage.getTotalPages());
        response.setTotalItems(ResultPage.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse  findById(Integer id) {
        log.info("*** TaxCategory service impl, find TaxCategory by id *");
        GlobalReponse result = new GlobalReponse();
        Tax entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("tax.category.not.found",null, LocaleContextHolder.getLocale()),id)));
        TaxDto dto = taxMapper.toTaxDto(entity);
        if(entity.getTaxCategoryId() != null)
        {
            TaxCategoryDto taxCategory = modelMapper.map(taxCategoryRepository.findById(entity.getTaxCategoryId()).get(), TaxCategoryDto.class);
            dto.setTaxCategoryDto(taxCategory);
        }
        result.setMessage(messageSource.getMessage("tax.fetch.success",null, LocaleContextHolder.getLocale()));
        result.setData(dto);
        return result;
    }

    @Override
    public GlobalReponse save(TaxDto entityDto) {
        log.info("*** Tax service impl, save Tax dto *");

        GlobalReponse result = new GlobalReponse();
        Tax entitySave = null;
        if(entityDto.getId() != null){
            //clear
            if("Y".equals(entityDto.getIsDefault())){
                entityRepository.updateAllIsDefaultById("N", "Y");
            }

            Integer id = entityDto.getId();
            entitySave = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(
                    String.format(messageSource.getMessage("tax.not.found",null, LocaleContextHolder.getLocale()), id)));
            modelMapper.map(entityDto,entitySave);
            entityRepository.save(entitySave);
            result.setMessage(messageSource.getMessage("tax.updated",null, LocaleContextHolder.getLocale() ));
            result.setStatus(HttpStatus.OK.value());
            result.setData(modelMapper.map(entitySave, TaxCategory.class));

            entityDto = taxMapper.toTaxDto(entitySave);
            if(entitySave.getTaxCategoryId() != null)
            {
                TaxCategoryDto taxCategory = modelMapper.map(taxCategoryRepository.findById(entitySave.getTaxCategoryId()).get(), TaxCategoryDto.class);
                entityDto.setTaxCategoryDto(taxCategory);
            }
        }else {
            if("Y".equals(entityDto.getIsDefault())){
                entityRepository.updateAllIsDefaultById("N", "Y");
            }

            Tax entityCheck = this.entityRepository.findByNameAndTenantId(entityDto.getName(), AuditContext.getAuditInfo().getTenantId()).orElse(null);
            if(entityCheck != null)
            {
                throw new ObjectNotFoundException(messageSource.getMessage("tax.name.exist",null, LocaleContextHolder.getLocale()));
            }

            entitySave = taxMapper.toTax(entityDto);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave.setOrgId(0);
            entityRepository.save(entitySave);
            result.setStatus(HttpStatus.CREATED.value());
            result.setMessage(messageSource.getMessage("tax.created",null, LocaleContextHolder.getLocale() ));
            result.setData(modelMapper.map(entitySave, TaxCategoryDto.class));
            entityDto = taxMapper.toTaxDto(entitySave);
            if(entitySave.getTaxCategoryId() != null)
            {
                TaxCategoryDto taxCategory = modelMapper.map(taxCategoryRepository.findById(entitySave.getTaxCategoryId()).get(), TaxCategoryDto.class);
                entityDto.setTaxCategoryDto(taxCategory);
            }
        }

        result.setData(entityDto);

        return result;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Tax service impl, update Tax dto *");
        GlobalReponse result = new GlobalReponse();
        if(productRepository.existsByTaxId(id))
            throw new ObjectNotFoundException(messageSource.getMessage("tax.used",null, LocaleContextHolder.getLocale()));
        Tax entity = entityRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("tax.not.found",null, LocaleContextHolder.getLocale()),id)));

        entityRepository.delete(entity);
        result.setMessage(messageSource.getMessage("tax.deleted",null, LocaleContextHolder.getLocale()));
        result.setData("");

        return result;
    }
}
