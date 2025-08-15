    package com.dbiz.app.productservice.service.impl;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.PriceListOrg;
import com.dbiz.app.productservice.domain.PriceListProduct;
import com.dbiz.app.productservice.domain.Product;
import com.dbiz.app.productservice.helper.DateHelper;
import com.dbiz.app.productservice.repository.*;
import com.dbiz.app.productservice.service.PriceListService;
import com.dbiz.app.productservice.specification.PriceListSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.productRequest.PriceListQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PriceListServiceImpl implements PriceListService {

    private final PriceListRepository priceListRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final PriceListOrgRepository priceListOrgRepository;

    private final RestTemplate restTemplate;

    private final PriceListVRepository priceListVRepository;

    private final PriceListProductRepository priceListProductRepository;

    private final ProductRepository productRepository;

    private final EntityManager entityManager;

    private StringBuilder sql;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";
    private final static String TOPIC = "sync-integration-to-pricelist";

    private final static String TOPIC2 = "sync-pricelist-to-integration";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public GlobalReponsePagination findAll(PriceListQueryRequest querys) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        Pageable pageable = requestParamsUtils.getPageRequest(querys);
        Specification<PriceList> spec = PriceListSpecification.getEntitySpecification(querys);
        if (querys.getPosTerminalId() != null) {
            StringBuilder slqCheck = new StringBuilder("select d_pricelist_id,is_default from d_pos_terminal where d_pos_terminal_id = :posterminalId   and  d_tenant_id = :tenantId");
            List<Map<String, Object>> resultList = entityManager.createNativeQuery(slqCheck.toString())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posterminalId", querys.getPosTerminalId())
                    .unwrap(org.hibernate.query.Query.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            log.info("tenantId: {}", AuditContext.getAuditInfo().getTenantId());
            if (ParseHelper.STRING.parse(resultList.get(0).get("is_default")).equals("N")) {
                spec = spec.and(PriceListSpecification.inPriceListId(ParseHelper.INT.parse(resultList.get(0).get("d_pricelist_id"))));
            } else {
                spec = spec.and(PriceListSpecification.hasOrgId(querys.getOrgId()));
            }
        }

        //check role
        if (querys.getRoleId() != null) {
            String sqlCheckRole = "select code from d_role where d_role_id = :roleId and d_tenant_id = :tenantId";
            List<Tuple> resultRole = entityManager.createNativeQuery(sqlCheckRole, Tuple.class)
                    .setParameter("roleId", querys.getRoleId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            if (!resultRole.get(0).get("code", String.class).equals("ADM")) {
                String sqlGetOrgAcc = "select d_org_id from d_userorg_access where d_user_id = :userId and d_tenant_id = :tenantId";
                List<Tuple> resultGetOrgAcc = entityManager.createNativeQuery(sqlGetOrgAcc, Tuple.class)
                        .setParameter("userId", AuditContext.getAuditInfo().getUserId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .getResultList();
                if (!resultGetOrgAcc.isEmpty()) {
                    Set<Integer> orgIds = new HashSet<>();
                    resultGetOrgAcc.forEach(item -> {
                        orgIds.add(ParseHelper.INT.parse(item.get("d_org_id")));
                    });
                    orgIds.add(0);
                    spec = spec.and(PriceListSpecification.hasOrgIds(new ArrayList<>(orgIds)));
                }
            }
        }

        Page<PriceList> entityList = priceListRepository.findAll(spec, pageable);
        List<PricelistDto> listData = new ArrayList<>();
        for (PriceList item : entityList.getContent()) {
            PricelistDto dto = modelMapper.map(item, PricelistDto.class);
            listData.add(dto);
        }


//        if(querys.getOrgIds()!= null && querys.getOrgIds().length > 0)
//        {
//            Page<PriceListOrg> entityList = priceListOrgRepository.findAllByOrgId(querys.getOrgIds(), AuditContext.getAuditInfo().getTenantId(), pageable);
//            List<PricelistDto> reponseData = new ArrayList<>();
//            entityList.forEach(
//                    item->{
//                        PriceList priceList = priceListRepository.findById(item.getPricelistId()).orElseThrow(()-> new ObjectNotFoundException("entity not found"));
//
//                        PricelistDto dto = modelMapper.map(priceList,PricelistDto.class);
//                        reponseData.add(dto);
//                    }
//            );
//            return GlobalReponsePagination.builder()
//                    .status(HttpStatus.OK.value())
//                    .message(messageSource.getMessage("price.list.fetch.all",null, LocaleContextHolder.getLocale()))
//                    .errors("")
//                    .pageSize(entityList.getSize())
//                    .totalPages(entityList.getTotalPages())
//                    .currentPage(entityList.getNumber())
//                    .totalItems(entityList.getTotalElements())
//                    .data(reponseData)
//                    .build();
//        }


//        Specification<PriceListV> spec = PriceListViewSpecification.getEntitySpecification(querys);
//
//
//
//        Page<PriceListV> entityList = priceListVRepository.findAll( spec,pageable);
//
//        List<PriceListVDto> listData = new ArrayList<>();
//        for(PriceListV item : entityList.getContent()){
//            listData.add(modelMapper.map(item,PriceListVDto.class));
//        }
        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage(messageSource.getMessage("price.list.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Transactional
    @Override
    public GlobalReponse save(PricelistDto paramDto) {
        log.info("*** pricelist, service; save pricelist ***");
        GlobalReponse response = new GlobalReponse();
        PriceList entitySave = modelMapper.map(paramDto, PriceList.class);

        if (entitySave.getId() != null) // update
        {
            Integer idPriceList = entitySave.getId();
            entitySave = this.priceListRepository.findById(paramDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

            this.priceListOrgRepository.deleteAllByPricelistId(idPriceList);
            this.priceListOrgRepository.updateIsActiveByPricelistId(idPriceList, "N");
            if (paramDto.getOrgIds() != null) {
                Arrays.stream(paramDto.getOrgIds()).forEach(element -> {
                    PriceListOrg priceListOrg = this.priceListOrgRepository.findByOrgIdAndPricelistId(element, idPriceList).orElse(null);
                    if (priceListOrg == null) {
                        priceListOrg = new PriceListOrg();
                        priceListOrg.setOrgId(element);
                        priceListOrg.setPricelistId(idPriceList);
                        priceListOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        this.priceListOrgRepository.save(priceListOrg);
                    } else {
                        priceListOrg.setIsActive("Y");
                        this.priceListOrgRepository.save(priceListOrg);
                    }
                    ;
                });
            }

            modelMapper.map(paramDto, entitySave);
            if (paramDto.getFromDate() != null)
                entitySave.setFromDate(DateHelper.toInstantDateUTC(paramDto.getFromDate()));
            if (paramDto.getToDate() != null)
                entitySave.setToDate(DateHelper.toInstantDateUTC(paramDto.getToDate()));
            this.priceListRepository.save(entitySave);

            response.setMessage(messageSource.getMessage("price.list.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        } else {// insert
            PriceList priceListCheck = this.priceListRepository.findByNameAndTenantId(paramDto.getName(), AuditContext.getAuditInfo().getTenantId());
            if (priceListCheck != null) {
                if (priceListCheck.getToDate().isAfter(Instant.now())) // kiem tra 1 co sau thoi gian hien tai so  voi 2 k
                {
                    throw new PosException(messageSource.getMessage("price.list.name.exist", null, LocaleContextHolder.getLocale()));
                }
                ;
            }

            entitySave.setOrgId(0);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if (paramDto.getFromDate() != null)
                entitySave.setFromDate(DateHelper.toInstantDateUTC(paramDto.getFromDate()));
            if (paramDto.getToDate() != null)
                entitySave.setToDate(DateHelper.toInstantDateUTC(paramDto.getToDate()));
            entitySave = this.priceListRepository.save(entitySave);
            Integer idPriceList = entitySave.getId();

            if (paramDto.getOrgIds() != null) {
                Arrays.stream(paramDto.getOrgIds()).forEach(element -> {
                    PriceListOrg priceListOrg = new PriceListOrg();
                    priceListOrg.setOrgId(element);
                    priceListOrg.setPricelistId(idPriceList);
                    priceListOrg.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    this.priceListOrgRepository.save(priceListOrg);
                });
            }


            response.setMessage(messageSource.getMessage("price.list.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }

        response.setData(modelMapper.map(entitySave, PricelistDto.class));
        log.info("PiceList saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete pricelist by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<PriceList> entityDelete = this.priceListRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("price.list.not.found", null, LocaleContextHolder.getLocale()), id));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        response.setMessage(String.format(messageSource.getMessage("price.list.deleted", null, LocaleContextHolder.getLocale()), id));
        response.setStatus(HttpStatus.OK.value());
        this.priceListRepository.delete(entityDelete.get());
        return response;
    }


    @Override
    public GlobalReponse findById(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        log.info("*** Pricelist, service; fetch Pricelist by id *");

        GlobalReponse response = new GlobalReponse();
        PricelistDto dto = modelMapper.map(this.priceListRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("price.list.not.found", null, LocaleContextHolder.getLocale()), id))), PricelistDto.class);

        List<PriceListOrg> listOrgs = priceListOrgRepository.findAllByPricelistId(dto.getId());
        List<OrgPriceListVDto> orgDtos = new ArrayList<>();
        for (PriceListOrg item : listOrgs) {
            GlobalReponse serviceDto = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_ORG_BY_ID + "/" + item.getOrgId(), HttpMethod.GET, entityHeader
                    , GlobalReponse.class).getBody();
            orgDtos.add(modelMapper.map(serviceDto.getData(), OrgPriceListVDto.class));
        }
        dto.setPriceListOrg(orgDtos);
        response.setData(dto);
        response.setMessage(messageSource.getMessage("price.list.fetch.success", null, LocaleContextHolder.getLocale()));


        return response;
    }

    /**
     * @param
     * @return
     */
    @Transactional
    @Override
    public GlobalReponse intSave(List<PriceListIntDto> param) {
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
        AtomicReference<Boolean> applyPriceNearest = new AtomicReference<>(false);

        log.info("*** Pricelist, s  ervice; save pricelist ***");
        if (param != null && !param.isEmpty()) {

            param.stream().forEach(item -> {
                PriceList itemInt = priceListRepository.findByErpPriceListId(item.getErpPriceListId());
                StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                        .setParameter("orgId", item.getOrgId())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();
                if (resultList.isEmpty())
                    throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), item.getOrgId()));
                Integer orgDtoID = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

//                Instant priceListMaxDate = priceListRepository.getMaxFromDate(AuditContext.getAuditInfo().getTenantId());
//                Instant now = Instant.now();
//                Duration duration1 = Duration.between(now, priceListMaxDate);
//                Duration duration2 = Duration.between(now, DateHelper.toInstantDateTime(item.getFromDate()));
//                log.info("duration1: {}", duration1.toString());
//                log.info("duration2: {}", duration2.toString());
//                if (duration1.abs().compareTo(duration2.abs()) > 0) {
//                    applyPriceNearest.set(true);
//                }
                if(item.getMaxDate().equals("Y"))
                {
                    applyPriceNearest.set(true);
                }

                if (itemInt == null) {
                    itemInt = modelMapper.map(item, PriceList.class);
                    itemInt.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    itemInt.setOrgId(0);
                    itemInt.setFromDate(DateHelper.toInstantDateTime(item.getFromDate()));
                    LocalDateTime castDateTime = LocalDateTime.ofInstant(DateHelper.toInstantDateTime(item.getFromDate()), ZoneId.of("Asia/Ho_Chi_Minh"));
                    castDateTime = castDateTime.plus(1, ChronoUnit.YEARS);
                    itemInt.setToDate(castDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
                    itemInt = priceListRepository.save(itemInt);

                    PriceListOrg priceListOrg = PriceListOrg.builder()
                            .pricelistId(itemInt.getId())
                            .orgId(orgDtoID)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    priceListOrgRepository.save(priceListOrg);
                } else {
                    modelMapper.map(item, itemInt);
                    itemInt.setFromDate(DateHelper.toInstantDateTime(item.getFromDate()));
                    itemInt.setName(item.getName());
                    PriceListOrg priceListOrgCheck = priceListOrgRepository.findByOrgIdAndPricelistId(orgDtoID, itemInt.getId()).orElse(null);
                    if (priceListOrgCheck == null) {
                        PriceListOrg priceListOrg = PriceListOrg.builder()
                                .pricelistId(itemInt.getId())
                                .orgId(orgDtoID)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        priceListOrgRepository.save(priceListOrg);
                    }
                    itemInt = priceListRepository.save(itemInt);
                }
                Integer idPriceList = itemInt.getId();
                if (item.getListPriceProduct() != null && !item.getListPriceProduct().isEmpty()) {
                    item.getListPriceProduct().forEach(itemProduct ->
                    {
                        Product product = productRepository.findByErpProductId(itemProduct.getProductId()).orElse(null);
                        if (product != null) {
                            PriceListProduct priceListProductCheck = priceListProductRepository.findByPriceListAndProductId(idPriceList, product.getId()).orElse(null);
                            if (priceListProductCheck == null) {
                                priceListProductCheck = PriceListProduct.builder()
                                        .productId(product.getId())
                                        .priceListId(idPriceList)
                                        .standardPrice(itemProduct.getStandardPrice())
                                        .salesPrice(itemProduct.getSalesPrice())
                                        .lastOrderPrice(itemProduct.getLastOrderPrice())
                                        .costPrice(itemProduct.getCostPrice())
                                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                                        .orgId(0).build();
                                priceListProductCheck.setIsActive(
                                        itemProduct.getIsActive() == null ? "Y" : itemProduct.getIsActive() );

                                priceListProductRepository.save(priceListProductCheck);
                                if (product.getSaleprice() == null || product.getSaleprice().compareTo(BigDecimal.ZERO) == 0) {
                                    product.setSaleprice(itemProduct.getSalesPrice());
                                    productRepository.save(product);
                                }
                            } else {
                                priceListProductCheck.setStandardPrice(itemProduct.getStandardPrice());
                                priceListProductCheck.setSalesPrice(itemProduct.getSalesPrice());
                                priceListProductCheck.setIsActive(
                                        itemProduct.getIsActive() == null ? "Y" : itemProduct.getIsActive() );

                                priceListProductRepository.save(priceListProductCheck);
                            }
                            // neu bang gia hieu luc gan nhat thi apply gia ban cho san pham
                            if (applyPriceNearest.get()) {
                                product.setSaleprice(itemProduct.getSalesPrice());
                                log.info("item product: {}", itemProduct.getSalesPrice());
                                log.info("product: {}", product.getSaleprice());
                                productRepository.save(product);
                                PriceListProduct priceListDefault = priceListProductRepository.findPriceListProductByDefault(product.getId(), product.getTenantId()).orElse(null);
                                if (priceListDefault != null) {
                                    priceListDefault.setSalesPrice(itemProduct.getSalesPrice());
                                    priceListDefault.setStandardPrice(itemProduct.getStandardPrice());
                                    priceListProductRepository.save(priceListDefault);
                                }

                            }
                        }
                    });
                }
                applyPriceNearest.set(false);
            });
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .data(true)
                .message("Success")
                .build();
    }

    /**
     * @param erpId
     * @return
     */
    @Override
    public GlobalReponse findByErpId(Integer erpId) {
        log.info("*** Pricelist, service; fetch Pricelist by erpId *");
        PriceList entity = priceListRepository.findByErpPriceListId(erpId);
        if (entity == null)
            throw new PosException(messageSource.getMessage("price.list.notsync", null, LocaleContextHolder.getLocale()));

        return GlobalReponse.builder()
                .data(modelMapper.map(entity, PricelistDto.class))
                .message(messageSource.getMessage("price.list.fetch.success", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAllByCashier(PriceListQueryRequest querys) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        Pageable pageable = requestParamsUtils.getPageRequest(querys);
        Specification<PriceList> spec = PriceListSpecification.getEntitySpecification(querys);
        if (querys.getPosTerminalId() != null) {
            StringBuilder sqlCheckOrg = new StringBuilder("select is_pos_mng,d_pricelist_id from d_org where d_org_id = :orgId");
            List<Tuple> resultOrg = entityManager.createNativeQuery(sqlCheckOrg.toString(), Tuple.class)
                    .setParameter("orgId", querys.getOrgId())
                    .getResultList();
            if (!resultOrg.isEmpty()) {
                if (resultOrg.get(0).get("is_pos_mng", String.class).equals("N"))
                    spec = spec.and(PriceListSpecification.inPriceListId(ParseHelper.INT.parse(resultOrg.get(0).get("d_pricelist_id"))));
                else {
                    StringBuilder sqlGetPtm = new StringBuilder("select d_pricelist_id from d_pos_terminal where d_pos_terminal_id = :posterminalId   and  d_tenant_id = :tenantId ");
                    List<Tuple> resultPtm = entityManager.createNativeQuery(sqlGetPtm.toString(), Tuple.class)
                            .setParameter("posterminalId", querys.getPosTerminalId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .getResultList();
                    if (!resultPtm.isEmpty()) {
                        spec = spec.and(PriceListSpecification.inPriceListId(ParseHelper.INT.parse(resultPtm.get(0).get("d_pricelist_id"))));
                    }
                }
            }


//            StringBuilder slqCheck = new StringBuilder("select d_pricelist_id,is_default from d_pos_terminal where d_pos_terminal_id = :posterminalId   and  d_tenant_id = :tenantId");
//            List<Map<String, Object>> resultList = entityManager.createNativeQuery(slqCheck.toString())
//                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                    .setParameter("posterminalId", querys.getPosTerminalId())
//                    .unwrap(org.hibernate.query.Query.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                    .getResultList();
//            if (ParseHelper.STRING.parse(resultList.get(0).get("is_default")).equals("N")) {
//                spec = spec.and(PriceListSpecification.inPriceListId(ParseHelper.INT.parse(resultList.get(0).get("d_pricelist_id"))));
//            } else {
//                // lay prie lice theo org
//                StringBuilder sqlGetPriceList = new StringBuilder("select d_pricelist_id from d_org where d_org_id = ? ");
//                List<Map<String, Object>> priceListId = entityManager.createNativeQuery(sqlGetPriceList.toString())
//                        .setParameter(1, querys.getOrgId())
//                        .unwrap(org.hibernate.query.Query.class)
//                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                        .getResultList();
//
//                spec = spec.and(PriceListSpecification.inPriceListId(ParseHelper.INT.parse(priceListId.get(0).get("d_pricelist_id"))));
//            }
        }
//        spec = spec.and(PriceListSpecification.validDate(querys.getCurrentDate()));
        spec = spec.and(PriceListSpecification.hasValidDateInRangeAndNotGeneralPrice(querys.getCurrentDate()));
        Page<PriceList> entityList = priceListRepository.findAll(spec, pageable);
        List<PricelistDto> listData = new ArrayList<>();
        if (entityList.getContent().isEmpty())
            throw new PosException(messageSource.getMessage("price.list.expried", null, LocaleContextHolder.getLocale()));

        for (PriceList item : entityList.getContent()) {
            PricelistDto dto = modelMapper.map(item, PricelistDto.class);
            listData.add(dto);
        }
        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage(messageSource.getMessage("price.list.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponse getOrgAccess(PriceListQueryRequest request) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        PriceList priceList = priceListRepository.findById(request.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

        return null;
    }

    /**
     *
     * @param pricelistDto
     * @return
     */
    @Override
    public GlobalReponse saveIntPosterminal(PriceListIntDto item) {
        PriceList itemInt = modelMapper.map(item, PriceList.class);
        itemInt.setTenantId(AuditContext.getAuditInfo().getTenantId());
        itemInt.setOrgId(0);
        itemInt.setFromDate(DateHelper.toInstantDateTime(item.getFromDate()));
        LocalDateTime castDateTime = LocalDateTime.ofInstant(DateHelper.toInstantDateTime(item.getFromDate()), ZoneId.of("Asia/Ho_Chi_Minh"));
        castDateTime = castDateTime.plus(1, ChronoUnit.YEARS);
        itemInt.setToDate(castDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
        itemInt = priceListRepository.save(itemInt);

        StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                .setParameter("orgId", item.getOrgId())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        if (resultList.isEmpty())
            throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), item.getOrgId()));
        Integer orgDtoID = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

        PriceListOrg priceListOrg = PriceListOrg.builder()
                .pricelistId(itemInt.getId())
                .orgId(orgDtoID)
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .build();
        priceListOrgRepository.save(priceListOrg);

        return GlobalReponse.builder().status(HttpStatus.OK.value()).data(itemInt.getId()).message("Success").build();
    }


    @KafkaListener(groupId = GROUP_ID, topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessage(ConsumerRecord<String, PriceListIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received message: " + consumerRecord.value());
        try {
            String key = consumerRecord.key(); // could be null
            PriceListIntKafkaDto value = consumerRecord.value();

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

            if (!value.getPriceListIntDtoList().isEmpty()) {
                log.info("Received message:");
                log.info("Key: " + key);
                log.info("Value: " + value);
                this.intPriceList(value, value.getSyncIntegrationCredential());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @Transactional
    public void intPriceList(PriceListIntKafkaDto param, SyncIntegrationCredential syncIntegrationCredential) {
        log.info("save kafla");
        PriceListIntKafkaDto result = new PriceListIntKafkaDto();
        result.setStatusIntegration("COM");
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
        AtomicReference<Boolean> applyPriceNearest = new AtomicReference<>(false);

        log.info("*** Pricelist, service; save pricelist ***");
        try {
            if (param != null && !param.getPriceListIntDtoList().isEmpty()) {
                param.getPriceListIntDtoList().stream().forEach(item -> {
                    PriceList itemInt = priceListRepository.findByErpPriceListId(item.getErpPriceListId());
                    StringBuilder sqlGetEntity = new StringBuilder("select d_org_id from d_org where erp_org_id = :orgId and d_tenant_id = :tenantId");
                    List<Map<String, Object>> resultList = entityManager.createNativeQuery(sqlGetEntity.toString())
                            .setParameter("orgId", item.getOrgId())
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    if (resultList.isEmpty())
                        throw new PosException(String.format(messageSource.getMessage("org.not.sync", null, LocaleContextHolder.getLocale()), item.getOrgId()));
                    Integer orgDtoID = ParseHelper.INT.parse(resultList.get(0).get("d_org_id"));

                    if(item.getMaxDate().equals("Y"))
                    {
                        applyPriceNearest.set(true);
                    }

                    if (itemInt == null) {
                        itemInt = modelMapper.map(item, PriceList.class);
                        itemInt.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        itemInt.setOrgId(0);
                        itemInt.setFromDate(DateHelper.toInstantDateTime(item.getFromDate()));
                        LocalDateTime castDateTime = LocalDateTime.ofInstant(DateHelper.toInstantDateTime(item.getFromDate()), ZoneId.of("Asia/Ho_Chi_Minh"));
                        castDateTime = castDateTime.plus(1, ChronoUnit.YEARS);
                        itemInt.setToDate(castDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
                        itemInt = priceListRepository.save(itemInt);

                        PriceListOrg priceListOrg = PriceListOrg.builder()
                                .pricelistId(itemInt.getId())
                                .orgId(orgDtoID)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .build();
                        priceListOrgRepository.save(priceListOrg);
                    } else {
                        modelMapper.map(item, itemInt);
                        PriceListOrg priceListOrgCheck = priceListOrgRepository.findByOrgIdAndPricelistId(orgDtoID, itemInt.getId()).orElse(null);
                        if (priceListOrgCheck == null) {
                            PriceListOrg priceListOrg = PriceListOrg.builder()
                                    .pricelistId(itemInt.getId())
                                    .orgId(orgDtoID)
                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                                    .build();
                            priceListOrgRepository.save(priceListOrg);
                        }
                        itemInt = priceListRepository.save(itemInt);
                    }
                    Integer idPriceList = itemInt.getId();
                    if (item.getListPriceProduct() != null && !item.getListPriceProduct().isEmpty()) {
                        item.getListPriceProduct().forEach(itemProduct ->
                        {
                            Product product = productRepository.findByErpProductId(itemProduct.getProductId()).orElse(null);
                            if (product != null) {
                                PriceListProduct priceListProductCheck = priceListProductRepository.findByPriceListAndProductId(idPriceList, product.getId()).orElse(null);
                                if (priceListProductCheck == null) {
                                    priceListProductCheck = PriceListProduct.builder()
                                            .productId(product.getId())
                                            .priceListId(idPriceList)
                                            .standardPrice(itemProduct.getStandardPrice())
                                            .salesPrice(itemProduct.getSalesPrice())
                                            .lastOrderPrice(itemProduct.getLastOrderPrice())
                                            .costPrice(itemProduct.getCostPrice())
                                            .tenantId(AuditContext.getAuditInfo().getTenantId())

                                            .orgId(0).build();
                                    priceListProductRepository.save(priceListProductCheck);
                                } else {
                                    priceListProductCheck.setStandardPrice(itemProduct.getStandardPrice());
                                    priceListProductCheck.setSalesPrice(itemProduct.getSalesPrice());
                                    priceListProductRepository.save(priceListProductCheck);
                                }
                                // neu bang gia hieu luc gan nhat thi apply gia ban cho san pham
                                if (applyPriceNearest.get()) {
                                    product.setSaleprice(itemProduct.getSalesPrice());
                                    productRepository.save(product);
                                    PriceListProduct priceListDefault = priceListProductRepository.findPriceListProductByDefault(product.getId(), product.getTenantId()).orElse(null);
                                    if (priceListDefault != null) {
                                        priceListDefault.setSalesPrice(itemProduct.getSalesPrice());
                                        priceListDefault.setStandardPrice(itemProduct.getStandardPrice());
                                        priceListProductRepository.save(priceListDefault);
                                    }
                                }
                            }
                        });
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setError(e.getMessage());
            result.setLastPage(param.getLastPage());
            result.setStatusIntegration("FAI");
        }

        log.info("save history price list to integration");
        result.setPriceListIntDtoList(param.getPriceListIntDtoList());
        result.setSyncIntegrationCredential(syncIntegrationCredential);
        result.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        result.setLastPage(param.getLastPage());
        kafkaTemplate.send(TOPIC2, result);
    }

}
