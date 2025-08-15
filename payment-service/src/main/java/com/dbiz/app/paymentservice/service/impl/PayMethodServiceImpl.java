package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.domain.PayMethod;
import com.dbiz.app.paymentservice.domain.PayMethodOrg;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.dto.paymentDto.PayMethodAndOrgDto;
import org.common.dbiz.dto.paymentDto.PayMethodDto;
import org.common.dbiz.dto.paymentDto.PayMethodOrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import com.dbiz.app.paymentservice.repository.PayMethodOrgRepository;
import com.dbiz.app.paymentservice.repository.PayMethodRepository;
import com.dbiz.app.paymentservice.service.PayMethodService;
import com.dbiz.app.paymentservice.specification.PayMethodSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PayMethodServiceImpl implements PayMethodService {
    private final MessageSource messageSource;

    private final PayMethodRepository payMethodRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final PayMethodOrgRepository
            payMethodOrgRepository;

    @Override
    public GlobalReponsePagination findAll(PayMethodQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setData(new ArrayList<>());
        Specification<PayMethod> spec = PayMethodSpecification.getSpecification(request);
        Pageable page = requestParamsUtils.getPageRequest(request);
        Page<PayMethod> payMethods = payMethodRepository.findAll(spec, page);
        List<PayMethodOrg> payMethodOrgs = payMethodOrgRepository.findAllByOrgId(request.getOrgId());
        List<PayMethodDto> list = new ArrayList<>();
        if(payMethodOrgs != null)
        {
            for(PayMethodOrg payMethodOrg : payMethodOrgs){
                PayMethod method = payMethodRepository.findById(payMethodOrg.getPayMethodId()).orElse(null);
                PayMethodDto itemDto = modelMapper.map(method, PayMethodDto.class);
                PayMethodOrgDto subItemDto = modelMapper.map(payMethodOrg, PayMethodOrgDto.class);
                subItemDto.setMerchantCode(payMethodOrg.getMerchantCode());
                itemDto.setPayMethodOrgDto(subItemDto);
                list.add(itemDto);
            }
            response.setData(list);
        }
        else{
            if(!payMethods.isEmpty())
            {
                payMethods.getContent().forEach(item -> {
                    PayMethodDto itemDto = modelMapper.map(item, PayMethodDto.class);
                    list.add(itemDto);
                });
            }
//            PayMethod method = payMethodRepository.findById(payMethodOrgs.getPayMethodId()).orElse(null);
//            PayMethodDto itemDto = modelMapper.map(method, PayMethodDto.class);
//            list.add(itemDto);
            response.setData(list);
        }

//        List<PayMethodDto> payMethodDtos = payMethods.getContent().stream().map(item ->
//        {
//            PayMethodDto itemDto = modelMapper.map(item, PayMethodDto.class);
//            PayMethodOrg payMethodOrg = payMethodOrgRepository.findByPayMethodId(item.getId()).orElse(null);
//            if (payMethodOrg != null) {
//                PayMethodOrgDto subItemDto = modelMapper.map(payMethodOrg, PayMethodOrgDto.class);
//                itemDto.setPayMethodOrgDto(subItemDto);
//            }
//            return itemDto;
//        }).collect(Collectors.toList());
        response.setTotalItems(payMethods.getTotalElements());
        response.setTotalPages(payMethods.getTotalPages());
        response.setCurrentPage(payMethods.getNumber());
        response.setPageSize(payMethods.getSize());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(PayMethodDto entity) {
        GlobalReponse response = new GlobalReponse();
        PayMethod payMethod = payMethodRepository.findByBankId(entity.getBankId());
        PayMethodOrg payOrg =null;
        if(payMethod == null)
        {
            payMethod =  modelMapper.map(entity, PayMethod.class);
            payMethod.setOrgId(0);
            payMethod.setTenantId(AuditContext.getAuditInfo().getTenantId());
            payMethod = payMethodRepository.save(payMethod);
        }
        Optional<PayMethodOrg> paycheck = payMethodOrgRepository.findByOrgIdAndPayMethodName(AuditContext.getAuditInfo().getTenantId(),
                entity.getOrgId(), payMethod.getName());
        if(paycheck.isEmpty())
        {
            payOrg=modelMapper.map(entity.getPayMethodOrgDto(), PayMethodOrg.class);
            payOrg.setPayMethodId(payMethod.getId());
            payOrg.setTenantId(0);
            payOrg.setOrgId(entity.getOrgId());
//            payOrg.setMerchantCode(entity.getPayMethodOrgDto().getMerchantCode());
//            payOrg.setAccessCode(entity.getName());
            payMethodOrgRepository.save(payOrg);
        }
        else
        {
            PayMethodOrg payOrgSave = paycheck.get();
            payOrg = modelMapper.map(entity.getPayMethodOrgDto(), PayMethodOrg.class);
            modelMapper.map(entity.getPayMethodOrgDto(), payOrgSave);
            payOrgSave.setPayMethodId(payMethod.getId());
            this.payMethodOrgRepository.save(payOrgSave);
        }

        entity = modelMapper.map(payMethod, PayMethodDto.class);
        entity.setPayMethodOrgDto(modelMapper.map(payOrg, PayMethodOrgDto.class));

        response.setData(entity);
        response.setMessage("PayMethod saved successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse savePayMethod(PayMethodAndOrgDto dto) {

//        PayMethod payMethod = modelMapper.map(dto, PayMethod.class);
//        payMethodRepository.save(payMethod);
//
//
//        PayMethodOrg payMethodOrg = PayMethodOrg.builder()
//                .hashKey(dto.getPayMethodOrgDto().getHashKey())
//                .payMethodId(payMethod.getId())
//                .terminalId(dto.getPayMethodOrgDto().getTerminalId())
//                .accessCode(dto.getPayMethodOrgDto().getAccessCode())
//                .merchantCode(dto.getPayMethodOrgDto().getMerchantCode())
//                .build();
//        payMethodOrgRepository.save(payMethodOrg);
//        entity = modelMapper.map(payMethod, PayMethodDto.class);

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("payment.method.setup", null, LocaleContextHolder.getLocale()))
                .data(dto)
                .errors("")
                .build();
    }
}
