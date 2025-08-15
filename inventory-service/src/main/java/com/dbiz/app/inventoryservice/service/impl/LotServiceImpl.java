package com.dbiz.app.inventoryservice.service.impl;


import com.dbiz.app.inventoryservice.domain.Lot;
import com.dbiz.app.inventoryservice.repository.LotRepository;
import com.dbiz.app.inventoryservice.service.LotService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.CreateLotDto;
import org.common.dbiz.dto.inventoryDto.LotDto;
import org.common.dbiz.dto.inventoryDto.LotReqDto;
import org.common.dbiz.dto.productDto.BrandDto;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final MessageSource messageSource;
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination getLots(LotReqDto dto) {
        log.info("LotDto: {}", dto);


        Parameter parameter = new Parameter();
        parameter.add("d_lot_id", dto.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", dto.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_product_id", dto.getProductId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_locator_id", dto.getLocatorId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_warehouse_id", dto.getWarehouseId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("lot_code", dto.getCode(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("manufacture_date", Param.getBetweenParam(dto.getManufactureDateFrom(),dto.getManufactureDateTo()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("expiry_date", Param.getBetweenParam(dto.getExpirationDateFrom(),dto.getExpirationDateTo()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("is_active", dto.getIsActive(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("created", Param.getBetweenParam(dto.getCreated(),dto.getCreated()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("updated", Param.getBetweenParam(dto.getUpdated(),dto.getUpdated()), Param.Logical.BETWEEN, Param.Relational.NONE, Param.NONE);
        dto.setSortBy("expiry_date");
        dto.setOrder("asc");
        ResultSet rs = queryEngine.getRecords( "d_lot_v",
                parameter, dto);

        List<LotDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                LotDto lot = LotDto.builder()
                        .id(rs.getInt("d_lot_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .code(rs.getString("lot_code"))
                        .product(ProductDto.builder()
                                .id(rs.getInt("d_product_id"))
                                .name(rs.getString("product_name")).build())
                        .locator(LocatorDto.builder()
                                .id(rs.getInt("d_locator_id"))
                                .name(rs.getString("locator_name")).build())
                        .warehouse(WarehouseDto.builder()
                                .id(rs.getInt("d_warehouse_id"))
                                .name(rs.getString("warehouse_name")).build())
                        .costPrice(rs.getBigDecimal("costprice"))
                        .onHandQty(rs.getBigDecimal("onhand_qty"))
                        .manufactureDate(DateHelper.fromTimestampStd(rs.getTimestamp("manufacture_date")))
                        .expirationDate(DateHelper.fromTimestampStd(rs.getTimestamp("expiry_date")))
                        .isActive(rs.getString("is_active"))
                        .description(rs.getString("description"))
                        .created(DateHelper.fromTimestampStd(rs.getTimestamp("created")))
                        .updated(DateHelper.fromTimestampStd(rs.getTimestamp("updated"))).build();
                data.add(lot);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_lot_v", parameter, dto);
        log.info("Load pagination...");
        return GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    @Transactional
    @Override
    public GlobalReponse create(CreateLotDto dto) {

        log.info("Creating Lot: {}", dto);
        Lot lot = null;
        boolean isNew = true;
        try {
            if(dto.getId() == null) {
                lot = modelMapper.map(dto, Lot.class);
                if(dto.getCode() != null){
                    if(checkExistedCode(dto.getCode()).equals("FAI")){
                        throw new PosException(messageSource.getMessage("lot.code.existed", null, LocaleContextHolder.getLocale()));
                    }
                }else{
                    do{
                        Integer maxId = lotRepository.getMaxId() + 1;
                        String docNo = DocHelper.generateDocNo("LO",maxId) ;
                        lot.setCode(docNo);
                    }while (checkExistedCode(lot.getCode()).equals("FAI"));
                }

                lot.setTenantId(AuditContext.getAuditInfo().getTenantId());
            }else{
                lot = lotRepository.findById(dto.getId()).orElseThrow(() -> new PosException(messageSource.getMessage("lot.not.found", null, LocaleContextHolder.getLocale())));
                if(dto.getCode() != null && !lot.getCode().equals(dto.getCode())){
                    if(checkExistedCode(dto.getCode()).equals("FAI")){
                        throw new PosException(messageSource.getMessage("lot.code.existed", null, LocaleContextHolder.getLocale()));
                    }
                }
                modelMapper.map(dto, lot);
                isNew = false;
            }

            if(dto.getExpirationDate() != null){
                lot.setExpiryDate(DateHelper.toInstantUTC(dto.getExpirationDate()));
            }

            if(dto.getManufactureDate() != null){
                lot.setManufactureDate(DateHelper.toInstantUTC(dto.getManufactureDate()));
            }

            lot = lotRepository.save(lot);
            dto.setId(lot.getId());
        } catch (Exception e) {
            log.error("Error creating Lot: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
        GlobalReponse response = new GlobalReponse();
        response.setData(dto);
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setStatus(isNew == false  ? HttpStatus.OK.value() : HttpStatus.CREATED.value());
        log.info("Lot saved successfully with ID: {}", dto.getId());
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {

        try{
            if(checkExistedTransaction(id).equals("FAI")){
                throw new PosException(messageSource.getMessage("lot.transaction.existed", null, LocaleContextHolder.getLocale()));
            }
            lotRepository.deleteById(id);
        }catch (Exception e) {
            log.error("Error deleting Lot: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }


        GlobalReponse response = new GlobalReponse();
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }


    public String checkExistedTransaction(Integer lotId) {
        String sql = "SELECT count(1) FROM pos.d_transaction WHERE d_lot_id = :lotId";
        Query query = entityManager.createNativeQuery(sql);
        query = query.setParameter("lotId", lotId);
        Long count = ((Number) query.getSingleResult()).longValue();
        if (count > 0) {
            return "FAI";
        }
        return "COM";
    }

    public String checkExistedCode(String code) {
        String sql = "SELECT count(1) FROM pos.d_lot WHERE code = :code";
        Query query = entityManager.createNativeQuery(sql);
        query = query.setParameter("code", code);
        Long count = ((Number) query.getSingleResult()).longValue();
        if (count > 0) {
            return "FAI";
        }
        return "COM";
    }
}
