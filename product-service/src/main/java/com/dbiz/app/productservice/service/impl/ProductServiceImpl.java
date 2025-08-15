

package com.dbiz.app.productservice.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;


import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import com.dbiz.app.productservice.service.ProductIntService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.dbiz.app.productservice.domain.*;

import com.dbiz.app.productservice.domain.view.GetKcProductV;
import com.dbiz.app.productservice.domain.view.PcERequestV;
import com.dbiz.app.productservice.domain.view.ProductERequestV;
import com.dbiz.app.productservice.repository.*;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.helper.Mapper.ProductMapper;
import com.dbiz.app.productservice.specification.ProductSpecification;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.productDto.otherServiceDto.PcERequestVDto;
import org.common.dbiz.dto.productDto.otherServiceDto.ProductERequestVDto;
import org.common.dbiz.dto.tenantDto.TenantAndOrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.externalRequest.SaveImageMDMRequest;
import org.common.dbiz.request.productRequest.PcErEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductEquestQueryRequest;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.dbiz.app.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductServiceImpl implements ProductService {

    //    @Autowired
//    ProductService productService ;
    private final IProductRepository iProductRepository;

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;

    private final ProductMapper productMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final ProductCategoryRepository productCategoryRepository;

    private final BusinessSectorRepository businessSectorRepository;

    private final UomRepository uomRepository;

    private final MessageSource messageSource;

    private final EntityManager entityManager;

    private final ProductComboRepository productComboRepository;

    private final AssignOrgRepository assignOrgRepository;

    private final UomProductRepository uomProductRepository;

    private final PriceListRepository priceListRepository;

    private final PriceListProductRepository priceListProductRepository;

    private final PcERequestVRepository pcERequestVRepository;

    private final ProductERequestVRepository productERequestVRepository;

    private final GetKcProductVRepository getKcProductVRepository;

    private final AttributeValueRepository attributeValueRepository;

    private final TaxRepository taxRepository;

    private final ProductLocationRepository productLocationRepository;

    private final QueryEngine queryEngine;

    private final ProductIntService productIntService;

    private final KafkaAuditUserHelper kafkaAuditUserHelper;

    private final ObjectMapper objectMapper;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;


    @Override
    public GlobalReponsePagination findAll(ProductQueryRequest paramRequest) {


        log.info("*** ProductDto List, service; fetch all products *");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<Product> spec = ProductSpecification.getProductSpecification(paramRequest);
        GlobalReponsePagination response = new GlobalReponsePagination();
        if(paramRequest.getWarehouseId()!= null)
        {
            String sql = "select d_product_id from d_product_location where d_warehouse_id  = ? and is_active = 'Y' ";
            List<Tuple> rs = entityManager.createNativeQuery(sql,Tuple.class)
                    .setParameter(1, paramRequest.getWarehouseId())
                    .getResultList();
            List<Integer> listProductId = rs.stream().map(tuple ->  Integer.valueOf(tuple.get("d_product_id").toString())).collect(Collectors.toList());
            spec = ProductSpecification.inProductIds(listProductId);
        }
        Page<Product> products = productRepository.findAll(spec, pageable);
        List<ProductDto> listData = new ArrayList<>();
        for (Product item : products.getContent()) {

            ProductDto itemDto = productMapper.toDProductDto(item);
            taxRepository.findById(itemDto.getTaxId())
                    .ifPresent(tax -> itemDto.setTax(TaxDto
                                    .builder()
                                    .name(tax.getName())
                                    .taxRate(tax.getTaxRate())
                                    .isActive(tax.getIsActive())
                                    .isDefault(tax.getIsDefault())
                                    .build())
                    );

            List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(item.getId()).stream()
                    .map(product -> {
                        ProductChildDto attrDto = modelMapper.map(product, ProductChildDto.class);
                        this.getAttr(item, attrDto);
                        return attrDto;
                    })
                    .collect(Collectors.toList());
            itemDto.setProductAttributes(listProductDto);

            ProductCategory productCategory = productCategoryRepository.findById(item.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                itemDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }

            if (item.getBusinessSectorId() != null) {

                BusinessSector businessSector = businessSectorRepository.findById(item.getBusinessSectorId()).orElse(null);
                if (businessSector != null) itemDto.setBusinessSector(modelMapper.map(businessSector, BusinessSectorDto.class));
            }
            listData.add(itemDto);
        }
        response.setMessage(messageSource.getMessage("product.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse save(ProductDto productDto) {
        log.info("*** ProductDto, service; save product ***");
        GlobalReponse response = new GlobalReponse();
        Product productSave = productMapper.toProduct(productDto);

        if (productDto.getId() != null) // update
        {
            productSave = this.productRepository.findById(productDto.getId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));

            if (productDto.getProductCategory() != null) {
                ProductCategory productCategory = this.productCategoryRepository.findById(productDto.getProductCategory().getId()).orElseThrow(() -> new ObjectNotFoundException("Product Category not found"));
                productSave.setProductCategoryId(productCategory.getId());
            }
            if (productDto.getUom() != null) {
                Uom uom = this.uomRepository.findById(productDto.getUom().getId()).orElseThrow(() -> new ObjectNotFoundException("UOM not found"));
                productSave.setUom(uom);
            }

            if (productDto.getImage() != null) {
                Image image = saveImage(productDto);
                productSave.setImage(image);
            }
            if (productDto.getOrgId() != null) {
                productSave.setOrgId(productDto.getOrgId());
            }
            productDto.setUom(null);
            productSave = productMapper.updateEntity(productDto, productSave);

            this.productRepository.save(productSave);
            response.setMessage(messageSource.getMessage("product.updated", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
        } else {

            if (productDto.getImage() != null) {

                Image image = saveImage(productDto);
                productSave.setImage(image);
            }

            if (productDto.getProductType() == null) {

                productSave.setProductType("OTH");
            }

            if (productDto.getTaxId() == null ) {

                Tax taxTemp = taxRepository.findFirstByIsDefault("Y").orElse(null);

                if(taxTemp != null) {

                    productSave.setTaxId(taxTemp.getId());
                }
            }

            if (productSave.getIsTopping() == null) {
                productSave.setIsTopping("N");
            }

            productSave = this.productRepository.save(productSave);
            response.setMessage(messageSource.getMessage("product.created", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }

        ProductDto productDtoResponse = productMapper.toDProductDto(productSave);
        List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(productDtoResponse.getId()).stream()
                .map(product -> modelMapper.map(product, ProductChildDto.class))
                .collect(Collectors.toList());

        productDtoResponse.setProductAttributes(listProductDto);


        response.setData(productDtoResponse);
        log.info("Product saved successfully with ID: {}", productSave.getId());
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse deleteById(Integer productId) {
        log.info("*** Void, service; delete product by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<Product> productDelete = this.productRepository.findById(productId);
        if (productDelete.isEmpty()) {
            response.setMessage(String.format(messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()), productId));
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }

        if (isProductHasTransaction(productId)) {
            throw new PosException(messageSource.getMessage("product.has.transaction", null, LocaleContextHolder.getLocale()));
        }

//		this.imageRepository.deleteById(productDelete.get().getImage().getId());
//		this.productRepository.deleteById(productId);

        try {
            Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_delete_product(:tenantId, :productId)");
            queryProcedure.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
            queryProcedure.setParameter("productId", productId);
            queryProcedure.executeUpdate();
        } catch (Exception e) {
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }
        response.setMessage(messageSource.getMessage("product.deleted", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());

        try {
            // Send Kafka save Audit for user
            // Not check if Delete procedure succeed: Because procedure doesn't swallow exceptions and call statement is try-catch'ed in code

            Map<String, String> productInfo = Map.of(
                    "name", productDelete.get().getName(),
                    "document_no", productDelete.get().getCode()
            );
            String productInfoJson = objectMapper.writeValueAsString(productInfo);

            // Send Kafka save AuditUser for Delete Product
            // Not check if Delete procedure succeed: Because procedure doesn't swallow exceptions and call statement is try-catch'ed in code
            kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                    AuditContext.getAuditInfo().getMainTenantId(),
                    AuditContext.getAuditInfo().getOrgId(),
                    serviceName,
                    DbMetadataHelper.getTableName(productDelete.get()),
                    productId,
                    "DELETE",
                    AuditContext.getAuditInfo().getUserId(),
                    productInfoJson);

        } catch (Exception e) {
            log.info("Error: ProductServiceImpl: deleteById(): send Kafka save AuditLogUser: {}", e.getMessage());
            throw new PosException("Error: ProductServiceImpl: deleteById(): send Kafka save AuditLogUser"); // thêm messageSource
        }

        return response;
    }


    @Override
    public GlobalReponse findById(Integer productId) {
        log.info("*** ProductDto, service; fetch product by id *");

        GlobalReponse response = new GlobalReponse();
        Product productDto = this.productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()), productId)));

        if (productDto.getProductParentId() != null && productDto.getProductParentId() != 0) {
            ProductChildDto childDto = modelMapper.map(productDto, ProductChildDto.class);
            this.getAttr(this.productRepository.findById(childDto.getProductParentId()).get(), childDto);
            ProductCategory productCategory = this.productCategoryRepository.findById(productDto.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                childDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }
            response.setData(childDto);
        } else {
            ProductDto responseDto = modelMapper.map(productDto, ProductDto.class);
            List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(productDto.getId()).stream()
                    .map(product -> {
                        ProductChildDto itemDto = modelMapper.map(product, ProductChildDto.class);
                        this.getAttr(this.productRepository.findById(productId).get(), itemDto);

                        return itemDto;
                    })
                    .collect(Collectors.toList());

            ProductCategory productCategory = this.productCategoryRepository.findById(productDto.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                responseDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }
            responseDto.setProductAttributes(listProductDto);
            response.setData(responseDto);
        }


        response.setMessage(messageSource.getMessage("product.fetch.success", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse saveAll(SaveAllProductAttr request) {
        log.info("*** List<ProductDto>, service; save all products *");

        Integer priceListId = priceListRepository.findPriceListByGeneralPriceList("Y", AuditContext.getAuditInfo().getTenantId(), 0);
        GlobalReponse response = new GlobalReponse();
        Integer idParent = 0;
        List<ProductDto> dtoResponse = new ArrayList<>();
        for (ProductDto item : request.getListProductDto()) {
            item.setOrgId(0);

            // update
            if (item.getId() != null && item.getId() != 0) {
                Product productUpdate = productRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
                UomDto uom  = item.getUom() == null ? null : item.getUom();
                item.setUom(null);
                productUpdate = productMapper.updateEntity(item, productUpdate);
                if (item.getProductCategory() != null) {
                    ProductCategory proCt = productCategoryRepository.findById(item.getProductCategory().getId()).orElseThrow(() -> new ObjectNotFoundException("Product Category not found"));
                    productUpdate.setProductCategoryId(proCt.getId());
                }
                if(uom != null)
                {
                    Uom uomUpdate = uomRepository.findById(uom.getId()).orElseThrow(() -> new ObjectNotFoundException("UOM not found"));
                    productUpdate.setUom(uomUpdate);
                }

                if (item.getImage() != null) {
                    Image imgnew = saveImage(item);
                    productUpdate.setImage(imgnew);
                }

                if(item.getErpProductId() != null){
                    productUpdate.setErpProductId(item.getErpProductId());
                }

                productRepository.save(productUpdate);
                ProductDto resultItem = productMapper.toDProductDto(productUpdate);
                dtoResponse.add(resultItem);

                PriceListProduct priceListProductUpdate = this.priceListProductRepository.findAllPriceListProductByGeneralPriceListAndProductId(item.getId(), AuditContext.getAuditInfo().getTenantId());
                priceListProductUpdate.setSalesPrice(item.getSaleprice());
                this.priceListProductRepository.save(priceListProductUpdate);

                saveAssignProduct(request, item.getId(), item.getIsTopping(),"N");
                response.setMessage(messageSource.getMessage("product.updated", null, LocaleContextHolder.getLocale()));

                // Send Kafka save AuditUser for Delete Product
                // Do we need to Check if Update procedure succeed ?
                String productInfoJson = null;
                try {
                    Map<String, String> productInfo = Map.of(
                            "name", productUpdate.getName(),
                            "document_no", productUpdate.getCode()
                    );
                    productInfoJson = objectMapper.writeValueAsString(productInfo);
                } catch (Exception ignored) {}

                kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                        AuditContext.getAuditInfo().getMainTenantId(),
                        AuditContext.getAuditInfo().getOrgId(),
                        serviceName,
                        DbMetadataHelper.getTableName(productUpdate),
                        productUpdate.getId(),
                        "UPDATE",
                        AuditContext.getAuditInfo().getUserId(),
                        productInfoJson);
            } else { // create
                if (item.getIsTopping() == null) {
                    item.setIsTopping("N");
                }
                log.info("log: product,"+item);
                Product productSave = productMapper.toProduct(item);

                int id = productRepository.getMaxId() + 1;

                if (item.getTaxId() == null ) {

                    Tax taxTemp = taxRepository.findFirstByIsDefault("Y").orElse(null);

                    if(taxTemp != null) {

                        productSave.setTaxId(taxTemp.getId());
                    }
                }

                if (item.getAttribute1() != null && !item.getAttribute1().isEmpty()) { // luu san pham lon
                    String docNo = "PROD" + id;
                    if (item.getCode() == null || item.getCode().isEmpty())
                        productSave.setCode(docNo);

                    if (item.getImage() != null) {
                        Image imgnew = saveImage(item);
                        productSave.setImage(imgnew);
                    }
                    productSave.setOrgId(0);
                    productSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    Product productParent = productRepository.save(productSave);
                    idParent = productParent.getId();
                    dtoResponse.add(productMapper.toDProductDto(productParent));

                    // tao bang gia chung cho product lon
                    PriceListProduct priceListProduct = PriceListProduct.builder()
                            .productId(productParent.getId())
                            .priceListId(priceListId)
                            .costPrice(productSave.getCostprice())
                            .salesPrice(productSave.getSaleprice())
                            .lastOrderPrice(BigDecimal.ZERO)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .orgId(0)
                            .build();
                    priceListProductRepository.save(priceListProduct);
                    // luu topping, item, assign org
                    saveAssignProduct(request, productParent.getId(), item.getIsTopping(),item.getId() == null ? "Y" : "N");
                } else { // luu thuoc tinh cua san pham lon
                    if (id == idParent)
                        id++;
                    String docNo = "PROD" + id;

                    if (item.getCode() == null || item.getCode().isEmpty())
                        productSave.setCode(docNo);
                    if (idParent != 0)
                        productSave.setProductParentId(idParent);
                    if (item.getImage() != null) {
                        Image imgnew = saveImage(item);
                        productSave.setImage(imgnew);
                    }
                    productSave.setOrgId(0);
                    productSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    Product product = productRepository.save(productSave);
                    if (product.getUom() != null) {
                        UomProduct uomProduct = UomProduct.builder()
                                .productId(product.getId())
                                .uomId(product.getUom().getId())
                                .conversionValue(product.getQtyConversion())
                                .costprice(product.getCostprice())
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(0)
                                .build();
                        uomProductRepository.save(uomProduct);
                    }


                    if (item.getOrgId() != null && item.getOrgId() != 0) {
                        this.assignOrgRepository.updateIsActiveAllByProductId(product.getId());
                        AssignOrgProduct assignCheck = this.assignOrgRepository.getByOrgIdAndProductId(item.getOrgId(), product.getId()).orElse(null);
                        if (assignCheck == null) {
                            AssignOrgProduct assignOrgProduct = AssignOrgProduct.builder()
                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                                    .productId(product.getId()).orgId(item.getOrgId()).build();
                            assignOrgRepository.save(assignOrgProduct);
                        } else {
                            assignCheck.setIsActive("Y");
                            assignOrgRepository.save(assignCheck);

                        }
                    }


                    // tao bang gia cho product attribute
                    List<PriceListProduct> listPrice = new ArrayList<>();
                    PriceListProduct priceListProduct = PriceListProduct.builder()
                            .productId(product.getId())
                            .priceListId(priceListId)
                            .costPrice(productSave.getCostprice())
                            .salesPrice(productSave.getSaleprice())
                            .lastOrderPrice(BigDecimal.ZERO)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .orgId(0)
                            .build();
                    listPrice.add(priceListProduct);
                    log.info("PriceListProduct: {}", priceListProduct);
                    if(item.getPriceListId()  != null && !item.getPriceListId().equals(priceListId)){
                        log.info("vao day: " + item.getPriceListId() + " - " + priceListId);
                        PriceListProduct priceListProduct1 = PriceListProduct.builder()
                                .productId(product.getId())
                                .priceListId(item.getPriceListId())
                                .costPrice(productSave.getCostprice())
                                .salesPrice(productSave.getSaleprice())
                                .lastOrderPrice(BigDecimal.ZERO)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(0)
                                .build();
                        listPrice.add(priceListProduct1);
                    }
                    log.info("item: "+ item);
                    priceListProductRepository.saveAll(listPrice);
                    // luu topping, item, assign org, product location
                    SaveAllProductAttr resultAssign = saveAssignProduct(request, product.getId(), item.getIsTopping(),item.getId() == null ? "Y" : "N");
                    ProductDto productDto = productMapper.toDProductDto(product);

                    Tax tax = null;
                    if (productDto.getTaxId() != null ) {

                        tax = taxRepository.findById(productDto.getTaxId()).orElse(null);
                    } else {

                        tax = taxRepository.findFirstByIsDefault("Y").orElse(null);
                    }

                    if(tax != null) {

                        productDto.setTax(TaxDto.builder()
                                .id(tax.getId())
                                .taxRate(tax.getTaxRate())
                                .build());
                    }
                    dtoResponse.add(productDto);
                }
                response.setMessage(messageSource.getMessage("product.created", null, LocaleContextHolder.getLocale()));

                // Send Kafka save AuditUser for Delete Product
                // Do we need to Check if Update procedure succeed ?
                String productInfoJson = null;
                try {
                    Map<String, String> productInfo = Map.of(
                            "name", productSave.getName(),
                            "document_no", productSave.getCode()
                    );
                    productInfoJson = objectMapper.writeValueAsString(productInfo);
                } catch (Exception ignored) {}

                kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                        AuditContext.getAuditInfo().getMainTenantId(),
                        AuditContext.getAuditInfo().getOrgId(),
                        serviceName,
                        DbMetadataHelper.getTableName(productSave),
                        productSave.getId(),
                        "INSERT",
                        AuditContext.getAuditInfo().getUserId(),
                        productInfoJson);
            }
        }

        response.setData(dtoResponse);
        response.setStatus(HttpStatus.CREATED.value());
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse saveAllProduct(ListProductDto request) {
        log.info("*** List<ProductDto>, service; save all products *");

        GlobalReponse response = new GlobalReponse();
        if (request.getProducts() != null && !request.getProducts().isEmpty()) {
            for (ProductDto item : request.getProducts()) {
                Product productSave = productRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
                BigDecimal costPrice = productSave.getOnHand();
                if (costPrice == null) costPrice = BigDecimal.ZERO;
                modelMapper.map(item, productSave);
                productSave.setOnHand(costPrice.add(item.getOnHand()));
                productSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                productRepository.save(productSave);

            }
        }

        response.setMessage(messageSource.getMessage("product.created", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;

    }



    @Override
    public GlobalReponsePagination eRequestGetPc(PcErEquestQueryRequest request) {
        log.info("*** ProductDto List, service; fetch all products *");


        Specification<PcERequestV> specification = Specification.where(null);
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId()));
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orgId"), request.getOrgId()));
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("posTerminalId"), request.getPosTerminalId()));

        Page<PcERequestV> getData = pcERequestVRepository.findAll(specification, pageable);
        List<PcERequestVDto> listData = new ArrayList<>();
        for(PcERequestV item : getData)
        {
            String sqlGetPrice = "select coalesce(min(saleprice),0) as from_price from d_product where saleprice <> 0 and d_product_category_id = ? ";
            PcERequestVDto itemDto = modelMapper.map(item, PcERequestVDto.class);
            List<Map<String, Object>> resultPrice = entityManager.createNativeQuery(sqlGetPrice)
                    .setParameter(1, ParseHelper.INT.parse(item.getId()))
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            if (!resultPrice.isEmpty()) {
                System.out.println("call vao day");
                resultPrice.forEach(itemPrice -> {
                    System.out.println(itemPrice.get("from_price"));
                    itemDto.setFromPrice(ParseHelper.BIGDECIMAL.parse(itemPrice.get("from_price")));
                });
            }
            listData.add(itemDto);
        }

        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .totalPages(getData.getTotalPages())
                .pageSize(getData.getSize())
                .currentPage(getData.getNumber())
                .totalItems(getData.getTotalElements())
                .data(listData)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }
    /**
     * @param request
     * @return GlobalReponsePagination
     */
    @Override
    public GlobalReponsePagination eRequestGetProduct(ProductEquestQueryRequest request) {
        log.info("*** ProductDto List, service; fetch all products *");
        Specification<ProductERequestV> specification = Specification.where(null);
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId()));

        if(request.getProductId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("productId"), request.getProductId()));
        }

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orgId"), request.getOrgId()));

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("posTerminalId"), request.getPosTerminalId()));

        specification = specification.and((root, query, criteriaBuilder) ->
                request.getProductCategoryId() == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("productCategoryId"), request.getProductCategoryId()));

        specification = specification.and((root, query, criteriaBuilder) ->
                request.getName() == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));

        Page<ProductERequestV> getData = productERequestVRepository.findAll(specification, pageable);
        List<ProductERequestVDto> listData = getData.stream()
                .map(element -> {
                    ProductERequestVDto itemDto = modelMapper.map(element, ProductERequestVDto.class);
                    List<ProductCDto> componentDtos = new ArrayList<>();
                    List<ProductCDto> extraItems = new ArrayList<>();
                    List<Tuple> componentQuery = entityManager.createNativeQuery("select d_product_component_id,product_name , product_code,coalesce(saleprice, 0) as saleprice ,coalesce(costprice,0) as costprice,uom_name,uom_code,is_item,tax_name,tax_rate,d_tax_id,qty" +
                                    " from d_rq_get_product where is_item ='Y' and d_product_id = :productId", Tuple.class)
                            .setParameter("productId",element.getProductId())
                            .getResultList();
                    if(!componentQuery.isEmpty())
                    {
                        componentQuery.forEach(i -> {
                            componentDtos.add(ProductCDto.builder()
                                    .name(i.get("product_name").toString())
                                    .code(i.get("product_code").toString())
                                    .id(Integer.valueOf(i.get("d_product_component_id").toString()))
                                    .saleprice(new BigDecimal(i.get("saleprice").toString()))
                                    .costprice(new BigDecimal(i.get("costprice").toString()))
                                    .taxId(Integer.valueOf(i.get("d_tax_id").toString()))
                                    .uomCode(i.get("uom_code").toString())
                                    .uomName(i.get("uom_name").toString())
                                    .taxName(i.get("tax_name").toString())
                                    .taxRate(new BigDecimal(i.get("tax_rate").toString()))
                                    .qty(new BigDecimal(i.get("qty").toString()))
                                    .build());
                        });
                    }
                    List<Tuple> extraItemsQuery = entityManager.createNativeQuery("select d_product_component_id,product_name , product_code,coalesce(saleprice, 0) as saleprice,coalesce(costprice,0) as costprice,uom_name,uom_code,is_item,tax_name,tax_rate,d_tax_id" +
                                    " from d_rq_get_product where is_item ='N' and d_product_id = :productId", Tuple.class)
                            .setParameter("productId",element.getProductId())
                            .getResultList();
                    if(!extraItemsQuery.isEmpty())
                    {
                        extraItemsQuery.forEach(i -> {
                            extraItems.add(ProductCDto.builder()
                                    .name(i.get("product_name").toString())
                                    .code(i.get("product_code").toString())
                                    .id(Integer.valueOf(i.get("d_product_component_id").toString()))
                                    .saleprice(new BigDecimal(i.get("saleprice").toString()))
                                    .costprice(new BigDecimal(i.get("costprice").toString()))
                                    .taxId(Integer.valueOf(i.get("d_tax_id").toString()))
                                    .uomCode(i.get("uom_code").toString())
                                    .uomName(i.get("uom_name").toString())
                                    .taxName(i.get("tax_name").toString())
                                    .taxRate(new BigDecimal(i.get("tax_rate").toString()))
                                    .build());
                        });
                    }
                    itemDto.setComponents(componentDtos);
                    itemDto.setExtraItems(extraItems);
                    return itemDto;
                })
                .collect(Collectors.toList());



        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .data(listData)
                .totalPages(getData.getTotalPages())
                .totalItems(getData.getTotalElements())
                .pageSize(getData.getSize())
                .currentPage(getData.getNumber())
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination KcGetProduct(ProductQueryRequest request) {
        log.info("*** ProductDto List, service; fetch all products *");
        Specification<GetKcProductV> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) -> request.getName() == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + request.getName().toLowerCase() + "%"
        ));
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Page<GetKcProductV> pageResponse = getKcProductVRepository.findAll(specification, pageable);

        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(pageResponse.getContent())
                .errors("")
                .currentPage(pageResponse.getNumber())
                .totalItems(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .pageSize(pageResponse.getSize())
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse intSave(List<ProductIntDto> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        int rsInsertProduct = 0;
        int rsUpdateProduct = 0;
        int rsInsertImage = 0;
        int rsUpdateImage = 0;
        log.info("*** List<ProductDto>, service; save int product  *");
        Integer priceListId = priceListRepository.findPriceListByGeneralPriceList("Y", AuditContext.getAuditInfo().getTenantId(), 0);

        List<ProductIntDto> listProduct = new ArrayList<>();
        for (ProductIntDto itemProduct : request) {
            String saveSuccess = this.productIntService.saveSigProduct(itemProduct,priceListId);
            listProduct.add(itemProduct);
            if(!saveSuccess.isBlank())
            {
                IProduct iProduct = IProduct.builder()
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .orgId(AuditContext.getAuditInfo().getOrgId())
                        .name(itemProduct.getName())
                        .erpProductId(itemProduct.getErpProductId())
                        .errorMessage(saveSuccess)
                        .build();
                iProductRepository.save(iProduct);
            }
        }
        log.info("rsInsertProduct {}", rsInsertProduct + " - rsUpdateProduct " + rsUpdateProduct + " - rsInsertImage " + rsInsertImage + " - rsUpdateImage " + rsUpdateImage);
        return GlobalReponse.builder()
                .data(true)
                .errors("")
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponse findByIdOrgAccess(ProductQueryRequest request) {
        log.info("*** ProductDto, service; fetch product by id *");

        GlobalReponse response = new GlobalReponse();
        Product productDto = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format(messageSource.getMessage("product.not.found", null, LocaleContextHolder.getLocale()), request.getProductId())));


        StringBuilder sql = new StringBuilder("SELECT\n" +
                "    dog.code AS code,\n" +
                "    dog.name AS name,\n" +
                "    dog.phone AS phone,\n" +
                "    dog.address AS address ,dog.area as area, \n" +
                "    dog.is_active AS is_active,\n" +
                "    dog.d_org_id AS d_org_id,\n" +
                "    dpct.d_product_id AS d_product_id , dpct.is_active as is_active_dpct \n" +
                "FROM\n" +
                "    d_userorg_access dua\n" +
                "        JOIN\n" +
                "    d_org dog ON dua.d_org_id = dog.d_org_id\n" +
                "        LEFT JOIN\n" +
                "    d_assign_org_product dpct ON dua.d_org_id = dpct.d_org_id\n" +
                "        AND dpct.d_product_id = :productId\n" +
                "WHERE\n" +
                "    dua.d_user_id = :userId\n" +
                "  AND dog.d_tenant_id = :tenantId");
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sql.toString())
                .setParameter("productId", request.getProductId())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("userId", request.getUserId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<AssignOrgProductDto> listAssignOrgProduct = new ArrayList<>();
        for (Map<String, Object> item : resultList) {
            AssignOrgProductDto assignOrgProductDto = AssignOrgProductDto.builder()
                    .orgId(Integer.parseInt(item.get("d_org_id").toString()))
                    .code(item.get("code") != null ? item.get("code").toString() : "")
                    .name(item.get("name").toString())
                    .phone(item.get("phone") != null ? item.get("phone").toString() : "")
                    .address(item.get("address") != null ? item.get("address").toString() : "")
                    .isActive(item.get("is_active").toString())
                    .area(item.get("area") != null ? item.get("area").toString() : "")
                    .isAssign(item.get("d_product_id") != null && ( item.get("is_active_dpct") != null &&  item.get("is_active_dpct").toString().equals("Y") )  ? "Y" : "N")
                    .build();
            listAssignOrgProduct.add(assignOrgProductDto);
        }
        if (productDto.getProductParentId() != null && productDto.getProductParentId() != 0) {
            ProductChildDto childDto = modelMapper.map(productDto, ProductChildDto.class);
            this.getAttr(this.productRepository.findById(childDto.getProductParentId()).get(), childDto);
            childDto.setAssignOrgProductDtos(listAssignOrgProduct);
            response.setData(childDto);
        } else {
            ProductDto responseDto = modelMapper.map(productDto, ProductDto.class);
            List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(productDto.getId()).stream()
                    .map(product -> {
                        ProductChildDto itemDto = modelMapper.map(product, ProductChildDto.class);
                        this.getAttr(this.productRepository.findById(request.getProductId()).get(), itemDto);
                        return itemDto;
                    })
                    .collect(Collectors.toList());
            responseDto.setAssignOrgProductDtos(listAssignOrgProduct);
            responseDto.setProductAttributes(listProductDto);
            response.setData(responseDto);
        }


        response.setMessage(messageSource.getMessage("product.fetch.success", null, LocaleContextHolder.getLocale()));
        return response;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponse findByIdLocator(ProductQueryRequest request) {
        log.info("*** ProductDto, service; fetch product by id *");
        StringBuilder sql = new StringBuilder("select dpl.d_product_location_id, dpl.d_product_id as d_product_id,\n" +
                "       dog.name as org_name,\n" +
                "       dw.name as warehouse_name,\n" +
                "       coalesce(dso.qty,0) AS stock_qty, \n" +
                "       dpl.is_active as is_active, dpl.d_locator_id as d_locator_id," +
                "       coalesce(dso.min_qty,0) as min_qty, coalesce(dso.max_qty,0) as max_qty," +
                "       dpl.d_org_id as d_org_id , dw.d_warehouse_id as d_warehouse_id, dpl.is_default  \n" +
                "       from d_product_location dpl\n" +
                "       join d_org dog on dpl.d_org_id = dog.d_org_id\n" +
                "       join d_warehouse dw on dpl.d_warehouse_id = dw.d_warehouse_id  " +
                "       join d_locator dl on dw.d_warehouse_id = dl.d_warehouse_id " +
                "      left join d_storage_onhand dso on " +
                "             (dpl.d_product_id = dso.d_product_id " +
                "              and dpl.d_warehouse_id = dso.d_warehouse_id " +
                "              and dl.d_locator_id = dso.d_locator_id) \n" +
                "       where dpl.d_product_id = :productId and dpl.d_tenant_id = :tenantId " +
                "       and dl.is_default='Y' and dl.is_active='Y'");
        List<Map<String, Object>> resultList = entityManager.createNativeQuery(sql.toString())
                .setParameter("productId", request.getProductId())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ProductLocationDto> resultReponse = new ArrayList<>();
        for (Map<String, Object> item : resultList) {
            ProductLocationDto productLocationDto = ProductLocationDto.builder()
                    .id(Integer.parseInt(item.get("d_product_location_id").toString()))
                    .productId(Integer.parseInt(item.get("d_product_id").toString()))
                    .orgName(item.get("org_name").toString())
                    .nameWarehouse(item.get("warehouse_name").toString())
                    .stockQty(item.get("stock_qty") == null ? BigDecimal.ZERO : new BigDecimal(item.get("stock_qty").toString()))
                    .minQty(item.get("min_qty") == null ? BigDecimal.ZERO : new BigDecimal(item.get("min_qty").toString()))
                    .maxQty(item.get("max_qty") == null ? BigDecimal.ZERO : new BigDecimal(item.get("max_qty").toString()))
                    .isActive(item.get("is_active").toString())
                    .warehouseId(item.get("d_warehouse_id") == null ? null : Integer.parseInt(item.get("d_warehouse_id").toString()))
                    .orgId(item.get("d_org_id") == null ? null : Integer.parseInt(item.get("d_org_id").toString()))
                    .locatorId(item.get("d_locator_id") == null ? null : Integer.parseInt(item.get("d_locator_id").toString()))
                    .isDefault(item.get("is_default").toString())
                    .build();
            resultReponse.add(productLocationDto);
        }

        return GlobalReponse.builder()
                .data(resultReponse)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("product.fetch.success", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     *
     * @param paramRequest
     * @return
     */
    @Override
    public GlobalReponsePagination findAllByWarehouse(ProductQueryRequest paramRequest) {


        log.info("*** ProductDto List, service; fetch all products *");

        Pageable pageable = requestParamsUtils.getPageRequest(paramRequest);
        Specification<Product> spec =null;
        GlobalReponsePagination response = new GlobalReponsePagination();
        if(paramRequest.getWarehouseId()!= null)
        {
            String sql = "select d_product_id from d_product_location where d_warehouse_id  = ? and is_active = 'Y' ";
            List<Tuple> rs = entityManager.createNativeQuery(sql,Tuple.class)
                    .setParameter(1, paramRequest.getWarehouseId())
                    .getResultList();
            List<Integer> listProductId = rs.stream().map(tuple ->  Integer.valueOf(tuple.get("d_product_id").toString())).collect(Collectors.toList());
            spec = ProductSpecification.inProductIds(listProductId);
        }
        Page<Product> products = productRepository.findAll(spec, pageable);
        List<ProductDto> listData = new ArrayList<>();
        for (Product item : products.getContent()) {
            ProductDto itemDto = productMapper.toDProductDto(item);
            ProductLocation location = productLocationRepository.findByProductIdAndWarehouseId(item.getId(), paramRequest.getWarehouseId()).orElse(null);
            itemDto.setListProductLocation(modelMapper.map(location, ProductLocationDto.class));
            List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(item.getId()).stream()
                    .map(product -> {
                        ProductChildDto attrDto = modelMapper.map(product, ProductChildDto.class);
                        ProductLocation locationChild = productLocationRepository.findByProductIdAndWarehouseId(attrDto.getId(), paramRequest.getWarehouseId()).orElse(null);
                        itemDto.setListProductLocation(modelMapper.map(location, ProductLocationDto.class));
                        this.getAttr(item, attrDto);
                        attrDto.setListProductLocation(modelMapper.map(locationChild, ProductLocationDto.class));
                        return attrDto;
                    })
                    .collect(Collectors.toList());
            itemDto.setProductAttributes(listProductDto);

            ProductCategory productCategory = productCategoryRepository.findById(item.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                itemDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }
            listData.add(itemDto);
        }
        response.setMessage(messageSource.getMessage("product.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());
        return response;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination getOnHand(ProductQueryRequest request) {
        log.info("*** fetch all on hand Product;");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Product> spec = ProductSpecification.getProductSpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();
        if(request.getWarehouseId()!= null)
        {
            String sql = "select d_product_id from d_product_location where d_warehouse_id  = ? and is_active = 'Y' ";
            List<Tuple> rs = entityManager.createNativeQuery(sql,Tuple.class)
                    .setParameter(1, request.getWarehouseId())
                    .getResultList();
            List<Integer> listProductId = rs.stream().map(tuple ->  Integer.valueOf(tuple.get("d_product_id").toString())).collect(Collectors.toList());
            spec = ProductSpecification.inProductIds(listProductId);
        }
        Page<Product> products = productRepository.findAll(spec, pageable);
        List<ProductDto> listData = new ArrayList<>();
        for (Product item : products.getContent()) {

            ProductDto itemDto = productMapper.toDProductDto(item);


            List<Tuple> onHands= entityManager.createNativeQuery("select qty from d_storage_onhand where d_product_id = :productId and d_warehouse_id = :warehouseId and d_tenant_id = :tenantId",Tuple.class)
                    .setParameter("productId",item.getId())
                    .setParameter("warehouseId",request.getWarehouseId())
                    .setParameter("tenantId",AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            for(Tuple onHand : onHands)
            {
                itemDto.setOnHand(ParseHelper.BIGDECIMAL.parse(onHand.get("qty").toString()));
            }


            //            List<ProductChildDto> listProductDto = productRepository.findAllByProductParentId(item.getId()).stream()
//                    .map(product -> {
//                        ProductChildDto attrDto = modelMapper.map(product, ProductChildDto.class);
//                        this.getAttr(item, attrDto);
//                        return attrDto;
//                    })
//                    .collect(Collectors.toList());
//            itemDto.setProductAttributes(listProductDto);

            ProductCategory productCategory = productCategoryRepository.findById(item.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                itemDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }
            listData.add(itemDto);
        }
        response.setMessage(messageSource.getMessage("product.fetch.all", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalPages(products.getTotalPages());
        response.setTotalItems(products.getTotalElements());
        return response;
    }

    @Override
    public GlobalReponse savePart(SaveAllProductAttr request) {

        log.info("*** List<ProductDto>, service; save all products mobile *");

        GlobalReponse response = new GlobalReponse();
        Integer idParent = 0;
        List<ProductDto> dtoResponse = new ArrayList<>();
        Integer priceListId = priceListRepository.findPriceListByGeneralPriceList("Y", AuditContext.getAuditInfo().getTenantId(), 0);


        if (request.getListProductDto() != null && !request.getListProductDto().isEmpty()) {
            for (ProductDto item : request.getListProductDto()) {

                item.setOrgId(0);
                // update
                if (item.getId() != null && item.getId() != 0) {
                    Product productUpdate = productRepository.findById(item.getId()).orElseThrow(() -> new PosException("Product not found"));

                    productUpdate = productMapper.updateEntity(item, productUpdate);
                    if (item.getProductCategory() != null) {
                        ProductCategory proCt = productCategoryRepository.findById(item.getProductCategory().getId()).orElseThrow(() -> new PosException("Product Category not found"));
                        productUpdate.setProductCategoryId(proCt.getId());
                    }

                    if (item.getImage() != null) {
                        Image imgnew = saveImage(item);
                        productUpdate.setImage(imgnew);
                    }

                    productRepository.save(productUpdate);
                    ProductDto resultItem = productMapper.toDProductDto(productUpdate);
                    dtoResponse.add(resultItem);

                    PriceListProduct priceListProductUpdate = this.priceListProductRepository.findAllPriceListProductByGeneralPriceListAndProductId(item.getId(), AuditContext.getAuditInfo().getTenantId());
                    priceListProductUpdate.setSalesPrice(item.getSaleprice());
                    this.priceListProductRepository.save(priceListProductUpdate);

                    response.setMessage(messageSource.getMessage("product.updated", null, LocaleContextHolder.getLocale()));
                } else { // create
                    if (item.getIsTopping() == null) {
                        item.setIsTopping("N");
                    }
                    Product productSave = productMapper.toProduct(item);

                    int id = productRepository.getMaxId() + 1;

                    if (item.getTaxId() == null ) {

                        Tax taxTemp = taxRepository.findFirstByIsDefault("Y").orElse(null);

                        if(taxTemp != null) {

                            item.setTaxId(taxTemp.getId());
                        }
                    }
                    if (item.getAttribute1() != null && !item.getAttribute1().isEmpty()) { // luu san pham lon
                        String docNo = "PROD" + id;
                        if (item.getCode() == null || item.getCode().isEmpty())
                            productSave.setCode(docNo);

                        if (item.getImage() != null) {
                            Image imgnew = saveImage(item);
                            productSave.setImage(imgnew);
                        }
                        productSave.setOrgId(0);
                        productSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        Product productParent = productRepository.save(productSave);

                        idParent = productParent.getId();
                        dtoResponse.add(productMapper.toDProductDto(productParent));

                        // tao bang gia chung cho product lon
                        PriceListProduct priceListProduct = PriceListProduct.builder()
                                .productId(productParent.getId())
                                .priceListId(priceListId)
                                .costPrice(productSave.getCostprice())
                                .salesPrice(productSave.getSaleprice())
                                .lastOrderPrice(BigDecimal.ZERO)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(0).build();
                        priceListProductRepository.save(priceListProduct);

                    } else { // luu thuoc tinh cua san pham lon
                        if (id == idParent)
                            id++;
                        String docNo = "PROD" + id;

                        if (item.getCode() == null || item.getCode().isEmpty())
                            productSave.setCode(docNo);
                        if (idParent != 0)
                            productSave.setProductParentId(idParent);
                        if (item.getImage() != null) {
                            Image imgnew = saveImage(item);
                            productSave.setImage(imgnew);
                        }
                        productSave.setOrgId(0);
                        productSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        Product product = productRepository.save(productSave);


                        // tao bang gia cho product attribute
                        PriceListProduct priceListProduct = PriceListProduct.builder()
                                .productId(productSave.getId())
                                .priceListId(priceListId)
                                .costPrice(productSave.getCostprice())
                                .salesPrice(productSave.getSaleprice())
                                .lastOrderPrice(BigDecimal.ZERO)
                                .tenantId(AuditContext.getAuditInfo().getTenantId())
                                .orgId(0)
                                .build();
                        priceListProductRepository.save(priceListProduct);
                        dtoResponse.add(productMapper.toDProductDto(product));
                    }
                }
                response.setMessage(messageSource.getMessage("product.created", null, LocaleContextHolder.getLocale()));
            }
            response.setData(dtoResponse);
        }else if(request.getUomId() != null){
            Uom uom = uomRepository.findById(request.getUomId()).orElseThrow(() -> new PosException("UOM not found"));
            Product productUpdate = productRepository.findById(request.getProductId()).orElseThrow(() -> new PosException("Product not found"));
            productUpdate.setUom(uom);
            productRepository.save(productUpdate);
            response.setMessage(messageSource.getMessage("product.updated", null, LocaleContextHolder.getLocale()));
        }else{
            Product productUpdate = productRepository.findById(request.getProductId()).orElseThrow(() -> new PosException("Product not found"));
            saveAssignProductPart(request, productUpdate.getId(), productUpdate.getIsTopping(),"N");
            response.setMessage(messageSource.getMessage("product.updated", null, LocaleContextHolder.getLocale()));
        }


        response.setStatus(HttpStatus.CREATED.value());
        return response;
    }

    @Override
    public GlobalReponsePagination getOrgAssign(ProductQueryRequest request) {
        log.info("*** ProductDto, service; fetch org by id *");
//        List<ProductOrgAssignDto> list = new ArrayList<>();
//        Long totalRecords = 0L;
//        try {
//
//            String sql = "SELECT d_org_id , " +
//                    "wards, " +
//                    "org_name, " +
//                    "org_code, " +
//                    "org_phone, " +
//                    "d_user_id, " +
//                    "d_product_id, " +
//                    "is_assign " +
//                    " FROM pos.d_product_org_assign_v WHERE d_user_id = :userId " +
//                    " AND d_product_id = :productId order by org_name asc LIMIT :limit OFFSET :offset";
//
//            String countSql = "SELECT count(1)  " +
//                    " FROM pos.d_product_org_assign_v WHERE d_user_id = :userId " +
//                    " AND d_product_id = :productId LIMIT :limit OFFSET :offset";
//
//            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
//            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
//                    .setParameter("userId", AuditContext.getAuditInfo().getUserId())
//                    .setParameter("productId", request.getProductId())
//                    .setParameter("limit", request.getPageSize())
//                    .setParameter("offset", request.getPage())
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//                    .getResultList();
//
//            // Get the total number of records
//            Query totalRecordsQuery = entityManager.createNativeQuery(countSql);
//            totalRecordsQuery.setParameter("userId", AuditContext.getAuditInfo().getUserId());
//            totalRecordsQuery.setParameter("productId", request.getProductId());
//            totalRecordsQuery.setParameter("limit", request.getPageSize());
//            totalRecordsQuery.setParameter("offset", request.getPage());
//            totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();
//
//            ProductOrgAssignDto dto = null;
//            for (Map<String, Object> row : results) {
//                log.info("Row: {}", row);
//
//                dto = ProductOrgAssignDto.builder()
//                        .tenantId(AuditContext.getAuditInfo().getTenantId())
//                        .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
//                        .orgName(ParseHelper.STRING.parse(row.get("org_name")))
//                        .orgCode(ParseHelper.STRING.parse(row.get("org_code")))
//                        .orgPhone(ParseHelper.STRING.parse(row.get("org_phone")))
//                        .orgWards(ParseHelper.STRING.parse(row.get("wards")))
//                        .isAssign(ParseHelper.STRING.parse(row.get("is_assign")))
//                        .isActive(ParseHelper.STRING.parse(row.get("is_assign")))
//                        .userId(ParseHelper.INT.parse(row.get("d_user_id")))
//                        .build();
//                list.add(dto);
//            }
//        } catch (Exception e) {
//            log.error("Error: {}", e.getMessage());
//            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
//        }


        if(request.getProductId() == null){
            request.setProductId(-1);
        }

        Parameter parameter = new Parameter();
        Integer userId = AuditContext.getAuditInfo().getUserId();

        parameter.add("d_user_id", userId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        StringBuilder tb1 = new StringBuilder("SELECT * FROM pos.d_get_user_org_access_2_v ");
        tb1.append(" where d_user_id = :userId ");


        StringBuilder tb2 = new StringBuilder("SELECT * FROM pos.d_assign_org_product where 1 = 1 ");
        tb2.append(" and d_product_id = :productId ");


        StringBuilder sql = new StringBuilder( "SELECT " +
                " tbl1.d_org_id," +
                " tbl1.wards," +
                " tbl1.org_name ," +
                " tbl1.org_code ," +
                " tbl1.phone," +
                " tbl1.d_user_id," +
                " tbl2.d_product_id," +
                " COALESCE(tbl2.is_active, 'N') AS is_assign" +
                " FROM (" + tb1 + ") as tbl1 left join (" + tb2 + ") as tbl2 " +
                "on tbl1.d_org_id = tbl2.d_org_id WHERE 1 = 1");

        log.info(sql.toString());

        if(request.getIsAssign() != null){
            sql.append(" AND COALESCE(tbl2.is_active, 'N') = :is_assign");
        }

        if(request.getOrgKeyword() != null){
            sql.append(" AND (unaccent(lower(tbl1.org_name)) like unaccent(:orgName) or unaccent(lower(tbl1.org_code)) like unaccent(:orgName)) ");
        }

        sql.append(" LIMIT :limit OFFSET :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        query.setParameter("productId", request.getProductId());
        if(request.getIsAssign() != null) {
            query.setParameter("is_assign", request.getIsAssign());
        }
        if(request.getOrgKeyword() != null) {
            query.setParameter("orgName", "%" + request.getOrgKeyword().toLowerCase() + "%");
        }

        query.setParameter("limit", request.getPageSize());
        query.setParameter("offset", request.getPageSize()*request.getPage());

        List<Map<String, Object>> results  = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ProductOrgAssignDto> list = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            ProductOrgAssignDto dto = ProductOrgAssignDto.builder()
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .orgName(ParseHelper.STRING.parse(row.get("org_name")))
                    .orgCode(ParseHelper.STRING.parse(row.get("org_code")))
                    .orgPhone(ParseHelper.STRING.parse(row.get("org_phone")))
                    .orgWards(ParseHelper.STRING.parse(row.get("wards")))
                    .isAssign(ParseHelper.STRING.parse(row.get("is_assign")))
                    .userId(ParseHelper.INT.parse(row.get("d_user_id")))
                    .build();
            list.add(dto);
        }

        request.setOrder(null);
        request.setSortBy(null);
        Pagination pagination = queryEngine.getPagination(" pos.d_get_user_org_access_2_v", parameter, request);
        log.info("Load pagination...");

        return GlobalReponsePagination.builder()
                .data(list)
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalItems(pagination.getTotalCount())
                .totalPages(pagination.getTotalPage())
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    @Transactional
    public GlobalReponse saveImage(Integer productId, ImageDto imageDto) {
        log.info("save Image Synchored");
        Product productSaveImage = this.productRepository.findByErpProductId(productId).orElse(null);
        if(productSaveImage!=null)
        {
            if(productSaveImage.getImage() != null)
            {
                imageDto.setId(productSaveImage.getImage().getId());
                imageDto.setImageCode(productSaveImage.getImage().getImageCode());
            }
            Image imageSynch = this.saveImage(ProductDto.builder().image(imageDto).build());
            if (imageSynch!=null)
                productSaveImage.setImage(imageSynch);
            this.productRepository.save(productSaveImage);
        }
        return GlobalReponse.builder().status(HttpStatus.OK.value()).message("Success").errors("").build();
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAllSimple(ProductQueryRequest request) {
        log.info("*** ProductDto List, service; fetch all products *");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        Specification<Product> spec   = Specification.where(null);
        if(request.getIsActive() != null)
            spec = ProductSpecification.hasIsActive(request.getIsActive());
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId()));
        if( request.getNotInIds() != null || !request.getNotInIds().isEmpty())
            spec = spec.and(ProductSpecification.notIds(request.getNotInIds()));
        Page<Product> products = productRepository.findAll(spec, pageable);
        List<ProductDto> listData = products.getContent().stream().map(product -> {
            ProductDto itemDto = productMapper.toDProductDto(product);
            ProductCategory productCategory = productCategoryRepository.findById(product.getProductCategoryId()).orElse(null);
            if (productCategory != null) {
                itemDto.setProductCategory(modelMapper.map(productCategory, ProductCategoryDto.class));
            }
            return itemDto;
        }).collect(Collectors.toList());
        return GlobalReponsePagination.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .data(listData)
                .totalItems(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .pageSize(products.getSize())
                .currentPage(products.getNumber())
                .build();
    }


    public SaveAllProductAttr saveAssignProductPart(SaveAllProductAttr request, Integer idProduct, String isItem,String isInsert) {

        //Component
        if(request.getItemProducts() != null && !request.getItemProducts().isEmpty()){
            this.productComboRepository.updateAllByProductId(idProduct, "Y");
            for (ProductComboDto itemId : request.getItemProducts()) { // thanh phan
                ProductCombo productCombo = null;
                productCombo = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, itemId.getProductComponentId(), "Y").orElse(null);
                if (productCombo == null) {
                    itemId.setProductId( idProduct);
                    itemId.setIsItem("Y");
                    itemId.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    itemId.setOrgId(0);
                    productComboRepository.save(modelMapper.map(itemId, ProductCombo.class));
                } else {
                    productCombo.setIsActive("Y");
                    productCombo.setQty(itemId.getQty());
                    productComboRepository.save(productCombo);
                }


            }
        }


        //Topping
        if(request.getToppingIds() != null && request.getToppingIds().length > 0){
            this.productComboRepository.updateAllByProductId(idProduct, "N");

            for (Integer toppingId : request.getToppingIds()) {// mon them

                // isItem  xu ly case product la mon them -> neu  la "Y" thi product save la productComponent con product Component la product Save
                ProductCombo productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, toppingId, "N").orElse(null);

                if (isItem.equals("Y"))
                    productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(toppingId, idProduct, "N").orElse(null);
                else
                    productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, toppingId, "N").orElse(null);

                if (productComboCheck == null) {
                    ProductCombo combo = ProductCombo.builder()
                            .productId(isItem.equals("Y") ? toppingId : idProduct)
                            .productComponentId(isItem.equals("Y") ? idProduct : toppingId)
                            .orgId(0)
                            .isItem("N")
                            .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                    productComboRepository.save(combo);
                } else {
                    productComboCheck.setIsActive("Y");
                    productComboRepository.save(productComboCheck);
                }

            }

        }

        //Assign org
        if (request.getOrgId() != null && request.getOrgId().length > 0){
            assignOrgRepository.updateIsActiveAllByProductId(idProduct);
            for (Integer orgId : request.getOrgId()) {
                AssignOrgProduct assignOrgCheck = assignOrgRepository.getByOrgIdAndProductId(orgId, idProduct).orElse(null);
                if (assignOrgCheck == null) {
                    AssignOrgProduct assignOrg = AssignOrgProduct.builder()
                            .productId(idProduct)
                            .orgId(orgId)
                            .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                    assignOrgRepository.save(assignOrg);
                } else {
                    assignOrgCheck.setIsActive("Y");
                    assignOrgRepository.save(assignOrgCheck);
                }
            }

        }




        return request;
    }


    /**
     * @param
     * @param
     * @return
     */


    public Image saveImage(ProductDto productDto) {
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
        mdmRequest.setImage64(productDto.getImage().getImage64());
        mdmRequest.setImageCode(productDto.getImage().getImageCode());
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
                        if (productDto.getImage().getId() != null)
                            image.setId(productDto.getImage().getId());

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

    //
    public SaveAllProductAttr saveAssignProduct(SaveAllProductAttr request, Integer idProduct, String isItem,String isInsert) {

        this.productComboRepository.updateAllByProductId(idProduct, "Y");
        for (ProductComboDto itemId : request.getItemProducts()) { // thanh phan
            ProductCombo productCombo = null;
            productCombo = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, itemId.getProductComponentId(), "Y").orElse(null);
            if (productCombo == null) {
                itemId.setProductId( idProduct);
                itemId.setIsItem("Y");
                itemId.setTenantId(AuditContext.getAuditInfo().getTenantId());
                itemId.setOrgId(0);
                productComboRepository.save(modelMapper.map(itemId, ProductCombo.class));
            } else {
                productCombo.setIsActive("Y");
                productCombo.setQty(itemId.getQty());
                productComboRepository.save(productCombo);
            }


        }
        this.productComboRepository.updateAllByProductId(idProduct, "N");

        for (Integer toppingId : request.getToppingIds()) {// mon them

            // isItem  xu ly case product la mon them -> neu  la "Y" thi product save la productComponent con product Component la product Save
            ProductCombo productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, toppingId, "N").orElse(null);

            if (isItem.equals("Y"))
                productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(toppingId, idProduct, "N").orElse(null);
            else
                productComboCheck = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(idProduct, toppingId, "N").orElse(null);

            if (productComboCheck == null) {
                ProductCombo combo = ProductCombo.builder()
                        .productId(isItem.equals("Y") ? toppingId : idProduct)
                        .productComponentId(isItem.equals("Y") ? idProduct : toppingId)
                        .orgId(0)
                        .isItem("N")
                        .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                productComboRepository.save(combo);
            } else {
                productComboCheck.setIsActive("Y");
                productComboRepository.save(productComboCheck);
            }

        }
//        productComboRepository.flush();
        if (request.getOrgId() != null) {
            assignOrgRepository.updateIsActiveAllByProductId(idProduct);
            for (Integer orgId : request.getOrgId()) {
                AssignOrgProduct assignOrgCheck = assignOrgRepository.getByOrgIdAndProductId(orgId, idProduct).orElse(null);
                if (assignOrgCheck == null) {
                    AssignOrgProduct assignOrg = AssignOrgProduct.builder()
                            .productId(idProduct)
                            .orgId(orgId)
                            .tenantId(AuditContext.getAuditInfo().getTenantId()).build();
                    assignOrgRepository.save(assignOrg);
                } else {
                    assignOrgCheck.setIsActive("Y");
                    assignOrgRepository.save(assignOrgCheck);
                }
            }
//            assignOrgRepository.flush();
        }


//        productLocationRepository.flush();

        return request;
    }


    public ProductChildDto getAttr(Product parent, ProductChildDto childDto) {
        // xu ly attr
        if (parent.getAttribute1() != null) {

            String attrName = parent.getAttribute1(); // 0 ->attr2 | 1 -> attr3 || 2 -> attr4

            String[] arrAttr = attrName.split(",");
            int i = 0;
            for (String item : arrAttr) {
                switch (i) {
                    case 0:
                        childDto.setNameAttr2(childDto.getAttribute2() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute2(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 1:
                        childDto.setNameAttr3(childDto.getAttribute3() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute3(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 2:
                        childDto.setNameAttr4(childDto.getAttribute4() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute4(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 3:
                        childDto.setNameAttr5(childDto.getAttribute5() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute5(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 4:
                        childDto.setNameAttr6(childDto.getAttribute6() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute6(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 5:
                        childDto.setNameAttr7(childDto.getAttribute7() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute7(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 6:
                        childDto.setNameAttr8(childDto.getAttribute8() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute8(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 7:
                        childDto.setNameAttr9(childDto.getAttribute8() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute9(), item, AuditContext.getAuditInfo().getTenantId()));
                        break;
                    case 8:
                        childDto.setNameAttr10(childDto.getAttribute10() == null ? "" : attributeValueRepository.getValueByAttr(childDto.getAttribute10(), item, AuditContext.getAuditInfo().getTenantId()));
                    default:

                }
                i++;
            }


        }
        return childDto;
    }

    public boolean isProductHasTransaction(Integer id) {
        try {
            String sql = "SELECT 1 " +
                    " FROM pos.d_pos_orderline WHERE d_tenant_id = :tenantId " +
                    " AND d_product_id = :productId limit 1";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("productId", id)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                return true;
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(messageSource.getMessage("receipt.other.calculate.error", null, LocaleContextHolder.getLocale()));
        }
        return false;
    }

    @Override
    public GlobalReponsePagination findAllV2(ProductQueryRequest req) {
        req.setOrder("desc");
        req.setSortBy("product_name");
        Parameter parameter = new Parameter();
        parameter.add("d_product_id", req.getProductId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("product_name", req.getSearchKey(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("product_code", req.getSearchKey(), Param.Logical.LIKE, Param.Relational.NONE, Param.END);

        ResultSet rs = queryEngine.getRecords( "pos.d_product_v", parameter, req);
        List<ProductDto> data = new ArrayList<>();
        try{
            while(rs.next()){
                ProductDto dto = ProductDto.builder()
                        .id(rs.getInt("d_product_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .code(rs.getString("product_code"))
                        .name(rs.getString("product_name"))
                        .saleprice(rs.getBigDecimal("saleprice"))
                        .costprice(rs.getBigDecimal("costprice"))
                        .productType(rs.getString("product_type"))
                        .productCategory(ProductCategoryDto.builder()
                                .id(rs.getInt("d_product_category_id"))
                                .name(rs.getString("product_category_name"))
                                .code(rs.getString("product_category_code"))
                                .build())
                        .tax(TaxDto.builder()
                                .id(rs.getInt("d_tax_id"))
                                .name(rs.getString("tax_name"))
                                .taxRate(rs.getBigDecimal("tax_rate"))
                                .build())
                        .uom(UomDto.builder()
                                .id(rs.getInt("d_uom_id"))
                                .name(rs.getString("uom_name"))
                                .code(rs.getString("uom_code"))
                                .build())
                        .image(ImageDto.builder()
                                .id(rs.getInt("d_image_id"))
                                .imageUrl(rs.getString("image_url"))
                                .build())
                        .build();
                data.add(dto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        Pagination pagination = queryEngine.getPagination("pos.d_product_v", parameter, req);
        log.info("Load pagination...");


        return  GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

}









