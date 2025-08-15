package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.domain.Uom;
import com.dbiz.app.productservice.repository.UomRepository;
import com.dbiz.app.productservice.service.UomService;
import com.dbiz.app.productservice.specification.UomSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.UomQueryRequest;
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
public class UomServiceImpl implements UomService{
    private final UomRepository uomRepository;
    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;
    private final MessageSource messageSource;
    @Override
    public GlobalReponsePagination  findAll(UomQueryRequest  uomQueryRequest) {
        log.info("*** Uom List, service; fetch all products *");
        Pageable pageable = requestParamsUtils.getPageRequest(uomQueryRequest);
        Specification<Uom> spec = UomSpecification.getUomSpecification(uomQueryRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Uom> uomPage = uomRepository.findAll( spec,pageable);
        List<UomDto > listData = new ArrayList<>();
        for(Uom item : uomPage.getContent()){
            listData.add(modelMapper.map(item, UomDto.class));
        }
        response.setMessage(messageSource.getMessage("uom.fetch.all",null, LocaleContextHolder.getLocale() ));
        response.setData(listData);
        response.setCurrentPage(uomPage.getNumber());
        response.setPageSize(uomPage.getSize());
        response.setTotalPages(uomPage.getTotalPages());
        response.setTotalItems(uomPage.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(UomDto uomDto) {
        log.info("*** Uom service impl, save uom dto *");
        GlobalReponse result = new GlobalReponse();
        Uom uom = null;
        if(uomDto.getId() != null){
             uom = uomRepository.findById(uomDto.getId()).orElseThrow(()->new ObjectNotFoundException("Uom not found"));
            modelMapper.map(uomDto, uom);
            uomRepository.save(uom);
            result.setMessage(messageSource.getMessage("uom.updated",null, LocaleContextHolder.getLocale() ));
            result.setStatus(HttpStatus.OK.value());
            result.setData(modelMapper.map(uom, UomDto.class));
        }else {
            if(uomDto.getName()!= null)
            {
                uom = uomRepository.findByNameAndTenantId(uomDto.getName(),  AuditContext.getAuditInfo().getTenantId()).orElse(null);
            } else if (uomDto.getCode()!=null) {
                uom = uomRepository.findByCodeAndTenantId(uomDto.getCode(), AuditContext.getAuditInfo().getTenantId()).orElse(null);
            }

            if(uom != null){
                result.setStatus(HttpStatus.CREATED.value());
                result.setMessage(messageSource.getMessage("uom.created",null, LocaleContextHolder.getLocale() ));
                result.setData(modelMapper.map(uom, UomDto.class));
                return result;
            }
            uom = modelMapper.map(uomDto, Uom.class);
            uom.setOrgId(0);
            uom.setTenantId(AuditContext.getAuditInfo().getTenantId());
            uomRepository.save(uom);
            result.setStatus(HttpStatus.CREATED.value());
            result.setMessage(messageSource.getMessage("uom.created",null, LocaleContextHolder.getLocale() ));
            result.setData(modelMapper.map(uom, UomDto.class));
        }
        result.setData(modelMapper.map(uom, UomDto.class));

        return result;
    }


    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Uom service impl, update uom dto *");
        GlobalReponse result = new GlobalReponse();
        Uom uom = uomRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("uom.not.found",null, LocaleContextHolder.getLocale()),id)));

        uomRepository.delete(uom);
        result.setMessage(messageSource.getMessage("uom.deleted",null, LocaleContextHolder.getLocale() ));
        result.setData("");

        return result;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** Uom service impl, find uom by id *");
        GlobalReponse result = new GlobalReponse();
        Uom uom = uomRepository.findById(id).orElseThrow(()->new ObjectNotFoundException(String.format(messageSource.getMessage("uom.not.found",null, LocaleContextHolder.getLocale()),id)));
        result.setMessage("Uom with id: "+id+" fetched successfully");
        result.setData(modelMapper.map(uom, UomDto.class));
        return result;
    }

    /**
     * @param uomDto
     * @return
     */
    @Override
    public GlobalReponse intSave(UomDto uomDto) {
        log.info("*** Uom service impl, save uom dto *");


        Uom uom = uomRepository.findByNameAndTenantId(uomDto.getName(),  AuditContext.getAuditInfo().getTenantId()).orElse(null);
        if(uom != null)
        {
            uom.setErpUomId(uomDto.getErpUomId());
            uom.setCode(uomDto.getCode());
            uom.setName(uomDto.getName());
            uomRepository.save(uom);
            uomDto = modelMapper.map(uom, UomDto.class);
        }
        else{
            uom = modelMapper.map(uomDto, Uom.class);
            uom.setTenantId(AuditContext.getAuditInfo().getTenantId());
            uom.setOrgId(0);
            uomRepository.save(uom);
            uomDto = modelMapper.map(uom, UomDto.class);
        }
        return GlobalReponse.builder().data(uomDto).status(HttpStatus.OK.value()).message("Uom saved successfully").build();
    }
}
