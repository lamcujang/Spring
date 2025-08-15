package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.Production;
import com.dbiz.app.orderservice.domain.ProductionLine;
import com.dbiz.app.orderservice.repository.DoctypeRepository;
import com.dbiz.app.orderservice.repository.ProductionLineRepository;
import com.dbiz.app.orderservice.repository.ProductionRepository;
import com.dbiz.app.orderservice.service.ProductionService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.orderDto.ProductionDto;
import org.common.dbiz.dto.orderDto.ProductionLineDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ProductionQueryRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ProductionServiceImpl implements ProductionService {

    private final MessageSource messageSource;
    private final ProductionRepository productionRepository;
    private final ProductionLineRepository productionLineRepository;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final DoctypeRepository doctypeRepository;
    private final RestTemplate restTemplate;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ProductionQueryRequest request) {
        log.info("ProductionDto findAll, service; find all production");
        List<ProductionDto> rsList = new ArrayList<>();
        StringBuilder sqlGetHeader = new StringBuilder("select * from d_production_get_all_v ");
        StringBuilder sqlGetDetail = new StringBuilder("select * from d_productionline_get_all_v where d_production_id = :productionId ");
        StringBuilder sqlCount = new StringBuilder("select count(1) from d_production_get_all_v ");
        StringBuilder sqlWhere = new StringBuilder(" where d_tenant_id = :tenantId ");

        if (request.getDocumentno() != null)
            sqlWhere.append(" and lower(documentno) = lower(:documentNo) ");
        if (request.getFromDate() != null)
            sqlWhere.append(" and DATE(movement_date) >= :fromDate ");
        if (request.getToDate() != null)
            sqlWhere.append(" and DATE(movement_date) <= :toDate ");
        if (request.getOrgId() != null)
            sqlWhere.append(" and d_org_id = :orgId ");
        if (request.getWarehouseId() != null)
            sqlWhere.append(" and d_warehouse_id = :warehouseId ");
        if (request.getOrderStatus() != null)
            sqlWhere.append(" and documentstatus = :status ");


        sqlGetHeader.append(sqlWhere);
        sqlGetHeader.append(" order by movement_date desc ");
        sqlCount.append(sqlWhere);

        Query queryHeader = entityManager.createNativeQuery(sqlGetHeader.toString(), Tuple.class);
        Query queryCount = entityManager.createNativeQuery(sqlCount.toString() );
        queryHeader.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        queryCount.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        if (request.getDocumentno() != null) {
            queryHeader.setParameter("documentNo", request.getDocumentno());
            queryCount.setParameter("documentNo", request.getDocumentno());
        }
        if (request.getFromDate() != null) {
            queryHeader.setParameter("fromDate", DateHelper.toLocalDate(request.getFromDate()));
            queryCount.setParameter("fromDate", DateHelper.toLocalDate(request.getFromDate()));
        }
        if (request.getToDate() != null) {
            queryHeader.setParameter("toDate", DateHelper.toLocalDate(request.getToDate()));
            queryCount.setParameter("toDate", DateHelper.toLocalDate(request.getToDate()));
        }
        if (request.getOrgId() != null) {
            queryHeader.setParameter("orgId", request.getOrgId());
            queryCount.setParameter("orgId", request.getOrgId());
        }
        if (request.getWarehouseId() != null) {
            queryHeader.setParameter("warehouseId", request.getWarehouseId());
            queryCount.setParameter("warehouseId", request.getWarehouseId());
        }
        if (request.getOrderStatus() != null) {
            queryHeader.setParameter("status", request.getOrderStatus());
            queryCount.setParameter("status", request.getOrderStatus());
        }

        BigInteger totalItemsBigInt = (BigInteger) queryCount.getSingleResult();
        Long totalItems = totalItemsBigInt.longValue();
        int offset = request.getPage() * request.getPageSize();

        sqlGetDetail.append(" order by lineno asc ");
        List<Tuple> resultList = queryHeader.setFirstResult(offset)
                .setMaxResults(request.getPageSize())
                .getResultList();
        for (Tuple item : resultList) {
            Instant movementDate = ParseHelper.INSTANT.parse(item.get("movement_date"));
            movementDate = movementDate.minus(7, ChronoUnit.HOURS);
            ProductionDto rsItem = ProductionDto.builder()
                    .id(ParseHelper.INT.parse(item.get("d_production_id")))
                    .isActive(item.get("is_active", String.class))
                    .documentStatus(item.get("documentstatus", String.class))
                    .statusName(item.get("status_name", String.class))
                    .documentno(item.get("documentno", String.class))
                    .name(item.get("production_name", String.class))
                    .productName(item.get("product_name", String.class))
                    .warehouseName(item.get("warehouse_name", String.class))
                    .movementDate(DateHelper.fromInstantUTC(movementDate))
                    .productId(ParseHelper.INT.parse(item.get("d_product_id")))
                    .orgId(ParseHelper.INT.parse(item.get("d_org_id")))
                    .isProcessed(item.get("is_processed", String.class))
                    .orgName(item.get("org_name", String.class))
                    .productionQty(ParseHelper.BIGDECIMAL.parse(item.get("production_qty")))
                    .warehouseId(ParseHelper.INT.parse(item.get("d_warehouse_id")))
                    .description(item.get("description", String.class))
                    .build();
            List<Tuple> rsLine = entityManager.createNativeQuery(sqlGetDetail.toString(), Tuple.class)
                    .setParameter("productionId", rsItem.getId())
                    .getResultList();
            List<ProductionLineDto> productionLines = rsLine.stream().map((element) ->
            {
                ProductionLineDto productionLine = ProductionLineDto.builder()
                        .id(ParseHelper.INT.parse(element.get("d_productionline_id")))
                        .orgId(ParseHelper.INT.parse(element.get("d_org_id")))
                        .isActive(ParseHelper.STRING.parse(element.get("is_active")))
                        .description(ParseHelper.STRING.parse(element.get("description")))
                        .productId(ParseHelper.INT.parse(element.get("d_product_id")))
                        .plannedQty(ParseHelper.BIGDECIMAL.parse(element.get("planned_qty")))
                        .uomName(element.get("uom_name", String.class))
                        .productName(element.get("product_name", String.class))
                        .usedQty(ParseHelper.BIGDECIMAL.parse(element.get("used_qty")))
                        .isEndProduct(element.get("is_end_product", String.class) == null ? "N" : element.get("is_end_product", String.class))
                        .lineNo(ParseHelper.INT.parse(element.get("lineno")))
                        .productionId(ParseHelper.INT.parse(element.get("d_production_id")))
                        .build();
                return productionLine;
            }).collect(Collectors.toList());
            rsItem.setProductionLines(productionLines);
            rsList.add(rsItem);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<ProductionDto> pageRs = new PageImpl<>(rsList, pageable, totalItems);
        return GlobalReponsePagination.builder()
                .data(rsList)
                .totalItems(pageRs.getTotalElements())
                .totalPages(pageRs.getTotalPages())
                .currentPage(pageRs.getNumber())
                .pageSize(pageRs.getSize())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        Query queryHeader = entityManager.createNativeQuery("select * from d_production_get_all_v where d_production_id = :productionId ", Tuple.class);
        GlobalReponse rs = new GlobalReponse();
        List<Tuple> resultList = queryHeader
                .getResultList();
        for (Tuple item : resultList) {
            Instant movementDate = ParseHelper.INSTANT.parse(item.get("movement_date"));
            movementDate = movementDate.minus(7, ChronoUnit.HOURS);
            ProductionDto rsItem = ProductionDto.builder()
                    .id(ParseHelper.INT.parse(item.get("d_production_id")))
                    .isActive(item.get("is_active", String.class))
                    .documentStatus(item.get("documentstatus", String.class))
                    .statusName(item.get("status_name", String.class))
                    .documentno(item.get("documentno", String.class))
                    .name(item.get("production_name", String.class))
                    .productName(item.get("product_name", String.class))
                    .warehouseName(item.get("warehouse_name", String.class))
                    .movementDate(DateHelper.fromInstantUTC(movementDate))
                    .productId(ParseHelper.INT.parse(item.get("d_product_id")))
                    .orgId(ParseHelper.INT.parse(item.get("d_org_id")))
                    .isProcessed(item.get("is_processed", String.class))
                    .build();
            List<Tuple> rsLine = entityManager.createNativeQuery("select * from  ", Tuple.class)
                    .setParameter("productionId", rsItem.getId())
                    .getResultList();
            List<ProductionLineDto> productionLines = rsLine.stream().map((element) ->
            {
                ProductionLineDto productionLine = ProductionLineDto.builder()
                        .id(ParseHelper.INT.parse(element.get("d_productionline_id")))
                        .productId(ParseHelper.INT.parse(element.get("d_product_id")))
                        .plannedQty(ParseHelper.BIGDECIMAL.parse(element.get("planned_qty")))
                        .isEndProduct(element.get("is_end_product", String.class))
                        .uomName(element.get("uom_name", String.class))
                        .productName(element.get("product_name", String.class))
                        .usedQty(ParseHelper.BIGDECIMAL.parse(element.get("used_qty")))
                        .isEndProduct(element.get("is_end_product", String.class))
                        .lineNo(ParseHelper.INT.parse(element.get("lineno")))
                        .productionId(ParseHelper.INT.parse(element.get("d_production_id")))
                        .build();
                return productionLine;
            }).collect(Collectors.toList());
            rsItem.setProductionLines(productionLines);
            rs.setData(rsItem);
            rs.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
            rs.setStatus(HttpStatus.OK.value());
        }
        return rs;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public GlobalReponse save(ProductionDto entity) {
        log.info("ProductionDto save, service; save production");
        Production production = modelMapper.map(entity, Production.class);
        GlobalReponse response = new GlobalReponse();
        String isUpdate = "N";

        if (entity.getId() != null)// update
        {
            Production productionDb = productionRepository.findById(entity.getId()).orElseThrow(() -> new PosException(messageSource.getMessage("production.not.found", null, LocaleContextHolder.getLocale())));
            modelMapper.map(entity, productionDb);
            if (productionDb.getDocumentStatus().equals("COM"))
                productionDb.setIsProcessed("Y");
            productionRepository.save(productionDb);

            if(entity.getOrgId() == null){
                entity.setOrgId(productionDb.getOrgId());
            }
            response.setMessage(messageSource.getMessage("production.update.success", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.OK.value());
            isUpdate = "Y";
        } else { // insert

            if (entity.getDocumentno() == null || entity.getDocumentno().isEmpty())
                production.setDocumentno("KCB" + (this.productionRepository.getMaxId() + 1) + LocalDate.now().getDayOfMonth());
            production.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.PRODUCTION).getId());
            if (production.getDocumentStatus().equals("COM"))
                production.setIsProcessed("Y");
            else
                production.setIsProcessed("N");
            production.setIsSyncErp("N");
            if(entity.getMovementDate()!= null)
            {
//                Instant movementDate = DateHelper.toInstantDateAndTime1(entity.getMovementDate());
//                movementDate = movementDate.plus(7, ChronoUnit.HOURS);
//                production.setMovementDate(movementDate);
                production.setMovementDate(DateHelper.toInstantUTC(entity.getMovementDate()));
            }
            else
                production.setMovementDate(DateHelper.toInstantNowUTC());
            production.setTenantId(AuditContext.getAuditInfo().getTenantId());
            productionRepository.save(production);

            entity.setId(production.getId());
            entity.setDocumentno(production.getDocumentno());
            entity.setDoctypeId(production.getDoctypeId());

            response.setMessage(messageSource.getMessage("production.save.success", null, LocaleContextHolder.getLocale()));
            response.setStatus(HttpStatus.CREATED.value());
        }



        if(entity.getProductionLines() != null && entity.getProductionLines().size() > 0){
            if(isUpdate.equals("Y"))
            {
                productionLineRepository.deleteAllByProductionId(entity.getId());
            }
            final Integer[] lineNo = {20};
            List<ProductionLineDto> productionLines = entity.getProductionLines().stream().map((element) ->
                    {
                        ProductionLine productionLine = modelMapper.map(element, ProductionLine.class);
                        productionLine.setProductionId(production.getId());
                        productionLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        productionLine.setLineNo(lineNo[0]);
                        productionLine.setIsEndProduct("N");
                        productionLine.setId(null);
                        productionLineRepository.save(productionLine);
                        element.setId(productionLine.getId());
                        lineNo[0] += 10;
                        return element;
                    }
            ).collect(Collectors.toList());
            ProductionLine lineEndProduct = ProductionLine.builder()
                    .productId(entity.getProductId())
                    .plannedQty(entity.getProductionQty())
                    .usedQty(entity.getProductionQty())
                    .isEndProduct("Y")
                    .productionId(production.getId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(production.getOrgId())
                    .lineNo(10)
                    .build();
            productionLineRepository.save(lineEndProduct);
            productionLines.add(modelMapper.map(lineEndProduct, ProductionLineDto.class));
            entity.setProductionLines(productionLines);

        }

        response.setData(entity);

        if(production.getDocumentStatus().equals("COM"))
        {
//            productionRepository.flush();
//            productionRepository.flush();

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
            headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
            TransactionDto transactionDto = TransactionDto.builder()
                    .orgId(entity.getOrgId())

                    .build();
            if(entity.getProductionLines() != null && entity.getProductionLines().size() > 0)
            {
                transactionDto.setProductionDtos(List.of(entity));
            }else{
                transactionDto.setProductionIds(List.of(entity.getId()));
            }
            HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionDto, headers);
            GlobalReponse responseTransaction = this.restTemplate
                    .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION ,
                            requestEntity,
                            GlobalReponse.class)
                    .getBody();
            if(responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                    && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                throw new RuntimeException(responseTransaction.getMessage());
            }
        }
        return response;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse deleteProductionLineById(Integer id) {
        log.info("*** Production Line, service; delete Production Line ***");

        try{
            ProductionLine productionLine = productionLineRepository.findById(id).orElseThrow(() -> new PosException(messageSource.getMessage("not.found", null, LocaleContextHolder.getLocale())));
            productionLineRepository.deleteById(id);
        }catch (Exception e){
            log.error("Production Line, service; delete Production Line error: {}", e.getMessage());
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }
        return GlobalReponse.builder().message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }
}
