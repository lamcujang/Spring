package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.helper.promotion.GetApplicablePromoStrategy;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.service.PromotionService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.request.ApplicablePromoReqDto;
import org.common.dbiz.dto.orderDto.response.ApplicablePromoResDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.ProductCategoryDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PromotionQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PromotionServiceImpl implements PromotionService {

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;
    private final EntityManager entityManager;

    private final PromotionRepository promotionRepository;
    private final PromotionTimeRepository promotionTimeRepository;
    private final PromotionMethodRepository promotionMethodRepository;
    private final PromoAssignOrgRepository promoAssignOrgRepository;
    private final PromoBpGroupRepository promoBpGroupRepository;

    private final Map<String, GetApplicablePromoStrategy> getPromoStratMap;

    private static final Integer JDBC_AUTO_OFFSET = 8;

    /**
     * @return
     */
    @Override
    public GlobalReponsePagination getAllPromotion(PromotionQueryRequest request) {
        log.info("*** Get All Promotion ***");
        Parameter parameter = new Parameter();
        if (request.getOrgId() != null) {
            List<Integer> promotionIdList = this.promoAssignOrgRepository.findPromotionIdByOrgIdAndIsActive(request.getOrgId(), "Y");
            if (promotionIdList.isEmpty())
                promotionIdList.add(0);
            parameter.add("d_promotion_id", promotionIdList, Param.Logical.IN, Param.Relational.AND, Param.NONE);
        }
        parameter.add("d_promotion_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("name", request.getSearchKey(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("code", request.getSearchKey(), Param.Logical.LIKE, Param.Relational.AND, Param.END);
        parameter.add("promotion_based_on", request.getPromotionBasedOn(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("promotion_type", request.getPromotionType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("is_active", request.getIsActive(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("is_all_org", request.getIsAllAssignOrg(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("is_all_bpartner", request.getIsAllBpartnerGroup(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("d_promotion_get_all_v",
                parameter, request);
        List<PromotionDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                PromotionDto item = PromotionDto.builder()
                        .id(rs.getInt("d_promotion_id"))
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .userName(rs.getString("user_name"))
                                .fullName(rs.getString("full_name"))
                                .build())
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .byDate(rs.getString("by_date"))
                        .byMonth(rs.getString("by_month"))
                        .isAllOrg(rs.getString("is_all_org"))
                        .isAllBpartner(rs.getString("is_all_bpartner"))
                        .isApplyBirthday(rs.getString("is_apply_birthday")) // .applyBirthday(rs.getString("apply_birthday"))
                        .isWarnIfUsed(rs.getString("is_warn_if_used")) // .isUsed(rs.getString("is_used"))
                        .isScaleWithQty(rs.getString("is_scale_with_qty"))
                        .fromDate(rs.getString("from_date"))
                        .toDate(rs.getString("to_date"))
                        .excludedDate(rs.getString("excluded_date"))
                        .promotionBasedOn(rs.getString("promotion_based_on"))
                        .promotionType(rs.getString("promotion_type"))
                        .qty(rs.getBigDecimal("qty"))
                        .promotionBasedOnName(rs.getString("promotion_based_on_name"))
                        .promotionTypeName(rs.getString("promotion_type_name"))
                        .isActive(rs.getString("is_active"))
                        .build();
                Parameter parameterPromoMethod = new Parameter();
                parameterPromoMethod.add("d_promotion_id", item.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
                ResultSet rsPromoMethod = queryEngine.getRecords("d_promotion_methods_get_all_v", parameterPromoMethod, request);
                List<PromotionMethodDto> promotionMethodDtoList = new ArrayList<>();
                while (rsPromoMethod.next()) {
                    PromotionMethodDto promotionMethodDto = PromotionMethodDto.builder()
                            .id(rsPromoMethod.getInt("d_promotion_methods_id"))
                            .totalAmount(rsPromoMethod.getBigDecimal("total_amount"))
                            .productQty(rsPromoMethod.getBigDecimal("product_qty"))
                            .promoProductQty(rsPromoMethod.getBigDecimal("promo_product_qty"))
                            // .discountAmount(rsPromoMethod.getBigDecimal("discount_amount"))
                            .promoPercent(rsPromoMethod.getBigDecimal("promo_percent"))
                            .promoAmount(rsPromoMethod.getBigDecimal("promo_amount"))
                            .percentMaxAmt(rsPromoMethod.getBigDecimal("percent_max_amt"))
                            .salesPrice(rsPromoMethod.getBigDecimal("sales_price"))
                            .productId(rsPromoMethod.getInt("d_product_id") == 0 ? null : rsPromoMethod.getInt("d_product_id"))
                            .promoProductId(rsPromoMethod.getInt("d_promo_product_id") == 0 ? null : rsPromoMethod.getInt("d_promo_product_id"))
                            .productCategoryId(rsPromoMethod.getInt("d_product_category_id") == 0 ? null : rsPromoMethod.getInt("d_product_category_id"))
                            .promoCategoryId(rsPromoMethod.getInt("d_promo_category_id") == 0 ? null : rsPromoMethod.getInt("d_promo_category_id"))
                            .promotionId(rsPromoMethod.getInt("d_promotion_id"))
                            .productName(rsPromoMethod.getString("product_name"))
                            .promoProductName(rsPromoMethod.getString("promo_product_name"))
                            .productCategoryName(rsPromoMethod.getString("product_category_name"))
                            .promoCategoryName(rsPromoMethod.getString("promo_category_name"))
                            // thiếu pointQty
                            .build();
                    promotionMethodDtoList.add(promotionMethodDto);
                }
                List<PromotionTimeDto> promotionTimeDtoList = new ArrayList<>();
                Parameter parameterPromoTime = new Parameter();
                parameterPromoTime.add("d_promotion_id", item.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
                ResultSet rsPromoTime = queryEngine.getRecords("d_promotion_time", parameterPromoTime, request);
                while (rsPromoTime.next()) {
                    PromotionTimeDto promotionTimeDto = PromotionTimeDto.builder()
                            .id(rsPromoTime.getInt("d_promotion_time_id"))
                            .fromHour(rsPromoTime.getString("from_hour"))
                            .toHour(rsPromoTime.getString("to_hour"))
                            .promotionId(rsPromoTime.getInt("d_promotion_id"))
                            .isActive(rsPromoTime.getString("is_active"))
                            .build();
                    promotionTimeDtoList.add(promotionTimeDto);
                }

                List<PromoAssignOrgDto> promoAssignOrgList = new ArrayList<>();
                Parameter parameterPromoAssignOrg = new Parameter();
                parameterPromoAssignOrg.add("d_promotion_id", item.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
                ResultSet rsPromoAssignOrg = queryEngine.getRecords("d_promotion_assign_org_get_all_v", parameterPromoAssignOrg, request);
                while (rsPromoAssignOrg.next()) {
                    PromoAssignOrgDto promoAssignOrg = PromoAssignOrgDto.builder()
                            .id(rsPromoAssignOrg.getInt("d_promo_assign_org_id"))
                            .orgId(rsPromoAssignOrg.getInt("d_org_id"))
                            .promotionId(rsPromoAssignOrg.getInt("d_promotion_id"))
                            .nameOrg(rsPromoAssignOrg.getString("name"))
                            .code(rsPromoAssignOrg.getString("code"))
                            .isActive(rsPromoAssignOrg.getString("is_active"))
                            .build();
                    promoAssignOrgList.add(promoAssignOrg);
                }

                List<PromoBpGroupDto> promoBpGroupList = new ArrayList<>();
                Parameter parameterPromoBpGroup = new Parameter();
                parameterPromoBpGroup.add("d_promotion_id", item.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
                ResultSet rsPromoBpGroup = queryEngine.getRecords("d_promo_bp_group_get_all_v", parameterPromoBpGroup, request);
                while (rsPromoBpGroup.next()) {
                    PromoBpGroupDto promoBpGroup = PromoBpGroupDto.builder()
                            .id(rsPromoBpGroup.getInt("d_promo_bp_group_id"))
                            .bpGroupId(rsPromoBpGroup.getInt("d_bp_group_id"))
                            .promotionId(rsPromoBpGroup.getInt("d_promotion_id"))
                            .namePartnerGroup(rsPromoBpGroup.getString("name"))
                            .codePartnerGroup(rsPromoBpGroup.getString("code"))
                            .isActive(rsPromoBpGroup.getString("is_active"))
                            .build();
                    promoBpGroupList.add(promoBpGroup);
                }
                item.setPromoBpGroupDto(promoBpGroupList);
                item.setPromoAssignOrgDto(promoAssignOrgList);
                item.setPromotionTimeDto(promotionTimeDtoList);
                item.setPromotionMethodDto(promotionMethodDtoList);
                listResponse.add(item);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        Pagination pagination = queryEngine.getPagination("d_promotion_get_all_v", parameter, request);
        return GlobalReponsePagination.builder()
                .data(listResponse)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    /**
     * @param paramDto
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse save(PromotionDto paramDto) {
        log.info("save Promotion ");
        Promotion promotion;
        int status;
        String message;
        
        if (paramDto.getId() != null) {
            // update
            if (paramDto.getCode() != null && !paramDto.getCode().isEmpty()) {
                if (promotionRepository.existsByCodeAndIdNot(paramDto.getCode(), paramDto.getId())) {
                    throw new PosException(messageSource.getMessage("promotion_code_exist", null, LocaleContextHolder.getLocale()));
                }
            }

            promotion = this.promotionRepository.findById(paramDto.getId()).orElse(null);
            if (promotion == null) {
                throw new PosException(messageSource.getMessage("promotion_not_found", null, LocaleContextHolder.getLocale()));
            } else {
                modelMapper.map(paramDto, promotion);
            }

            if (paramDto.getFromDate() != null) {
                promotion.setFromDate(DateHelper.toInstantUTC(paramDto.getFromDate()));
            }
            if (paramDto.getToDate() != null) {
                promotion.setToDate(DateHelper.toInstantUTC(paramDto.getToDate()));
            }
            promotion.setUserId(AuditContext.getAuditInfo().getUserId());

            promotionRepository.save(promotion);

            status = HttpStatus.OK.value();
            message = messageSource.getMessage("promotion_updated", null, LocaleContextHolder.getLocale());

        } else {
            // insert
            promotion = modelMapper.map(paramDto, Promotion.class);
            if (promotion.getCode() == null || promotion.getCode().isEmpty())
                promotion.setCode("PROMO_" + (this.promotionRepository.getMaxPromotionId() + 1) + LocalDate.now().getDayOfMonth());
            else if (promotionRepository.existsByCode(promotion.getCode()))
                throw new PosException(messageSource.getMessage("promotion_code_exist", null, LocaleContextHolder.getLocale()));
            promotion.setOrgId(0);
            promotion.setTenantId(AuditContext.getAuditInfo().getTenantId());

            if (paramDto.getId() == null) {
                if (paramDto.getFromDate() == null || paramDto.getToDate() == null) {
                    throw new PosException("valid time is not passed for case Insert");
                }
            }
            promotion.setFromDate(DateHelper.toInstantUTC(paramDto.getFromDate()));
            promotion.setToDate(DateHelper.toInstantUTC(paramDto.getToDate()));
            promotion.setUserId(AuditContext.getAuditInfo().getUserId());

            promotionRepository.save(promotion);
            paramDto.setCode(promotion.getCode());
            paramDto.setId(promotion.getId());

            status = HttpStatus.CREATED.value();
            message = messageSource.getMessage("promotion_created", null, LocaleContextHolder.getLocale());
        }

        // PromotionLine
        paramDto.getPromotionMethodDto().forEach(promotionMethodDto -> {
            PromotionMethod promotionMethod = modelMapper.map(promotionMethodDto, PromotionMethod.class);
            promotionMethod.setPromotionId(promotion.getId());
            promotionMethod.setOrgId(0);
            promotionMethod.setTenantId(AuditContext.getAuditInfo().getTenantId());
            promotionMethodRepository.save(promotionMethod);
            promotionMethodDto.setId(promotionMethod.getId());
            promotionMethodDto.setPromotionId(promotion.getId());
        });

        // PromotionTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm[:ss]");
        paramDto.getPromotionTimeDto().forEach(promotionTimeDto -> { // thoi gian promotion
            PromotionTime promotionTime = modelMapper.map(promotionTimeDto, PromotionTime.class);
            promotionTime.setPromotionId(promotion.getId());
            promotionTime.setOrgId(0);
            promotionTime.setTenantId(AuditContext.getAuditInfo().getTenantId());
            if (promotionTimeDto.getFromHour() != null && promotionTimeDto.getToHour() != null) {
                promotionTime.setFromHour(LocalTime.parse(promotionTimeDto.getFromHour(), timeFormatter).plusHours(JDBC_AUTO_OFFSET));
                promotionTime.setToHour(LocalTime.parse(promotionTimeDto.getToHour(), timeFormatter).plusHours(JDBC_AUTO_OFFSET));
//                // JVM default: UTC+8
//                ZoneId systemZone = ZoneId.systemDefault(); // → Asia/Ho_Chi_Minh or similar
//                log.info("JVM time zone: {}", systemZone); // For debugging
//                // Step 1: Parse string input as LocalTime (assume UTC)
//                LocalTime fromInputLocal = LocalTime.parse(promotionTimeDto.getFromHour(), timeFormatter);
//                LocalTime toInputLocal = LocalTime.parse(promotionTimeDto.getToHour(), timeFormatter);
//                log.info("LocalTime fromInputLocal: {}", fromInputLocal);
//                log.info("LocalTime toInputLocal: {}", toInputLocal);
//                // Step 2: Attach UTC zone to parsed LocalTime (pretend it was UTC)
//                ZonedDateTime fromInputUTC = ZonedDateTime.of(LocalDate.now(), fromInputLocal, ZoneOffset.UTC); // 07:30 UTC
//                ZonedDateTime toInputUTC = ZonedDateTime.of(LocalDate.now(), toInputLocal, ZoneOffset.UTC);     // 11:30 UTC
//                log.info("ZonedDateTime fromInputUTC: {}", fromInputUTC);
//                log.info("ZonedDateTime toInputUTC: {}", toInputUTC);
//                // Step 3: Convert to system default zone (e.g., UTC+8)
//                ZonedDateTime fromSystemZone = fromInputUTC.withZoneSameInstant(systemZone); // → 15:30 system time
//                ZonedDateTime toSystemZone = toInputUTC.withZoneSameInstant(systemZone);     // → 19:30 system time
//                log.info("ZonedDateTime fromSystemZone: {}", fromSystemZone);
//                log.info("ZonedDateTime toSystemZone: {}", toSystemZone);
//                // Step 4: Extract LocalTime (so Hibernate/JDBC will store it as-is)
//                LocalTime fromSystemLocal = fromSystemZone.toLocalTime(); // → 15:30:00
//                LocalTime toSystemLocal = toSystemZone.toLocalTime();     // → 19:30:00
//                log.info("LocalTime fromSystemLocal: {}", fromSystemLocal);
//                log.info("LocalTime toSystemLocal: {}", toSystemLocal);
//                // Final assignment to entity
//                promotionTime.setFromHour(fromSystemLocal);
//                promotionTime.setToHour(toSystemLocal);
            }
            promotionTimeRepository.save(promotionTime);
            promotionTimeDto.setId(promotionTime.getId());
            promotionTimeDto.setPromotionId(promotion.getId());
        });

        // bPartnerGr and Org assignments
        this.promoAssignOrgRepository.updateAllByPromotionId(promotion.getId(), "N");
        this.promoBpGroupRepository.updateAllByPromotionId(promotion.getId(), "N");
        if (paramDto.getIsAllOrg().equals("N")) {
            paramDto.getPromoAssignOrgDto().forEach(promoAssignOrgDto -> {
                PromoAssignOrg orgDto = this.promoAssignOrgRepository.findByPromotionIdAndOrgId(promotion.getId(), promoAssignOrgDto.getOrgId()).orElse(null);
                if (orgDto == null) {
                    orgDto = PromoAssignOrg.builder()
                            .orgId(promoAssignOrgDto.getOrgId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .promotionId(promotion.getId())
                            .build();
                }
                orgDto.setIsActive("Y");
                promoAssignOrgRepository.save(orgDto);
            });
        }
        if (paramDto.getIsAllBpartner().equals("N")) {
            paramDto.getPromoBpGroupDto().forEach(promoBpGroupDto -> {
                PromoBpGroup bpGroup = this.promoBpGroupRepository.findByPromotionIdAndBpGroupId(promotion.getId(), promoBpGroupDto.getBpGroupId()).orElse(null);
                if (bpGroup == null) {
                    bpGroup = PromoBpGroup.builder()
                            .bpGroupId(promoBpGroupDto.getBpGroupId())
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .promotionId(promotion.getId())
                            .orgId(0)
                            .build();
                }
                bpGroup.setIsActive("Y");
                promoBpGroupRepository.save(bpGroup);
            });
        }

        return GlobalReponse.builder()
                .status(status)
                .data(paramDto)
                .message(message).build();
    }

    @Transactional
    @Override
    public GlobalReponse delete(Integer id) {

        //check if used by posOrder, if yes then ?

        Promotion promotionEntity = promotionRepository.findById(id)
                .orElseThrow(() -> new PosException(messageSource.getMessage("promotion_not_found", null, LocaleContextHolder.getLocale())));
        PromotionDto promotionDto = modelMapper.map(promotionEntity, PromotionDto.class);

        List<PromotionTime> promotionTimeList = promotionTimeRepository.findByPromotionId(id);
        promotionTimeRepository.deleteAll(promotionTimeList);
        List<PromotionMethod> promotionMethodList = promotionMethodRepository.findByPromotionId(id);
        promotionMethodRepository.deleteAll(promotionMethodList);
        List<PromoBpGroup> promoBpGroupList= promoBpGroupRepository.findByPromotionId(id);
        promoBpGroupRepository.deleteAll(promoBpGroupList);
        List<PromoAssignOrg> promoAssignOrgList = promoAssignOrgRepository.findByPromotionId(id);
        promoAssignOrgRepository.deleteAll(promoAssignOrgList);

        promotionRepository.deleteById(id);

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(promotionDto)
                .build();
    }

    @Override
    public GlobalReponse getApplicablePromos(ApplicablePromoReqDto reqDto) {

        ApplicablePromoResDto response = new ApplicablePromoResDto();

        // compute totalAmount if not passed
        if (reqDto.getTotalAmount() == null) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PosOrderLineVAllDto productDto : reqDto.getProducts()) {
                totalAmount = totalAmount.add(productDto.getSalesPrice().multiply(productDto.getQty()));
            }
            reqDto.setTotalAmount(totalAmount);
        }
//        // compute totalQty
//        BigDecimal originalTotalQty = BigDecimal.ZERO;
//        for (PosOrderLineVAllDto productDto : reqDto.getProducts()) {
//            originalTotalQty = originalTotalQty.add(productDto.getQty());
//        }
        // set original amt, qty in response
        response.setOriginalTotal(reqDto.getTotalAmount());
//        response.setOriginalTotalQty(originalTotalQty);

        // total product qty per product
        Map<Integer, BigDecimal> qtyByProduct = reqDto.getProducts().stream()
                .filter(product -> product.getPromotionIds() == null)
                .collect(Collectors.toMap(
                        PosOrderLineVAllDto::getProductId,
                        PosOrderLineVAllDto::getQty,
                        BigDecimal::add
                ));
        // total line/product qty per category
        Map<Integer, BigDecimal> qtyByCategory = reqDto.getProducts().stream()
                .filter(product -> product.getPromotionIds() == null)
                .collect(Collectors.toMap(
                        PosOrderLineVAllDto::getProductCategoryId,
                        product -> BigDecimal.ONE, // line qty
//                        PosOrderLineVAllDto::getQty, // product qty
                        BigDecimal::add
                ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime orderDate = LocalDateTime.parse(reqDto.getOrderDate(), formatter).atZone(ZoneOffset.UTC);
//        log.info("String reqDto.getOrderDate(): {}", reqDto.getOrderDate());
//        log.info("LocalDateTime orderDate: {}", orderDate);

        // load all valid promotions from db
        String sql = "SELECT * FROM pos.d_get_valid_promotion(:orgId, :customerId, :tenantId, :orderTs)";

        List<Map<String, Object>> promoResults = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", reqDto.getOrgId())
                .setParameter("customerId", reqDto.getCustomerId())
                .setParameter("orderTs", orderDate)
                .unwrap(NativeQuery.class)
//                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

//        // log returned valid promo
//        ObjectMapper mapper = new ObjectMapper();
//        for (int i = 0; i < promoResults.size(); i++) {
//            try {
//                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(promoResults.get(i));
//                log.info("Row {}: {}", i, json);
//            } catch (Exception e) {
//                log.warn("Failed to serialize row {}", i, e);
//            }
//        }

        Map<Integer, PromotionDto> promoMap = new LinkedHashMap<>();
        for (Map<String, Object> row : promoResults) {
            Integer promotionId = ParseHelper.INT.parse(row.get("d_promotion_id"));
            PromotionDto promotionDto = promoMap.computeIfAbsent(promotionId, id -> PromotionDto.builder()
                    .id(id)
                    .code(ParseHelper.STRING.parse(row.get("code")))
                    .name(ParseHelper.STRING.parse(row.get("name")))
//                    .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                    .promotionBasedOn(ParseHelper.STRING.parse(row.get("promotion_based_on")))
                    .promotionBasedOnName(ParseHelper.STRING.parse(row.get("promotion_based_on_name")))
                    .promotionType(ParseHelper.STRING.parse(row.get("promotion_type")))
                    .promotionTypeName(ParseHelper.STRING.parse(row.get("promotion_type_name")))
                    .isApplyBirthday(ParseHelper.STRING.parse(row.get("is_apply_birthday")))
                    .isWarnIfUsed(ParseHelper.STRING.parse(row.get("is_warn_if_used")))
                    .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                    .fromDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("from_date"))))
                    .toDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("to_date"))))
//                    .byDate(ParseHelper.STRING.parse(row.get("by_date")))
//                    .byMonth(ParseHelper.STRING.parse(row.get("by_month")))
//                    .excludedDate(ParseHelper.STRING.parse(row.get("excluded_date")))
//                    .isAllOrg(ParseHelper.STRING.parse(row.get("is_all_org")))
//                    .isAllBpartner(ParseHelper.STRING.parse(row.get("is_all_bpartner")))
                    .isScaleWithQty(ParseHelper.STRING.parse(row.get("is_scale_with_qty")))
                    .promotionMethodDto(new ArrayList<>())
                    .build()
            );

            Integer condProdId = ParseHelper.INT.parse(row.get("d_product_id"));
            Integer condCateId = ParseHelper.INT.parse(row.get("d_product_category_id"));
            Integer promoProdId = ParseHelper.INT.parse(row.get("d_promo_product_id"));
            Integer promoCateId = ParseHelper.INT.parse(row.get("d_promo_category_id"));
            PromotionMethodDto promotionMethodDto = PromotionMethodDto.builder()
                    .id(ParseHelper.INT.parse(row.get("d_promotion_methods_id")))
                    .promotionId(ParseHelper.INT.parse(row.get("d_promotion_id")))
                    .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                    .productId(condProdId)
                    .productCategoryId(condCateId)
                    .condProduct(condProdId != null ?
                            ProductDto.builder()
                                    .id(condProdId)
                                    .code(ParseHelper.STRING.parse(row.get("cond_prod_code")))
                                    .name(ParseHelper.STRING.parse(row.get("cond_prod_name")))
                                    .saleprice(ParseHelper.BIGDECIMAL.parse(row.get("cond_prod_price")))
                                    .productCategory(ProductCategoryDto.builder()
                                            .id(ParseHelper.INT.parse(row.get("cond_prod_cate_id")))
                                            .code(ParseHelper.STRING.parse(row.get("cond_prod_cate_code")))
                                            .name(ParseHelper.STRING.parse(row.get("cond_prod_cate_name")))
                                            .build())
                                    .image(ImageDto.builder()
                                            .imageCode(ParseHelper.STRING.parse(row.get("cond_prod_img_code")))
                                            .imageUrl(ParseHelper.STRING.parse(row.get("cond_prod_img_url")))
                                            .build())
                                    .build() :
                            null)
                    .condCategory(condCateId != null ?
                            ProductCategoryDto.builder()
                                    .id(condCateId)
                                    .code(ParseHelper.STRING.parse(row.get("cond_cate_code")))
                                    .name(ParseHelper.STRING.parse(row.get("cond_cate_name")))
                                    .imageDto(ImageDto.builder()
                                            .imageCode(ParseHelper.STRING.parse(row.get("cond_cate_img_code")))
                                            .imageUrl(ParseHelper.STRING.parse(row.get("cond_cate_img_url")))
                                            .build())
                                    .build() :
                            null)
                    .productQty(ParseHelper.BIGDECIMAL.parse(row.get("product_qty")))
                    .promoProductId(promoProdId)
                    .promoCategoryId(promoCateId)
                    .promoProduct(promoProdId != null ?
                            ProductDto.builder()
                                    .id(promoProdId)
                                    .code(ParseHelper.STRING.parse(row.get("promo_prod_code")))
                                    .name(ParseHelper.STRING.parse(row.get("promo_prod_name")))
                                    .saleprice(ParseHelper.BIGDECIMAL.parse(row.get("promo_prod_price")))
                                    .productCategory(ProductCategoryDto.builder()
                                            .id(ParseHelper.INT.parse(row.get("promo_prod_cate_id")))
                                            .code(ParseHelper.STRING.parse(row.get("promo_prod_cate_code")))
                                            .name(ParseHelper.STRING.parse(row.get("promo_prod_cate_name")))
                                            .build())
                                    .image(ImageDto.builder()
                                            .imageCode(ParseHelper.STRING.parse(row.get("promo_prod_img_code")))
                                            .imageUrl(ParseHelper.STRING.parse(row.get("promo_prod_img_url")))
                                            .build())
                                    .build() :
                            null)
                    .promoCategory(promoCateId != null ?
                            ProductCategoryDto.builder()
                                    .id(promoCateId)
                                    .code(ParseHelper.STRING.parse(row.get("promo_cate_code")))
                                    .name(ParseHelper.STRING.parse(row.get("promo_cate_name")))
                                    .imageDto(ImageDto.builder()
                                            .imageCode(ParseHelper.STRING.parse(row.get("promo_cate_img_code")))
                                            .imageUrl(ParseHelper.STRING.parse(row.get("promo_cate_img_url")))
                                            .build())
                                    .build() :
                            null)
                    .promoProductQty(ParseHelper.BIGDECIMAL.parse(row.get("promo_product_qty")))
                    .promoPercent(ParseHelper.BIGDECIMAL.parse(row.get("promo_percent")))
                    .promoAmount(ParseHelper.BIGDECIMAL.parse(row.get("promo_amount")))
                    .percentMaxAmt(ParseHelper.BIGDECIMAL.parse(row.get("percent_max_amt")))
                    .salesPrice(ParseHelper.BIGDECIMAL.parse(row.get("sales_price")))
                    .pointQty(ParseHelper.BIGDECIMAL.parse(row.get("point_qty")))
                    .build();
            promotionDto.getPromotionMethodDto().add(promotionMethodDto);
        }

        List<PromotionDto> promotionDtoList = new ArrayList<>(promoMap.values());
        Map<String, List<PromotionDto>> promosByType = promotionDtoList.stream()
                .collect(Collectors.groupingBy(PromotionDto::getPromotionType));

        List<PromotionDto> promotionDtoResultList = new ArrayList<>();
        for (Map.Entry<String, GetApplicablePromoStrategy> stratEntry : getPromoStratMap.entrySet()) {
            String promoType = stratEntry.getKey();
            GetApplicablePromoStrategy strat = stratEntry.getValue();
            List<PromotionDto> handledPromos = promosByType.getOrDefault(promoType, List.of());
            promotionDtoResultList.addAll(strat.getApplicablePromo(qtyByProduct, qtyByCategory, reqDto.getTotalAmount(), handledPromos));
        }

        response.setPromotionList(promotionDtoResultList);
//        response.setFinalTotal(BigDecimal.ZERO);
//        response.setTotalDiscount(BigDecimal.ZERO);

        return GlobalReponse.builder()
                .data(response)
                .message("Get Applicable Promo success")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * @param assignOrgId
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse deleteAssignOrg(Integer assignOrgId) {
        if (!this.promoAssignOrgRepository.existsById(assignOrgId))
            throw new PosException(messageSource.getMessage("promotion_assign_org_not_found", null, LocaleContextHolder.getLocale()));
        this.promoAssignOrgRepository.updateIsActiveById(assignOrgId, "N");
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("promotion_assign_org_deleted", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * @param partnerGroupId
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse deleteAssignBpartnerGroup(Integer partnerGroupId) {
        if (!this.promoBpGroupRepository.existsById(partnerGroupId))
            throw new PosException(messageSource.getMessage("promotion_bp_group_not_found", null, LocaleContextHolder.getLocale()));
        this.promoBpGroupRepository.updateIsActiveById(partnerGroupId, "N");
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("promotion_bp_group_deleted", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override

    public GlobalReponsePagination getAllPromotionForCahiser(PromotionQueryRequest request) {
        log.info("*** Get All Promotion For Cahiser ***");
        List<PromotionDto> listResponse = new ArrayList<>();
        Parameter parameter = new Parameter();
        StringBuilder message = new StringBuilder(messageSource.getMessage("promotion_empty", null, LocaleContextHolder.getLocale()));
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(request.getCurrentDate(), formatter);
            String date = dateTime.toLocalDate().toString(); // yyyy-mm-dd
            String time = dateTime.toLocalTime().toString(); // HH:mm:ss
            // dieu kien query: lay ra cac cktm khong thuoc cac ngay excluded_date, duoc assign cho org, partner group, active, tu ngay den ngay , nam trong ngày tháng , giowf
            Set<Integer> uniqueLists = new HashSet<>();
            uniqueLists.addAll(this.promoAssignOrgRepository.findPromotionIdByOrgIdAndIsActiveReponseSet(request.getOrgId(), "Y"));
            uniqueLists.addAll( this.promoBpGroupRepository.findPromotionIdByBpGroupIdAndIsActive(request.getBpGroupId(), "Y"));
            Set<Integer> uniqueIdHours =  new HashSet<>(this.promotionTimeRepository.findPromotionIdByHour(new Time(sdf.parse(time).getTime()), "Y"));
            uniqueIdHours.retainAll(uniqueLists);

            if (uniqueLists.isEmpty()) {
                return GlobalReponsePagination.builder()
                        .data(listResponse)
                        .message(message.toString())
                        .status(HttpStatus.OK.value())
                        .pageSize(0)
                        .currentPage(0)
                        .totalPages(0)
                        .totalItems(0L)
                        .build();
            }

            parameter.add("d_promotion_id", uniqueLists, Param.Logical.IN, Param.Relational.OR, Param.START);
            parameter.add("is_all_bpartner", "Y", Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
            parameter.add("is_all_org", "Y", Param.Logical.EQUAL, Param.Relational.AND, Param.END);

            parameter.add("is_active", "Y", Param.Logical.EQUAL, Param.Relational.AND, Param.START);
            parameter.add("from_date", request.getCurrentDate(), Param.Logical.LESS_THAN_EQ, Param.Relational.AND, Param.NONE);
            parameter.add("to_date", request.getCurrentDate(), Param.Logical.GREATER_THAN_EQ, Param.Relational.OR, Param.NONE);
            parameter.add("d_promotion_id",uniqueIdHours, Param.Logical.IN, Param.Relational.OR, Param.NONE);
            parameter.add("by_date", String.format("%02d", dateTime.getDayOfMonth()), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
            parameter.add("by_date", String.format("%02d", dateTime.getMonthValue()), Param.Logical.EQUAL, Param.Relational.AND, Param.END);
            if (request.getCode() != null && !request.getCode().isBlank())
                parameter.add("code", request.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
            List<Tuple> rsNative = entityManager.createNativeQuery("select d_promotion_id from d_promotion where :currentDate IN (SELECT unnest(string_to_array(excluded_date, ',')) FROM d_promotion_get_all_v) ", Tuple.class)
                    .setParameter("currentDate", date).getResultList();
            List<Integer> promotionIdExcluded = new ArrayList<>();
            if (!rsNative.isEmpty()) {
                rsNative.forEach(tuple -> {
                    promotionIdExcluded.add(Integer.valueOf(tuple.get("d_promotion_id").toString()));
                });
            }
            if (!promotionIdExcluded.isEmpty()) {
                parameter.add("d_promotion_id", promotionIdExcluded, Param.Logical.NOT_IN, Param.Relational.NONE, Param.NONE);
            }
            ResultSet rs = queryEngine.getRecords("d_promotion_get_all_v", parameter, request);

            while (rs.next()) {
                PromotionDto item = PromotionDto.builder()
                        .id(rs.getInt("d_promotion_id"))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .byDate(rs.getString("by_date"))
                        .byMonth(rs.getString("by_month"))
                        .isAllOrg(rs.getString("is_all_org"))
                        .isAllBpartner(rs.getString("is_all_bpartner"))
                        .isApplyBirthday(rs.getString("is_apply_birthday")) // .applyBirthday(rs.getString("apply_birthday"))
                        .isWarnIfUsed(rs.getString("is_warn_if_used")) // .isUsed(rs.getString("is_used"))
                        .fromDate(rs.getString("from_date"))
                        .toDate(rs.getString("to_date"))
                        .excludedDate(rs.getString("excluded_date"))
                        .promotionBasedOn(rs.getString("promotion_based_on"))
                        .promotionType(rs.getString("promotion_type"))
                        .qty(rs.getBigDecimal("qty"))
                        .promotionBasedOnName(rs.getString("promotion_based_on_name"))
                        .promotionTypeName(rs.getString("promotion_type_name"))
                        .isActive(rs.getString("is_active"))
                        .build();
                Parameter parameterPromoMethod = new Parameter();
                parameterPromoMethod.add("d_promotion_id", item.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
                ResultSet rsPromoMethod = queryEngine.getRecords("d_promotion_methods_get_all_v", parameterPromoMethod, request);
                List<PromotionMethodDto> promotionMethodDtoList = new ArrayList<>();
                while (rsPromoMethod.next()) {
                    PromotionMethodDto promotionMethodDto = PromotionMethodDto.builder()
                            .id(rsPromoMethod.getInt("d_promotion_methods_id"))
                            .totalAmount(rsPromoMethod.getBigDecimal("total_amount"))
                            // .discountAmount(rsPromoMethod.getBigDecimal("discount_amount"))
                            .promoPercent(rsPromoMethod.getBigDecimal("promo_percent"))
                            .promoAmount(rsPromoMethod.getBigDecimal("promo_amount"))
                            .percentMaxAmt(rsPromoMethod.getBigDecimal("percent_max_amt"))
                            .productId(rsPromoMethod.getInt("d_product_id") == 0 ? null : rsPromoMethod.getInt("d_product_id"))
                            .promoProductId(rsPromoMethod.getInt("d_promo_product_id") == 0 ? null : rsPromoMethod.getInt("d_promo_product_id"))
                            .productCategoryId(rsPromoMethod.getInt("d_product_category_id") == 0 ? null : rsPromoMethod.getInt("d_product_category_id"))
                            .promoCategoryId(rsPromoMethod.getInt("d_promo_category_id") == 0 ? null : rsPromoMethod.getInt("d_promo_category_id"))
                            .promotionId(rsPromoMethod.getInt("d_promotion_id"))
                            .productName(rsPromoMethod.getString("product_name"))
                            .promoProductName(rsPromoMethod.getString("promo_product_name"))
                            .productCategoryName(rsPromoMethod.getString("product_category_name"))
                            .promoCategoryName(rsPromoMethod.getString("promo_category_name"))
                            .pointQty(rsPromoMethod.getBigDecimal("point_qty"))
                            .productQty(rsPromoMethod.getBigDecimal("product_qty"))
                            .salesPrice(rsPromoMethod.getBigDecimal("sales_price"))
                            .build();
                    promotionMethodDtoList.add(promotionMethodDto);
                }
//                List<PromotionMethodDto> maxMethod = promotionMethodDtoList.stream()
//                        .filter(p -> p.getDiscountAmount() != null)
//                        .max(Comparator.comparing(PromotionMethodDto::getDiscountAmount))
//                        .map(List::of)
//                        .orElseGet(ArrayList::new);

                List<PromotionMethodDto> maxMethod = promotionMethodDtoList.stream()
                        .max(Comparator.comparing(
                                PromotionMethodDto::getPromoAmount, //PromotionMethodDto::getDiscountAmount, // sai logic (cả 2 versions), phải xét cả 2 percent và amount
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ))
                        .map(List::of)
                        .orElseGet(ArrayList::new);
                item.setPromotionMethodDto(maxMethod);

                listResponse.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }
        if (!listResponse.isEmpty())
            message =  new StringBuilder(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        Pagination pagination = queryEngine.getPagination("d_promotion_get_all_v", parameter, request);
        return GlobalReponsePagination.builder()
                .data(listResponse)
                .message(message.toString())
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

}
