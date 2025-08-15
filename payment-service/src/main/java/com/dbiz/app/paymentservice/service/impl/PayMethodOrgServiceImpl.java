package com.dbiz.app.paymentservice.service.impl;


import com.dbiz.app.paymentservice.domain.PayMethodOrg;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.paymentDto.PayMethodOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PayMethodOrgQueryRequest;
import com.dbiz.app.paymentservice.repository.PayMethodOrgRepository;
import com.dbiz.app.paymentservice.service.PayMethodOrgService;
import com.dbiz.app.paymentservice.specification.PayMethodOrgSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PayMethodOrgServiceImpl implements PayMethodOrgService {
    private final MessageSource messageSource;

    private final PayMethodOrgRepository payMethodRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    @Override
    public GlobalReponsePagination findAll(PayMethodOrgQueryRequest request) {
        Specification<PayMethodOrg> spec = PayMethodOrgSpecification.getSpecification(request);
        Pageable page = requestParamsUtils.getPageRequest(request);
        Page<PayMethodOrg> payMethodOrgs = payMethodRepository.findAll(spec, page);
        GlobalReponsePagination res = new GlobalReponsePagination();
        res.setData(payMethodOrgs.getContent().stream().map(item -> modelMapper.map(item, PayMethodOrgDto.class)).collect(Collectors.toList()));
        res.setPageSize(payMethodOrgs.getSize());
        res.setTotalPages(payMethodOrgs.getTotalPages());
        res.setCurrentPage(payMethodOrgs.getNumber());
        res.setTotalItems(payMethodOrgs.getTotalElements());
        res.setMessage("fetch pay method success");
        return res;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(PayMethodOrgDto entity) {
        GlobalReponse response = new GlobalReponse();
        PayMethodOrg payMethodOrg =payMethodRepository.save( modelMapper.map(entity, PayMethodOrg.class));
        payMethodOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
        entity = modelMapper.map(payMethodOrg, PayMethodOrgDto.class);
        response.setData(entity);
        response.setMessage("save pay method success");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

}
