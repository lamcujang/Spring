package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.*;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.OrgBannerMapper;
import com.dbiz.app.tenantservice.helper.TenantBannerMapper;
import com.dbiz.app.tenantservice.repository.OrgBannerRepository;
import com.dbiz.app.tenantservice.repository.OrgRepository;
import com.dbiz.app.tenantservice.repository.TenantBannerRepository;
import com.dbiz.app.tenantservice.repository.TenantRepository;
import com.dbiz.app.tenantservice.service.OrgBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.OrgBannerDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.dto.tenantDto.TenantBannerDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.OrgBannerQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrgBannerServiceImpl implements OrgBannerService {

    private final OrgBannerRepository orgBannerRepository;

    private final ModelMapper modelMapper;

    private final RestTemplate restTemplate;

    private final TenantBannerRepository tenantBannerRepository;

    private final OrgRepository orgRepository;

    private final OrgBannerMapper orgBannerMapper;

    private final TenantRepository tenantRepository;

    private final EntityManager entityManager;

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(OrgBannerQueryRequest request) {
        return null;
    }

    /**
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(OrgBannerDto entity) {
        log.info("**ServiceImpl::layer -  tenant banner integration  **");
        ImageDto image = null;
        Org org = orgRepository.findById(entity.getOrgId()).get() ;
        TenantAndOrgDto tenantAndOrgDto = tenantRepository.findByCodeTenantAndCodeOrg(org.getTenantId());


//        Tenant
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
            entity.setImageId(image.getId());
        }

        if (entity.getId() != null) {

        } else {

            OrgBanner orgBanner = orgBannerRepository.save(orgBannerMapper.toEntity(entity));
            entity = orgBannerMapper.toDto(orgBanner );
            if (image != null)
                entity.setImage(image);
            return GlobalReponse.builder().data(entity).status(HttpStatus.OK.value()).message("success").build();
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
     * @param orgId
     * @return
     */
    @Override
    public GlobalReponse findByOrgId(Integer orgId) {
        StringBuilder sql ;
        OrgBanner orgBanner = orgBannerRepository.findFirstByOrgIdAndCode(orgId,AppConstant.EMENU_BANNER);
        Org orgG = orgRepository.findById(orgId).get();
        GlobalReponse exRes = null;

        OrgBannerDto orgBannerDto = null;
        if (orgBanner == null) {
            Org org = orgRepository.findById(orgId).get();
            TenantBanner tenant = tenantBannerRepository.findByTenantId(org.getTenantId());
            System.out.println("url : "+AppConstant.DiscoveredDomainsApi.GET_IMAGE_ID + tenant.getImageId());
                exRes = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_IMAGE_ID + tenant.getImageId(), GlobalReponse.class);

            ImageDto image = modelMapper.map(exRes.getData(), ImageDto.class);
            orgBannerDto = OrgBannerDto.builder()
                    .orgId(orgId)
                    .image(image)
                    .orgName(orgG.getName())
                    .build();
        } else {
            orgBannerDto = orgBannerMapper.toDto(orgBanner);
            sql = new StringBuilder("select * from d_image where d_image_id = :imageId");
            List<Tuple> rs = entityManager.createNativeQuery(sql.toString(), Tuple.class)
                    .setParameter("imageId", orgBannerDto.getImageId())
                    .getResultList();
            for(Tuple item: rs)
            {
                ImageDto image = ImageDto.builder()
                        .id(ParseHelper.INT.parse(item.get("d_image_id")))
                        .imageUrl(item.get("image_url", String.class))
                        .imageCode(item.get("image_code", String.class))
                        .isActive(item.get("is_active", String.class))
                        .build();
                orgBannerDto.setImage(image);
            }
            orgBannerDto.setOrgName(orgG.getName());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .errors(null)
                .data(orgBannerDto)
                .build();
    }
}
