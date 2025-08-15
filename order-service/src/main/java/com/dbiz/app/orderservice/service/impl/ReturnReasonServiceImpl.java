package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.domain.ReturnReason;
import com.dbiz.app.orderservice.repository.ReturnReasonRepository;
import com.dbiz.app.orderservice.repository.TableRepository;
import com.dbiz.app.orderservice.service.ReturnReasonService;
import com.dbiz.app.orderservice.specification.ReturnReasonSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.CancelReasonDto;
import org.common.dbiz.dto.orderDto.ReturnReasonDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ReturnReasonQueryRequest;
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
public class ReturnReasonServiceImpl implements ReturnReasonService {
    private final RequestParamsUtils requestParamsUtils;
    private final ModelMapper modelMapper;
    private final TableRepository tableRepository;
    private final MessageSource messageSource;
    private final ReturnReasonRepository returnReasonRepository;

    @Override
    public GlobalReponsePagination findAll(ReturnReasonQueryRequest request) {
        log.info("*** ReturnReasonDTO List, service; fetch all ReturnReason *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<ReturnReason> spec = ReturnReasonSpecification.getReturnReasonSpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<ReturnReason> returnReasons = returnReasonRepository.findAll( spec,pageable);
        List<ReturnReasonDto> listData = new ArrayList<>();
        for(ReturnReason item : returnReasons.getContent()){
            ReturnReasonDto dto= modelMapper.map(item,ReturnReasonDto.class);
            listData.add(dto);
        }
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(returnReasons.getNumber());
        response.setPageSize(returnReasons.getSize());
        response.setTotalPages(returnReasons.getTotalPages());
        response.setTotalItems(returnReasons.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        GlobalReponse response = new GlobalReponse();
        response.setData(modelMapper.map(this.returnReasonRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("return_reason_notFound",null, LocaleContextHolder.getLocale()))), CancelReasonDto.class));
        response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(ReturnReasonDto Dto) {

        GlobalReponse response = new GlobalReponse();
        ReturnReason returnReason = modelMapper.map(Dto,ReturnReason.class);

        if(returnReason.getId() !=null && returnReason.getId() > 0) // update
        {
            returnReason = this.returnReasonRepository.findById(Dto.getId()).orElseThrow(()->
                    new ObjectNotFoundException(messageSource.getMessage("return_reason_notFound",null, LocaleContextHolder.getLocale())));

            modelMapper.map(Dto,returnReason);
            this.returnReasonRepository.save(returnReason);
            response.setMessage(messageSource.getMessage("return_reason_update",null, LocaleContextHolder.getLocale()));
        }else
        {
            returnReason.setTenantId(AuditContext.getAuditInfo().getTenantId());
            returnReason = this.returnReasonRepository.save(returnReason);
            response.setMessage(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()));

        }

        response.setData(modelMapper.map(returnReason, CancelReasonDto.class));
        response.setStatus(HttpStatus.OK.value());
        log.info("ReturnReason saved successfully with ID: {}", returnReason.getId());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete ReturnReason by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<ReturnReason> entityDelete = this.returnReasonRepository.findById(id);
        if(entityDelete.isEmpty())
        {
            response.setMessage(messageSource.getMessage("return_reason_notFound",null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }

        this.returnReasonRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("return_reason_delete",null, LocaleContextHolder.getLocale()));
        return response;
    }


}
