package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.PriceListOrg;

import com.dbiz.app.productservice.helper.DateHelper;
import com.dbiz.app.productservice.repository.*;

import com.dbiz.app.productservice.service.PriceListV2Service;

import com.dbiz.app.tenantservice.domain.AuditContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.common.dbiz.dto.productDto.*;

import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

import org.common.dbiz.request.productRequest.PriceListQueryRequest;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.http.*;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PriceListV2ServiceImpl implements PriceListV2Service {

    private final PriceListRepository priceListRepository;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final PriceListOrgRepository priceListOrgRepository;

    @Override
    public GlobalReponsePagination findAll(PriceListQueryRequest request) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse save(PricelistDto paramDto) {
        log.info("*** pricelist, service; save pricelist ***");
        GlobalReponse response = new GlobalReponse();
        PriceList entitySave = modelMapper.map(paramDto, PriceList.class);

        if (entitySave.getId() != null) // update
        {
            Integer idPriceList = entitySave.getId();
            entitySave = this.priceListRepository.findById(paramDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

            this.priceListOrgRepository.deleteAllByPricelistId(idPriceList);
            this.priceListOrgRepository.updateIsActiveByPricelistId(idPriceList, "N");
            if (paramDto.getOrgIds() != null) {
                Arrays.stream(paramDto.getOrgIds()).forEach(element -> {
                    PriceListOrg priceListOrg = this.priceListOrgRepository.findByOrgIdAndPricelistId(element, idPriceList).orElse(null);
                    if (priceListOrg == null) {
                        priceListOrg = new PriceListOrg();
                        priceListOrg.setOrgId(element);
                        priceListOrg.setPricelistId(idPriceList);
                        priceListOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        this.priceListOrgRepository.save(priceListOrg);
                    } else {
                        priceListOrg.setIsActive("Y");
                        this.priceListOrgRepository.save(priceListOrg);
                    }
                    ;
                });
            }

            modelMapper.map(paramDto, entitySave);
            if (paramDto.getFromDate() != null)
                entitySave.setFromDate(DateHelper.toInstant(paramDto.getFromDate()));
            if (paramDto.getToDate() != null)
                entitySave.setToDate(DateHelper.toInstant(paramDto.getToDate()));
            this.priceListRepository.save(entitySave);

            response.setMessage(messageSource.getMessage("price.list.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        } else {// insert
            PriceList priceListCheck = this.priceListRepository.findByNameAndTenantId(paramDto.getName(), AuditContext.getAuditInfo().getTenantId());
            if (priceListCheck != null) {
                if (priceListCheck.getToDate().isAfter(Instant.now())) // kiem tra 1 co sau thoi gian hien tai so  voi 2 k
                {
                    throw new PosException(messageSource.getMessage("price.list.name.exist", null, LocaleContextHolder.getLocale()));
                }
                ;
            }

            entitySave.setOrgId(0);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if (paramDto.getFromDate() != null)
                entitySave.setFromDate(DateHelper.toInstant(paramDto.getFromDate()));
            if (paramDto.getToDate() != null)
                entitySave.setToDate(DateHelper.toInstant(paramDto.getToDate()));
            entitySave = this.priceListRepository.save(entitySave);
            Integer idPriceList = entitySave.getId();

            if (paramDto.getOrgIds() != null) {
                Arrays.stream(paramDto.getOrgIds()).forEach(element -> {
                    PriceListOrg priceListOrg = new PriceListOrg();
                    priceListOrg.setOrgId(element);
                    priceListOrg.setPricelistId(idPriceList);
                    priceListOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    this.priceListOrgRepository.save(priceListOrg);
                });
            }


            response.setMessage(messageSource.getMessage("price.list.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }

        response.setData(modelMapper.map(entitySave, PricelistDto.class));
        log.info("PiceList saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
