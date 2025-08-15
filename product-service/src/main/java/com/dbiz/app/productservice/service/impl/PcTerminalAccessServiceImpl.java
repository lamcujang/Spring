package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.PcTerminalAccess;
import com.dbiz.app.productservice.domain.ProductCategory;
import com.dbiz.app.productservice.repository.PcTerminalAccessRepository;
import com.dbiz.app.productservice.repository.ProductCategoryRepository;
import com.dbiz.app.productservice.service.PcTerminalAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.PcTerminalAccessQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PcTerminalAccessServiceImpl implements PcTerminalAccessService {

    private final PcTerminalAccessRepository pcTerminalAccessRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final RestTemplate restTemplate;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;
    @Override
    public GlobalReponsePagination findAll(PcTerminalAccessQueryRequest request) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse save(PcTerminalAccessDto entity) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse intSave(List<PcTerminalAccessDto> listDto) {
        listDto.forEach(item-> {
            GlobalReponse exReponse =  restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ERP_ID+"/"+item.getOrgId(), GlobalReponse.class);
            if(exReponse.getData()== null)
                throw new PosException(messageSource.getMessage("org.not.found",null, LocaleContextHolder.getLocale()));
            OrgDto orgDto = modelMapper.map( exReponse.getData(), OrgDto.class);

            exReponse = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_POSTERMINAL_BY_ERP_ID+"/"+item.getPosTerminalId(), GlobalReponse.class);
            if(exReponse.getData()== null)
                throw new PosException(messageSource.getMessage("posterminal.not.found",null, LocaleContextHolder.getLocale()));
            PosTerminalDto posTerminalDto = modelMapper.map( exReponse.getData(), PosTerminalDto.class);

            ProductCategory productCategory = productCategoryRepository.findByErpProductCategoryId(item.getProductCategoryId());

            PcTerminalAccess entity = pcTerminalAccessRepository.findByProductCategoryIdAndPosTerminalId(item.getProductCategoryId(), item.getPosTerminalId());
        });
        // luu list
        return null;
    }
}
