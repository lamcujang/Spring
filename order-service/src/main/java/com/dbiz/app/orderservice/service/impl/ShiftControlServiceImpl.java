package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.SCPosOrderGetV;
import com.dbiz.app.orderservice.domain.ShiftControl;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.service.ShiftControlService;
import com.dbiz.app.orderservice.specification.ShiftControlSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.dtoView.GetListPosOrderVDto;
import org.common.dbiz.dto.orderDto.dtoView.ShiftControlGetPaymentVDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ShiftControlServiceImpl implements ShiftControlService {
    private final ShiftControlRepository entityRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final DoctypeRepository doctypeRepository;

    private final SCPosOrderGetVRepository scPosOrderGetVRepository;

    private final GetListPosOrderVRepository getListPosOrderVRepository;

    private final EntityManager entityManager;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final static String TOPIC = "sync-shift-control";

    private final DataSourceContextHolder dataSourceContextHolder;

    private final DataSourceConfigService dataSourceConfigService;

    private final ShiftControlGetPaymentVRepository shiftControlGetPaymentVRepository;

    @Override
    public GlobalReponsePagination findAll(ShiftControlQueryRequest request) {
        log.info("** service layer: PosClosedCashServiceImpl.findAll() **");
        Pageable pageable = requestParamsUtils.getPageRequest(request);
        pageable.getSortOr(Sort.by("sequenceNo").ascending());
        Specification<ShiftControl> spec = ShiftControlSpecification.getEntitySpecification(request);

        Page<ShiftControl> page = entityRepository.findAll(spec, pageable);
        List<ShiftControlDto> reponseData = page.map(entity -> {
            ShiftControlDto dto = modelMapper.map(entity, ShiftControlDto.class);
            dto.setStartDate(entity.getStartDate() != null ? DateHelper.fromInstantUTC(entity.getStartDate()) : null);
            dto.setEndDate(entity.getEndDate() != null ? DateHelper.fromInstantUTC(entity.getEndDate()) : null);

            //Total Amount Receipt Other
            String sql1 = "SELECT name FROM pos.d_reference_get_v WHERE d_tenant_id = :tenantId " +
                    " AND name_reference = 'Shift Type' and value = :shiftType";

            String refShiftTypeName = ((String) entityManager.createNativeQuery(sql1)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("shiftType", entity.getShiftType())
                    .getSingleResult()).toString();
            dto.setShiftTypeName(refShiftTypeName);

//            SCPosOrderGetV posOrderRP = scPosOrderGetVRepository.findByShiftControlId(entity.getId());
//            SCPosOrderGetV paymentRp = scPosOrderGetVRepository.findByShiftControlId(entity.getId());
            SCPosOrderGetV posOrderRP = null;
            SCPosOrderGetV paymentRp = null;
            SCPosOrderGetV purchaseRP = null;
            SCPosOrderGetV returnRP = null;
            if (posOrderRP == null)
                posOrderRP = new SCPosOrderGetV(entity.getId());
            if (paymentRp == null)
                paymentRp = new SCPosOrderGetV(entity.getId());
//                ReportPosOrderDto  reportPosOrderDto2 = getShiftInfoByType(entity.getId(), "SOR");

            if (purchaseRP == null)
                purchaseRP = new SCPosOrderGetV(entity.getId());
            if (returnRP == null)
                returnRP = new SCPosOrderGetV(entity.getId());


            ReportPosOrderDto  reportPosOrderDto1 = getShiftInfoByType(entity.getId(), "SOR");
            SCPosOrderGetVDto posOrderRPDto = modelMapper.map(posOrderRP, SCPosOrderGetVDto.class);

            posOrderRPDto.setCash(reportPosOrderDto1.getCashAmount());
            posOrderRPDto.setBank(reportPosOrderDto1.getBankAmount());
            posOrderRPDto.setDebt(reportPosOrderDto1.getDebtAmount());
            posOrderRPDto.setLoyalty(reportPosOrderDto1.getLoyaltyAmount());
            posOrderRPDto.setCoupon(reportPosOrderDto1.getCouponAmount());
            posOrderRPDto.setFree(reportPosOrderDto1.getFreeAmount());
            posOrderRPDto.setQrcode(reportPosOrderDto1.getQrCodeAmount());
            posOrderRPDto.setVoucher(reportPosOrderDto1.getVoucherAmount());
            posOrderRPDto.setVisa(reportPosOrderDto1.getVisaAmount());
            posOrderRPDto.setTotal(reportPosOrderDto1.getTotalAmount());
            posOrderRPDto.setTotalQty(reportPosOrderDto1.getTotalQty());


            ReportPosOrderDto  paymentRpDto1 = getShiftInfoByType(entity.getId(), "PAY");
            SCPosOrderGetVDto paymentRpDto = modelMapper.map(paymentRp, SCPosOrderGetVDto.class);

            paymentRpDto.setCash(paymentRpDto1.getCashAmount());
            paymentRpDto.setBank(paymentRpDto1.getBankAmount());
            paymentRpDto.setDebt(paymentRpDto1.getDebtAmount());
            paymentRpDto.setLoyalty(paymentRpDto1.getLoyaltyAmount());
            paymentRpDto.setCoupon(paymentRpDto1.getCouponAmount());
            paymentRpDto.setFree(paymentRpDto1.getFreeAmount());
            paymentRpDto.setQrcode(paymentRpDto1.getQrCodeAmount());
            paymentRpDto.setVoucher(paymentRpDto1.getVoucherAmount());
            paymentRpDto.setVisa(paymentRpDto1.getVisaAmount());
            paymentRpDto.setTotal(paymentRpDto1.getTotalAmount());
            paymentRpDto.setTotalQty(paymentRpDto1.getTotalQty());


//            SCPosOrderGetVDto posOrderRPDto = modelMapper.map(posOrderRP, SCPosOrderGetVDto.class);
//            SCPosOrderGetVDto paymentRpDto = modelMapper.map(paymentRp, SCPosOrderGetVDto.class);
            SCPosOrderGetVDto purchaseRPDto = modelMapper.map(purchaseRP, SCPosOrderGetVDto.class);
            SCPosOrderGetVDto returnRPDto = modelMapper.map(returnRP, SCPosOrderGetVDto.class);

//            posOrderRPDto.setTotal(posOrderRP.getCash().add(posOrderRP.getBank()).add(posOrderRP.getDebt()).add(posOrderRP.getLoyalty()));
//            paymentRpDto.setTotal(paymentRp.getCash().add(paymentRp.getBank()).add(paymentRp.getDebt()).add(paymentRp.getLoyalty()));
            purchaseRPDto.setTotal(purchaseRP.getCash().add(purchaseRP.getBank()).add(purchaseRP.getDebt()).add(purchaseRP.getLoyalty()));
            returnRPDto.setTotal(returnRP.getCash().add(returnRP.getBank()).add(returnRP.getDebt()).add(returnRP.getLoyalty()));

            dto.setOnCash
                    (
//                            posOrderRPDto.getTotal().subtract(purchaseRPDto.getTotal()).subtract(returnRPDto.getTotal())
                            posOrderRPDto.getCash()

                    );

            BigDecimal startCash = entity.getStartCash() == null ? BigDecimal.ZERO : entity.getStartCash();
            BigDecimal onCash = dto.getOnCash() == null ? BigDecimal.ZERO : dto.getOnCash();
            dto.setEndCash(startCash
                    .add(onCash));
            dto.setPosOrderRP(posOrderRPDto);
            dto.setPaymentOrderRP(paymentRpDto);
            dto.setPurchaseOrderRP(purchaseRPDto);
            dto.setReturnGoodsRP(returnRPDto);

            //get tax
            List<ShiftTaxDto> taxLine = new ArrayList<>();
            StringBuilder sql = new StringBuilder("select sum(dpt.tax_amount) as total_tax_amount,sum(dpt.tax_base_amount) as total_linenet_amt,dti.name as tax_name " +
                    " from pos.d_pos_taxline dpt  " +
                    " inner join d_tax dti on dpt.d_tax_id = dti.d_tax_id " +
                    " inner join d_pos_order dpo on dpt.d_pos_order_id = dpo.d_pos_order_id " +
                    "where dpo.d_shift_control_id =:shiftControlId and dpt.d_tenant_id = :tenantId and dpo.order_status = 'COM' " +
                    "group by dti.name ");
            List<Object[]> result = entityManager.createNativeQuery(sql.toString())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("shiftControlId", entity.getId())
                    .getResultList();
            for (Object[] row : result) {
                ShiftTaxDto taxDto = new ShiftTaxDto();
                taxDto.setTaxName(row[2] == null ? "" : row[2].toString());
                taxDto.setTaxAmount(row[0] == null ? BigDecimal.ZERO : new BigDecimal(row[0].toString()));
                taxDto.setTotalLineAmount(row[1] == null ? BigDecimal.ZERO : new BigDecimal(row[1].toString()));
                taxLine.add(taxDto);
            }

            sql = new StringBuilder("select count(dpo.d_pos_order_id ) as qty, dpo.order_status as order_status \n" +
                    "from pos.d_pos_order dpo\n" +
                    "         join pos.d_reference_list drl on dpo.order_status = drl.value\n" +
                    "         join pos.d_reference dr on drl.d_reference_id = dr.d_reference_id\n" +
                    "and dr.name = 'Document Status' where dpo.d_shift_control_id = :shiftControlId and dpo.d_tenant_id = :tenantId group by order_status;");

            List<Object[]> resul3 = entityManager.createNativeQuery(sql.toString())
                    .setParameter("shiftControlId", entity.getId())
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            for (Object[] row : resul3) {
                String orderStatus = ParseHelper.STRING.parse(row[1]);
                Integer qty = ((BigInteger) row[0]).intValue();
                if (orderStatus.equals("COM")) {
                    dto.setQtyTransSale(qty);
                } else if (orderStatus.equals("VOD")) {
                    dto.setQtyTransReturn(qty);
                }
            }
            dto.setShiftTaxDtoList(taxLine);
            dto.setQtyTrans((dto.getQtyTransSale() == null ? 0 : dto.getQtyTransSale()) + (dto.getQtyTransReturn() == null ? 0 : dto.getQtyTransReturn()));
            return dto;

        }).getContent();

        return GlobalReponsePagination.builder()
                .errors("")
                .data(reponseData)
                .pageSize(reponseData.size())
                .currentPage(page.getNumber())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("pos_shift_close_fetched", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        ShiftControl entity = entityRepository.findById(integer).orElseThrow(() -> {
            throw new PosException("ShiftControl not found");
        });
        ShiftControlDto dto = modelMapper.map(entity, ShiftControlDto.class);
        dto.setStartDate(entity.getStartDate() != null ? DateHelper.fromInstantUTC(entity.getStartDate()) : null);
        dto.setEndDate(entity.getEndDate() != null ? DateHelper.fromInstantUTC(entity.getEndDate()) : null);

        SCPosOrderGetV posOrderRP = scPosOrderGetVRepository.findByShiftControlId(entity.getId());
        SCPosOrderGetV paymentRp = scPosOrderGetVRepository.findByShiftControlId(entity.getId());
        SCPosOrderGetV purchaseRP = null;
        SCPosOrderGetV returnRP = null;

        if (posOrderRP == null)
            posOrderRP = new SCPosOrderGetV(entity.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, BigDecimal.ZERO, 0);
        if (paymentRp == null)
            paymentRp = new SCPosOrderGetV(entity.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, BigDecimal.ZERO, 0);
        if (purchaseRP == null)
            purchaseRP = new SCPosOrderGetV(entity.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, BigDecimal.ZERO, 0);
        if (returnRP == null)
            returnRP = new SCPosOrderGetV(entity.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, BigDecimal.ZERO, 0);

        SCPosOrderGetVDto posOrderRPDto = modelMapper.map(posOrderRP, SCPosOrderGetVDto.class);
        SCPosOrderGetVDto paymentRpDto = modelMapper.map(paymentRp, SCPosOrderGetVDto.class);
        SCPosOrderGetVDto purchaseRPDto = modelMapper.map(purchaseRP, SCPosOrderGetVDto.class);
        SCPosOrderGetVDto returnRPDto = modelMapper.map(returnRP, SCPosOrderGetVDto.class);

        posOrderRPDto.setTotal(posOrderRP.getCash().add(posOrderRP.getBank()).add(posOrderRP.getDebt()).add(posOrderRP.getLoyalty()));
        paymentRpDto.setTotal(paymentRp.getCash().add(paymentRp.getBank()).add(paymentRp.getDebt()).add(paymentRp.getLoyalty()));

        purchaseRPDto.setTotal(purchaseRP.getCash().add(purchaseRP.getBank()).add(purchaseRP.getDebt()).add(purchaseRP.getLoyalty()));
        returnRPDto.setTotal(returnRP.getCash().add(returnRP.getBank()).add(returnRP.getDebt()).add(returnRP.getLoyalty()));

        dto.setPosOrderRP(posOrderRPDto);
        dto.setPaymentOrderRP(paymentRpDto);
        dto.setPurchaseOrderRP(purchaseRPDto);
        dto.setReturnGoodsRP(returnRPDto);

        return GlobalReponse.builder()
                .data(dto)
                .message(messageSource.getMessage("pos_shift_close_fetched", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public GlobalReponse save(ShiftControlDto entityDto) {
        log.info("** service layer: PosClosedCashServiceImpl.save() **");
        GlobalReponse response = new GlobalReponse();

        if (entityDto.getId() != null) {// update
            ShiftControl entitySave = entityRepository.findById(entityDto.getId()).get();
            modelMapper.map(entityDto, entitySave);

            if(entityDto.getStartDate() != null) {
                entitySave.setStartDate(DateHelper.toInstantUTC(entityDto.getStartDate()));
            }

            if(entityDto.getEndDate() != null) {
                entitySave.setEndDate(DateHelper.toInstantUTC(entityDto.getEndDate()));
            }
            entityRepository.save(entitySave);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(messageSource.getMessage("pos_shift_close_update", null, LocaleContextHolder.getLocale()));
            response.setData(modelMapper.map(entitySave, ShiftControlDto.class));

            // send message to kafka
            try {
                boolean checkIntegration =  entityManager.createNativeQuery("select * from d_erp_integration where d_tenant_id = :tenantId and is_active = 'Y' and is_default = 'Y'", Tuple.class)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .getResultList().size() > 0;
                if(entitySave.getIsClosed().equals("Y") && checkIntegration) {
                    try {
                        ShiftIntDto synShift = ShiftIntDto.builder()
                                .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                                .orgId(entitySave.getOrgId())
                                .shiftId(entitySave.getId())
                                .userId(AuditContext.getAuditInfo().getUserId())
                                .build();
                        kafkaTemplate.send(TOPIC, synShift);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        } else {
            //insert
            ShiftControl entitySave = modelMapper.map(entityDto, ShiftControl.class);
            entitySave.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.POS_CLOSED_CASH).getId());
            Integer maxId = entityRepository.getMaxId();
            maxId = maxId == 1000000 ? 1000000 : maxId + 1;
            entitySave.setDocumentNo("CA" + maxId);
            entitySave.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.POS_CLOSED_CASH).getId());
            entitySave.setSequenceNo(entityRepository.getMaxSequenceNo() + 1);
            entitySave.setStartDate(entityDto.getStartDate() != null ? DateHelper.toInstantUTC(entityDto.getStartDate()) : null);
            entitySave.setEndDate(entityDto.getEndDate() != null ? DateHelper.toInstantUTC(entityDto.getEndDate()) : null);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entityRepository.save(entitySave);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(messageSource.getMessage("pos_shift_close_create", null, LocaleContextHolder.getLocale()));
            response.setData(modelMapper.map(entitySave, ShiftControlDto.class));
        }
        response.setErrors("");

        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override

    public GlobalReponse findAllOrderByShiftId(Integer shiftId) {

        List<GetListPosOrderVDto> listPosOrderVS = getListPosOrderVRepository.findAllByShiftControlId(shiftId).

                stream().map((element) -> {

                    GetListPosOrderVDto itemDto = modelMapper.map(element, GetListPosOrderVDto.class);

                    itemDto.setOrderDate(element.getOrderDate() != null ? DateHelper.fromLocalDate(element.getOrderDate()) : null);

                    return itemDto;

                }).collect(Collectors.toList());

        return GlobalReponse.builder()

                .errors("")

                .data(listPosOrderVS)

                .status(HttpStatus.OK.value())

                .errors("")

                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))

                .build();

    }


    @Override

    public GlobalReponse findAllPaymentByShiftId(Integer shiftId) {

        List<ShiftControlGetPaymentVDto> listPayment = shiftControlGetPaymentVRepository.findAllByShiftControlId(shiftId)

                .stream().map((element) -> {

                    ShiftControlGetPaymentVDto itemDto = modelMapper.map(element, ShiftControlGetPaymentVDto.class);

                    itemDto.setOrderDate(element.getOrderDate() != null ? DateHelper.fromLocalDate(element.getOrderDate()) : null);

                    return itemDto;

                }).collect(Collectors.toList());


        return GlobalReponse.builder()
                .errors("")
                .data(listPayment)
                .status(HttpStatus.OK.value())
                .errors("")
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    /**
     * @param shiftId
     * @return
     */
    @Override
    public GlobalReponse findAllPurchaseByShiftId(Integer shiftId) {

        return null;
    }

    @Override
    public GlobalReponse findAllReturnByShiftId(Integer shiftId) {
        return null;
    }


    /**
     * @param shiftId
     * @return
     */
    @Override
    public GlobalReponse getReportClosingShift(Integer shiftId) {
        return null;
    }

    @Override
    public GlobalReponse getShiftInfoById(Integer shiftId) {
        log.info("ShiftControlServiceImpl.getShiftInfoById()");

        ShiftControl entity = entityRepository.findById(shiftId).orElseThrow(() -> {
            throw new PosException("ShiftControl not found");
        });

        ListReportPosOrderDto listReportPosOrderDto = ListReportPosOrderDto
                .builder()
                .sale(getShiftInfoByType(shiftId, "SOR"))
                .payment(getShiftInfoByType(shiftId, "PAY"))
                .refund(getShiftInfoByType(shiftId, "RFN"))
                .purchase(getShiftInfoByType(shiftId, "POR"))
                .build();

        listReportPosOrderDto.setStartCash(entity.getStartCash());
        if(listReportPosOrderDto.getSale() != null) {
            listReportPosOrderDto.setSaleCash(listReportPosOrderDto.getSale().getCashAmount());
        }

        BigDecimal startAmt = listReportPosOrderDto.getStartCash() == null ? BigDecimal.ZERO : listReportPosOrderDto.getStartCash();
        BigDecimal saleAmt = listReportPosOrderDto.getSaleCash() == null ? BigDecimal.ZERO : listReportPosOrderDto.getSaleCash();

        BigDecimal endAmt = startAmt.add(saleAmt);
        if (endAmt.scale() > 2) {
            endAmt = endAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        listReportPosOrderDto.setEndCash(endAmt);

        //get tax
        List<ShiftTaxDto> taxLine = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select sum(dpt.tax_amount) as total_tax_amount,sum(dpt.tax_base_amount) as total_linenet_amt,dti.name as tax_name " +
                " from pos.d_pos_taxline dpt  " +
                " inner join d_tax dti on dpt.d_tax_id = dti.d_tax_id " +
                " inner join d_pos_order dpo on dpt.d_pos_order_id = dpo.d_pos_order_id " +
                "where dpo.d_shift_control_id =:shiftControlId and dpt.d_tenant_id = :tenantId " +
                "group by dti.name ");
        List<Object[]> result = entityManager.createNativeQuery(sql.toString())
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("shiftControlId", shiftId)
                .getResultList();
        for (Object[] row : result) {
            ShiftTaxDto taxDto = new ShiftTaxDto();
            taxDto.setTaxName(row[2] == null ? "" : row[2].toString());
            taxDto.setTaxAmount(row[0] == null ? BigDecimal.ZERO : new BigDecimal(row[0].toString()));
            taxDto.setTotalLineAmount(row[1] == null ? BigDecimal.ZERO : new BigDecimal(row[1].toString()));
            taxLine.add(taxDto);
        }

        sql = new StringBuilder("select count(dpo.d_pos_order_id ) as qty, dpo.order_status as order_status \n" +
                "from pos.d_pos_order dpo\n" +
                "         join pos.d_reference_list drl on dpo.order_status = drl.value\n" +
                "         join pos.d_reference dr on drl.d_reference_id = dr.d_reference_id\n" +
                "and dr.name = 'Document Status' where dpo.d_shift_control_id = :shiftControlId and dpo.d_tenant_id = :tenantId group by order_status;");

        List<Object[]> resul3 = entityManager.createNativeQuery(sql.toString())
                .setParameter("shiftControlId", shiftId)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getResultList();
        for (Object[] row : resul3) {
            String orderStatus = ParseHelper.STRING.parse(row[1]);
            Integer qty = ((BigInteger) row[0]).intValue();
            if (orderStatus.equals("COM")) {
                listReportPosOrderDto.setQtyTransSale(qty);
            } else if (orderStatus.equals("VOD")) {
                listReportPosOrderDto.setQtyTransVoid(qty);
            }
        }

        listReportPosOrderDto.setShiftTaxDtoList(taxLine);
        listReportPosOrderDto.setQtyTrans((listReportPosOrderDto.getQtyTransSale() == null ? 0 : listReportPosOrderDto.getQtyTransSale()) + (listReportPosOrderDto.getQtyTransVoid() == null
                ? 0 : listReportPosOrderDto.getQtyTransVoid()));

        listReportPosOrderDto.setStartDate(entity.getStartDate() != null ? DateHelper.fromInstantDateAndTime(entity.getStartDate()) : null);
        listReportPosOrderDto.setEndDate(entity.getEndDate() != null ? DateHelper.fromInstantDateAndTime(entity.getEndDate()) : null);

        return GlobalReponse.builder()
                .errors("")
                .data(listReportPosOrderDto)
                .status(HttpStatus.OK.value())
                .errors("")
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Override
    public GlobalReponse getNewestShiftInfo(Integer orgId, Integer posTerminalId) {
        GlobalReponse response = new GlobalReponse();
        Integer shiftId = entityRepository.getMaxSequenceNoByOrgAndPosTerminal(orgId, posTerminalId);
        Optional<ShiftControl> entity = entityRepository.findByTenantIdAndOrgIdAndPosTerminalIdAndSequenceNo(AuditContext.getAuditInfo().getTenantId(),
                orgId, posTerminalId, shiftId);
        if (entity.isPresent()) {

            if (entity.get().getStartDate() != null && entity.get().getEndDate() != null) {

                response.setData(null);
            } else {

                response.setData(modelMapper.map(entity.get(), ShiftControlDto.class));
            }
        } else {

            response.setData(null);
        }

        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    public ReportPosOrderDto getShiftInfoByType(Integer shiftId,String type){

        try{
            StringBuilder sql = null;
            Integer qty = 0;
            if(type.equals("SOR")){
                sql = new StringBuilder();

                sql.append(String.join(" ",
                        "SELECT d_shift_control_id,",
                        "COALESCE(ROUND(SUM(total_cash), 2), 0) AS total_cash,",
                        "COALESCE(ROUND(SUM(total_visa), 2), 0) AS total_visa,",
                        "COALESCE(ROUND(SUM(total_deb), 2), 0) AS total_deb,",
                        "COALESCE(ROUND(SUM(total_loyalty), 2), 0) AS total_loyalty,",
                        "COALESCE(ROUND(SUM(total_bank), 2), 0) AS total_bank,",
                        "COALESCE(ROUND(SUM(total_coupon), 2), 0) AS total_coupon,",
                        "COALESCE(ROUND(SUM(total_free), 2), 0) AS total_free,",
                        "COALESCE(ROUND(SUM(total_qrcode), 2), 0) AS total_qrcode,",
                        "COALESCE(ROUND(SUM(total_voucher), 2), 0) AS total_voucher",
                        "FROM ("
                )).append(" ");

                sql.append(String.join(" ",
                        "SELECT d_shift_control_id,",
                        "cash AS total_cash, visa AS total_visa, deb AS total_deb,",
                        "loyalty AS total_loyalty, bank AS total_bank, coupon AS total_coupon,",
                        "free AS total_free, qrcode AS total_qrcode, voucher AS total_voucher",
                        "FROM pos.d_pos_order_report2_v",
                        "WHERE d_tenant_id = :tenantId AND d_shift_control_id = :shiftId"
                )).append(" ");

                sql.append("UNION ALL ");

           sql.append(String.join(" ",
                        "SELECT",
                        ":shiftId AS d_shift_control_id,",
                        "dporv.cash AS total_cash,",
                        "dporv.visa AS total_visa,",
                        "dporv.deb AS total_deb,",
                        "dporv.loyalty AS total_loyalty,",
                        "dporv.bank AS total_bank,",
                        "dporv.coupon AS total_coupon,",
                        "dporv.free AS total_free,",
                        "dporv.qrcode AS total_qrcode,",
                        "dporv.voucher AS total_voucher",
                        "FROM pos.d_pos_order_report2_v dporv",
                        "INNER JOIN d_shift_control dsc ON dporv.d_shift_control_id = dsc.d_shift_control_id",
                        "INNER JOIN d_pos_terminal dpm ON dsc.d_pos_terminal_id = dpm.d_pos_terminal_id",
                        "WHERE dporv.d_tenant_id = :tenantId",
                        "AND dporv.d_shift_control_id <> :shiftId",
                        "AND dsc.d_pos_terminal_id = (",
                        "SELECT d_pos_terminal_id",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        ")",
                        "AND dporv.date_invoiced BETWEEN (",
                        "SELECT start_date",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        ") AND COALESCE((",
                        "SELECT end_date",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        "), NOW())"
                )).append(" ");

                sql.append(") AS combined GROUP BY d_shift_control_id");

                //Total Amount Receipt Other
                String sqlTaxSum =
                        "SELECT COUNT(dporv.d_shift_control_id) AS qty " +
                                "FROM pos.d_pos_order_report2_v dporv " +
                                "INNER JOIN d_shift_control dsc ON dporv.d_shift_control_id = dsc.d_shift_control_id " +
                                "INNER JOIN d_pos_terminal dpm ON dsc.d_pos_terminal_id = dpm.d_pos_terminal_id " +
                                "WHERE dporv.d_tenant_id = :tenantId " +
                                "AND ( " +
                                "      dporv.d_shift_control_id = :shiftId " +
                                "   OR ( " +
                                "        dporv.d_shift_control_id <> :shiftId " +
                                "        AND date_invoiced BETWEEN ( " +
                                "            SELECT start_date " +
                                "            FROM pos.d_shift_control " +
                                "            WHERE d_shift_control_id = :shiftId " +
                                "            LIMIT 1) " +
                                "        AND COALESCE(( " +
                                "            SELECT end_date " +
                                "            FROM pos.d_shift_control " +
                                "            WHERE d_shift_control_id = :shiftId " +
                                "            LIMIT 1), NOW()) " +
                                "      ) " +
                                "   AND dsc.d_pos_terminal_id = ( " +
                                "        SELECT d_pos_terminal_id " +
                                "        FROM pos.d_shift_control " +
                                "        WHERE d_shift_control_id = :shiftId " +
                                "        LIMIT 1) " +
                                ")";

                qty = ((Number) entityManager.createNativeQuery(sqlTaxSum)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("shiftId", shiftId)
                        .getSingleResult()).intValue();
            }else if(type.equals("PAY")){
                sql = new StringBuilder();

                sql.append(String.join(" ",
                        "SELECT d_shift_control_id,",
                        "COALESCE(ROUND(SUM(total_cash), 2), 0) AS total_cash,",
                        "COALESCE(ROUND(SUM(total_visa), 2), 0) AS total_visa,",
                        "COALESCE(ROUND(SUM(total_deb), 2), 0) AS total_deb,",
                        "COALESCE(ROUND(SUM(total_loyalty), 2), 0) AS total_loyalty,",
                        "COALESCE(ROUND(SUM(total_bank), 2), 0) AS total_bank,",
                        "COALESCE(ROUND(SUM(total_coupon), 2), 0) AS total_coupon,",
                        "COALESCE(ROUND(SUM(total_free), 2), 0) AS total_free,",
                        "COALESCE(ROUND(SUM(total_qrcode), 2), 0) AS total_qrcode,",
                        "COALESCE(ROUND(SUM(total_voucher), 2), 0) AS total_voucher",
                        "FROM ("
                )).append(" ");

                sql.append(String.join(" ",
                        "SELECT d_shift_control_id,",
                        "cash AS total_cash, visa AS total_visa, deb AS total_deb,",
                        "loyalty AS total_loyalty, bank AS total_bank, coupon AS total_coupon,",
                        "free AS total_free, qrcode AS total_qrcode, voucher AS total_voucher",
                        "FROM pos.d_payment_shift_v",
                        "WHERE d_tenant_id = :tenantId AND d_shift_control_id = :shiftId"
                )).append(" ");

                sql.append("UNION ALL ");

                sql.append(String.join(" ",
                        "SELECT",
                        ":shiftId AS d_shift_control_id,",
                        "dpsv.cash AS total_cash,",
                        "dpsv.visa AS total_visa,",
                        "dpsv.deb AS total_deb,",
                        "dpsv.loyalty AS total_loyalty,",
                        "dpsv.bank AS total_bank,",
                        "dpsv.coupon AS total_coupon,",
                        "dpsv.free AS total_free,",
                        "dpsv.qrcode AS total_qrcode,",
                        "dpsv.voucher AS total_voucher",
                        "FROM pos.d_payment_shift_v dpsv",
                        "INNER JOIN d_shift_control dsc ON dpsv.d_shift_control_id = dsc.d_shift_control_id",
                        "INNER JOIN d_pos_terminal dpm ON dsc.d_pos_terminal_id = dpm.d_pos_terminal_id",
                        "WHERE dpsv.d_tenant_id = :tenantId",
                        "AND dpsv.d_shift_control_id <> :shiftId",
                        "AND dsc.d_pos_terminal_id = (",
                        "SELECT d_pos_terminal_id",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        ")",
                        "AND dpsv.date_invoiced BETWEEN (",
                        "SELECT start_date",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        ") AND COALESCE((",
                        "SELECT end_date",
                        "FROM pos.d_shift_control",
                        "WHERE d_shift_control_id = :shiftId",
                        "LIMIT 1",
                        "), NOW())"
                )).append(" ");

                sql.append(") AS combined GROUP BY d_shift_control_id");

                //Total Amount Receipt Other
                String sqlTaxSum =
                        "SELECT COUNT(dporv.d_shift_control_id) AS qty " +
                                "FROM pos.d_payment_shift_v dporv " +
                                "INNER JOIN d_shift_control dsc ON dporv.d_shift_control_id = dsc.d_shift_control_id " +
                                "INNER JOIN d_pos_terminal dpm ON dsc.d_pos_terminal_id = dpm.d_pos_terminal_id " +
                                "WHERE dporv.d_tenant_id = :tenantId " +
                                "AND ( " +
                                "      dporv.d_shift_control_id = :shiftId " +
                                "   OR ( " +
                                "        dporv.d_shift_control_id <> :shiftId " +
                                "        AND dporv.date_invoiced BETWEEN ( " +
                                "            SELECT start_date " +
                                "            FROM pos.d_shift_control " +
                                "            WHERE d_shift_control_id = :shiftId " +
                                "            LIMIT 1) " +
                                "        AND COALESCE(( " +
                                "            SELECT end_date " +
                                "            FROM pos.d_shift_control " +
                                "            WHERE d_shift_control_id = :shiftId " +
                                "            LIMIT 1), NOW()) " +
                                "        AND dsc.d_pos_terminal_id = ( " +
                                "            SELECT d_pos_terminal_id " +
                                "            FROM pos.d_shift_control " +
                                "            WHERE d_shift_control_id = :shiftId " +
                                "            LIMIT 1) " +
                                "      ) " +
                                ")";

                qty = ((Number) entityManager.createNativeQuery(sqlTaxSum)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("shiftId", shiftId)
                        .getSingleResult()).intValue();
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            ReportPosOrderDto dto = new ReportPosOrderDto();
            if(sql != null){
                // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
                List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("shiftId", shiftId)
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();


                for (Map<String, Object> item : results) {
                    log.info("Row: {}", item);
                    dto.setShiftControlId(ParseHelper.INT.parse(item.get("d_shift_control_id")));
                    dto.setCashAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_cash")));
                    dto.setBankAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_bank")));
                    dto.setCouponAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_coupon")));
                    dto.setVoucherAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_voucher")));
                    dto.setDebtAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_deb")));
                    dto.setFreeAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_free")));
                    dto.setQrCodeAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_qrcode")));
                    dto.setLoyaltyAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_loyalty")));
                    dto.setVisaAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_visa")));

                    totalAmount = totalAmount.add(dto.getCashAmount());
                    totalAmount = totalAmount.add(dto.getBankAmount());
                    totalAmount = totalAmount.add(dto.getCouponAmount());
                    totalAmount = totalAmount.add(dto.getVoucherAmount());
                    totalAmount = totalAmount.add(dto.getDebtAmount());
                    totalAmount = totalAmount.add(dto.getFreeAmount());
                    totalAmount = totalAmount.add(dto.getQrCodeAmount());
                    totalAmount = totalAmount.add(dto.getLoyaltyAmount());
                    totalAmount = totalAmount.add(dto.getVisaAmount());
                    dto.setTotalAmount(totalAmount);
                    dto.setTotalQty(qty);

                }
            }else{
                dto.setCashAmount(BigDecimal.ZERO);
                dto.setBankAmount(BigDecimal.ZERO);
                dto.setCouponAmount(BigDecimal.ZERO);
                dto.setVoucherAmount(BigDecimal.ZERO);
                dto.setDebtAmount(BigDecimal.ZERO);
                dto.setFreeAmount(BigDecimal.ZERO);
                dto.setQrCodeAmount(BigDecimal.ZERO);
                dto.setLoyaltyAmount(BigDecimal.ZERO);
                dto.setVisaAmount(BigDecimal.ZERO);
                dto.setTotalAmount(totalAmount);
                dto.setTotalQty(qty);
            }
            return dto;
        }catch (Exception e){
            e.printStackTrace();
            log.error("Error: {}", e.getMessage());
        }
        return null;

    }

}
