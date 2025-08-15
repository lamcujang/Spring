package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.PriceListOrg;

import com.dbiz.app.productservice.repository.PriceListOrgRepository;
import com.dbiz.app.productservice.service.PriceListOrgService;
import com.dbiz.app.productservice.specification.PriceListOrgSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.PriceListOrgDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PriceListOrgQueryRequest;
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
import java.util.Optional;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PriceListOrgServiceImpl implements PriceListOrgService {

    private final PriceListOrgRepository entityRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(PriceListOrgQueryRequest query) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        Pageable pageable = requestParamsUtils.getPageRequest(query);


        Specification<PriceListOrg> spec = PriceListOrgSpecification.getEntitySpecification(query);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PriceListOrg> entityList = entityRepository.findAll( spec,pageable);
        List<PriceListOrgDto> listData = new ArrayList<>();
        for(PriceListOrg item : entityList.getContent()){
            listData.add(modelMapper.map(item,PriceListOrgDto.class));
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(PriceListOrgDto paramDto) {
        log.info("*** pricelist, service; save pricelist ***");
        GlobalReponse response = new GlobalReponse();
        PriceListOrg entitySave = modelMapper.map(paramDto, PriceListOrg.class);

        if(entitySave.getId() !=null) // update
        {
            entitySave = this.entityRepository.findById(paramDto.getId()).orElseThrow(()-> new ObjectNotFoundException("entity not found"));


            if(entitySave.getOrgId() != null)
            {
                entitySave.setOrgId(paramDto.getOrgId());
            }
            modelMapper.map(paramDto,entitySave);
            this.entityRepository.save(entitySave);
            response.setMessage(messageSource.getMessage("price.list.org.updated",null, LocaleContextHolder.getLocale())) ;
        }else
        {
            entitySave = this.entityRepository.save(entitySave);
            response.setMessage(messageSource.getMessage("price.list.org.created",null, LocaleContextHolder.getLocale()));
        }

        response.setData(modelMapper.map(entitySave,PriceListOrgDto.class));
        response.setMessage("Warehouse saved successfully");
        response.setStatus(HttpStatus.OK.value());
        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete priceListOrg by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<PriceListOrg> entityDelete = this.entityRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("price.list.org.not.found",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("price.list.org.deleted",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** Pricelist, service; fetch Pricelist by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.entityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id))),PriceListOrgDto.class));
        response.setMessage(messageSource.getMessage("price.list.org.fetch.success",null, LocaleContextHolder.getLocale()));
        return response;
    }

}
