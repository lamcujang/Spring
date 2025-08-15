package com.dbiz.app.orderservice.service.impl;


import com.dbiz.app.orderservice.domain.CancelReason;
import com.dbiz.app.orderservice.repository.TableRepository;
import com.dbiz.app.orderservice.service.CancelReasonService;
import com.dbiz.app.orderservice.specification.CancelReasonSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.CancelReasonQueryRequest;
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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CancelReasonServiceImpl implements CancelReasonService {
    private final RequestParamsUtils requestParamsUtils;
    private final com.dbiz.app.orderservice.repository.CancelReasonRepository CancelReasonRepository;
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;
    private final MessageSource messageSource;
    @Override
    public GlobalReponsePagination findAll(CancelReasonQueryRequest paramRequest) {
        log.info("*** CancelReasonDTO List, service; fetch all CancelReason *");
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<CancelReason> spec = CancelReasonSpecification.getCancelReasonSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<CancelReason> CancelReasons = CancelReasonRepository.findAll( spec,pageable);
        List<CancelReasonDto > listData = new ArrayList<>();
        for(CancelReason item : CancelReasons.getContent()){
           CancelReasonDto dto= modelMapper.map(item,CancelReasonDto.class);
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("cancel_reason_successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(CancelReasons.getNumber());
        response.setPageSize(CancelReasons.getSize());
        response.setTotalPages(CancelReasons.getTotalPages());
        response.setTotalItems(CancelReasons.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse save(CancelReasonDto Dto) {
        log.info("*** warehouse, service; save warehouse ***");
        GlobalReponse response = new GlobalReponse();
        CancelReason CancelReasonSave = modelMapper.map(Dto,CancelReason.class);

        if(CancelReasonSave.getId() !=null && CancelReasonSave.getId() > 0) // update
        {
            CancelReasonSave = this.CancelReasonRepository.findById(Dto.getId()).orElseThrow(()->
                    new ObjectNotFoundException(messageSource.getMessage("cancel_reason_notFound",null, LocaleContextHolder.getLocale())));

            modelMapper.map(Dto,CancelReasonSave);
            this.CancelReasonRepository.save(CancelReasonSave);
            response.setMessage(messageSource.getMessage("cancel_reason_create",null, LocaleContextHolder.getLocale()));
        }else
        {
            CancelReasonSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            CancelReasonSave = this.CancelReasonRepository.save(CancelReasonSave);
            response.setMessage(messageSource.getMessage("cancel_reason_update",null, LocaleContextHolder.getLocale()));

        }

        response.setData(modelMapper.map(CancelReasonSave, CancelReasonDto.class));
        response.setStatus(HttpStatus.OK.value());
        log.info("CancelReason saved successfully with ID: {}", CancelReasonSave.getId());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete CancelReason by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<CancelReason> entityDelete = this.CancelReasonRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("cancel_reason_notFound",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.CancelReasonRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("cancel_reason_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** WarehouseDto, service; fetch warehouse by id *");

        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.CancelReasonRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Product with id: %d not found", id))), CancelReasonDto.class));
        response.setMessage(messageSource.getMessage("cancel_reason_successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }
}
