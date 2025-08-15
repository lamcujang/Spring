package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.domain.BusinessSector;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.PriceListProduct;
import com.dbiz.app.productservice.domain.Product;
import com.dbiz.app.productservice.dto.PriceListAndProduct;
import com.dbiz.app.productservice.helper.Mapper.PriceListProductMapper;
import com.dbiz.app.productservice.repository.BusinessSectorRepository;
import com.dbiz.app.productservice.repository.PriceListProductRepository;
import com.dbiz.app.productservice.repository.PriceListRepository;
import com.dbiz.app.productservice.repository.ProductRepository;
import com.dbiz.app.productservice.service.PriceListProductService;
import com.dbiz.app.productservice.specification.PriceListProductSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.productDto.request.PriceListProductReqDto;
import org.common.dbiz.dto.userDto.UserOrgAccessDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ApplyNewFormulaPriceRequest;
import org.common.dbiz.request.productRequest.FindAllPriceListAndProductRequest;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;

import com.dbiz.app.tenantservice.helper.RequestParamsUtils;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class PriceListProductServiceImpl implements PriceListProductService {
    private final PriceListProductRepository entityRepository;

    private final ProductRepository productRepository;

    private final BusinessSectorRepository businessSectorRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final MessageSource messageSource;

    private final QueryEngine queryEngine;

    private final PriceListRepository priceListRepository;

    private final PriceListProductMapper priceListProductMapper;

    private final KafkaAuditUserHelper kafkaAuditUserHelper;

    private final ObjectMapper objectMapper;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GlobalReponsePagination findAll(PriceListProductQueryRequest queryParam) {
        log.info("*** Pricelist List, service; fetch all Pricelist *");
        Pageable pageable = requestParamsUtils.getPageRequest(queryParam);


        Specification<PriceListProduct> spec = PriceListProductSpecification.getEntitySpecification(queryParam);
        GlobalReponsePagination response = new GlobalReponsePagination();
        List<Integer> idsProduct = new ArrayList<>();
        if (queryParam.getProductCategoryId() != null) {
            idsProduct.addAll(productRepository.getIdByProductCategoryId(queryParam.getProductCategoryId()));
        }
        if (queryParam.getKeyword() != null) {
            idsProduct.addAll(productRepository.getIdByKeyWord(queryParam.getKeyword()));
        }
        if (!idsProduct.isEmpty()) {
            Specification<PriceListProduct> priceListSpec = (root, query, criteriaBuilder) ->
                    root.get("productId").in(idsProduct);
            spec = spec.and(priceListSpec);
        }

        Page<PriceListProduct> entityList = entityRepository.findAll(spec, pageable);
        List<PriceListProductDto> listData = new ArrayList<>();

        for (PriceListProduct item : entityList.getContent()) {
            Optional<Product> product = productRepository.findById(item.getProductId());

            ProductDto productDto = new ProductDto();
            if (product.isEmpty()) {
                throw new ObjectNotFoundException(String.format(messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()), item.getProductId()));
            }
            productDto.setId(product.get().getId());
            productDto.setName(product.get().getName());
            productDto.setIsActive(product.get().getIsActive());
            productDto.setCode(product.get().getCode());

            if (product.get().getUom() != null)
                productDto.setUom(modelMapper.map(product.get().getUom(), UomDto.class));


            PriceListProductDto priceListProductDto = modelMapper.map(item, PriceListProductDto.class);
            priceListProductDto.setProduct(productDto);
            listData.add(priceListProductDto);
        }
        response.setMessage(messageSource.getMessage("price.list.product.fetch.success", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        List<Integer> page = new LinkedList<>();
        return response;
    }

    @Override
    public GlobalReponse save(PriceListProductDto paramDto) {
        log.info("*** pricelist, service; save pricelist ***");
        GlobalReponse response = new GlobalReponse();
        PriceListProduct priceListProductSave = priceListProductMapper.toEntity(paramDto);
        PriceListProduct entitySave = priceListProductMapper.toEntity(paramDto);

        if (entitySave.getId() != null) // update
        {
            Integer id = entitySave.getId();
            entitySave = this.entityRepository.findById(paramDto.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            String.format(messageSource.getMessage("price.list.product.not.found", null, LocaleContextHolder.getLocale()), id)
                    ));
            response.setMessage(messageSource.getMessage("price.list.product.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());


            priceListProductMapper.updateEntity(paramDto, entitySave);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            this.entityRepository.save(entitySave);

            // check bang gia chung
            PriceListProduct priceListGeneral = entityRepository.findByProductIdAndTenantId(AuditContext.getAuditInfo().getTenantId(), priceListProductSave.getId())
                    .orElse(null);
            if (priceListGeneral != null) {
                List<PriceListProduct> updatePriceList = entityRepository.findAllNotInGeneralPriceList(priceListGeneral.getId(), priceListProductSave.getProductId());
                for (PriceListProduct item : updatePriceList) {
                    item.setStandardPrice(priceListProductSave.getSalesPrice());
                    item.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    item.setOrgId(0);
                    entityRepository.save(item);
                }

            }
            PriceList priceList = priceListRepository.findPriceListByGeneralPriceList(AuditContext.getAuditInfo().getTenantId(), paramDto.getPriceListId());
            if (priceList != null) { // cap nhat gia cho product neu la bang gia chung
                Product productDto = productRepository.findById(paramDto.getProductId()).get();
                productDto.setSaleprice(paramDto.getSalesPrice());
                productRepository.save(productDto);
            }


        } else {// insert
            if (paramDto.getProductCategoryId() != null) {

                List<Product> listProduct = productRepository.findByProductCategoryId(paramDto.getProductCategoryId());
                if (!listProduct.isEmpty()) {
                    for (Product item : listProduct) {
                        PriceListProduct priceListGeneral = entityRepository.findByProductIdAndTenantId(item.getId(), AuditContext.getAuditInfo().getTenantId())
                                .orElse(null);

                        PriceListProduct priceListProduct = entityRepository.findByPriceListAndProductId(paramDto.getPriceListId(), item.getId()).orElse(null);
                        if (priceListProduct == null) {
                            priceListProductSave = priceListProductMapper.toEntity(paramDto);
                            priceListProductSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            priceListProductSave.setProductId(item.getId());
                            priceListProductSave.setStandardPrice(priceListGeneral.getSalesPrice());
                            priceListProductSave.setCostPrice(priceListGeneral.getCostPrice());
                            priceListProductSave.setSalesPrice(priceListGeneral.getSalesPrice());
                            priceListProductSave.setLastOrderPrice(priceListGeneral.getLastOrderPrice());
                            priceListProductSave.setOrgId(0);
                            this.entityRepository.save(priceListProductSave);
                        }

                    }
                }

            }
            if (paramDto.getProductId() != null) { // case update 1 product

                priceListProductSave = entityRepository.findByPriceListAndProductId(paramDto.getPriceListId(), paramDto.getProductId()).orElse(null);

                if (priceListProductSave == null) { //  insert
                    priceListProductMapper.toEntity(paramDto);
                    PriceListProduct priceListGeneral = entityRepository.findByProductIdAndTenantId(paramDto.getProductId(), AuditContext.getAuditInfo().getTenantId())
                            .orElse(null);
                    priceListProductSave = priceListProductMapper.toEntity(paramDto);
                    priceListProductSave.setStandardPrice(priceListGeneral.getSalesPrice());
                    priceListProductSave.setCostPrice(priceListGeneral.getCostPrice());
                    priceListProductSave.setSalesPrice(priceListGeneral.getSalesPrice());
                    priceListProductSave.setLastOrderPrice(priceListGeneral.getLastOrderPrice());
                    priceListProductSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    priceListProductSave.setOrgId(0);
                    this.entityRepository.save(priceListProductSave);
                } else {// update
                    // check bang gia chung
                    PriceListProduct priceListGeneral = entityRepository.findByProductIdAndTenantId(AuditContext.getAuditInfo().getTenantId(), priceListProductSave.getId())
                            .orElse(null);
                    priceListProductMapper.updateEntity(paramDto, priceListProductSave);
                    this.entityRepository.save(priceListProductSave);
                    if (priceListGeneral != null) {
                        List<PriceListProduct> updatePriceList = entityRepository.findAllNotInGeneralPriceList(priceListGeneral.getId(), priceListProductSave.getProductId());
                        for (PriceListProduct item : updatePriceList) {
                            item.setStandardPrice(priceListProductSave.getStandardPrice());
                            entityRepository.save(item);
                        }
                    }
                }

            }
            paramDto = priceListProductMapper.toDto(priceListProductSave);
            response.setData(paramDto);
            response.setMessage(messageSource.getMessage("price.list.product.created", null, LocaleContextHolder.getLocale()));

        }

        response.setData(paramDto);
        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());
        return response;

    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete pricelist by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<PriceListProduct> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("price.list.product.not.found", null, LocaleContextHolder.getLocale()), id));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("price.list.product.deleted", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** Pricelist, service; fetch Pricelist by id *");

        GlobalReponse response = new GlobalReponse();
        try {
            response.setData(priceListProductMapper.toDto(this.entityRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("price.list.product.not.found", null, LocaleContextHolder.getLocale()), id)))));
            response.setMessage(messageSource.getMessage("price.list.product.fetch.success", null, LocaleContextHolder.getLocale()));
            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setData(null);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    @Override
    public GlobalReponse deleteAllByIds(PriceListProductDto.IdsRequest ids) {
        log.info("*** Void, service; delete all pricelist by ids *");
        GlobalReponse response = new GlobalReponse();

        for (Integer i : ids.getIds()) {
            entityRepository.deleteById(i);
        }
        response.setData(null);
        response.setMessage(messageSource.getMessage("price.list.product.deleted", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponse applyAllNewFormulaPrice(ApplyNewFormulaPriceRequest request) {
        log.info("*** Void, service; apply all new formula price *");
        GlobalReponse response = new GlobalReponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("price.list.product.updated", null, LocaleContextHolder.getLocale()));
        response.setErrors("");
        PriceList priceListGeneral = priceListRepository.findPriceListByGeneralPriceList(AuditContext.getAuditInfo().getTenantId(), request.getPriceListId());

        if (!request.getIsAll()) {
            PriceListProduct priceListProduct = entityRepository.findByPriceListAndProductId(request.getPriceListId(), request.getProductId())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("price.list.product.not.found", null, LocaleContextHolder.getLocale()), request.getProductId())));
            priceListProduct.setSalesPrice(request.getSalesPrice());
            entityRepository.save(priceListProduct);
            response.setData(priceListProductMapper.toDto(priceListProduct));
            if (priceListGeneral != null) {
                Product productDto = productRepository.findById(request.getProductId()).get();
                productDto.setSaleprice(request.getSalesPrice());
                productRepository.save(productDto);
            }
        } else {
            List<PriceListProduct> listPriceListProduct = entityRepository.findAllByPriceListId(request.getPriceListId());
            List<PriceListProductDto> listData = new ArrayList<>();

            for (PriceListProduct item : listPriceListProduct) {
                if (request.getPercent().equals("Y")) { //  case apply by percent
                    BigDecimal percent = request.getOperandB().divide(new BigDecimal(100));

                    if (request.getOperandA().equals("salesPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getSalesPrice().add(item.getSalesPrice().multiply(percent)) : item.getSalesPrice().subtract(item.getSalesPrice().multiply(percent)));
                    if (request.getOperandA().equals("standardPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getStandardPrice().add(item.getStandardPrice().multiply(percent)) : item.getStandardPrice().subtract(item.getStandardPrice().multiply(percent)));
                    if (request.getOperandA().equals("costPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getCostPrice().add(item.getCostPrice().multiply(percent)) : item.getCostPrice().subtract(item.getCostPrice().multiply(percent)));
                    if (request.getOperandA().equals("lastOrderPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getLastOrderPrice().add(item.getLastOrderPrice().multiply(percent)) : item.getLastOrderPrice().subtract(item.getLastOrderPrice().multiply(percent)));
                } else {
                    if (request.getOperandA().equals("salesPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getSalesPrice().add(request.getOperandB()) : item.getSalesPrice().subtract(request.getOperandB()));
                    if (request.getOperandA().equals("standardPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getStandardPrice().add(request.getOperandB()) : item.getStandardPrice().subtract(request.getOperandB()));
                    if (request.getOperandA().equals("costPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getCostPrice().add(request.getOperandB()) : item.getCostPrice().subtract(request.getOperandB()));
                    if (request.getOperandA().equals("lastOrderPrice"))
                        item.setSalesPrice(request.getCalculation().equals("+") ? item.getLastOrderPrice().add(request.getOperandB()) : item.getLastOrderPrice().subtract(request.getOperandB()));

                }
                if (priceListGeneral != null) {
                    Product productDto = productRepository.findById(item.getProductId()).get();
                    productDto.setSaleprice(item.getSalesPrice());
                    productRepository.save(productDto);
                }
                entityRepository.save(item);
                listData.add(priceListProductMapper.toDto(item));
            }
            response.setData(listData);
        }
        try { // Send Kafka save Audit for user
            PriceList priceList = priceListRepository.findById(request.getPriceListId()).get();
            Map<String, String> priceListInfo = Map.of(
                    "name", priceList.getName()
            );
            String priceListInfoJson = objectMapper.writeValueAsString(priceListInfo);

            // Send Kafka save AuditUser for Save Payment
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(priceList),
                    priceList.getId(),
                    "UPDATE",
                    AuditContext.getAuditInfo().getUserId(),
                    priceListInfoJson);

        } catch (Exception e) {
            log.info("Error: ProductServiceImpl: save(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: ProductServiceImpl: save(): send Kafka save AuditLogUser"); // th�m messageSource
        }
        return response;
    }

    @Override
    public GlobalReponse deleteAllByPriceListId(ApplyNewFormulaPriceRequest request) {
        log.info("*** Void, service; delete all pricelist by priceListId *");

        GlobalReponse response = new GlobalReponse();
        List<PriceListProduct> listPriceListProduct = entityRepository.findAllByPriceListId(request.getPriceListId());
        entityRepository.deleteAll(listPriceListProduct);
        response.setData(null);
        response.setMessage(messageSource.getMessage("price.list.product.deleted", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return response;
    }

    @Override
    public GlobalReponse findPriceListProductByProductId(Integer productId) {
        return null;
    }

    @Override
    public GlobalReponsePagination findAllPriceListAndProduct(FindAllPriceListAndProductRequest request) {
        if (request.getPriceListId() == null)
            throw new PosException(messageSource.getMessage("price.list.choice", null, LocaleContextHolder.getLocale()));
        GlobalReponsePagination globalReponsePagination = new GlobalReponsePagination();
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());

        Page<ProductDto> priceListProductPage = this.findAllProductAndPrice(request);

//        Page<ProductDto> priceListAndProductDtos = priceListProductPage.map(priceListAndProduct -> {
//
//        });

        globalReponsePagination.setData(priceListProductPage.getContent());
        globalReponsePagination.setMessage(messageSource.getMessage("price.list.product.fetch.success", null, LocaleContextHolder.getLocale()));
        globalReponsePagination.setTotalPages(priceListProductPage.getTotalPages());
        globalReponsePagination.setPageSize(priceListProductPage.getSize());
        globalReponsePagination.setCurrentPage(priceListProductPage.getNumber());
        globalReponsePagination.setTotalItems(priceListProductPage.getTotalElements());
        globalReponsePagination.setStatus(HttpStatus.OK.value());

        return globalReponsePagination;
    }

    @Override
    public GlobalReponsePagination findAllPriceListProduct(PriceListProductReqDto req) {
        if (req.getPriceListId() == null) {
            throw new PosException(messageSource.getMessage("price.list.choice", null, LocaleContextHolder.getLocale()));
        }
        Parameter parameter = new Parameter();
        parameter.add("d_pricelist_id", req.getPriceListId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
//        parameter.add("d_pos_terminal_id", req.getPosTerminalId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", req.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("product_name", req.getName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("product_category_id", req.getCategoryId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("barcode", req.getBarcode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecords( "d_pricelist_product_2_v", parameter, req);

        List<ProductDto> priceListProducts = new ArrayList<>();
        try {
            while(rs.next()) {
                ProductDto productDto = ProductDto.builder()
                        .id(rs.getInt("product_id"))
                        .name(rs.getString("product_name"))
                        .code(rs.getString("product_code"))
                        .qrcode(rs.getString("qrcode"))
                        .saleprice(rs.getBigDecimal("saleprice"))
                        .groupType(rs.getString("group_type"))
                        .isSales(rs.getString("is_sales"))
                        .productType(rs.getString("product_type"))
                        .attribute1(rs.getString("attribute1"))
                        .attribute2(rs.getString("attribute2"))
                        .costprice(rs.getBigDecimal("costprice"))
                        .barcode(rs.getString("barcode_query"))
                        .productCategory(ProductCategoryDto.builder()
                                .id(rs.getInt("product_category_id"))
                                .code(rs.getString("product_category_code"))
                                .name(rs.getString("product_category_name"))
                                .erpProductCategoryId(rs.getInt("erp_product_category_id"))
                                .productCategoryParentId(rs.getInt("d_product_category_parent_id")).build())
                        .tax(TaxDto.builder()
                                .id(rs.getInt("d_tax_id"))
                                .name(rs.getString("tax_name"))
                                .taxRate(rs.getBigDecimal("tax_rate")).build())
                        .build();
                int uomIdValue = rs.getInt("uom_id");
                Integer uomId = rs.wasNull() ? null : uomIdValue;
                if (uomId != null) {
                    UomDto uomDto = UomDto.builder()
                            .id(rs.getInt("uom_id"))
                            .name(rs.getString("uom_name"))
                            .code(rs.getString("uom_code"))
                            .erpUomId(rs.getInt("erp_uom_id"))
                            .build();
                    productDto.setUom(uomDto);
                }
                int imageIdValue = rs.getInt("d_image_id");
                Integer imageId = rs.wasNull() ? null : imageIdValue;
                if (imageId != null) {
                    ImageDto imageDto = ImageDto.builder()
                            .id(rs.getInt("d_image_id"))
                            .imageUrl(rs.getString("image_url"))
                            .imageCode(rs.getString("image_code"))
                            .build();
                    productDto.setImage(imageDto);
                }
                priceListProducts.add(productDto);
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_pricelist_product_2_v", parameter, req);

        return GlobalReponsePagination.builder()
                .data(priceListProducts)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    public Page<ProductDto> findAllProductAndPrice(FindAllPriceListAndProductRequest request) {

        String checkOrgMng = "N";
        String slqCheck = "select coalesce(d_pricelist_id, 0 ) from d_pos_terminal where d_pos_terminal_id = :posterminalId and is_default = 'Y' and  d_tenant_id = :tenantId";

        List<Tuple> resultOrg = entityManager.createNativeQuery("select is_pos_mng,d_pricelist_id from d_org where d_org_id = :orgId", Tuple.class)
                .setParameter("orgId", request.getOrgId())
                .getResultList();
        if (!resultOrg.isEmpty()) {
            if (resultOrg.get(0).get("is_pos_mng", String.class).equals("Y")) {
                checkOrgMng = "Y";
            }
        }

//        List<BigDecimal> resultList = entityManager.createNativeQuery(slqCheck)
//                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                .setParameter("posterminalId", request.getPosTerminalId())
//                .getResultList();

//        Integer posterminalCheck = resultList.isEmpty() ? 0 : resultList.get(0);
//        BigDecimal posterminalCheck = resultList.isEmpty() ? BigDecimal.ZERO : resultList.get(0);


        StringBuilder sql = new StringBuilder("select product_id, product_category_id, product_category_code, product_category_name, " +
                "erp_product_category_id, d_product_category_parent_id, product_name, product_code, qrcode, costprice, saleprice, d_tax_id, " +
                "tax_name, tax_rate, group_type, product_type, is_sales, attribute1, attribute2, uom_id, uom_name, uom_code, erp_uom_id, image_url, " +
                "d_image_id, image_code, dpv.d_tenant_id as d_tenant_id, barcode_query, d_business_sector_id " +
                "from d_pricelist_product_v dpv ");

        StringBuilder countQuery = new StringBuilder("select count(*) " +
                "from d_pricelist_product_v dpv "   );
        if (checkOrgMng.equals("Y")) {   // case posterminal khong la default
            sql.append("  join d_pos_terminal dpt on dpv.d_pricelist_id = dpt.d_pricelist_id ");
            sql.append(" join d_pc_terminalaccess dpta on dpt.d_pos_terminal_id = dpta.d_pos_terminal_id and dpv.product_category_id = dpta.d_product_category_id and dpta.d_org_id = dpv.d_org_id where  dpt.d_pos_terminal_id = :posterminalId  and dpv.d_tenant_id = :tenantId ");
            countQuery.append("  join d_pos_terminal dpt on dpv.d_pricelist_id = dpt.d_pricelist_id ");
            countQuery.append(" join d_pc_terminalaccess dpta on dpt.d_pos_terminal_id = dpta.d_pos_terminal_id and dpv.product_category_id = dpta.d_product_category_id and dpta.d_org_id = dpv.d_org_id  where  dpt.d_pos_terminal_id = :posterminalId  and dpv.d_tenant_id = :tenantId ");
            if (request.getOrgId() != null) {
                sql.append(" and dpv.d_org_id = :orgId");
                countQuery.append(" and dpv.d_org_id = :orgId");
            }
            if (request.getName() != null && !request.getName().isEmpty()) {
                sql.append(" and unaccent(lower(product_name)) like unaccent(lower(CONCAT('%', :name, '%')))");
                countQuery.append(" and unaccent(lower(product_name)) like unaccent(lower(CONCAT('%', :name, '%')))");
            }
            if (request.getCategoryId() != null) {
                sql.append(" and product_category_id = :productCategoryId");
                countQuery.append(" and product_category_id = :productCategoryId");
            }
            if(request.getBarcode() != null){
                sql.append(" and (barcode = :barcode or product_code = :barcode ) ");
                countQuery.append(" and (barcode = :barcode or product_code = :barcode )  ");
            }
        } else {
            sql.append(" where dpv.d_org_id = :orgId and dpv.d_tenant_id = :tenantId and dpv.d_pricelist_id = :priceListId and dpv.d_org_id = :orgId");
            countQuery.append(" where dpv.d_org_id = :orgId and dpv.d_tenant_id = :tenantId and dpv.d_pricelist_id = :priceListId and dpv.d_org_id = :orgId");
            if (request.getName() != null && !request.getName().isEmpty()) {
                sql.append(" and unaccent(lower(product_name)) like unaccent(lower(CONCAT('%', :name, '%')))");
                countQuery.append(" and unaccent(lower(product_name)) like unaccent(lower(CONCAT('%', :name, '%')))");
            }
            if (request.getCategoryId() != null) {
                sql.append(" and product_category_id = :productCategoryId");
                countQuery.append(" and product_category_id = :productCategoryId");
            }
            if(request.getBarcode() != null){
                sql.append(" and (barcode = :barcode or product_code = :barcode )  ");
                countQuery.append(" and (barcode = :barcode or product_code = :barcode )  ");
            }
        }

        sql.append(" ORDER BY dpv." + request.getSortBy() + " " + request.getOrder() + " ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());


        Query excuteCount = entityManager.createNativeQuery(countQuery.toString());
        excuteCount.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        excuteCount.setParameter("orgId", request.getOrgId());

        if (request.getOrgId() != null) {
            query.setParameter("orgId", request.getOrgId());
            excuteCount.setParameter("orgId", request.getOrgId());
        }

        if (request.getName() != null && !request.getName().isEmpty())
            query.setParameter("name", request.getName());
        if (request.getName() != null && !request.getName().isEmpty()) {

            excuteCount.setParameter("name", request.getName());
        }

        if (request.getCategoryId() != null) {
            query.setParameter("productCategoryId", request.getCategoryId());
            excuteCount.setParameter("productCategoryId", request.getCategoryId());
        }

        if(request.getBarcode() != null){
            query.setParameter("barcode", request.getBarcode());
            excuteCount.setParameter("barcode", request.getBarcode());
        }

        if (checkOrgMng.equals("Y")) { // case posterminal khong la default
            query.setParameter("posterminalId", request.getPosTerminalId());
            excuteCount.setParameter("posterminalId", request.getPosTerminalId());
        } else {
            query.setParameter("priceListId", request.getPriceListId());
            excuteCount.setParameter("priceListId", request.getPriceListId());
        }

        BigInteger totalItemsBigInt = BigInteger.ZERO;
        if(excuteCount != null && excuteCount.getSingleResult() != null) totalItemsBigInt = (BigInteger) excuteCount.getSingleResult();


        Long totalItems = totalItemsBigInt.longValue();
        int offset = request.getPage() * request.getPageSize();
        List<Map<String, Object>> results = query
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .setFirstResult(offset)
                .setMaxResults(request.getPageSize())
                .getResultList();

        List<ProductDto> priceListAndProducts = new ArrayList<>();
        for (Map<String, Object> row : results) {

            ProductDto productDto = new ProductDto();
            productDto.setId(ParseHelper.INT.parse(row.get("product_id")));
            productDto.setName(ParseHelper.STRING.parse(row.get("product_name")));
            productDto.setCode(ParseHelper.STRING.parse(row.get("product_code")));
            productDto.setQrcode(ParseHelper.STRING.parse(row.get("qrcode")));
            productDto.setSaleprice(ParseHelper.BIGDECIMAL.parse(row.get("saleprice")));
            productDto.setGroupType(ParseHelper.STRING.parse(row.get("group_type")));
            productDto.setIsSales(ParseHelper.STRING.parse(row.get("is_sales")));
            productDto.setProductType(ParseHelper.STRING.parse(row.get("product_type")));
            productDto.setAttribute1(ParseHelper.STRING.parse(row.get("attribute1")));
            productDto.setAttribute2(ParseHelper.STRING.parse(row.get("attribute2")));
            productDto.setCostprice(ParseHelper.BIGDECIMAL.parse(row.get("costprice")));
            productDto.setBarcode(ParseHelper.STRING.parse(row.get("barcode_query")));

            log.info("row: " + row.get("product_id"));
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            productCategoryDto.setId(ParseHelper.INT.parse(row.get("product_category_id")));
            productCategoryDto.setCode(ParseHelper.STRING.parse(row.get("product_category_code")));
            productCategoryDto.setName(ParseHelper.STRING.parse(row.get("product_category_name")));
            productCategoryDto.setErpProductCategoryId(ParseHelper.INT.parse(row.get("erp_product_category_id")));
            productCategoryDto.setProductCategoryParentId(ParseHelper.INT.parse(row.get("d_product_category_parent_id")));
            productDto.setProductCategory(productCategoryDto);
            if (ParseHelper.INT.parse(row.get("uom_id")) != null) {
                UomDto uomDto = new UomDto();
                uomDto.setId(ParseHelper.INT.parse(row.get("uom_id")));
                uomDto.setName(ParseHelper.STRING.parse(row.get("uom_name")));
                uomDto.setCode(ParseHelper.STRING.parse(row.get("uom_code")));
                uomDto.setErpUomId(ParseHelper.INT.parse(row.get("erp_uom_id")));
                productDto.setUom(uomDto);
            }

            if (ParseHelper.INT.parse(row.get("d_image_id")) != null) {
                ImageDto imageDto = new ImageDto();
                imageDto.setId(ParseHelper.INT.parse(row.get("d_image_id")));
                imageDto.setImageUrl(ParseHelper.STRING.parse(row.get("image_url")));
                imageDto.setImageCode(ParseHelper.STRING.parse(row.get("image_code")));
                productDto.setImage(imageDto);
            }
            TaxDto taxDto = new TaxDto();
            taxDto.setId(ParseHelper.INT.parse(row.get("d_tax_id")));
            taxDto.setName(ParseHelper.STRING.parse(row.get("tax_name")));
            taxDto.setTaxRate(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")));
            productDto.setTax(taxDto);
            productDto.setTaxId(ParseHelper.INT.parse(row.get("d_tax_id")));

            if (row.get("d_business_sector_id") != null) {

                BusinessSector businessSector = businessSectorRepository.findById(ParseHelper.INT.parse(row.get("d_business_sector_id"))).orElse(null);
                if (businessSector != null) productDto.setBusinessSector(modelMapper.map(businessSector, BusinessSectorDto.class));
            }

            priceListAndProducts.add(productDto);
        }


        Sort sort = Sort.by(Sort.Direction.fromString(request.getOrder()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
        Page<ProductDto> products = new PageImpl<>(priceListAndProducts, pageable, totalItems);
        return products;
    }

    public Page<PriceListAndProduct> findAllProductAndPriceList(FindAllPriceListAndProductRequest request, Pageable pageable) {

        StringBuilder queryStr = new StringBuilder("SELECT DISTINCT  new com.dbiz.app.productservice.dto.PriceListAndProduct( " +
                " pr.name," +
                "pr.id," +
                "t.name , " +
                "t.taxRate, " +
                "t.id, " +
                "COALESCE(p, p2))" +
                "FROM Product pr  JOIN AssignOrgProduct aop on pr.id = aop.productId and  aop.orgId = " + request.getOrgId() +// " and pr.attribute1 is null "+
                " LEFT JOIN PriceListProduct p  ON p.productId = pr.id ")
                .append("LEFT JOIN Tax t ON t.id = pr.taxId  ");

        if (request.getPriceListId() != null)
            queryStr.append(" AND p.priceListId = :priceListId ");

        queryStr.append(" LEFT JOIN PriceListProduct p2  ON p2.productId = pr.id ");
        queryStr.append("AND p2.tenantId = :tenantId ");
        if (request.getPriceListId() != null)
            queryStr.append("AND p2.priceListId = (select prl.id from PriceList prl where prl.generalPriceList = 'Y' and prl.tenantId = :tenantId )");

        queryStr.append(" WHERE  pr.tenantId = :tenantId and pr.isActive = 'Y' ");
        queryStr.append(" and pr.id not in (select pcb.productComponentId from ProductCombo pcb where pcb.tenantId = :tenantId) ");


        StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(pr) " +
                "FROM Product pr  JOIN AssignOrgProduct aop on pr.id = aop.productId and  aop.orgId = " + request.getOrgId() + //"  and pr.attribute1 is null " +
                " LEFT JOIN PriceListProduct p  ON p.productId = pr.id ");

        countQueryStr.append(" AND  p.tenantId = :tenantId ");
        if (request.getPriceListId() != null)
            countQueryStr.append(" AND p.priceListId = :priceListId ");

        countQueryStr.append(" LEFT JOIN PriceListProduct p2  ON p2.productId = pr.id ");
        countQueryStr.append("AND p2.tenantId = :tenantId ");
        countQueryStr.append("AND p2.priceListId = (select prl.id from PriceList prl where prl.generalPriceList = 'Y' and prl.tenantId = :tenantId )");


        countQueryStr.append(" WHERE pr.tenantId = :tenantId AND pr.tenantId = :tenantId and pr.isActive = 'Y' ");
        countQueryStr.append(" and pr.id not in (select pcb.productComponentId from ProductCombo pcb where pcb.tenantId = :tenantId) ");

        if (request.getProductType() != null) {
            queryStr.append(" AND pr.productType = :productType");
            countQueryStr.append(" AND pr.productType = :productType");
        }
        if (request.getName() != null) {
            queryStr.append(" AND unaccent(lower(pr.name)) LIKE unaccent(lower(CONCAT('%', :name, '%')))");
            countQueryStr.append(" AND unaccent(lower(pr.name)) LIKE unaccent(lower(CONCAT('%', :name, '%')))");
        }
        if (request.getProductCategoryId() != null) {
            queryStr.append(" AND pr.productCategory.id = :productCategoryId");
            countQueryStr.append(" AND pr.productCategory.id = :productCategoryId");
        }
        if (request.getCategoryId() != null) {
            queryStr.append(" AND pr.productCategory.id = :productCategoryId");
            countQueryStr.append(" AND pr.productCategory.id = :productCategoryId");
        }

        queryStr.append(" order by pr.created desc");

        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);
        countQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if (request.getProductType() != null) {
            countQuery.setParameter("productType", request.getProductType());
        }
        if (request.getPriceListId() != null) {
            countQuery.setParameter("priceListId", request.getPriceListId());
        }
        if (request.getName() != null) {
            countQuery.setParameter("name", request.getName());
        }
        if (request.getProductCategoryId() != null) {
            countQuery.setParameter("productCategoryId", request.getProductCategoryId());
        }
        if (request.getCategoryId() != null) {
            countQuery.setParameter("productCategoryId", request.getCategoryId());
        }

        long totalRecords = countQuery.getSingleResult();

        TypedQuery<PriceListAndProduct> typedQuery = entityManager.createQuery(queryStr.toString(), PriceListAndProduct.class);
        typedQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if (request.getProductType() != null) {
            typedQuery.setParameter("productType", request.getProductType());
        }
        if (request.getPriceListId() != null) {
            typedQuery.setParameter("priceListId", request.getPriceListId());
        }
        if (request.getName() != null) {
            typedQuery.setParameter("name", request.getName() );
        }
        if (request.getProductCategoryId() != null) {
            typedQuery.setParameter("productCategoryId", request.getProductCategoryId());
        }
        if (request.getCategoryId() != null) {
            typedQuery.setParameter("productCategoryId", request.getCategoryId());
        }

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<PriceListAndProduct> resultList = typedQuery.getResultList();
        for (PriceListAndProduct item : resultList) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            product.setSaleprice(item.getPriceListProductDto().getSalesPrice());
            product.setCostprice(item.getPriceListProductDto().getCostPrice());
            item.setProductDto(modelMapper.map(product, ProductDto.class));

            if (product.getBusinessSectorId() != null) {

                BusinessSector businessSector = businessSectorRepository.findById(product.getBusinessSectorId()).orElse(null);
                if (businessSector != null) item.getProductDto().setBusinessSector(modelMapper.map(businessSector, BusinessSectorDto.class));
            }
        }
        return new PageImpl<>(resultList, pageable, totalRecords);
    }
}
