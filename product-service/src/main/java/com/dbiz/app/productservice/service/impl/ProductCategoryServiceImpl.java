package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.Icon;
import com.dbiz.app.productservice.domain.Image;
import com.dbiz.app.productservice.domain.PcTerminalAccess;
import com.dbiz.app.productservice.domain.ProductCategory;

import com.dbiz.app.productservice.helper.Mapper.ProductCategoryMapper;
import com.dbiz.app.productservice.repository.IconRepository;
import com.dbiz.app.productservice.repository.ImageRepository;
import com.dbiz.app.productservice.repository.PcTerminalAccessRepository;
import com.dbiz.app.productservice.repository.ProductCategoryRepository;
import com.dbiz.app.productservice.service.ProductCategoryService;
import com.dbiz.app.productservice.specification.CategoryProductSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.domain.PosTerminal;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.repository.PosTerminalRepository;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.integrationDto.IntegrationProductDto;
import org.common.dbiz.dto.integrationDto.ProductCatIntDto;
import org.common.dbiz.dto.integrationDto.ProductCategoryKafkaDto;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.productDto.otherServiceDto.PcERequestVDto;
import org.common.dbiz.dto.productDto.request.ProductCategoryReq;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.externalRequest.SaveImageMDMRequest;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ImageRepository imageRepository;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper categoryMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final IconRepository iconRepository;

    private final MessageSource messageSource;

    private final PcTerminalAccessRepository pcTerminalAccessRepository;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    private final QueryEngine queryEngine;

    private final PosTerminalRepository posTerminalRepository;


    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";
    private final static String TOPIC_SEND_PCG = "sync-integration-to-product-category";

    private final static String TOPIC_RECEIVE_PCG = "sync-product-category-to-integration";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public GlobalReponsePagination findAll(ProductCategoryQueryRequest paramRequest) {
        log.info("*** ProductDto List, service; fetch all products *");

        Specification<ProductCategory> spec = CategoryProductSpecification.getEntitySpecification(paramRequest);
        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();

        if (paramRequest.getPosTerminalId() != null) {
            List<Tuple> resultOrg = entityManager.createNativeQuery("select is_pos_mng,d_pricelist_id from d_org where d_org_id = :orgId", Tuple.class)
                    .setParameter("orgId", paramRequest.getOrgId())
                    .getResultList();
            if (!resultOrg.isEmpty()) {
                if (resultOrg.get(0).get("is_pos_mng", String.class).equals("Y")) {
                    StringBuilder slqCheck = new StringBuilder("select d_product_category_id from d_pc_terminalaccess where d_pos_terminal_id = :posTerminalId and d_tenant_id=:tenantId and d_org_id = :orgId and is_active = 'Y'");
                    List<BigDecimal> resultList = entityManager.createNativeQuery(slqCheck.toString())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("posTerminalId", paramRequest.getPosTerminalId().intValue())
                            .setParameter("orgId", paramRequest.getOrgId())
                            .getResultList();
                    spec = spec.and(CategoryProductSpecification.inProductCategoryId(resultList.stream().map(BigDecimal::intValue).collect(Collectors.toList())));
                } else {
                    StringBuilder slqCheck = new StringBuilder("select d_product_category_id from d_pc_terminalaccess where d_tenant_id=:tenantId and d_org_id = :orgId and is_active = 'Y'");
                    List<BigDecimal> resultList = entityManager.createNativeQuery(slqCheck.toString())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("orgId", paramRequest.getOrgId())
                            .getResultList();
                    spec = spec.and(CategoryProductSpecification.inProductCategoryId(resultList.stream().map(BigDecimal::intValue).collect(Collectors.toList())));
                }
            }

//            StringBuilder slqCheck = new StringBuilder("select coalesce(d_pos_terminal_id, 0 ) from d_pos_terminal where d_pos_terminal_id = :posterminalId and is_default = 'Y' and  d_tenant_id = :tenantId");
//            List<BigDecimal> resultList = entityManager.createNativeQuery(slqCheck.toString())
//                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                    .setParameter("posterminalId", paramRequest.getPosTerminalId())
//                    .getResultList();
//            BigDecimal posterminalCheck = resultList.isEmpty() ? BigDecimal.ZERO : resultList.get(0);
//            if (posterminalCheck.compareTo(BigDecimal.ZERO) == 0) {
//                slqCheck = new StringBuilder("select d_product_category_id from d_pc_terminalaccess where d_pos_terminal_id = :posTerminalId and d_tenant_id=:tenantId and d_org_id = :orgId and is_active = 'Y'");
//                resultList = entityManager.createNativeQuery(slqCheck.toString())
//                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                        .setParameter("posTerminalId", paramRequest.getPosTerminalId().intValue())
//                        .setParameter("orgId", paramRequest.getOrgId())
//                        .getResultList();
//                spec = spec.and(CategoryProductSpecification.inProductCategoryId(resultList.stream().map(BigDecimal::intValue).collect(Collectors.toList())));
//            }
        }

        Page<ProductCategory> products = productCategoryRepository.findAll(spec, pageable);
        List<ProductCategoryDto> listData = new ArrayList<>();
        for (ProductCategory item : products.getContent()) {
            ProductCategoryDto productCategoryDto = categoryMapper.toProductCategoryDto(item);
            if (item.getProductCategoryParentId() != null && item.getProductCategoryParentId() > 0) {
                Optional<ProductCategory> parent = productCategoryRepository.findById(item.getProductCategoryParentId());
                productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
            }
            if (item.getImageId() != null) {
                Image image = this.imageRepository.findById(item.getImageId()).orElse(new Image());
                productCategoryDto.setImageDto(modelMapper.map(image, ImageDto.class));
            }

            if (item.getIconId() != null) {
                Icon icon = this.iconRepository.findById(item.getIconId()).orElse(new Icon());
                productCategoryDto.setIconDto(modelMapper.map(icon, IconDto.class));
            }

            List<PcTerminalAccess> listPcTerminalAccess = this.pcTerminalAccessRepository.findAllByProductCategoryId(item.getId());
            List<PcTerminalAccessDto> pcTerminalAccessDtos = listPcTerminalAccess.stream()
                    .map(pcTerminalAccess -> modelMapper.map(pcTerminalAccess, PcTerminalAccessDto.class))
                    .collect(Collectors.toList());
            productCategoryDto.setPcTerminalAccesses(pcTerminalAccessDtos);

            listData.add(productCategoryDto);
        }
        response.setMessage(messageSource.getMessage("product.category.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());

        return response;
    }


    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** product category service impl,  product category dto *");
        GlobalReponse result = new GlobalReponse();
        ProductCategory entity = productCategoryRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.category.not.found", null, LocaleContextHolder.getLocale()), id)));
        if (isProductCategoryHasChild(id)) {
            throw new PosException(String.format(messageSource.getMessage("product.category.has.child", null, LocaleContextHolder.getLocale()), id));
        }
        try {
            pcTerminalAccessRepository.deleteAllByProductCategoryId(id);
            productCategoryRepository.delete(entity);
            productCategoryRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(String.format(messageSource.getMessage("product.category.delete.error", null, LocaleContextHolder.getLocale()), id));
        }
        result.setMessage(String.format(messageSource.getMessage("product.category.deleted", null, LocaleContextHolder.getLocale()), id));
        result.setStatus(HttpStatus.OK.value());
        result.setData("");

        return result;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** product category service impl, find product category by id *");
        GlobalReponse result = new GlobalReponse();
        ProductCategory entity = productCategoryRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.category.not.found", null, LocaleContextHolder.getLocale()), id)));
        ProductCategoryDto productCategoryDto = categoryMapper.toProductCategoryDto(entity);

        if (entity.getProductCategoryParentId() != null) {
            Optional<ProductCategory> parent = productCategoryRepository.findById(entity.getProductCategoryParentId());
            productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
        }

        if (entity.getImageId() != null) {
            Image image = this.imageRepository.findById(entity.getImageId()).orElse(new Image());
            productCategoryDto.setImageDto(modelMapper.map(image, ImageDto.class));
        }

        if (entity.getIconId() != null) {
            Icon icon = this.iconRepository.findById(entity.getIconId()).orElse(new Icon());
            productCategoryDto.setIconDto(modelMapper.map(icon, IconDto.class));
        }

        List<PcTerminalAccess> listPcTerminalAccess = this.pcTerminalAccessRepository.findAllByProductCategoryId(entity.getId());
        List<PcTerminalAccessDto> pcTerminalAccessDtos = listPcTerminalAccess.stream()
                .map(pcTerminalAccess -> modelMapper.map(pcTerminalAccess, PcTerminalAccessDto.class))
                .collect(Collectors.toList());
        productCategoryDto.setPcTerminalAccesses(pcTerminalAccessDtos);

        result.setMessage(messageSource.getMessage("product.category.fetch.success", null, LocaleContextHolder.getLocale()));
        result.setData(productCategoryDto);
        return result;
    }

    @Override
    public GlobalReponse save(ProductCategoryDto saveCategoryDto) {
        log.info("*** Save Category *");

        GlobalReponse response = new GlobalReponse();
        ProductCategory entitySave = categoryMapper.toProductCategory(saveCategoryDto);
        ProductCategoryDto productCategoryDto = null;
        Image imageSave = null;
        IconDto iconDto = null;
        Integer maxId = productCategoryRepository.getMaxId();
        if (saveCategoryDto.getId() != null) // update
        {
            entitySave = this.productCategoryRepository.findById(saveCategoryDto.getId()).orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.category.not.found", null, LocaleContextHolder.getLocale()), saveCategoryDto.getId())));
            if (saveCategoryDto.getOrgId() != null) {
                entitySave.setOrgId(saveCategoryDto.getOrgId());
            }
            entitySave = categoryMapper.updateEntity(saveCategoryDto, entitySave);
            if (saveCategoryDto.getImageDto() != null) {
                imageSave = this.saveImage(saveCategoryDto);
                entitySave.setImageId(imageSave.getId());
            }

            if (saveCategoryDto.getIconDto() != null) {
                iconDto = saveIcon(saveCategoryDto.getIconDto());
                entitySave.setIconId(iconDto.getId());
            }

            this.productCategoryRepository.save(entitySave);
            response.setMessage(messageSource.getMessage("product.category.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            productCategoryDto = categoryMapper.toProductCategoryDto(entitySave);

            if (entitySave.getProductCategoryParentId() != null) {
                Optional<ProductCategory> parent = productCategoryRepository.findById(entitySave.getProductCategoryParentId());
                productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
            }

            // PcTerminalAccess Update
            if (saveCategoryDto.getPcTerminalAccesses() != null) {
                Integer categoryId = entitySave.getId();
                productCategoryDto.setPcTerminalAccesses(new ArrayList<>());

                pcTerminalAccessRepository.updateIsActiveByTenantIdAndProductCategoryId("N", AuditContext.getAuditInfo().getTenantId(), categoryId);
//                saveCategoryDto.getPcTerminalAccesses().forEach(pcTerminalAccessDto -> {
//                    PcTerminalAccess pcCheck = pcTerminalAccessRepository.findByProductCategoryIdAndOrgId(categoryId, pcTerminalAccessDto.getOrgId());
//                    if (pcCheck == null) {
//                        PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
//                                .productCategoryId(categoryId)
//                                .posTerminalId(pcTerminalAccessDto.getPosTerminalId())
//                                .orgId(pcTerminalAccessDto.getOrgId())
//                                .tenantId(AuditContext.getAuditInfo().getTenantId())
//                                .build();
//                        pcTerminalAccess.setIsActive("Y");
//                        pcTerminalAccessDto = modelMapper.map(pcTerminalAccessRepository.save(pcTerminalAccess), PcTerminalAccessDto.class);
//                        productCategoryDto.getPcTerminalAccesses().add(pcTerminalAccessDto);
//                    } else {
//                        pcCheck.setPosTerminalId(pcTerminalAccessDto.getPosTerminalId());
//                        pcCheck.setIsActive("Y");
//                        pcTerminalAccessRepository.save(pcCheck);
//                        pcTerminalAccessDto = modelMapper.map(pcTerminalAccessRepository.save(pcCheck), PcTerminalAccessDto.class);
//                        productCategoryDto.getPcTerminalAccesses().add(pcTerminalAccessDto);
//                    }
//                });
                for (PcTerminalAccessDto pcTerminalAccessDto : saveCategoryDto.getPcTerminalAccesses()) {
                    for (PosTerminal posTerminal : posTerminalRepository.findByOrgId(pcTerminalAccessDto.getOrgId())) {
                        PcTerminalAccess pcCheck = pcTerminalAccessRepository.findByProductCategoryIdAndPosTerminalId(categoryId, posTerminal.getId());
                        if (pcCheck == null) {
                            pcCheck = PcTerminalAccess.builder()
                                    .productCategoryId(categoryId)
                                    .posTerminalId(posTerminal.getId())
                                    .orgId(posTerminal.getOrgId())
                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                                    .build();
                        }
                        pcCheck.setIsActive("Y");
                        PcTerminalAccessDto mapped = modelMapper.map(pcTerminalAccessRepository.save(pcCheck), PcTerminalAccessDto.class);
                        productCategoryDto.getPcTerminalAccesses().add(mapped);
                    }
                }
            }

            productCategoryDto = categoryMapper.toProductCategoryDto(entitySave);
            if (imageSave != null) {
                productCategoryDto.setImageDto(modelMapper.map(imageSave, ImageDto.class));
            }

        } else {
            if (entitySave.getCode() == null || entitySave.getCode().isEmpty())
                entitySave.setCode("CAT" + (maxId + 1));
            else {
                ProductCategory checkCode = productCategoryRepository.findByCode(entitySave.getCode());
                if (checkCode != null)
                    throw new PosException(String.format(messageSource.getMessage("product.category.code.exist", null, LocaleContextHolder.getLocale()), entitySave.getCode()));
            }
            if (saveCategoryDto.getImageDto() != null) {
                imageSave = this.saveImage(saveCategoryDto);
                entitySave.setImageId(imageSave.getId());
            }

            if (saveCategoryDto.getIconDto() != null) {
                iconDto = saveIcon(saveCategoryDto.getIconDto());
                entitySave.setIconId(iconDto.getId());
            }

            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave.setOrgId(0);
            entitySave = this.productCategoryRepository.save(entitySave);
            response.setMessage(messageSource.getMessage("product.category.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
            productCategoryDto = categoryMapper.toProductCategoryDto(entitySave);

            if (imageSave != null)
                productCategoryDto.setImageDto(modelMapper.map(imageSave, ImageDto.class));
            if (entitySave.getProductCategoryParentId() != null) {
                Optional<ProductCategory> parent = productCategoryRepository.findById(entitySave.getProductCategoryParentId());
                productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
            }

            // PcTerminalAccess Create
            if (saveCategoryDto.getPcTerminalAccesses() != null) {
                Integer categoryId = entitySave.getId();
                productCategoryDto.setPcTerminalAccesses(new ArrayList<>());
//                productCategoryDto.setPcTerminalAccesses(new ArrayList<>());
//                saveCategoryDto.getPcTerminalAccesses().forEach(pcTerminalAccessDto -> {
//                    PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
//                            .productCategoryId(categoryId)
//                            .posTerminalId(pcTerminalAccessDto.getPosTerminalId())
//                            .orgId(pcTerminalAccessDto.getOrgId())
//                            .tenantId(AuditContext.getAuditInfo().getTenantId())
//                            .build();
//                    pcTerminalAccess.setIsActive("Y");
//                    PcTerminalAccessDto mapped = modelMapper.map(pcTerminalAccessRepository.save(pcTerminalAccess), PcTerminalAccessDto.class);
//                    productCategoryDto.getPcTerminalAccesses().add(mapped);
//
//                });
                for (PcTerminalAccessDto pcTerminalAccessDto : saveCategoryDto.getPcTerminalAccesses()) {
                    for (PosTerminal posTerminal : posTerminalRepository.findByOrgId(pcTerminalAccessDto.getOrgId())) {
                        PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
                                .productCategoryId(categoryId)
                                .posTerminalId(posTerminal.getId())
                                .orgId(posTerminal.getOrgId())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        pcTerminalAccess.setIsActive("Y");
                        PcTerminalAccessDto mapped = modelMapper.map(pcTerminalAccessRepository.save(pcTerminalAccess), PcTerminalAccessDto.class);
                        productCategoryDto.getPcTerminalAccesses().add(mapped);
                    }
                }
            }

        }

        productCategoryDto.setIconDto(iconDto);
        response.setData(productCategoryDto);

        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse intSave(ProductCatIntDto param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        log.info("*** Save Category *");
        if (param.getType().equals(AppConstant.ERP_PLATFORM_IDEMPIERE)) {
            // save truoc
            List<ProductCategoryDto> dtoListSave = param.getProductCategoryDtos().stream().map(item -> {
                ProductCategory pcCheck = productCategoryRepository.findByErpProductCategoryId(item.getErpProductCategoryId());
                ProductCategoryDto reDto = new ProductCategoryDto();
                reDto.setPcTerminalAccesses(item.getPcTerminalAccesses()); // Lưu lại PcTerminalAccesses

                // Lưu parent
                if (item.getProductCategoryParentId() == 0) {
                    item.setProductCategoryParentId(null);

                    if (pcCheck == null) {
                        pcCheck = categoryMapper.toProductCategory(item);
                        pcCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        pcCheck.setOrgId(0);
                        pcCheck.setIsMenu("Y");
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    } else {
                        categoryMapper.updateEntity(item, pcCheck);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    }
                }

                // Gán lại PcTerminalAccesses sau khi chuyển đổi
                item.setPcTerminalAccesses(reDto.getPcTerminalAccesses());
                return item;
            }).collect(Collectors.toList());


            List<ProductCategoryDto> dtoListFinal = dtoListSave.stream().map(item -> {
                ProductCategory pcCheck = productCategoryRepository.findByErpProductCategoryId(item.getErpProductCategoryId());
                ProductCategoryDto reDto = new ProductCategoryDto();
                reDto.setPcTerminalAccesses(item.getPcTerminalAccesses()); // Lưu lại PcTerminalAccesses

                // Lưu parent
                if (item.getProductCategoryParentId() != null) {
                    log.info("Check Parent ID: {}", item.getProductCategoryParentId());
                    ProductCategory pcParent = productCategoryRepository.findByErpProductCategoryId(item.getProductCategoryParentId());

                    if (pcParent == null)
                        item.setProductCategoryParentId(null);
                    else
                        item.setProductCategoryParentId(pcParent.getId());

                    if (pcCheck == null) {
                        ProductCategory entitySave = categoryMapper.toProductCategory(item);
                        entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        entitySave.setOrgId(0);
                        entitySave.setIsMenu("Y");
                        entitySave = this.productCategoryRepository.save(entitySave);
                        item = categoryMapper.toProductCategoryDto(entitySave);
                        item.setId(entitySave.getId());
                    } else {
                        categoryMapper.updateEntity(item, pcCheck);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                        item.setId(pcCheck.getId());
                    }
                }

                // Gán lại PcTerminalAccesses sau khi chuyển đổi
                item.setPcTerminalAccesses(reDto.getPcTerminalAccesses());
                return item;
            }).collect(Collectors.toList());


            dtoListFinal.forEach(item -> {
                // luu pc terminal access
                if (item.getPcTerminalAccesses() != null) {
                    item.getPcTerminalAccesses().forEach(pcTerminalAccessDto -> {

//                    GlobalReponse exReponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ERP_ID + "/" + pcTerminalAccessDto.getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//                    if (exReponse.getData() == null)
//                        throw new PosException(messageSource.getMessage("org.not.found", null, LocaleContextHolder.getLocale()));
//                    OrgDto orgDto = modelMapper.map(exReponse.getData(), OrgDto.class);

                        StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                                .setParameter("orgId", pcTerminalAccessDto.getOrgId())
                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                                .unwrap(NativeQuery.class)
                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                .getResultList();
                        if (resultList.isEmpty())
                            throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), pcTerminalAccessDto.getOrgId()));
                        Integer orgDtoId = Integer.parseInt(resultList.get(0).get("d_org_id").toString());

//                    exReponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_POSTERMINAL_BY_ERP_ID + "/" + pcTerminalAccessDto.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//                    if (exReponse.getData() == null)
//                        throw new PosException(messageSource.getMessage("posterminal.not.found", null, LocaleContextHolder.getLocale()));
//                    PosTerminalDto posTerminalDto = modelMapper.map(exReponse.getData(), PosTerminalDto.class);

                        sqlGetEntity = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where erp_pos_id = :posTerminalId and d_tenant_id = :tenantId");
                        resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                                .setParameter("posTerminalId", pcTerminalAccessDto.getPosTerminalId())
                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                                .unwrap(NativeQuery.class)
                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                .getResultList();
                        if (resultList.size() == 0)
                            throw new PosException(messageSource.getMessage("posterminal.not.sync", null, LocaleContextHolder.getLocale()));
                        Integer posTerminalDtoId = Integer.parseInt(resultList.get(0).get("d_pos_terminal_id").toString());
                        PcTerminalAccess pcTerminalAccessCheck = pcTerminalAccessRepository.findByProductCategoryIdAndPosTerminalId(item.getId(), posTerminalDtoId);
                        if (pcTerminalAccessCheck == null) {
                            if(Objects.equals(pcTerminalAccessDto.getIsActive(), "Y")) {
                                PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
                                        .productCategoryId(item.getId())
                                        .posTerminalId(posTerminalDtoId)
                                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                                        .build();
                                pcTerminalAccess.setOrgId(orgDtoId);
                                pcTerminalAccessRepository.save(pcTerminalAccess);
                            }
                        } else {
                            if(Objects.equals(pcTerminalAccessDto.getIsActive(), "Y")) {
                                pcTerminalAccessCheck.setPosTerminalId(posTerminalDtoId);
                                pcTerminalAccessCheck.setOrgId(orgDtoId);
                                pcTerminalAccessCheck.setIsActive(pcTerminalAccessDto.getIsActive());
                                pcTerminalAccessRepository.save(pcTerminalAccessCheck);
                            }else{
                                pcTerminalAccessRepository.deleteById(pcTerminalAccessCheck.getId());
                            }
                        }


                    });
                }
            });
        } else if (param.getType().equals(AppConstant.ERP_PLATFORM_ERPNEXT)) {
            // save truoc
            List<ProductCategoryDto> dtoListSave = param.getProductCategoryDtos().stream().map(item -> {
                ProductCategory pcCheck = productCategoryRepository.findByErpProductCategoryId(item.getErpProductCategoryId());
                ProductCategoryDto reDto = new ProductCategoryDto();
                reDto.setPcTerminalAccesses(item.getPcTerminalAccesses()); // Lưu lại PcTerminalAccesses

                // Lưu parent
                if (item.getProductCategoryParentId() == 0) {
                    item.setProductCategoryParentId(null);

                    if (pcCheck == null) {
                        pcCheck = categoryMapper.toProductCategory(item);
                        pcCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        pcCheck.setOrgId(0);
                        pcCheck.setIsMenu("Y");
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    } else {
                        categoryMapper.updateEntity(item, pcCheck);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    }
                }

                // Gán lại PcTerminalAccesses sau khi chuyển đổi
                item.setPcTerminalAccesses(reDto.getPcTerminalAccesses());
                return item;
            }).collect(Collectors.toList());

        }


        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .errors("")
                .message("Product Category saved successfully")
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getOrgAccess(ProductCategoryQueryRequest request) {
        log.info("*** product category service impl, find product category by id *");
        GlobalReponse result = new GlobalReponse();
        ProductCategory entity = productCategoryRepository.findById(request.getProductCategoryId()).orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.category.not.found", null, LocaleContextHolder.getLocale()), request.getProductCategoryId())));
        ProductCategoryDto productCategoryDto = categoryMapper.toProductCategoryDto(entity);
        if (entity.getProductCategoryParentId() != null) {
            Optional<ProductCategory> parent = productCategoryRepository.findById(entity.getProductCategoryParentId());
            productCategoryDto.setProductCategoryParent(categoryMapper.toProductCategoryDto(parent.get()));
        }


//        List<Object[]> resultList = entityManager.createNativeQuery(sql.toString())
//                .setParameter("productCategoryId", request.getProductCategoryId())
//                .getResultList();
//        List<OrgDto> orgDtos = new ArrayList<>();
//        resultList.forEach(item -> {
//            OrgDto orgDto = OrgDto.builder()
//                    .code(item[0].toString())
//                    .name(item[1].toString())
//                    .phone(item[2].toString())
//                    .address(item[3].toString())
//                    .isActive(item[4].toString())
//                    .build();
//            orgDtos.add(orgDto);
//        });

        productCategoryDto.setPcTerminalAccesses(this.getPcOrgAccess(request.getProductCategoryId(), request.getUserId(), AuditContext.getAuditInfo().getTenantId()));
        result.setMessage(messageSource.getMessage("product.category.fetch.success", null, LocaleContextHolder.getLocale()));
        result.setData(productCategoryDto);
        return result;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse updatePcE_request(List<PcTerminalAccessDto> request) {
        log.info("*** product category service impl, find product category by id *");
        List<PcTerminalAccessDto> result = new ArrayList<>();

        for (PcTerminalAccessDto item : request) {
            PcTerminalAccess pcTerminalAccess = pcTerminalAccessRepository.findById(item.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("pc.terminal.access.not.found", null, LocaleContextHolder.getLocale()), item.getId())));

            pcTerminalAccess.setIsActive(item.getIsActive());
            pcTerminalAccessRepository.save(pcTerminalAccess);

            result.add(modelMapper.map(pcTerminalAccess, PcTerminalAccessDto.class));
        }

        return GlobalReponse.builder().data(result).message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).status(HttpStatus.OK.value()).build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination getAllPcAccess(ProductCategoryQueryRequest request) {
        List<PcERequestVDto> result = new ArrayList<>();
        StringBuilder sqlGet = new StringBuilder("SELECT  dpct.d_pc_terminalaccess_id, dpc.d_product_category_id,\n" +
                "                dpct.d_tenant_id,\n" +
                "                dpct.d_org_id,\n" +
                "                dpc.name,\n" +
                "                dpc.d_product_category_parent_id,\n" +
                "                dpct.is_active,\n" +
                "                dpc.index_sequence,\n" +
                "                dpct.d_pos_terminal_id,\n" +
                "                dpc.is_summary, \n" +
                "                dpc.created,\n" +
                "                dpc.created_by,\n" +
                "                dpc.updated,\n" +
                "                dpc.updated_by," +
                " dpc.code \n" +
                "FROM pos.d_pc_terminalaccess dpct\n" +
                "         JOIN pos.d_product_category dpc ON dpct.d_product_category_id = dpc.d_product_category_id and dpc.is_menu = 'Y' " +
                "  where dpct.d_tenant_id = :tenantId and dpct.d_org_id = :orgId and dpct.d_pos_terminal_id =:posterminalId \n");
        if (request.getSearchKey() != null)
            sqlGet.append(" and (upper(dpc.name)  like upper(:searchKey) or upper(dpc.code) like upper(:searchKey))");
        if (request.getIsActive() != null)
            sqlGet.append(" and dpct.is_active = :isActive");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(DISTINCT dpc.d_product_category_id)\n" +
                "FROM pos.d_pc_terminalaccess dpct\n" +
                "         JOIN pos.d_product_category dpc ON dpct.d_product_category_id = dpc.d_product_category_id and dpc.is_menu = 'Y'  " +
                "  where dpct.d_tenant_id = :tenantId and dpct.d_org_id = :orgId and dpct.d_pos_terminal_id =:posterminalId \n");
        if (request.getSearchKey() != null)
            sqlCount.append(" and (upper(dpc.name)  like upper(:searchKey) or upper(dpc.code) like upper(:searchKey))");
        if (request.getIsActive() != null)
            sqlCount.append(" and dpct.is_active = :isActive");

        Query excuteCount = entityManager.createNativeQuery(sqlCount.toString());
        excuteCount.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        excuteCount.setParameter("orgId", request.getOrgId());
        excuteCount.setParameter("posterminalId", request.getPosTerminalId());
        if (request.getSearchKey() != null)
            excuteCount.setParameter("searchKey", "%" + request.getSearchKey() + "%");
        if (request.getIsActive() != null)
            excuteCount.setParameter("isActive", request.getIsActive());

        BigInteger totalItemsBigInt = (BigInteger) excuteCount.getSingleResult();
        int offset = request.getPage() * request.getPageSize();
        Long totalItems = totalItemsBigInt.longValue();
        Query excuteGet = entityManager.createNativeQuery(sqlGet.toString(), Tuple.class)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", request.getOrgId())
                .setParameter("posterminalId", request.getPosTerminalId());

        if (request.getSearchKey() != null)
            excuteGet.setParameter("searchKey", "%" + request.getSearchKey() + "%");
        if (request.getIsActive() != null)
            excuteGet.setParameter("isActive", request.getIsActive());

        List<Tuple> resultSqlGet = excuteGet.setFirstResult(offset)
                .setMaxResults(request.getPageSize())
                .getResultList();
        for (Tuple item : resultSqlGet) {
            String sqlGetPrice = "select coalesce(min(saleprice),0)::numeric as from_price from d_product where saleprice <> 0 and d_product_category_id = :productCategoryId ";
            List<Map<String, Object>> resultPrice = entityManager.createNativeQuery(sqlGetPrice)
                    .setParameter("productCategoryId", item.get("d_pc_terminalaccess_id"))
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            PcERequestVDto productCategoryDto = PcERequestVDto.builder()
                    .id(ParseHelper.INT.parse(item.get("d_pc_terminalaccess_id")))
                    .orgId(ParseHelper.INT.parse(item.get("d_org_id")))
                    .name(item.get("name", String.class))
                    .productCategoryParentId(ParseHelper.INT.parse(item.get("d_product_category_parent_id")))
                    .isActive(item.get("is_active", String.class))
                    .indexSequence(ParseHelper.INT.parse(item.get("index_sequence")))
                    .posTerminalId(ParseHelper.INT.parse(item.get("d_pos_terminal_id")))
                    .isSummary(item.get("is_summary", String.class))
                    .code(item.get("code", String.class))
                    .build();
            if (!resultPrice.isEmpty()) {
                resultPrice.forEach(itemPrice -> {
                    productCategoryDto.setFromPrice(ParseHelper.BIGDECIMAL.parse(String.valueOf(itemPrice.get("from_price"))));
                });
            }
            result.add(productCategoryDto);
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<PcERequestVDto> products = new PageImpl<>(result, pageable, totalItems);

        return GlobalReponsePagination.builder()
                .data(products.getContent())
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .currentPage(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalItems(products.getTotalElements())
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination getAllSimple(ProductCategoryQueryRequest request) {
        Specification<ProductCategory> specification = Specification.where(null);
        if (request.getIsActive() != null)
            specification = specification.and(CategoryProductSpecification.hasIsActive(request.getIsActive()));
        if (request.getNotInIds() != null || !request.getNotInIds().isEmpty())
            specification = specification.and(CategoryProductSpecification.notInProductCategoryId(request.getNotInIds()));
        Page<ProductCategory> products = productCategoryRepository.findAll(specification, requestParamsUtils.getPageRequest(request));
        List<ProductCategoryDto> listData = new ArrayList<>();
        for (ProductCategory item : products.getContent()) {
            ProductCategoryDto productCategoryDto = categoryMapper.toProductCategoryDto(item);
            listData.add(productCategoryDto);
        }

        return GlobalReponsePagination.builder()
                .data(listData)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .currentPage(products.getNumber())
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalItems(products.getTotalElements())
                .build();
    }

    private List<PcTerminalAccessDto> getPcOrgAccess(Integer productCategoryId, Integer userId, Integer tenantId) {
        StringBuilder sql = new StringBuilder("select dog.code as code ,dog.name as name,dog.area as area ,dog.phone as phone ,dog.address as address, dog.is_active as is_active,dog.d_org_id as d_org_id " +
                " ,dpct.d_product_category_id as d_product_category_id " +
                " from d_userorg_access dua join d_org dog on dua.d_org_id = dog.d_org_id \n" +
                "         left join d_pc_terminalaccess dpct on dua.d_org_id = dpct.d_org_id and dpct.d_product_category_id = :productCategoryId " +
                "where dua.d_user_id = :userId and dua.d_tenant_id = :tenantId \n");
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sql.toString())
                .setParameter("productCategoryId", productCategoryId)
                .setParameter("tenantId", tenantId)
                .setParameter("userId", userId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PcTerminalAccessDto> pcTerminalAccessDtos = new ArrayList<>();
        for (Map<String, Object> item : resultList) {

            PcTerminalAccessDto pcTerminalAccessDto = PcTerminalAccessDto.builder()
                    .orgId(Integer.parseInt(item.get("d_org_id").toString()))
//                    .productCategoryId(Integer.parseInt(item.get("d_product_category_id").toString()))
                    .code(item.get("code") != null ? item.get("code").toString() : "")
                    .name(item.get("name").toString())
                    .phone(item.get("phone") != null ? item.get("phone").toString() : "")
                    .address(item.get("address") != null ? item.get("address").toString() : "")
                    .isActive(item.get("is_active").toString())
                    .area(item.get("area") != null ? item.get("area").toString() : "")
                    .isAssign(item.get("d_product_category_id") != null ? "Y" : "N")
                    .build();
            pcTerminalAccessDtos.add(pcTerminalAccessDto);
        }
        return pcTerminalAccessDtos;
    }


    @KafkaListener(groupId = GROUP_ID, topics = TOPIC_SEND_PCG, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessage(ConsumerRecord<String, ProductCategoryKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
//        log.info("Received message: " + consumerRecord.value());
        try {
            String key = consumerRecord.key(); // could be null
            ProductCategoryKafkaDto value = consumerRecord.value();

            try {
                int tenantNumbers = getTenantNumbers();
                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                    Map<Object, Object> configuredDataSources = dataSourceConfigService
                            .configureDataSources();

                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
                }
//                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
//                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//                            .configureDataSources();
//
//                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (value.getTenantId() != 0) {
                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);

            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "vi", value.getTenantId()));


            log.info("Received message:");
            log.info("Key: " + key);
//                log.info("Value: " + value);
            ProductCategoryKafkaDto result = this.intSaveKafka(value.getProductCategoryDtos());
            kafkaTemplate.send(TOPIC_RECEIVE_PCG, result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    // kafka
    public ProductCategoryKafkaDto intSaveKafka(List<ProductCategoryDto> dtoList) {
        ProductCategoryKafkaDto result = new ProductCategoryKafkaDto();
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        result.setStatus("COM");
        result.setError("");
        String dbName = "";
        try {
            Query query = entityManager.createNativeQuery("SELECT current_database()");
            dbName = (String) query.getSingleResult();
            log.info("dbName " + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            log.info("*** Save Category *");
            // save truoc
            List<ProductCategoryDto> dtoListSave = dtoList.stream().map(item -> {
                ProductCategory pcCheck = productCategoryRepository.findByErpProductCategoryId(item.getErpProductCategoryId());
                ProductCategoryDto reDto = new ProductCategoryDto();
                reDto.setPcTerminalAccesses(item.getPcTerminalAccesses()); // Lưu lại PcTerminalAccesses

                // Lưu parent
                if (item.getProductCategoryParentId() == 0) {
                    item.setProductCategoryParentId(null);

                    if (pcCheck == null) {
                        pcCheck = categoryMapper.toProductCategory(item);
                        pcCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        pcCheck.setOrgId(0);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    } else {
                        categoryMapper.updateEntity(item, pcCheck);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                    }
                }

                // Gán lại PcTerminalAccesses sau khi chuyển đổi
                item.setPcTerminalAccesses(reDto.getPcTerminalAccesses());
                return item;
            }).collect(Collectors.toList());


            List<ProductCategoryDto> dtoListFinal = dtoListSave.stream().map(item -> {
                ProductCategory pcCheck = productCategoryRepository.findByErpProductCategoryId(item.getErpProductCategoryId());
                ProductCategoryDto reDto = new ProductCategoryDto();
                reDto.setPcTerminalAccesses(item.getPcTerminalAccesses()); // Lưu lại PcTerminalAccesses

                // Lưu parent
                if (item.getProductCategoryParentId() != null) {
                    log.info("Check Parent ID: {}", item.getProductCategoryParentId());
                    ProductCategory pcParent = productCategoryRepository.findByErpProductCategoryId(item.getProductCategoryParentId());

                    if (pcParent == null)
                        item.setProductCategoryParentId(null);
                    else
                        item.setProductCategoryParentId(pcParent.getId());

                    if (pcCheck == null) {
                        ProductCategory entitySave = categoryMapper.toProductCategory(item);
                        entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        entitySave.setOrgId(0);
                        entitySave = this.productCategoryRepository.save(entitySave);
                        item = categoryMapper.toProductCategoryDto(entitySave);
                        item.setId(entitySave.getId());
                    } else {
                        categoryMapper.updateEntity(item, pcCheck);
                        this.productCategoryRepository.save(pcCheck);
                        item = categoryMapper.toProductCategoryDto(pcCheck);
                        item.setId(pcCheck.getId());
                    }
                }

                // Gán lại PcTerminalAccesses sau khi chuyển đổi
                item.setPcTerminalAccesses(reDto.getPcTerminalAccesses());
                return item;
            }).collect(Collectors.toList());


            dtoListFinal.forEach(item -> {
                // luu pc terminal access
                if (item.getPcTerminalAccesses() != null) {
                    item.getPcTerminalAccesses().forEach(pcTerminalAccessDto -> {

//                        GlobalReponse exReponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ERP_ID + "/" + pcTerminalAccessDto.getOrgId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//                        if (exReponse.getData() == null)
//                            throw new PosException(messageSource.getMessage("org.not.found", null, LocaleContextHolder.getLocale()));
//                        OrgDto orgDto = modelMapper.map(exReponse.getData(), OrgDto.class);
                        StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                                .setParameter("orgId", pcTerminalAccessDto.getOrgId())
                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                                .unwrap(NativeQuery.class)
                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                .getResultList();
                        if (resultList.isEmpty())
                            throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), pcTerminalAccessDto.getOrgId()));
                        Integer orgDtoId = Integer.parseInt(resultList.get(0).get("d_org_id").toString());

//                        exReponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_POSTERMINAL_BY_ERP_ID + "/" + pcTerminalAccessDto.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
//                        if (exReponse.getData() == null)
//                            throw new PosException(messageSource.getMessage("posterminal.not.found", null, LocaleContextHolder  .getLocale()));
//                        PosTerminalDto posTerminalDto = modelMapper.map(exReponse.getData(), PosTerminalDto.class);

                        sqlGetEntity = new StringBuilder("select d_pos_terminal_id from d_pos_terminal where erp_pos_id = :posTerminalId and d_tenant_id = :tenantId");
                        resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                                .setParameter("posTerminalId", pcTerminalAccessDto.getPosTerminalId())
                                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                                .unwrap(NativeQuery.class)
                                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                                .getResultList();
                        if (resultList.size() == 0)
                            throw new PosException(messageSource.getMessage("posterminal.not.sync", null, LocaleContextHolder.getLocale()));
                        Integer posTerminalDtoId = Integer.parseInt(resultList.get(0).get("d_pos_terminal_id").toString());

                        PcTerminalAccess pcTerminalAccessCheck = pcTerminalAccessRepository.findByProductCategoryIdAndPosTerminalId(pcTerminalAccessDto.getId(), posTerminalDtoId);
                        if (pcTerminalAccessCheck == null) {
                            PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
                                    .productCategoryId(item.getId())
                                    .posTerminalId(posTerminalDtoId)
                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                                    .build();
                            pcTerminalAccess.setOrgId(orgDtoId);
                            pcTerminalAccessRepository.save(pcTerminalAccess);
                        } else {
                            pcTerminalAccessCheck.setPosTerminalId(posTerminalDtoId);
                            pcTerminalAccessCheck.setOrgId(orgDtoId);
                            pcTerminalAccessCheck.setIsActive(pcTerminalAccessDto.getIsActive());
                            pcTerminalAccessRepository.save(pcTerminalAccessCheck);
                        }


                    });
                }
            });


        } catch (Exception e) {
            result.setError(e.getMessage());
            result.setStatus("FAI");
            e.printStackTrace();
        }
        return result;
    }

    public boolean isProductCategoryHasChild(Integer id) {
        try {
            String sql = "SELECT 1 " +
                    " FROM pos.d_product WHERE d_tenant_id = :tenantId " +
                    " AND d_product_category_id = :productCategoryId limit 1";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("productCategoryId", id)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                return true;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        return false;
    }

    @Override
    public GlobalReponsePagination getPcTerminalAccess(ProductCategoryReq req) {
        log.info("ProductCategoryQueryRequest: {}", req);

        Parameter parameter = new Parameter();
        Integer d_user_id = AuditContext.getAuditInfo().getUserId();
        StringBuilder table2 = new StringBuilder(" SELECT * FROM pos.d_get_user_org_access_2_v where 1 = 1 ");

        parameter.add("d_user_id", d_user_id, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

//        if (req.getProductCategoryId()!= null){
//            parameter.add("d_product_category_id", req.getProductCategoryId(), Param.Logical.EQUAL, Param.Relational.OR, Param.START);
//            parameter.add("d_product_category_id", req.getProductCategoryId(), Param.Logical.IS_NULL, Param.Relational.AND, Param.END);
//        }
//        parameter.add("is_assign", req.getIsAssign(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
//        parameter.add("org_name", req.getOrgName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
//        parameter.add("org_code", req.getOrgCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
//        parameter.add("org_phone", req.getOrgPhone(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
//        parameter.add("wards", req.getOrgWards(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        StringBuilder table1 = new StringBuilder(" SELECT * FROM d_pc_terminalaccess where 1 = 1 ");
        table1.append(" AND d_product_category_id = :d_product_category_id ");

        table2.append(" AND d_user_id = :userid");

        StringBuilder sql = new StringBuilder("SELECT " +
                " tbl1.d_org_id," +
                " tbl1.wards," +
                " tbl1.org_name," +
                " tbl1.org_code," +
                " tbl1.phone AS org_phone," +
                " tbl1.d_user_id," +
                " tbl2.d_product_category_id," +
                " COALESCE(tbl2.is_active, 'N') AS is_assign " +
                " FROM (" + table2 + ") as tbl1 left join" +
                " (" + table1 + ") as tbl2 " +
                "  on tbl1.d_org_id = tbl2.d_org_id WHERE 1 = 1 ");

        if (req.getKeyword() != null) {
            sql.append(" AND (lower(org_code) like lower(:keyWord) or lower(org_name) like lower(:keyWord))");
        }

        log.info(sql.toString());

        if (req.getIsAssign() != null) {
            sql.append(" AND COALESCE(tbl2.is_active, 'N') = :isAssign ");
        }

        sql.append(" LIMIT :limit OFFSET :offset ");

        log.info(sql.toString());

        Query query = entityManager.createNativeQuery(sql.toString());

        if (req.getProductCategoryId() != null) {
            query.setParameter("d_product_category_id", req.getProductCategoryId());
        }
        if (req.getIsAssign() != null) {
            query.setParameter("isAssign", req.getIsAssign());
        }
        if (req.getKeyword() != null) {
            query.setParameter("keyWord", "%" + req.getKeyword() + "%");
        }
        query.setParameter("userid", d_user_id);
        query.setParameter("limit", req.getPageSize());
        query.setParameter("offset", req.getPageSize() * req.getPage());

        List<Map<String, Object>> results = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        List<ProductCategoryReq> data = new ArrayList<>();
        for (Map<String, Object> result : results) {
            ProductCategoryReq pc = ProductCategoryReq.builder()
                    .productCategoryId(ParseHelper.INT.parse(result.get("d_product_category_id")))
                    .orgId(ParseHelper.INT.parse(result.get("d_org_id")))
                    .orgName(ParseHelper.STRING.parse(result.get("org_name")))
                    .orgCode(ParseHelper.STRING.parse(result.get("org_code")))
                    .orgPhone(ParseHelper.STRING.parse(result.get("org_phone")))
                    .orgName(ParseHelper.STRING.parse(result.get("org_name")))
                    .orgWards(ParseHelper.STRING.parse(result.get("wards")))
                    .userId(ParseHelper.INT.parse(result.get("d_user_id")))
                    .isAssign(ParseHelper.STRING.parse(result.get("is_assign")))
                    .build();
            data.add(pc);
        }

        req.setOrder(null);
        req.setSortBy(null);
        Pagination pagination = queryEngine.getPagination("pos.d_get_user_org_access_2_v", parameter, req);

        log.info("Load pagination...");

        return GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .currentPage(pagination.getPage())
                .pageSize(pagination.getPageSize())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    public Image saveImage(ProductCategoryDto dto) {
        log.info("save Image ");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + ""); // Thêm tenantId vào header
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        GlobalReponse callExService = restTemplate.getForObject(AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE + "/" + com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId() + "/" + AuditContext.getAuditInfo().getOrgId(), GlobalReponse.class);

        GlobalReponse callExService = restTemplate.exchange(
                AppConstant.DiscoveredDomainsApi.GET_ORG_TENANT_CODE + "/"
                        + AuditContext.getAuditInfo().getTenantId() + "/"
                        + AuditContext.getAuditInfo().getOrgId(),
                HttpMethod.GET,
                entity,
                GlobalReponse.class
        ).getBody();


        TenantAndOrgDto tenantAndOrgDto = modelMapper.map(callExService.getData(), TenantAndOrgDto.class);
        SaveImageMDMRequest mdmRequest = new SaveImageMDMRequest();
        mdmRequest.setTenantCode(tenantAndOrgDto.getCodeTenant());
        mdmRequest.setBranchCode(tenantAndOrgDto.getCodeIndustry());
        mdmRequest.setImage64(dto.getImageDto().getImage64());
        mdmRequest.setImageCode(dto.getImageDto().getImageCode());
        callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_HOST_FINDBYNAME + AppConstant.NameSystemConfig.MDM_URL_SAVE_IMAGE, HttpMethod.GET, entity, GlobalReponse.class).getBody();
        String urlMDM = String.valueOf(callExService.getData());
        log.info("payload {}", mdmRequest.getImageCode());
        Map<String, Object> finalRequest = new HashMap<>();
        finalRequest.put("data", List.of(mdmRequest));

        HttpHeaders headersPost = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(finalRequest, headersPost);

        ResponseEntity<String> response = externalRestTemplate.exchange(urlMDM, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode root = objectMapper.readTree(responseBody);

                String resultCode = root.path("resultCode").asText();
                String errorMessage = root.path("errorMessage").asText(null);
                JsonNode data = root.path("data");
                log.info("resultCode: " + resultCode);
                log.info("errorMessage: " + errorMessage);
                log.info("data: " + data);

                if (resultCode.equals("00") && data.isArray()) {
                    for (JsonNode item : data) {
                        String urlValue = item.path("url").asText();
                        String imageCode = item.path("imageCode").asText();

                        Image image = Image.builder()
                                .imageUrl(urlValue)
                                .imageCode(imageCode)
                                .isActive("Y")
                                .dTenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        if (dto.getImageDto().getId() != null)
                            image.setId(dto.getImageDto().getId());

                        image = imageRepository.save(image);
                        return image;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("API call failed with status code: " + response.getStatusCode());
        }
        return null;
    }

    public IconDto saveIcon(IconDto dto) {

        log.info("save Icon ");

        List<Icon> iconCheck = this.iconRepository.findAllIconsByImageUrl(dto.getImageUrl());

        if (iconCheck != null && !iconCheck.isEmpty()) {

            return modelMapper.map(iconCheck.get(0), IconDto.class); // Trả về ID của icon đã tồn tại
        }

        Icon entitySave = modelMapper.map(dto, Icon.class);

        entitySave.setIsActive("Y");
        entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
        entitySave.setOrgId(0);

        entitySave = this.iconRepository.save(entitySave);
        return  modelMapper.map(entitySave, IconDto.class);
    }
}
