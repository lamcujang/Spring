package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.TenantBanner;
import com.dbiz.app.tenantservice.repository.TenantBannerRepository;
import com.dbiz.app.tenantservice.repository.TenantRepository;
import com.dbiz.app.tenantservice.service.TenantBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.TenantBannerQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantBannerServiceImpl implements TenantBannerService {
    private final TenantRepository tenantRepository;

    private final RestTemplate restTemplate;

    private final TenantBannerRepository tenantBannerRepository;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(TenantBannerQueryRequest request) {
        return null;
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        TenantBannerDto tenantBannerDto = modelMapper.map(tenantBannerRepository.findById(integer).orElseThrow(() -> new PosException("Tenant banner not found")), TenantBannerDto.class);
        GlobalReponse exRes = null;
        if (tenantBannerDto.getImageId() != null) {
            exRes = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_IMAGE_ID + tenantBannerDto.getImageId(), GlobalReponse.class);
        }
        ImageDto image = modelMapper.map(exRes.getData(), ImageDto.class);
        tenantBannerDto.setImage(image);

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .errors(null)
                .data(tenantBannerDto)
                .build();
    }

    /**
     * @param entity
     * @return
     */

    @Override
    public GlobalReponse save(TenantBannerDto entity) {
        log.info("**ServiceImpl::layer -  tenant banner integration  **");
        ImageDto image = null;
        TenantAndOrgDto tenantAndOrgDto = tenantRepository.findByCodeTenantAndCodeOrg(entity.getTenantId());
        if (entity.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            entity.getImage().setTenantCode(tenantAndOrgDto.getCodeTenant());
            entity.getImage().setIndustryCode(tenantAndOrgDto.getCodeIndustry());
            HttpEntity<ImageDto> requestEntity = new HttpEntity<>(entity.getImage(), headers);
            GlobalReponse exResponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.SAVE_IMAGE, requestEntity, GlobalReponse.class);
            image = modelMapper.map(exResponse.getData(), ImageDto.class);
        }

        if (entity.getId() != null) {

        } else {
            if (image != null)
                entity.setImage(image);
                entity.setImageId(image.getId());
            TenantBanner tenantBanner = tenantBannerRepository.save(modelMapper.map(entity, TenantBanner.class));
            return GlobalReponse.builder()
                    .status(HttpStatus.OK.value())
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .data(modelMapper.map(tenantBanner, TenantBannerDto.class)).build();
        }


        return null;
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    /**
     * @param tenantId
     * @return
     */
    @Override
    public GlobalReponse findByTenantId(Integer tenantId) {

        TenantBannerDto tenantBannerDto = modelMapper.map(tenantBannerRepository.findByTenantId(tenantId), TenantBannerDto.class);
        GlobalReponse exRes = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_IMAGE_ID + tenantBannerDto.getImageId(), GlobalReponse.class);
        ImageDto image = modelMapper.map(exRes.getData(), ImageDto.class);
        tenantBannerDto.setImage(image);
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(tenantBannerDto)
                .build();
    }
}
