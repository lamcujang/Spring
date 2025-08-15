package com.dbiz.app.reportservice.service.impl;

import com.dbiz.app.reportservice.service.ReportService;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.dto.reportDto.response.*;
import org.common.dbiz.dto.reportDto.response.customer.*;
import org.common.dbiz.dto.reportDto.response.businessPerformance.*;
import org.common.dbiz.dto.reportDto.response.expenseList.*;
import org.common.dbiz.dto.reportDto.response.material.*;
import org.common.dbiz.dto.reportDto.response.revenue.*;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EntityManager entityManager;
    private final DataSource dataSource;
    private final MessageSource messageSource;


    @Override
    public GlobalReponse getReportByServiceType(ReportReqDto rq) {

        ReportSaleServiceTypeDto reportSaleServiceTypeDto =null;

        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_sale_service_type(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getQueryType());
            callableStatement.setString(5, rq.getStartDate());
            callableStatement.setString(6, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportSaleServiceTypeDto = ReportSaleServiceTypeDto.builder()
                        .serviceTypeObject(rs.getString("json_data"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportSaleServiceTypeDto)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportByEmployee(ReportReqDto rq) {

        ReportSaleEmpDto reportEmp =null;
        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getEmpId() == null){
            rq.setEmpId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_sale_emp(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getEmpOrAll());
            callableStatement.setInt(5, rq.getEmpId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportEmp = ReportSaleEmpDto.builder()
                        .empObject(rs.getString("json_data"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportEmp)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportByCustomer(ReportReqDto rq) {
        ReportSaleCusDto reportCus =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getCusId() == null){
            rq.setCusId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_sale_cus(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getCusOrAll());
            callableStatement.setInt(5, rq.getCusId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportCus = ReportSaleCusDto.builder()
                        .cusObject(rs.getString("json_data"))
                        .saleQty(rs.getBigDecimal("sale_qty"))
                        .returnQty(rs.getBigDecimal("return_qty"))
                        .returnAmount(rs.getBigDecimal("return_amount"))
                        .revenue(rs.getBigDecimal("revenue"))
                        .netRevenue(rs.getBigDecimal("net_revenue"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportCus)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportByPayment(ReportReqDto rq) {

        ReportSalePaymentDto reportPayment =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_sale_payment(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getTerminalOrAll());
            callableStatement.setInt(5, rq.getTerminalId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportPayment = ReportSalePaymentDto.builder()
                        .paymentObject(rs.getString("json_data"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .totalGuests(rs.getBigDecimal("total_guests"))
                        .cashAmount(rs.getBigDecimal("cash"))
                        .bankAmount(rs.getBigDecimal("bank"))
                        .visaAmount(rs.getBigDecimal("visa"))
                        .debtAmount(rs.getBigDecimal("deb"))
                        .loyaltyAmount(rs.getBigDecimal("loyalty"))
                        .couponAmount(rs.getBigDecimal("coupon"))
                        .freeAmount(rs.getBigDecimal("free"))
                        .qrcodeAmount(rs.getBigDecimal("qrcode"))
                        .voucherAmount(rs.getBigDecimal("voucher"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportPayment)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportByCancel(ReportReqDto rq) {

        ReportSaleCancelDto reportCancel =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_sale_cancel(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getTerminalOrAll());
            callableStatement.setInt(5, rq.getTerminalId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportCancel = ReportSaleCancelDto.builder()
                        .cancelObject(rs.getString("json_data"))
                        .totalCancelAmount(rs.getBigDecimal("total_cancel_amount"))
                        .totalCancelQty(rs.getBigDecimal("total_cancel_qty"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportCancel)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportByProductRevenue(ReportReqDto rq) {

        ReportProductRevenueDto reportProduct =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_product_revenue(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getTerminalOrAll());
            callableStatement.setInt(5, rq.getTerminalId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportProduct = ReportProductRevenueDto.builder()
                        .productObject(rs.getString("json_data"))
                        .saleQty(rs.getBigDecimal("sale_qty"))
                        .returnQty(rs.getBigDecimal("return_qty"))
                        .returnAmount(rs.getBigDecimal("return_amount"))
                        .revenue(rs.getBigDecimal("revenue"))
                        .costPrice(rs.getBigDecimal("costprice"))
                        .netRevenue(rs.getBigDecimal("net_revenue"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportProduct)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportINVOnHand(ReportReqDto rq) {

        ReportInvOnHand reportInv =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getWarehouseId() == null){
            rq.setWarehouseId(0);
        }

        if(rq.getProductId() == null){
            rq.setProductId(0);
        }

        if(rq.getProductCategoryId() == null){
            rq.setProductCategoryId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_inv_onhand(?, ?, ?,?,?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getWarehouseOrAll());
            callableStatement.setInt(5, rq.getWarehouseId());
            callableStatement.setString(6, rq.getProductCategoryOrAll());
            callableStatement.setInt(7, rq.getProductCategoryId());
            callableStatement.setString(8, rq.getProductOrAll());
            callableStatement.setInt(9, rq.getProductId());
            callableStatement.setString(10, rq.getQueryType());
            callableStatement.setString(11, rq.getStartDate());
            callableStatement.setString(12, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportInv = ReportInvOnHand.builder()
                        .invObject(rs.getString("json_data"))
                        .totalQty(rs.getBigDecimal("total_qty"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportInv)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportINVInOut(ReportReqDto rq) {

        ReportInvInOut reportInv =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getWarehouseId() == null){
            rq.setWarehouseId(0);
        }

        if(rq.getProductId() == null){
            rq.setProductId(0);
        }

        if(rq.getProductCategoryId() == null){
            rq.setProductCategoryId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_inv_inout(?, ?, ?,?,?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getWarehouseOrAll());
            callableStatement.setInt(5, rq.getWarehouseId());
            callableStatement.setString(6, rq.getProductCategoryOrAll());
            callableStatement.setInt(7, rq.getProductCategoryId());
            callableStatement.setString(8, rq.getProductOrAll());
            callableStatement.setInt(9, rq.getProductId());
            callableStatement.setString(10, rq.getQueryType());
            callableStatement.setString(11, rq.getStartDate());
            callableStatement.setString(12, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                reportInv = ReportInvInOut.builder().
                        invObject(rs.getString("json_data"))
                        .startQty(rs.getBigDecimal("start_qty"))
                        .startAmount(rs.getBigDecimal("start_amount"))
                        .inQty(rs.getBigDecimal("in_qty"))
                        .inAmount(rs.getBigDecimal("in_amount"))
                        .outQty(rs.getBigDecimal("out_qty"))
                        .outAmount(rs.getBigDecimal("out_amount"))
                        .endQty(rs.getBigDecimal("end_qty"))
                        .endAmount(rs.getBigDecimal("end_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(reportInv)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportProductionDT(ReportReqDto rq) {

        ReportProductionDT report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getWarehouseId() == null){
            rq.setWarehouseId(0);
        }

        if(rq.getProductId() == null){
            rq.setProductId(0);
        }

        if(rq.getProductionId() == null){
            rq.setProductionId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_production_dt(?, ?, ?,?,?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getWarehouseOrAll());
            callableStatement.setInt(5, rq.getWarehouseId());
            callableStatement.setString(6, rq.getProductOrAll());
            callableStatement.setInt(7, rq.getProductId());
            callableStatement.setString(8, rq.getProductionOrAll());
            callableStatement.setInt(9, rq.getProductionId());
            callableStatement.setString(10, rq.getQueryType());
            callableStatement.setString(11, rq.getStartDate());
            callableStatement.setString(12, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportProductionDT.builder().
                        productionObj(rs.getString("json_data"))
                        .usedQty(rs.getBigDecimal("used_qty"))
                        .costPrice(rs.getBigDecimal("costprice"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportProductionDTV2(ReportReqDto rq) {
        ReportProductionDT report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getWarehouseId() == null){
            rq.setWarehouseId(0);
        }

        if(rq.getProductId() == null){
            rq.setProductId(0);
        }

        if(rq.getProductionId() == null){
            rq.setProductionId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_production_dt_v2(?, ?, ?,?,?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getWarehouseOrAll());
            callableStatement.setInt(5, rq.getWarehouseId());
            callableStatement.setString(6, rq.getProductOrAll());
            callableStatement.setInt(7, rq.getProductId());
            callableStatement.setString(8, rq.getProductionOrAll());
            callableStatement.setInt(9, rq.getProductionId());
            callableStatement.setString(10, rq.getQueryType());
            callableStatement.setString(11, rq.getStartDate());
            callableStatement.setString(12, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportProductionDT.builder().
                        productionObj(rs.getString("json_data"))
                        .usedQty(rs.getBigDecimal("used_qty"))
                        .costPrice(rs.getBigDecimal("costprice"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportKitchenOrderDT(ReportReqDto rq) {

        ReportKitchenOrderDT report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getWarehouseId() == null){
            rq.setWarehouseId(0);
        }


        if(rq.getStatusValue() == null){
            rq.setStatusValue("DBIZ");
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_kitchen_order_dt(?, ?, ?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getWarehouseOrAll());
            callableStatement.setInt(5, rq.getWarehouseId());
            callableStatement.setString(6, rq.getStatusOrAll());
            callableStatement.setString(7, rq.getStatusValue());
            callableStatement.setString(8, rq.getQueryType());
            callableStatement.setString(9, rq.getStartDate());
            callableStatement.setString(10, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportKitchenOrderDT.builder().
                        object(rs.getString("json_data"))
                        .totalQty(rs.getBigDecimal("total_qty"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportSCTransfer(ReportReqDto rq) {

        ReportKitchenOrderDT report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(-1);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(-1);
        }


        if(rq.getShiftTypeValue() == null){
            rq.setShiftTypeValue("DBIZ");
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_shift_control_transfer(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setInt(2, rq.getOrgId());
            callableStatement.setInt(3, rq.getTerminalId());
            callableStatement.setString(4, rq.getShiftTypeOrAll());
            callableStatement.setString(5, rq.getShiftTypeValue());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportKitchenOrderDT.builder().
                        object(rs.getString("json_data"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportSCToClose(ReportReqDto rq) {

        ReportSCToClose report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(0);
        }

//        if(rq.getShiftControlId() == null){
//            rq.setShiftControlId(0);
//        }

        if(rq.getShiftTypeValue() == null){
            rq.setShiftTypeValue("DBIZ");
        }

        if(rq.getEmpId() == null){
            rq.setEmpId(0);
        }

        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_shift_control_to_close(?, ?, ?,?,?,?,?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getTerminalOrAll());
            callableStatement.setInt(5, rq.getTerminalId());
            callableStatement.setString(6, rq.getShiftTypeOrAll());
            callableStatement.setString(7, rq.getShiftTypeValue());
            callableStatement.setString(8, rq.getEmpOrAll());
            callableStatement.setInt(9, rq.getEmpId());
            callableStatement.setString(10, rq.getQueryType());
            callableStatement.setString(11, rq.getStartDate());
            callableStatement.setString(12, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportSCToClose.builder().
                        object(rs.getString("json_data"))
                        .totalGuests(rs.getBigDecimal("total_guests"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .cashAmount(rs.getBigDecimal("cash"))
                        .bankAmount(rs.getBigDecimal("bank"))
                        .visaAmount(rs.getBigDecimal("visa"))
                        .debtAmount(rs.getBigDecimal("deb"))
                        .loyaltyAmount(rs.getBigDecimal("loyalty"))
                        .couponAmount(rs.getBigDecimal("coupon"))
                        .freeAmount(rs.getBigDecimal("free"))
                        .qrcodeAmount(rs.getBigDecimal("qrcode"))
                        .voucherAmount(rs.getBigDecimal("voucher"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportAcctByCustomer(ReportReqDto rq) {

        ReportAcctCusDto report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getCusId() == null){
            rq.setCusId(0);
        }



        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_acct_cus(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getCusOrAll());
            callableStatement.setInt(5, rq.getCusId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportAcctCusDto.builder().
                        object(rs.getString("json_data"))
                        .openingDebtAmount(rs.getBigDecimal("opening_debt_amount"))
                        .debitAmount(rs.getBigDecimal("debit_amount"))
                        .creditAmount(rs.getBigDecimal("credit_amount"))
                        .lastDebtAmount(rs.getBigDecimal("last_debt_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportAcctByPayment(ReportReqDto rq) {
        ReportAcctPaymentDto report =null;

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

        if(rq.getTerminalId() == null){
            rq.setTerminalId(0);
        }



        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_rp_acct_payment(?, ?, ?,?,?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setString(4, rq.getTerminalOrAll());
            callableStatement.setInt(5, rq.getTerminalId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                report = ReportAcctPaymentDto.builder().
                        object(rs.getString("json_data"))
                        .arAmount(rs.getBigDecimal("ar_amount"))
                        .apAmount(rs.getBigDecimal("ap_amount"))
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportCashBook(ReportReqDto req) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_rp_cash_book(?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, req.getOrgOrAll());

            List<Integer> orgIds = req.getOrgIds();
            if (orgIds != null && !orgIds.isEmpty()) {
                Integer[] array = orgIds.toArray(new Integer[0]);
                Array sqlArray = connection.createArrayOf("NUMERIC", array); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(3, sqlArray);
            } else {
                callableStatement.setNull(3, Types.ARRAY);
            }


            callableStatement.setString(4, req.getQueryType());
            callableStatement.setString(5, req.getStartDate());
            callableStatement.setString(6, req.getEndDate());
            callableStatement.setString(7, req.getPayMethodOrAll());
            callableStatement.setString(8, req.getPayMethodValue());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs != null && rs.next())  {
                String jsonData = rs.getString("json_data");

                List<PaymentDto> list = parsePayments(jsonData);

                ReportCashBook report = ReportCashBook.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .openingBalance(ParseHelper.BIGDECIMAL.parse(rs.getBigDecimal("openning_balance")))
                        .closingBalance(ParseHelper.BIGDECIMAL.parse(rs.getBigDecimal("closing_balance")))
                        .totalOutAmount(ParseHelper.BIGDECIMAL.parse(rs.getBigDecimal("ap_payment")))
                        .totalInAmount(ParseHelper.BIGDECIMAL.parse(rs.getBigDecimal("ar_payment")))
                        .payments(list)
                        .build();

                return GlobalReponse.builder()
                        .data(report)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }else{ // trường hợp ko trả về data
                return GlobalReponse.builder()
                        .data(null)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        throw new PosException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale()));
    }

    @Override
    public GlobalReponse getReportCustomerDebit(ReportReqDto req) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_rp_customer_debit(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific

            List<Integer> customerIds = req.getPartnerIds();
            if (customerIds != null && !customerIds.isEmpty()) {
                Integer[] array = customerIds.toArray(new Integer[0]);
                Array sqlArray = connection.createArrayOf("NUMERIC", array); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(2, sqlArray);
            } else {
                callableStatement.setNull(2, Types.ARRAY);
            }


            callableStatement.setString(3, req.getQueryType());
            callableStatement.setString(4, req.getStartDate());
            callableStatement.setString(5, req.getEndDate());
            callableStatement.setString(6, req.getPartnerHasData());
            callableStatement.execute();
            // Execute the query and retrieve the ref cursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs != null && rs.next())  {
                String jsonData = rs.getString("json_data");

                List<PartnerDebitDto> list = parsePartnerDebit(jsonData);

                ReportPartnerDebit report = ReportPartnerDebit.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .negOpening(rs.getBigDecimal("total_negative_opening"))
                        .posOpening(rs.getBigDecimal("total_positive_opening"))
                        .receivable(rs.getBigDecimal("total_receivable_amount"))
                        .collected(rs.getBigDecimal("total_amount_collected"))
                        .negClosing(rs.getBigDecimal("total_negative_closing"))
                        .posClosing(rs.getBigDecimal("total_positive_closing"))
                        .partnerDebit(list)
                        .build();

                return GlobalReponse.builder()
                        .data(report)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }else{ // trường hợp ko trả về data
                return GlobalReponse.builder()
                        .data(null)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        throw new PosException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale()));
    }

    @Override
    public GlobalReponse getReportVendorDebit(ReportReqDto req) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_rp_vendor_debit(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific

            List<Integer> customerIds = req.getPartnerIds();
            if (customerIds != null && !customerIds.isEmpty()) {
                Integer[] array = customerIds.toArray(new Integer[0]);
                Array sqlArray = connection.createArrayOf("NUMERIC", array); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(2, sqlArray);
            } else {
                callableStatement.setNull(2, Types.ARRAY);
            }


            callableStatement.setString(3, req.getQueryType());
            callableStatement.setString(4, req.getStartDate());
            callableStatement.setString(5, req.getEndDate());
            callableStatement.setString(6, req.getPartnerHasData());
            callableStatement.execute();
            // Execute the query and retrieve the ref cursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs != null && rs.next())  {
                String jsonData = rs.getString("json_data");
                log.info("String jsonData: {}", jsonData);

                List<PartnerDebitDto> list = parsePartnerDebit(jsonData);

                ReportPartnerDebit report = ReportPartnerDebit.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .negOpening(rs.getBigDecimal("total_negative_opening"))
                        .posOpening(rs.getBigDecimal("total_positive_opening"))
                        .receivable(rs.getBigDecimal("total_receivable_amount"))
                        .collected(rs.getBigDecimal("total_amount_collected"))
                        .negClosing(rs.getBigDecimal("total_negative_closing"))
                        .posClosing(rs.getBigDecimal("total_positive_closing"))
                        .partnerDebit(list)
                        .build();

                return GlobalReponse.builder()
                        .data(report)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }else{ // trường hợp ko trả về data
                return GlobalReponse.builder()
                        .data(null)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        throw new PosException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale()));
    }

    @Override
    public GlobalReponse getRevenueProduction(ReportReqDto req) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_rp_revenue_production(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, req.getOrgOrAll());

            List<Integer> orgIds = req.getOrgIds();
            if (orgIds != null && !orgIds.isEmpty()) {
                Integer[] array = orgIds.toArray(new Integer[0]);
                Array sqlArray = connection.createArrayOf("NUMERIC", array); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(3, sqlArray);
            } else {
                callableStatement.setNull(3, Types.ARRAY);
            }


            callableStatement.setString(4, req.getQueryType());
            callableStatement.setString(5, req.getStartDate());
            callableStatement.setString(6, req.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs != null && rs.next())  {
                String jsonTotalAmount = rs.getString("summary");
                String jsonRevenue = rs.getString("data");

                List<RevenueChildDTO> list1 = parseSummary(jsonTotalAmount);
                List<InvoiceHouseHoldDto> list2 = parseRevenueProduction(jsonRevenue);

                RevenueBusiness report = RevenueBusiness.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .summaryAmount(list1)
                        .raws(list2)
                        .build();

                return GlobalReponse.builder()
                        .data(report)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }else{ // trường hợp ko trả về data
                return GlobalReponse.builder()
                        .data(null)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        throw new PosException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale()));
    }

    @Override
    public GlobalReponse getReportDetailedMaterialLedger(ReportReqDto req) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_detailed_material_ledger(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, req.getOrgOrAll());

            List<Integer> orgIds = req.getOrgIds();
            if (orgIds != null && !orgIds.isEmpty()) {
                Integer[] array = orgIds.toArray(new Integer[0]);
                Array sqlArray = connection.createArrayOf("NUMERIC", array); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(3, sqlArray);
            } else {
                callableStatement.setNull(3, Types.ARRAY);
            }


            callableStatement.setString(4, req.getWarehouseOrAll());
            if (req.getWarehouseId() != null) {
                callableStatement.setInt(5, req.getWarehouseId());
            } else {
                callableStatement.setNull(5, Types.INTEGER);
            }
            callableStatement.setString(6, req.getProductCategoryOrAll());
            if (req.getProductCategoryId() != null) {
                callableStatement.setInt(7, req.getProductCategoryId());
            } else {
                callableStatement.setNull(7, Types.INTEGER);
            }
            callableStatement.setString(8, req.getProductOrAll());

            List<Integer> productIds = req.getProductIds();
            if (productIds != null && !productIds.isEmpty()) {
                Integer[] array1 = productIds.toArray(new Integer[0]);
                Array sqlArray1 = connection.createArrayOf("NUMERIC", array1); // "NUMERIC" nếu bên DB là NUMERIC[]
                callableStatement.setArray(9, sqlArray1);
            } else {
                callableStatement.setNull(9, Types.ARRAY);
            }

            callableStatement.setString(10, req.getQueryType());
            callableStatement.setString(11, req.getStartDate());
            callableStatement.setString(12, req.getEndDate());
            callableStatement.setString(13, req.getShowInventory());

            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs != null && rs.next())  {
                String jsonRevenue = rs.getString("json_data");

                List<ProductLedger> list2 = parseProductLedger(jsonRevenue);

                MaterialLedger report = MaterialLedger.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .data(list2)
                        .build();

                return GlobalReponse.builder()
                        .data(report)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }else{ // trường hợp ko trả về data
                return GlobalReponse.builder()
                        .data(null)
                        .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                        .status(HttpStatus.OK.value())
                        .errors("")
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        throw new PosException(messageSource.getMessage("failed",null, LocaleContextHolder.getLocale()));
    }

    private List<PartnerDebitDto> parsePartnerDebit(String json) {
        if(json == null || json.isEmpty()) return null;

        List<PartnerDebitDto> result = new ArrayList<>();
        try {
            log.info(json);
            JSONObject root = new JSONObject(json);
            JSONArray paymentsArray = root.getJSONArray("partners");

            for (int i = 0; i < paymentsArray.length(); i++) {
                JSONObject obj = paymentsArray.getJSONObject(i);
                PartnerDebitDto dto = new PartnerDebitDto();

                dto.setId(obj.optInt("d_partner_id", 0));
                dto.setCode(obj.optString("code", null));
                dto.setName(obj.optString("name", null));
                dto.setAddress(obj.optString("address1", null));
                dto.setTaxCode(obj.optString("tax_code", null));
                dto.setClosingBalance(obj.has("closing_balance") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("closing_balance")) : null);
                dto.setOpeningBalance(obj.has("openning_balance") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("openning_balance")) : null);
                dto.setAmountCollected(obj.has("amount_collected") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("amount_collected")) : null);
                dto.setReceivableAmount(obj.has("receivable_amount") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("receivable_amount")) : null);

                result.add(dto);
            }
        } catch (JSONException e) {
             log.info("Invalid JSON structure: " + e.getMessage(), e);
        }

        return result;
    }

    private List<RevenueChildDTO> parseSummary(String json){
        if (json == null || json.isEmpty()) return null;

        List<RevenueChildDTO> result = new ArrayList<>();

        try {
            log.info(json);
            JSONArray paymentsArray = new JSONArray(json);

            for (int i = 0; i < paymentsArray.length(); i++) {
                JSONObject obj = paymentsArray.getJSONObject(i);

                RevenueChildDTO dto = new RevenueChildDTO();
                dto.setCode(obj.optString("childCode", null));
                dto.setTotalAmount(
                        obj.has("totalAmount") && !obj.isNull("totalAmount")
                                ? obj.getBigDecimal("totalAmount")
                                : BigDecimal.ZERO
                );
                dto.setName(obj.optString("childName", null));

                result.add(dto);
            }

            return result;
        } catch (JSONException e) {
            log.error("Invalid JSON structure: " + e.getMessage(), e);
        }

        return result;
    }

    private List<InvoiceHouseHoldDto> parseRevenueProduction(String json) {
        if (json == null || json.isEmpty()) return null;

        List<InvoiceHouseHoldDto> result = new ArrayList<>();

        try {
            log.info(json);
            JSONArray paymentsArray = new JSONArray(json);

            for (int i = 0; i < paymentsArray.length(); i++) {
                JSONObject obj = paymentsArray.getJSONObject(i);

                InvoiceHouseHoldDto dto = new InvoiceHouseHoldDto();
                dto.setInvoiceCode(obj.optString("invoiceCode", null));
                dto.setInvoiceDescription(obj.optString("invoiceDescription", null));

                // Parse date if needed
                if (obj.has("invoiceDate") && !obj.isNull("invoiceDate")) {
                    String isoDate = obj.getString("invoiceDate");
                    try {
                        LocalDateTime date = LocalDateTime.parse(isoDate);
                        dto.setInvoiceDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } catch (DateTimeParseException e) {
                        dto.setInvoiceDate(null);
                    }
                }

                // Parse revenue
                List<RevenueGroupDto> revenueGroups = new ArrayList<>();
                JSONArray revenueArray = obj.optJSONArray("revenue");
                if (revenueArray != null) {
                    for (int j = 0; j < revenueArray.length(); j++) {
                        JSONObject revObj = revenueArray.getJSONObject(j);
                        RevenueGroupDto revGroup = new RevenueGroupDto();
                        revGroup.setCode(revObj.optString("code", null));
                        revGroup.setNameGroup(revObj.optString("nameGroup", null));

                        // Parse child
                        List<RevenueChildDTO> children = new ArrayList<>();
                        JSONArray childArray = revObj.optJSONArray("child");
                        if (childArray != null) {
                            for (int k = 0; k < childArray.length(); k++) {
                                JSONObject childObj = childArray.getJSONObject(k);
                                RevenueChildDTO child = new RevenueChildDTO();
                                child.setName(childObj.optString("name", null));
                                child.setTotalAmount(
                                        childObj.has("totalAmount") && !childObj.isNull("totalAmount")
                                                ? childObj.getBigDecimal("totalAmount")
                                                : BigDecimal.ZERO
                                );
                                children.add(child);
                            }
                        }
                        revGroup.setChild(children);
                        revenueGroups.add(revGroup);
                    }
                }

                dto.setRevenue(revenueGroups);
                result.add(dto);
            }

            return result;
        } catch (JSONException e) {
            log.error("Invalid JSON structure: " + e.getMessage(), e);
        }
        return null;
    }

    private List<ProductLedger> parseProductLedger(String json){
        if (json == null || json.isEmpty()) return null;

        List<ProductLedger> result = new ArrayList<>();

        try {
            log.info(json);
            JSONArray paymentsArray = new JSONArray(json);

            for (int i = 0; i < paymentsArray.length(); i++) {
                JSONObject obj = paymentsArray.getJSONObject(i);

                ProductLedger productLedger = new ProductLedger();
                productLedger.setName(obj.optString("name"));
                productLedger.setCostPrice(obj.getBigDecimal("costPrice"));

                // Parse start_payment
                if (!obj.isNull("start_payment")) {
                    JSONObject startPaymentObj = obj.getJSONObject("start_payment");
                    PaymentInfo startPayment = new PaymentInfo();
                    startPayment.setStockQty(startPaymentObj.optInt("stockQty"));
                    startPayment.setStockAmount(startPaymentObj.optInt("stockAmount"));
                 //   startPayment.setCostPrice(startPaymentObj.optInt("costPrice"));
                    productLedger.setStart_payment(startPayment);
                }

                // Parse end_payment
                if (!obj.isNull("end_payment")) {
                    JSONObject endPaymentObj = obj.getJSONObject("end_payment");
                    PaymentInfo endPayment = new PaymentInfo();
                    endPayment.setStockQty(endPaymentObj.optInt("stockQty"));
                    endPayment.setStockAmount(endPaymentObj.optInt("stockAmount"));
                  //  endPayment.setCostPrice(endPaymentObj.optInt("costPrice"));
                    productLedger.setEnd_payment(endPayment);
                }

                // Parse raw transaction list
                List<TransactionRecord> records = new ArrayList<>();
                JSONArray rawArray = obj.optJSONArray("raw");
                if (rawArray != null) {
                    for (int j = 0; j < rawArray.length(); j++) {
                        JSONObject transObj = rawArray.getJSONObject(j);
                        TransactionRecord record = new TransactionRecord();

                        record.setStockQty(transObj.optInt("stockQty"));
                        record.setStockAmount(transObj.optInt("stockAmount"));
                        record.setQtyTransaction(transObj.optInt("qtyTransaction"));
                        record.setTransactionDate(transObj.optString("transactionDate"));
                        record.setDescription(transObj.optString("description"));
                        record.setDocumentNo(transObj.optString("document"));
                        record.setTransactiontype(transObj.optString("transactionType"));

                        if (!transObj.isNull("priceTransaction")) {
                            record.setPriceTransaction(transObj.getInt("priceTransaction"));
                        }

                        records.add(record);
                    }
                }

                productLedger.setRaw(records);
                result.add(productLedger);
            }

        } catch (JSONException e) {
            log.error("Invalid JSON structure: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public GlobalReponse getReportCalculateTotalProduction(ReportReqDto rq) {
        ReportCalculateTotalProductionDto report = null;
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_calculate_total_production(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());

            if (rq.getOrgIds() == null || rq.getOrgIds().isEmpty()) {
                callableStatement.setNull(3, Types.ARRAY);
            } else {
                Object[] orgIds = rq.getOrgIds().toArray();
                Array sqlArray = connection.createArrayOf("numeric", orgIds);

                callableStatement.setArray(3, sqlArray);
            }

            callableStatement.setString(4, rq.getQueryType());
            callableStatement.setString(5, rq.getStartDate());
            callableStatement.setString(6, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs.next()) {
                String totalJsonStr = rs.getString("total_json_data");
                String jsonDataStr = rs.getString("json_data");

                List<ToTalExpense> total = parseTotalPaymentsManually(totalJsonStr);

                List<PaymentDto> payment = parsePayments(jsonDataStr);

                report = ReportCalculateTotalProductionDto.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .payment(payment)
                        .toTalExpense(total)
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getReportBusinessPerformance(ReportReqDto rq) {
        BusinessPerformance report = null;
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call d_rp_get_business_performance(?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());

            if (rq.getOrgIds() == null || rq.getOrgIds().isEmpty()) {
                callableStatement.setNull(3, Types.ARRAY);
            } else {
                Object[] orgIds = rq.getOrgIds().toArray();
                Array sqlArray = connection.createArrayOf("numeric", orgIds);

                callableStatement.setArray(3, sqlArray);
            }

            callableStatement.setString(4, rq.getQueryType());
            callableStatement.setString(5, rq.getStartDate());
            callableStatement.setString(6, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            if (rs.next()) {
                String invoice = rs.getString("invoice_json");
                String expense = rs.getString("expense_json");
                String summary = rs.getString("summary_json");

                BusinessAmount revenueDto = parseBusinessAmount(invoice);
                BusinessAmount expenseDto = parseBusinessAmount(expense);
                BusinessAmount summaryDto = parseBusinessAmount(summary);

                report = BusinessPerformance.builder()
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .address(rs.getString("address"))
                        .revenue(revenueDto)
                        .expense(expenseDto)
                        .summary(summaryDto)
                        .build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(report)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    private List<ToTalExpense> parseTotalPaymentsManually(String json) {
        List<ToTalExpense> list = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode paymentsNode = root.get("total_payments");

            if (paymentsNode != null && paymentsNode.isArray()) {
                for (JsonNode node : paymentsNode) {
                    ToTalExpense expense = new ToTalExpense();

                    if (node.hasNonNull("sum")) {
                        expense.setTotalAmount(node.get("sum").longValue());
                    }

                    if (node.has("name") && !node.get("name").isNull()) {
                        expense.setName(node.get("name").asText());
                    }

                    if (node.has("d_expense_type_id") && !node.get("d_expense_type_id").isNull()) {
                        expense.setExpenseTypeId(node.get("d_expense_type_id").intValue());
                    }

                    list.add(expense);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

//    private List<PaymentDto> parsePayments(String json) {
//        if(json == null || json.isEmpty()) return null;
//
//        List<PaymentDto> result = new ArrayList<>();
//
//        try {
//            log.info(json);
//            JSONObject root = new JSONObject(json);
//            JSONArray paymentsArray = root.getJSONArray("payments");
//
//            for (int i = 0; i < paymentsArray.length(); i++) {
//                JSONObject obj = paymentsArray.getJSONObject(i);
//                PaymentDto dto = new PaymentDto();
//
//                dto.setId(obj.optInt("d_payment_id", 0));
//                dto.setDocumentNo(obj.optString("document_no", null));
//                dto.setPaymentAmount(obj.has("payment_amount") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("payment_amount")) : null);
//                dto.setDescription(obj.optString("description", null));
//                dto.setRemainAmount(obj.has("remain_amount") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("remain_amount")) : null);
//                dto.setDocType(obj.optString("doc_base_type", null));
//                if (obj.has("payment_date") && !obj.isNull("payment_date")) {
//                    String isoDate = obj.getString("payment_date");
//                    try {
//                        LocalDateTime dt = LocalDateTime.parse(isoDate);
//                        String formatted = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//                        dto.setPaymentDate(formatted);
//                    } catch (DateTimeParseException e) {
//                        dto.setPaymentDate(isoDate); // fallback nếu lỗi
//                    }
//                }
//
//                // dp_people
//                if (!obj.isNull("partner_id")) {
//                    dto.setUserId(obj.getInt("partner_id"));
//                }
//                result.add(dto);
//            }
//        } catch (JSONException e) {
//            log.error("Invalid JSON structure: " + e.getMessage(), e);
//        }
//
//        return result;
//    }

//    private List<PaymentDto> parsePayments(String json) {
//        List<PaymentDto> list = new ArrayList<>();
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(json);
//            JsonNode paymentsNode = root.get("payments");
//
//            if (paymentsNode != null && paymentsNode.isArray()) {
//                for (JsonNode node : paymentsNode) {
//                    PaymentDto p = new PaymentDto();
//
//                    p.setExpenseTypeName(node.hasNonNull("name") ? node.get("name").asText() : null);
//                    p.setDescription(node.hasNonNull("description") ? node.get("description").asText() : null);
//                    p.setDocumentNo(node.hasNonNull("document_no") ? node.get("document_no").asText() : null);
//                    p.setId(node.hasNonNull("d_payment_id") ? node.get("d_payment_id").intValue() : null);
//
//                    if (node.hasNonNull("payment_date")) {
//                        p.setPaymentDate(node.get("payment_date").asText());
//                    }
//
//                    p.setPaymentAmount(node.hasNonNull("payment_amount") ? node.get("payment_amount").decimalValue() : null);
//                    p.setExpenseTypeId(node.hasNonNull("d_expense_type_id") ? node.get("d_expense_type_id").intValue() : null);
//
//                    list.add(p);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }

    private List<PaymentDto> parsePayments(String json) {
        if(json == null || json.isEmpty()) return null;

        List<PaymentDto> result = new ArrayList<>();

        try {
            log.info(json);
            JSONObject root = new JSONObject(json);
            JSONArray paymentsArray = root.getJSONArray("payments");

            for (int i = 0; i < paymentsArray.length(); i++) {
                JSONObject obj = paymentsArray.getJSONObject(i);
                PaymentDto dto = new PaymentDto();

                dto.setId(obj.optInt("d_payment_id", 0));
                dto.setDocumentNo(obj.optString("document_no", null));
                dto.setPaymentAmount(obj.has("payment_amount") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("payment_amount")) : null);
                dto.setDescription(obj.optString("description", null));
                dto.setRemainAmount(obj.has("remain_amount") ? ParseHelper.BIGDECIMAL.parse(obj.getBigDecimal("remain_amount")) : null);
                dto.setDocType(obj.optString("doc_base_type", null));
                if (obj.has("payment_date") && !obj.isNull("payment_date")) {
                    String isoDate = obj.getString("payment_date");
                    try {
                        LocalDateTime dt = LocalDateTime.parse(isoDate);
                        String formatted = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        dto.setPaymentDate(formatted);
                    } catch (DateTimeParseException e) {
                        dto.setPaymentDate(isoDate); // fallback nếu lỗi
                    }
                }

                // dp_people
                if (!obj.isNull("partner_id")) {
                    dto.setUserId(obj.getInt("partner_id"));
                }
                result.add(dto);
            }
        } catch (JSONException e) {
            log.error("Invalid JSON structure: " + e.getMessage(), e);
        }

        return result;
    }

    private BusinessAmount parseBusinessAmount(String json){
        JSONObject jsonObject = new JSONObject(json);

        BigDecimal sumCurrentAmount = jsonObject.optBigDecimal("sumCurrentAmount", BigDecimal.ZERO);
        BigDecimal sumPeriodAmount = jsonObject.optBigDecimal("sumPreiodAmount", BigDecimal.ZERO);
        BigDecimal sumSamPeriodYearAmount = jsonObject.optBigDecimal("sumSamPreiodYearAmount", BigDecimal.ZERO);

        JSONArray rawsArray = jsonObject.optJSONArray("raws");;
        List<DistributionSupply> raws = new ArrayList<>();

        if (rawsArray != null) {
            for (int i = 0; i < rawsArray.length(); i++) {
                JSONObject raw = rawsArray.getJSONObject(i);

                DistributionSupply item = DistributionSupply.builder()
                        .name(raw.isNull("name") ? null : raw.getString("name"))
                        .code(raw.isNull("code") ? null : raw.getString("code"))
                        .expenseTypeId(raw.isNull("d_expense_type_id") ? null : raw.getInt("d_expense_type_id"))
                        .currentAmount(new BigDecimal(raw.getLong("current_amount")))
                        .periodAmount(new BigDecimal(raw.getLong("previous_amount")))
                        .samePeriodLastYearAmount(new BigDecimal(raw.getLong("same_period_last_year_amount")))
                        .build();

                raws.add(item);
            }
        }

        return BusinessAmount.builder()
                .sumCurrentAmount(sumCurrentAmount)
                .sumPeriodAmount(sumPeriodAmount)
                .sumSamPeriodYearAmount(sumSamPeriodYearAmount)
                .raws(raws)
                .build();
    }

}
