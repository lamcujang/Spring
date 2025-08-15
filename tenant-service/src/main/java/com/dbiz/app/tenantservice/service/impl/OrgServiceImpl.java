package com.dbiz.app.tenantservice.service.impl;


import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.Org;
import com.dbiz.app.tenantservice.domain.OrgBanner;
import com.dbiz.app.tenantservice.domain.Tenant;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.repository.OrgBannerRepository;
import com.dbiz.app.tenantservice.repository.OrgRepository;
import com.dbiz.app.tenantservice.repository.PosTerminalRepository;
import com.dbiz.app.tenantservice.repository.TenantRepository;
import com.dbiz.app.tenantservice.service.OrgService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.specification.OrgSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.OrgEmenuDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.BaseQueryRequest;
import org.common.dbiz.request.intergrationRequest.OrgIntDto;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class OrgServiceImpl implements OrgService {

    private final OrgRepository orgRepository;

    private final RequestParamsUtils requestParamsUtils;

    private final ModelMapper modelMapper;

    private final MessageSource messageSource;

    private final EntityManager entityManager;

    private final RestTemplate restTemplate;

    private final TenantRepository tenantRepository;

    private final OrgBannerRepository orgBannerRepository;

    private final PosTerminalRepository posTerminalRepository;

    private final DataSourceContextHolder dataSourceContextHolder;

    @Override
    public GlobalReponsePagination findAll(OrgQueryRequest request) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        StringBuilder sqlImage = new StringBuilder("select image_code,image_url from d_image where d_image_id = :imageId and d_tenant_id = :tenantId");

        Specification<Org> spec = OrgSpecification.getEntitySpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<Org> entityList = orgRepository.findAll(spec, pageable);
        List<OrgDto> listData = new ArrayList<>();

        for (Org item : entityList.getContent()) {

            OrgDto rs = modelMapper.map(item, OrgDto.class);

            if(item.getImageId()!= null)
            {
                List<Map<String,Object>> resultImage = entityManager.createNativeQuery(sqlImage.toString())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("imageId", item.getImageId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                ImageDto image = ImageDto.builder()
                        .imageCode(ParseHelper.STRING.parse(resultImage.get(0).get("image_code")))
                        .imageUrl(ParseHelper.STRING.parse(resultImage.get(0).get("image_url")))
                        .build();
                rs.setImage(image);
            }
            listData.add(rs);
        }

        response.setMessage(messageSource.getMessage("findAll.Org", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setStatus(HttpStatus.OK.value());
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    @Transactional
    public GlobalReponse save(OrgDto org) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("*** Org, service; save Org ***");
        GlobalReponse response = new GlobalReponse();
        Org entitySave = modelMapper.map(org, Org.class);
        OrgDto rs = new OrgDto();

        if (entitySave.getId() != null) // update
        {
            entitySave = this.orgRepository.findById(org.getId()).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));

            modelMapper.map(org, entitySave);
            if (org.getExDate() != null) {
                entitySave.setExDate(DateHelper.toInstantDateAndTime(org.getExDate()));
            }
            if(org.getImage()!= null)
            {
                TenantAndOrgDto tenantAndOrgDto = tenantRepository.findByCodeTenantAndCodeOrg(AuditContext.getAuditInfo().getTenantId());
                org.getImage().setTenantCode(tenantAndOrgDto.getCodeTenant());
                org.getImage().setIndustryCode(tenantAndOrgDto.getCodeIndustry());
                HttpEntity<ImageDto> requestEntity = new HttpEntity<>(org.getImage(), headers);
                GlobalReponse exResponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.SAVE_IMAGE, requestEntity, GlobalReponse.class);
                ImageDto image = modelMapper.map(exResponse.getData(), ImageDto.class);
                entitySave.setImageId(image.getId());
            }
            entitySave =this.orgRepository.save(entitySave);
            rs = modelMapper.map(entitySave, OrgDto.class);
            response.setMessage(messageSource.getMessage("updateOrg", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        } else {

            if (entitySave.getCode() == null || entitySave.getCode().isEmpty()) {
                String code = "CN" + (orgRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth();
                if (entitySave.getCode() == null || entitySave.getCode().isEmpty()) {
                    entitySave.setCode(code);
                }
            }
            if (org.getExDate() != null) {
                entitySave.setExDate(DateHelper.toInstantDateAndTime(org.getExDate()));
            }
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave = this.orgRepository.saveAndFlush(entitySave);
//            this.orgRepository.flush();
//            entitySave = saveFlush(entitySave);
            response.setMessage(messageSource.getMessage("createOrg", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());

            if (org.getRole().equals("ADM")) { // neu la admin thi assign org
                entityManager.createNativeQuery(
                                "INSERT INTO pos.d_userorg_access (d_org_id,d_user_id,d_tenant_id,created_by,updated_by) VALUES (:orgId,:userId,:tenantId,:audit,:audit)")
                        .unwrap(NativeQuery.class)
                        .setParameter("orgId",entitySave.getId())
                        .setParameter("userId",org.getUserId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("audit", 0)
                        .executeUpdate();
            }
            rs = modelMapper.map(entitySave, OrgDto.class);

            if(org.getImage()!=null)
            {
                TenantAndOrgDto tenantAndOrgDto = tenantRepository.findByCodeTenantAndCodeOrg(AuditContext.getAuditInfo().getTenantId());
                org.getImage().setTenantCode(tenantAndOrgDto.getCodeTenant());
                org.getImage().setIndustryCode(tenantAndOrgDto.getCodeIndustry());
                org.getImage().setImageCode("");
                HttpEntity<ImageDto> requestEntity = new HttpEntity<>(org.getImage(), headers);
                GlobalReponse exResponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.SAVE_IMAGE, requestEntity, GlobalReponse.class);
                ImageDto image = modelMapper.map(exResponse.getData(), ImageDto.class);
                entitySave.setImageId(image.getId());
                this.orgRepository.save(entitySave);
                rs.setImage(image);
            }

        }

        response.setData(rs);

        log.info("Org saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    @Transactional
    public GlobalReponse deleteById(Integer integer) {
        log.info("*** Org, service; delete Org by id ***");
        GlobalReponse reponse = new GlobalReponse();
        Org entity = this.orgRepository.findById(integer).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));
        this.orgRepository.delete(entity);
        reponse.setMessage(messageSource.getMessage("deleteOrg", null, LocaleContextHolder.getLocale()));
        reponse.setStatus(HttpStatus.OK.value());
        return reponse;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        StringBuilder sqlImage = new StringBuilder("select image_code,image_url from d_image where d_image_id = :imageId and d_tenant_id = :tenantId");
        log.info("*** Org, service; fetch Org by id ***");
        GlobalReponse response = new GlobalReponse();
        Org entity = this.orgRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));
        OrgDto dto = modelMapper.map(entity, OrgDto.class);
        Tenant tenant = tenantRepository.findById(entity.getTenantId()).get();
        dto.setUrl(tenant.getDomainUrl());
        if(entity.getImageId()!= null)
        {
            List<Map<String,Object>> resultImage = entityManager.createNativeQuery(sqlImage.toString())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("imageId", entity.getImageId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            ImageDto image = ImageDto.builder()
                    .imageCode(ParseHelper.STRING.parse(resultImage.get(0).get("image_code")))
                    .imageUrl(ParseHelper.STRING.parse(resultImage.get(0).get("image_url")))
                    .build();
            dto.setImage(image);
        }
        response.setData(dto);
        response.setMessage(messageSource.getMessage("findOrg", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());

        return response;
    }

    @Override
    @Transactional
    public GlobalReponse integrationOrg(OrgIntDto param) {
        log.info("*** Org, service; integration Org ***");
        List<Org> listOrgAll = this.orgRepository.findByErpOrgIdAndTenantId(0,AuditContext.getAuditInfo().getTenantId());
        listOrgAll.forEach(org -> {
            if(!org.getCode().equals("*"))
                org.setErpOrgId(null);
        });
        this.orgRepository.saveAll(listOrgAll);
        param.getListOrgInt().forEach(orgDto -> {
            Org entity = null;
            if(param.getType().equals(AppConstant.ERP_PLATFORM_IDEMPIERE))
                entity= orgRepository.findByErpOrgId(orgDto.getErpOrgId()).orElse(null);
            else if(param.getType().equals(AppConstant.ERP_PLATFORM_ERPNEXT))
                entity = orgRepository.findByErpOrgName(orgDto.getErpOrgName()).orElse(null);

           if (entity != null) {
                modelMapper.map(orgDto, entity);
                this.orgRepository.save(entity);
            } else {
                entity = modelMapper.map(orgDto, Org.class);
                entity.setTenantId(AuditContext.getAuditInfo().getTenantId());
                entity.setIsPosMng("N");
                this.orgRepository.save(entity);
            }

            modelMapper.map(entity, orgDto);
        });

        return GlobalReponse.builder()
                .message(messageSource.getMessage("integration_success", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .data( param.getListOrgInt())
                .build();
    }

    @Override
    public GlobalReponse findByOrgErp(Integer orgErpId) {
        log.info("*** Org, service; fetch Org by erpId ***");
        Org entity = this.orgRepository.findByErpOrgId(orgErpId).orElse(null);
        GlobalReponse response = new GlobalReponse();
        if (entity == null) {
            response.setMessage(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("findOrg", null, LocaleContextHolder.getLocale()))
                .data(modelMapper.map(entity, OrgDto.class))
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse findByOrgAccess(Integer userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        GlobalReponse exService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_URL_GET_ORG_ACCESS + "/" + userId, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
        List<OrgDto> dataResponse = new ArrayList<>();

        if (exService != null && exService.getData() != null) {
            if (exService.getData() instanceof Integer[]) {
                Integer[] orgIds = (Integer[]) exService.getData();
                Arrays.stream(orgIds).collect(Collectors.toList()).forEach(
                        item -> {
                            Org entity = this.orgRepository.findById(item).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));
                            dataResponse.add(modelMapper.map(entity, OrgDto.class));
                        }
                );
            } else if (exService.getData() instanceof List) {
                List<Integer> orgIdList = (List<Integer>) exService.getData();
                Integer[] orgIds = orgIdList.toArray(new Integer[0]);
                Arrays.stream(orgIds).collect(Collectors.toList()).forEach(
                        item -> {
                            Org entity = this.orgRepository.findById(item).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));
                            dataResponse.add(modelMapper.map(entity, OrgDto.class));
                        }
                );
            } else {
                throw new IllegalArgumentException("Dữ liệu không phải là kiểu Integer[] hoặc List<Integer>");
            }
        } else {
            throw new NullPointerException("Dữ liệu trả về từ dịch vụ là null hoặc không hợp lệ");
        }


        return GlobalReponse.builder()
                .data(dataResponse)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     *
     * @param orgId
     * @return
     */
    @Override
    public GlobalReponse getOrgEmenu(Integer orgId) {
        log.info("*** getOrg E-menu ***");
        OrgEmenuDto orgEmenuDto = new OrgEmenuDto();

        Org entity = this.orgRepository.findById(orgId).orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("notFoundOrg", null, LocaleContextHolder.getLocale())));
        orgEmenuDto = modelMapper.map(entity, OrgEmenuDto.class);

        dataSourceContextHolder.setCurrentTenantId(null);
        String sqlEmenu = " select value from d_config where name = :name and d_tenant_id = :tenantId";
        List<Map<String,Object>> resultEmenu = entityManager.createNativeQuery(sqlEmenu)
                .setParameter("tenantId", 0)
                .setParameter("name", "D_EMENU_URL")
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        String linkEmenu = ParseHelper.STRING.parse(resultEmenu.get(0).get("value"));
        orgEmenuDto.setUrlEmenu(linkEmenu);
        dataSourceContextHolder.setCurrentTenantId(new Long(AuditContext.getAuditInfo().getMainTenantId()));

        List<ImageDto> listImage = new ArrayList<>();

        String sqlGetImage = "select di.image_code as image_code , di.image_url as image_url from d_org_banner dob join d_image di on dob.d_image_id = di.d_image_id\n" +
                "where dob.d_org_id = :orgId and dob.name = :name and dob.is_active = 'Y' ";
        List<Tuple> resultImage = entityManager.createNativeQuery(sqlGetImage, Tuple.class)
                .setParameter("orgId", orgId)
                .setParameter("name", AppConstant.EMENU_BANNER)
                .getResultList();
        for (Tuple  item : resultImage) {
            ImageDto image = ImageDto.builder()
                    .imageCode(item.get("image_code",String.class))
                    .imageUrl(item.get("image_url",String.class))
                    .build();
            listImage.add(image);
        }
        orgEmenuDto.setImageEmenu(listImage);
        return GlobalReponse.builder()
                .data(orgEmenuDto)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse saveOrgEmenu(OrgEmenuDto request) {
        log.info("*** saveOrg E-menu ***");
        List<ImageDto> imageSave = new ArrayList<>();
        Org org = orgRepository.findById(request.getId()).get() ;
        modelMapper.map(request,org);
        orgRepository.save(org);
        TenantAndOrgDto tenantAndOrgDto = tenantRepository.findByCodeTenantAndCodeOrg(org.getTenantId());
        if(request.getImageEmenu()!= null)
        {
            request.getImageEmenu().forEach(item ->{
                if (item.getImage64() != null) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                    headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                    headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                    headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                    headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
                    headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    item.setTenantCode(tenantAndOrgDto.getCodeTenant());
                    item.setIndustryCode(tenantAndOrgDto.getCodeIndustry());
                    HttpEntity<ImageDto> requestEntity = new HttpEntity<>(item, headers);
                    GlobalReponse exResponse = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.SAVE_IMAGE, requestEntity, GlobalReponse.class);
                    ImageDto image = modelMapper.map(exResponse.getData(), ImageDto.class);
                    imageSave.add(image);
                    if(item.getImageCode() == null || item.getImageCode().isEmpty())
                    {
                        OrgBanner orgBanner = new OrgBanner();
                        orgBanner.setOrgId(request.getId());
                        orgBanner.setImageId(image.getId());
                        orgBanner.setName(AppConstant.EMENU_BANNER);
                        orgBanner.setCode(AppConstant.EMENU_BANNER);
                        orgBannerRepository.save(orgBanner);
                    }
                }
                if ( item.getImageCode()!= null && !item.getImageCode().isEmpty() && item.getIsActive() .equals("N")) {
                   List<Tuple> getImage = entityManager.createNativeQuery("select di.d_image_id as d_image_id from d_org_banner dob join d_image di on dob.d_image_id = di.d_image_id\n" +
                                   "                where dob.d_org_id = :orgId and    di.image_code = :imageCode", Tuple.class)
                            .setParameter("imageCode", item.getImageCode().toString())
                            .setParameter("orgId", request.getId())
                            .getResultList();
                    if(getImage != null && !getImage.isEmpty())
                    {
                        List<OrgBanner> orgUpdate = orgBannerRepository.findByImageIdAndOrgId(Integer.valueOf(getImage.get(0).get("d_image_id").toString()), request.getId());
                        if (orgUpdate != null && !orgUpdate.isEmpty()) {
                            orgUpdate.forEach(orgBanner -> {
                                orgBanner.setIsActive("N");
                                orgBannerRepository.save(orgBanner);
                            });
                        }
                    }
                }
            });
        }
        request.setImageEmenu(imageSave);
        return GlobalReponse.builder()
                .data(request)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     *
     * @param orgDtos
     * @return
     */


//    @Transactional
//    public Org saveFlush(Org orgDto)
//    {
//        return this.orgRepository.saveAndFlush(modelMapper.map(orgDto, Org.class));
//    }
}
