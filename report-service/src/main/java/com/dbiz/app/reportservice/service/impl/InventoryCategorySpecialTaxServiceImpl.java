

package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.domain.InventoryCategorySpecialTax;
import com.dbiz.app.reportservice.repository.InventoryCategorySpecialTaxRepository;
import com.dbiz.app.reportservice.service.InventoryCategorySpecialTaxService;
import com.dbiz.app.reportservice.specification.InventoryCategorySpecialTaxSpecification;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.reportDto.InventoryCategorySpecialTaxDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.reportRequest.InventoryCategorySpecialTaxQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class InventoryCategorySpecialTaxServiceImpl implements InventoryCategorySpecialTaxService {

    private final InventoryCategorySpecialTaxRepository inventoryCategorySpecialTaxRepository;
    private final ModelMapper modelMapper;
    private final RequestParamsUtils requestParamsUtils;
    private final EntityManager entityManager;
    private final MessageSource messageSource;

    @Override
    public GlobalReponsePagination findAll(InventoryCategorySpecialTaxQueryRequest paramRequest) {

        log.info("*** InventoryCategorySpecialTaxDto List, service; fetch all inventoryCategorySpecialTaxs *");
        paramRequest.setSortBy("name");
        paramRequest.setOrder("asc");

        InventoryCategorySpecialTaxQueryRequest query = (InventoryCategorySpecialTaxQueryRequest) paramRequest;

        Pageable pageable = requestParamsUtils.getPageRequest(query);

        Specification<InventoryCategorySpecialTax> spec = InventoryCategorySpecialTaxSpecification.getSpecification(query);
        Page<InventoryCategorySpecialTax> entityList = inventoryCategorySpecialTaxRepository.findAll(spec, pageable);
        List<InventoryCategorySpecialTaxDto> listData = new ArrayList<>();
        for (InventoryCategorySpecialTax item : entityList.getContent()) {
            InventoryCategorySpecialTaxDto inventoryCategorySpecialTaxDto = modelMapper.map(item, InventoryCategorySpecialTaxDto.class);
            listData.add(inventoryCategorySpecialTaxDto);
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("InventoryCategorySpecialTax fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer inventoryCategorySpecialTaxId) {
        log.info("*** InventoryCategorySpecialTaxDto, service; fetch inventoryCategorySpecialTax by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.inventoryCategorySpecialTaxRepository.findById(inventoryCategorySpecialTaxId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("InventoryCategorySpecialTax with id: %d not found", inventoryCategorySpecialTaxId))), InventoryCategorySpecialTaxDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(InventoryCategorySpecialTaxDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}









