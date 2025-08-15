package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.define.Reference;
import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.domain.view.PosOrderLineVAll;
import com.dbiz.app.orderservice.specification.FloorSpecification;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.orderDto.request.PosOrderReqDto;
import org.common.dbiz.dto.orderDto.response.PosOrderLineResDto;
import org.common.dbiz.dto.paymentDto.PaymentDetailDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.*;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.NapasErrorException;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.helper.Helper;
import com.dbiz.app.orderservice.helper.PosOrderMapper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.specification.PosOrderListSpecification;
import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.view.PosOrderListView;
import com.dbiz.app.orderservice.service.PosOrderService;
import com.dbiz.app.orderservice.specification.PosOrderSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.dtoView.PosOrderListViewDto;
import org.common.dbiz.dto.orderDto.dtoView.PosOrderLineCompleteVAllDto;
import org.common.dbiz.dto.orderDto.response.POSBillNumberRespDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.common.dbiz.request.orderRequest.PosOrderQueryRequest;
import org.common.dbiz.request.orderRequest.ReportPosQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.common.dbiz.dto.userDto.CustomerDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PosOrderSerivceImpl implements PosOrderService {
    private final PosOrderRepository entityRepository;

    private final PosOrderlineRepository posOrderlineRepository;

    private final ModelMapper modelMapper;

    private final RequestParamsUtils requestParamsUtils;

    private final EntityManager entityManager;

    private final RestTemplate restTemplate;

    private final TableRepository tableRepository;

    private final PosOrderMapper posOrderMapper;

    private final PosOrderLineVAllRepository posOrderLineVRepository;

    private final PosOrderListViewRepository completeViewRepository;

    private final MessageSource messageSource;

    private final DoctypeRepository doctypeRepository;

    private final PosOrderLineDetailRepository posOrderLineDetailRepository;

    private final Helper helper;

    private final CurrencyRepository currencyRepository;
    private StringBuilder sql;

    private final PosLotRepository posLotRepository;

    private final PosTaxLineRepository posTaxLineRepository;

    private final ShiftControlRepository shiftControlRepository;

    private final QueryEngine queryEngine;

    private final ObjectMapper objectMapper;

    private final KafkaAuditUserHelper kafkaAuditUserHelper;

    private final String GROUP_ID = "order-service";
    private final String ORDER_MULTI_SOURCE_TOPIC = "ORDER_MULTI_SOURCE_TOPIC";
    private final String SEND_BULK_ORDER_TOPIC = "SEND_BULK_ORDER_TOPIC";
    private final DataSourceContextHolder dataSourceContextHolder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;
//
//    select *
//    from d_pc_e_request_v;
//
//
//    select * from d_product_e_request_v;

    @Override
    @SuppressWarnings("unchecked")
    public GlobalReponsePagination findAll(PosOrderQueryRequest query) {
        log.info("*** PosOrder List, service; fetch all PosOrder *");

        Pageable pageable = requestParamsUtils.getPageRequest(query);


        Specification<PosOrder> spec = PosOrderSpecification.getEntitySpecification(query);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PosOrder> entityList = entityRepository.findAll(spec, pageable);
        List<PosOrderDto> listData = new ArrayList<>();
        for (PosOrder item : entityList.getContent()) {

            //get pos order line
            List<PosOrderLineVAllDto> listPosOrderLines;
            List<PosOrderLineVAll> lineQuery = posOrderLineVRepository.findByPosOrder_IdAndIsActiveAndParentId(item.getId(), "Y", null);
            if (!lineQuery.isEmpty()) {
                listPosOrderLines = lineQuery.stream()
                        .map(item2 -> {
                            PosOrderLineVAllDto itemDto = modelMapper.map(item2, PosOrderLineVAllDto.class);
                            itemDto.setPosOrderId(item2.getPosOrder().getId());
                            itemDto.setPosOrderLineId(itemDto.getId());
                            List<PosOrderLineDetailDto> lisDetail = new ArrayList<>();
                            List<PosOrderLineVAll> lineDetailQuery
                                    = posOrderLineVRepository.findByPosOrder_IdAndIsActiveAndParentId(item.getId(), "Y", item2.getId());
                            BigDecimal extraAmount = BigDecimal.ZERO;
                            itemDto.setExtraAmount(extraAmount);
                            if(!lineDetailQuery.isEmpty()){
                                lisDetail = lineDetailQuery.stream()
                                        .map(item3 -> {
                                            PosOrderLineDetailDto itemDetail = PosOrderLineDetailDto.builder()
                                                    .id(item3.getId())
                                                    .posOrderLineId(item2.getId())
                                                    .productName(item3.getProductDto().getName())
                                                    .qty(item3.getQty())
                                                    .productId(item3.getProductId())
                                                    .taxId(item3.getTaxId())
                                                    .salesPrice(item3.getSalesPrice())
                                                    .description(item3.getDescription())
                                                    .isActive(item3.getIsActive())
                                                    .status(item3.getStatus())
                                                    .valueStatus(item3.getValueStatus())
                                                    .build();
                                            itemDto.setExtraAmount(itemDto.getExtraAmount().add(item3.getSalesPrice().multiply(item3.getQty())));
                                            return itemDetail;
                                        })
                                        .collect(Collectors.toList());
                            }
                            itemDto.setLineDetails(lisDetail);

                            return itemDto;
                        })
                        .collect(Collectors.toList());
            } else {
                listPosOrderLines = new ArrayList<>();
            }
            PosOrderDto posOrderDto = posOrderMapper.toPosOrderDto(item);
            posOrderDto.setPosOrderLines(listPosOrderLines);
            posOrderDto.setOrderDate(DateHelper.fromInstantUTC(item.getOrderDate()));
            listData.add(posOrderDto);
        }
        response.setMessage(messageSource.getMessage("posorder_fetched", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());


        return response;
    }

    @Transactional
    @Override
    public GlobalReponse save(PosOrderDto paramDto) {


        HttpHeaders headersM = new HttpHeaders();
        headersM.setContentType(MediaType.APPLICATION_JSON);
        headersM.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersM);

        log.info("save posOrderDto ");
        log.info("*** PosOrder, service; save PosOrder *** {}", paramDto.toString());

        log.info("*** PosOrder, service; save PosOrder ***");
        GlobalReponse response = new GlobalReponse();

        // Get Customer phone, name from db
        if (paramDto.getCustomerId() != null) {
            paramDto.setPhone(getCustomerPhone(paramDto.getCustomerId()));
            paramDto.setCustomerName(getCustomerName(paramDto.getCustomerId()));
        }
        PosOrder entitySave = posOrderMapper.toPosOrder(paramDto);

        if (entitySave.getId() != null) // Update
        {
            GlobalReponse exRes = null;
            entitySave = this.entityRepository.findById(paramDto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

            if ("COM".equals(entitySave.getOrderStatus())) {

                throw new PosException(messageSource.getMessage("pos.order.has.been.completed", null, LocaleContextHolder.getLocale()));
            } else if ("VOD".equals(entitySave.getOrderStatus())) {

                throw new PosException(messageSource.getMessage("pos.order.has.been.voided", null, LocaleContextHolder.getLocale()));
            } else if ("MOV".equals(entitySave.getOrderStatus())) {

                throw new PosException(messageSource.getMessage("pos.order.has.been.moved", null, LocaleContextHolder.getLocale()));
            }

            paramDto.setShiftControlId(entitySave.getShiftControlId());

            entitySave = posOrderMapper.updatePosOrder(paramDto, entitySave);
//            if (paramDto.getOrderDate() != null)
//                entitySave.setOrderDate(DateHelper.toInstantDateAndTime(paramDto.getOrderDate()));

            //Xu ly image
            if (paramDto.getImageTransaction() != null) {
                ImageDto imageTransaction = null;
                if (entitySave.getImageId() != null) {
                    exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_GET_IMAGE_BY_ID + "/" + entitySave.getImageId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                    imageTransaction = modelMapper.map(exRes.getData(), ImageDto.class);

                }
                entitySave.setImageId(handleImage(paramDto.getImageTransaction().getImage64(),
                        imageTransaction != null ? imageTransaction.getImageCode() : paramDto.getImageTransaction().getImageCode()));
            }

            processPosTaxLine(paramDto);
            processPosReceiptOther(paramDto);


            this.entityRepository.saveAndFlush(entitySave);
            PosOrder finalEntitySave = entitySave;
            // chuyen ve dto
            PosOrderDto resultDto = modelMapper.map(finalEntitySave, PosOrderDto.class);
            if (paramDto.getPosOrderLines() != null && !paramDto.getPosOrderLines().isEmpty()) {
                // luu order line
                List<PosOrderline> posOrderLines = processOrderLines(paramDto, finalEntitySave);

                List<PosOrderLineVAllDto> posOrderlineDtos = posOrderLines.stream()
                        .map(line -> {
                            PosOrderLineVAllDto lineDto = modelMapper.map(line, PosOrderLineVAllDto.class);
                            PosOrderLineVAll productV = posOrderLineVRepository.findById(line.getId()).get();
                            lineDto.setStatus(productV.getStatus());
                            lineDto.setValueStatus(productV.getValueStatus());
                            lineDto.setPosOrderLineId(lineDto.getId());
                            lineDto.setProductDto(modelMapper.map(posOrderLineVRepository.findById(lineDto.getId()).get().getProductDto(), ProductPosOrderLineVAllDto.class));

                            paramDto.getPosOrderLines().stream().filter(item -> item.getId().equals(lineDto.getId())).findFirst().ifPresent(
                                    (item) ->
                                    {
                                        lineDto.setLineDetails(item.getLineDetails());
                                    }
                            );

                            return lineDto;
                        })
                        .collect(Collectors.toList());

                resultDto.setPosOrderLines(posOrderlineDtos);

            }
            resultDto.setOrderDate(DateHelper.fromInstantDateAndTime(finalEntitySave.getOrderDate()));
            response.setMessage(messageSource.getMessage("posorder_update", null, LocaleContextHolder.getLocale()));
            response.setData(resultDto);
        } else {



            if (entitySave.getDocumentNo() == null){
                Integer maxId = entityRepository.getMaxId() + 1;
                String docNo = DocHelper.generateDocNo("POS",maxId) ;
                entitySave.setDocumentNo(docNo);
            }


            entitySave.setOrderDate(DateHelper.toInstantNowUTC());
            if (entitySave.getIsProcessed() != null)
                entitySave.setIsProcessed("N");
            if (entitySave.getIsSyncErp() != null)
                entitySave.setIsSyncErp("N");
            if (entitySave.getIsAppliedSercharge() != null)
                entitySave.setIsAppliedSercharge("N");
            if (entitySave.getPricelistId() == null) {
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_POSTERMINAL_BY_ID + "/" + entitySave.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                PosTerminalDto posTerminalDto = modelMapper.map(exRes.getData(), PosTerminalDto.class);
                entitySave.setPricelistId(posTerminalDto.getPriceListId());
            }

            entitySave.setCurrencyId(currencyRepository.findByCurrencyCode(AppConstant.CURRENCY_CODE_VND).getId());
            entitySave.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.POS_ORDER).getId());
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());

            // xu ly image
            if (paramDto.getImageTransaction() != null) {
                entitySave.setImageId(handleImage(paramDto.getImageTransaction().getImage64(),paramDto.getImageTransaction().getImageCode()));
            }

            List<Tuple> rsCheckOrg = entityManager.createNativeQuery("select shift_mgmt from d_tenant where d_tenant_id = :tenantId", Tuple.class)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getResultList();
            if(!rsCheckOrg.isEmpty()) {

                if("Y".equals(ParseHelper.STRING.parse(rsCheckOrg.get(0).get("shift_mgmt")))) {

                    Integer shiftId = shiftControlRepository.getMaxSequenceNoByOrgAndPosTerminal(entitySave.getOrgId(), entitySave.getPosTerminalId());
                    Optional<ShiftControl> entity = shiftControlRepository.findByTenantIdAndOrgIdAndPosTerminalIdAndSequenceNo(AuditContext.getAuditInfo().getTenantId(),
                            entitySave.getOrgId(), entitySave.getPosTerminalId(), shiftId);
                    if (entity.isPresent()) {

                        if (entity.get().getEndDate() != null) {

                            throw new PosException(messageSource.getMessage("pos_shift_open_notFound", null, LocaleContextHolder.getLocale()));
                        } else {

                            entitySave.setShiftControlId(entity.get().getId());
                        }
                    } else {

                        throw new PosException(messageSource.getMessage("pos_shift_open_notFound", null, LocaleContextHolder.getLocale()));
                    }
                }
            }

            entitySave.setServiceType("ALT");

            processTable(paramDto);

            entitySave = this.entityRepository.saveAndFlush(entitySave);
            PosOrder finalEntitySave = entitySave;
            paramDto.setId(entitySave.getId());
            log.info("DocumentNo: " + entitySave.getDocumentNo());
            //Xu ly trang thai ban

            processPosTaxLine(paramDto);
            processPosReceiptOther(paramDto);

            // luu order line
            List<PosOrderline> posOrderLines = processOrderLines(paramDto, finalEntitySave);

            // chuyen ve dto
            PosOrderDto resultDto = modelMapper.map(finalEntitySave, PosOrderDto.class);

            List<PosOrderLineVAllDto> posOrderLineDto = posOrderLines.stream()
                    .map(line -> {
                        PosOrderLineVAllDto lineDto = modelMapper.map(line, PosOrderLineVAllDto.class);
                        PosOrderLineVAll productV = posOrderLineVRepository.findById(lineDto.getId()).get() ;
                        lineDto.setProductDto(modelMapper.map(productV.getProductDto(), ProductPosOrderLineVAllDto.class));
                        paramDto.getPosOrderLines().stream().filter(item -> item.getId().equals(lineDto.getId())).findFirst().ifPresent(
                                (item) -> {
                                    lineDto.setLineDetails(item.getLineDetails());
                                }
                        );
                        lineDto.setPosOrderLineId(lineDto.getId());
                        return lineDto;
                    })
                    .collect(Collectors.toList());

            resultDto.setPosOrderLines(posOrderLineDto);
            resultDto.setOrderDate(DateHelper.fromInstantDateAndTime(finalEntitySave.getOrderDate()));
            response.setData(resultDto);
            response.setMessage(messageSource.getMessage("posorder_create", null, LocaleContextHolder.getLocale()));

            log.info("Before multi source" );
            if(paramDto.getSource() != null && !paramDto.getSource().equals("POS")){
                log.info("In multi source");
                OrderMultiSourceNotiDto orderMultiSourceNotiDto = OrderMultiSourceNotiDto.builder()
                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                        .userId(entitySave.getUserId()).build();
                kafkaTemplate.send(ORDER_MULTI_SOURCE_TOPIC, orderMultiSourceNotiDto);
            }
        }
        response.setStatus(HttpStatus.OK.value());
        log.info("PosOrder saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    private String getCustomerName(Integer customerId) {
        Query query = this.entityManager.createNativeQuery("select name from d_customer dc where d_customer_id = :customerId");
        query = query.setParameter("customerId", customerId);

        Object name = query.getSingleResult();
        if (name != null) {
            return name.toString();
        }
        return "";
    }

    private String getCustomerPhone(Integer customerId){
        StringBuilder sql = new StringBuilder("select phone1 from d_customer dc where d_customer_id = :customerId");

        Query query = this.entityManager.createNativeQuery(sql.toString());
        query = query.setParameter("customerId", customerId);

        Object phone = query.getSingleResult();
        if(phone != null){
            return phone.toString();
        }
        return "";
    }

    @Transactional
    @Override
    public GlobalReponse deleteById(Integer id) {
        log.info("*** Void, service; delete PosOrder by id *");
        GlobalReponse response = new GlobalReponse();
        Optional<PosOrder> entityDelete = this.entityRepository.findById(id);
        if (entityDelete.isEmpty()) {
            response.setMessage("PosOrder not found with id: " + id);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return response;
        }
        // cap nhat trang thai cua ban
        Table table = tableRepository.findById(entityDelete.get().getTableId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));
        table.setTableStatus(Reference.TableStatus.TIU.name());
        tableRepository.save(table);

        this.posOrderlineRepository.deleteAllByPosOrderId(id);
        this.entityRepository.delete(entityDelete.get());
        response.setMessage(messageSource.getMessage("posorder_delete", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse cancelCompletedOrder(Integer posOrderId) {
        log.info("*** Void, service; delete product by id *");
        GlobalReponse response = new GlobalReponse();

        try {

            StringBuilder sqlUsername = new StringBuilder("select user_name from d_user where d_user_id = ? ");

            Query usernameQuery = entityManager.createNativeQuery(sqlUsername.toString());
            usernameQuery.setParameter(1, AuditContext.getAuditInfo().getUserId());
            String username = ParseHelper.STRING.parse(usernameQuery.getSingleResult());

            Query queryProcedure = entityManager.createNativeQuery("CALL pos.fn_void_completed_pos_order(:posOrderId, :username)");
            queryProcedure.setParameter("posOrderId", posOrderId);
            queryProcedure.setParameter("username", username);
            queryProcedure.executeUpdate();

            try { // Send Kafka save Audit for user
                Optional<PosOrder> optPosOrder = entityRepository.findById(posOrderId);
                if (optPosOrder.isPresent()) {
                    Map<String, String> description = Map.of(
                            "name", "Đơn hàng",
                            "document_no", optPosOrder.get().getDocumentNo()
                    );
                    String descriptionJson = objectMapper.writeValueAsString(description);

                    kafkaAuditUserHelper.sendKafkaSaveAuditUser(
                            AuditContext.getAuditInfo().getMainTenantId(),
                            AuditContext.getAuditInfo().getOrgId(),
                            serviceName,
                            DbMetadataHelper.getTableName(optPosOrder.get()),
                            posOrderId,
                            "CANCEL",
                            AuditContext.getAuditInfo().getUserId(),
                            descriptionJson);
                }
            } catch (Exception e) {
                String errMes = "Error: PosOrderServiceImpl: cancelCompletedOrder(): send Kafka save AuditLogUser";
                log.error("{}: {}", errMes, e.getMessage());
                throw new PosException(errMes); // thêm messageSource
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw new PosException(messageSource.getMessage("pos_order_voided_error", null, LocaleContextHolder.getLocale()));
        }

        response.setMessage(messageSource.getMessage("pos_order_voided", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
        return response;
    }


    @Override
    public GlobalReponse findById(Integer id) {
        log.info("*** PosOrder, service; fetch PosOrder by id *");
        PosOrder orderGet = this.entityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
        PosOrderDto posOrderDto = modelMapper.map(orderGet, PosOrderDto.class);
        PosOrder posOrder = this.entityRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

        List<PosOrderLineVAll> posOrderlines = posOrderLineVRepository.findAllByPosOrderAndIsActive(posOrder, "Y");

        List<PosOrderLineVAllDto> listPosOrderLines = posOrderlines.stream().map(
                (element) -> {
                    PosOrderLineVAllDto lineDto = modelMapper.map(element, PosOrderLineVAllDto.class);
                    sql = new StringBuilder("select d_pos_orderline_id,name,qty,d_product_id from pos.d_pos_orderline_detail_v where d_pos_orderline_id = :posOrderLineId");

                    List<Map<String, Object>> results = entityManager.createNativeQuery(sql.toString())
                            .setParameter("posOrderLineId", lineDto.getId())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();

                    List<PosOrderLineDetailDto> lisDetail = new ArrayList<>();
                    for (Map<String, Object> row : results) {
                        PosOrderLineDetailDto itemDetail = PosOrderLineDetailDto.builder()
                                .productName(ParseHelper.STRING.parse(row.get("name")))
                                .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                                .productId(ParseHelper.INT.parse(row.get("d_product_id")))
                                .build();
                        lisDetail.add(itemDetail);
                    }
                    lineDto.setLineDetails(lisDetail);
                    lineDto.setPosOrderId(posOrder.getId());
                    return lineDto;
                }
        ).collect(Collectors.toList());

        posOrderDto.setPosOrderLines(listPosOrderLines);
        posOrderDto.setOrderDate(DateHelper.fromInstantDateAndTime(orderGet.getOrderDate()));

        GlobalReponse response = new GlobalReponse();
        response.setData(posOrderDto);
        response.setMessage(messageSource.getMessage("posorder_fetched", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponsePagination findAllPosList(PosOrderListQueryRequest request) {
        Pageable pageable = requestParamsUtils.getPageRequest(request);

        Specification<PosOrderListView> spec = PosOrderListSpecification.getEntitySpecification(request);
        Page<PosOrderListView> pages = completeViewRepository.findAll(spec, pageable);

        List<PosOrderListViewDto> reposeData = pages.getContent().stream()
                .map(item -> {
                    PosOrderListViewDto dto = modelMapper.map(item, PosOrderListViewDto.class);
//                    List<PosOrderLineCompleteVAllDto> line = posOrderLineVRepository.findByPosOrder_Id(item.getPosOrderId()).stream()
//                            .map((element) -> {
//                                PosOrderLineCompleteVAllDto lineDto = modelMapper.map(element, PosOrderLineCompleteVAllDto.class);
//                                return lineDto;
//                            })
//                            .collect(Collectors.toList());
//                    dto.setPosOrderLines(line);
                    dto.setOrderDate(DateHelper.fromInstantUTC(item.getOrderDate()));
                    return dto;
                })
                .collect(Collectors.toList());


        return GlobalReponsePagination.builder()
                .data(reposeData)
                .currentPage(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalItems(pages
                        .getTotalElements())
                .status(HttpStatus.OK.value())
                .errors("")
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination getHistory(PosOrderQueryRequest request) {
        return null;
    }

    @Override
    public GlobalReponse generatePOSBillNumber(GeneratePOSBillNumberDto dto) {

        log.info("generatePOSBillNumber");
        GlobalReponse response = new GlobalReponse();


        StringBuilder billNo = new StringBuilder();
        billNo.append(dto.getPosTerminalId());

        if (dto.getFloorNo() != null)
            billNo.append("-" + dto.getFloorNo());
        if (dto.getTableNo() != null)
            billNo.append("-" + dto.getTableNo());

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String date = DateHelper.fromLocalDateMMyyyy(timestamp.toLocalDateTime().toLocalDate());
        billNo.append("-" + date);

        billNo.append("-" + dto.getPosOrderId());

        response.setData(POSBillNumberRespDto.builder().billNo(billNo.toString()).build());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Transactional
    @Override
    public GlobalReponse intSave(List<PosOrderDto> dto) {

        if (dto != null && !dto.isEmpty()) {
            dto.stream().forEach(item -> {
                PosOrder entitySave = this.entityRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));

                entitySave = posOrderMapper.updatePosOrder(item, entitySave);
                this.entityRepository.saveAndFlush(entitySave);
            });
        }
        return GlobalReponse.builder()
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @return
     */
    @Transactional
    @Override
    public GlobalReponsePagination reportOrder(ReportPosQueryRequest request) {
        Page<Map<String, Object>> report = getReport(request, requestParamsUtils.getPageRequest(request));
        List<Map<String, Object>> reportData = report.getContent();
        List<ReportPosOrderDto> listData = new ArrayList<>();

        for (Map<String, Object> item : reportData) {
            ReportPosOrderDto dto = new ReportPosOrderDto();
            dto.setDate(DateHelper.fromInstantDateAndTime(ParseHelper.INSTANT.parse(item.get("order_date"))));
            dto.setDocumentNo(ParseHelper.STRING.parse(item.get("document_no")));
            dto.setQtyGuest(ParseHelper.STRING.parse(item.get("order_guests")));
            dto.setCashAmount(ParseHelper.BIGDECIMAL.parse(item.get("cash")));
            dto.setBankAmount(ParseHelper.BIGDECIMAL.parse(item.get("bank")));
            dto.setCouponAmount(ParseHelper.BIGDECIMAL.parse(item.get("coupon")));
            dto.setVoucherAmount(ParseHelper.BIGDECIMAL.parse(item.get("voucher")));
            dto.setDebtAmount(ParseHelper.BIGDECIMAL.parse(item.get("deb")));
            dto.setFreeAmount(ParseHelper.BIGDECIMAL.parse(item.get("free")));
            dto.setQrCodeAmount(ParseHelper.BIGDECIMAL.parse(item.get("qrcode")));
            dto.setLoyaltyAmount(ParseHelper.BIGDECIMAL.parse(item.get("loyalty")));
            dto.setVisaAmount(ParseHelper.BIGDECIMAL.parse(item.get("visa")));
            dto.setCashier(ParseHelper.STRING.parse(item.get("cashier")));
            dto.setShift(ParseHelper.STRING.parse(item.get("name")));
            dto.setTotalAmount(ParseHelper.BIGDECIMAL.parse(item.get("total_amount")));

            listData.add(dto);
        }

        return GlobalReponsePagination.builder()
                .data(listData)
                .pageSize(report.getSize())
                .currentPage(report.getNumber())
                .totalItems(report.getTotalElements())
                .totalPages(report.getTotalPages())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @paramDto
     * @return
     */
    @Transactional
    @Override
    public GlobalReponse requestOrder(PosOrderDto paramDto) {
        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);

        log.info("save posOrderDto ");
        log.info("*** PosOrder, service; save PosOrder *** {}", paramDto.toString());

        log.info("*** PosOrder, service; save PosOrder ***");
        GlobalReponse response = new GlobalReponse();
        PosOrder entitySave = posOrderMapper.toPosOrder(paramDto);


        if (paramDto.getTableId() != null) {
            // cap nhat trang thai cua ban
            Table table = tableRepository.findById(paramDto.getTableId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));
            if (paramDto.getOrderStatus().equals(Reference.DocStatus.COM.name()))
                table.setTableStatus(Reference.TableStatus.ETB.name());
            else
                table.setTableStatus(Reference.TableStatus.TIU.name());
            tableRepository.save(table);
        }

//        PosOrder entityCheck = entityRepository.findByTableIdAndOrderStatusAndCustomerId(paramDto.getTableId(), Reference.DocStatus.DRA.name(), paramDto.getCustomerId());
        PosOrder entityCheck = null;
        if(paramDto.getId() != null){
            entityCheck = entityRepository.findById(paramDto.getId()).orElse(null);
        }

        if (entityCheck != null) // Update
        {
            PosOrder finalEntitySave = entityCheck;
            // chuyen ve dto
            PosOrderDto resultDto = modelMapper.map(finalEntitySave, PosOrderDto.class);
            if (paramDto.getPosOrderLines() != null && !paramDto.getPosOrderLines().isEmpty()) {
                // luu order line
                List<PosOrderline> posOrderlines = paramDto.getPosOrderLines().stream()
                        .map(element -> {
                            if (element.getId() == null) {// insert line moi
                                PosOrderline posOrderline = modelMapper.map(element, PosOrderline.class);
                                posOrderline.setPosOrderId(finalEntitySave.getId());
                                posOrderline.setTenantId(AuditContext.getAuditInfo().getTenantId());
                                posOrderline.setOrgId(finalEntitySave.getOrgId());
                                posOrderlineRepository.saveAndFlush(posOrderline);
                                // luu line product combo
                                if (element.getLineDetails() != null) {
                                    element.getLineDetails().forEach(
                                            (item) -> {
                                                item.setPosOrderLineId(posOrderline.getId());
                                                PosOrderLineDetail itemSave = modelMapper.map(item, PosOrderLineDetail.class);
                                                itemSave.setOrgId(posOrderline.getOrgId());
                                                itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                                                PosOrderLineDetail lineDetail = posOrderLineDetailRepository.save(itemSave);
                                                item = modelMapper.map(lineDetail, PosOrderLineDetailDto.class);
                                            }
                                    );
                                }
                                element.setId(posOrderline.getId());
                                return posOrderline;
                            } else {//update line
                                PosOrderline posOrderline = posOrderlineRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
                                modelMapper.map(element, posOrderline);
                                posOrderline.setOrgId(finalEntitySave.getOrgId());
                                posOrderlineRepository.saveAndFlush(posOrderline);
                                // luu line product combo
                                if (element.getLineDetails() != null) {
                                    element.getLineDetails().forEach(
                                            (item) ->
                                            {
                                                item.setPosOrderLineId(posOrderline.getId());
                                                PosOrderLineDetail itemSave = modelMapper.map(item, PosOrderLineDetail.class);
                                                itemSave.setOrgId(posOrderline.getOrgId());
                                                itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                                                PosOrderLineDetail lineDetail = posOrderLineDetailRepository.save(itemSave);
                                                item = modelMapper.map(lineDetail, PosOrderLineDetailDto.class);
                                            }
                                    );
                                }
                                element.setId(posOrderline.getId());
                                return posOrderline;
                            }
                        })
                        .collect(Collectors.toList());


                List<PosOrderLineVAllDto> posOrderlineDtos = posOrderlines.stream()
                        .map(line -> {
                            PosOrderLineVAllDto lineDto = modelMapper.map(line, PosOrderLineVAllDto.class);
                            lineDto.setProductDto(modelMapper.map(posOrderLineVRepository.findById(lineDto.getId()).get().getProductDto(), ProductPosOrderLineVAllDto.class));

                            paramDto.getPosOrderLines().stream().filter(item -> item.getId().equals(lineDto.getId())).findFirst().ifPresent(
                                    (item) ->
                                    {
                                        lineDto.setLineDetails(item.getLineDetails());
                                    }
                            );

                            return lineDto;
                        })
                        .collect(Collectors.toList());

                resultDto.setPosOrderLines(posOrderlineDtos);

            }
            response.setMessage(messageSource.getMessage("posorder_update", null, LocaleContextHolder.getLocale()));
            response.setData(resultDto);
        } else { // Create9

            int id = entityRepository.getMaxId() + 1;

            String docNo = "POS" + id;
            if (entitySave.getDocumentNo() == null)
                entitySave.setDocumentNo(docNo);
            // update ngay
            if (paramDto.getOrderDate() != null) {
                entitySave.setOrderDate(DateHelper.toInstantDateAndTime(paramDto.getOrderDate()));
            }
            if (entitySave.getIsProcessed() != null)
                entitySave.setIsProcessed("N");
            if (entitySave.getIsSyncErp() != null)
                entitySave.setIsSyncErp("N");
            if (entitySave.getIsAppliedSercharge() != null)
                entitySave.setIsAppliedSercharge("N");
            if (entitySave.getPricelistId() == null) {
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_POSTERMINAL_BY_ID + "/" + entitySave.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                PosTerminalDto posTerminalDto = modelMapper.map(exRes.getData(), PosTerminalDto.class);
                entitySave.setPricelistId(posTerminalDto.getPriceListId());
            }

            entitySave.setCurrencyId(currencyRepository.findByCurrencyCode(AppConstant.CURRENCY_CODE_VND).getId());
            entitySave.setDoctypeId(doctypeRepository.findByCode(AppConstant.DocTypeCode.POS_ORDER).getId());
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());
            entitySave.setOrderDate(Instant.now());

            String sql ="select d_customer_id from pos.d_customer where d_tenant_id  = :tenantId " +
                    " and  phone1 = :phone ";

            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("phone", paramDto.getPhone())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            if(results == null || results.isEmpty()) {
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_CUSTOMER_BY_PHONE1 + "/" + paramDto.getPhone() + "/" + paramDto.getCustomerName(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                CustomerDto cus = modelMapper.map(exRes.getData(), CustomerDto.class);
                entitySave.setCustomerId(cus.getId());
            }else{
                for ( Map<String, Object> row : results ) {
                    log.info("Row: {}", row);
                    entitySave.setCustomerId(ParseHelper.INT.parse(row.get("d_customer_id")));
                }
            }



            entitySave = this.entityRepository.save(entitySave);
            PosOrder finalEntitySave = entitySave;

            // luu order line
            List<PosOrderline> posOrderlines = paramDto.getPosOrderLines().stream()
                    .map(element -> {
                        PosOrderline posOrderline = modelMapper.map(element, PosOrderline.class);
                        posOrderline.setPosOrderId(finalEntitySave.getId());
                        posOrderline.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        posOrderline.setOrgId(finalEntitySave.getOrgId());
                        posOrderlineRepository.save(posOrderline);

                        if (element.getLineDetails() != null) {
                            element.getLineDetails().forEach(
                                    (item) -> {
                                        item.setPosOrderLineId(posOrderline.getId());
                                        item.setOrgId(posOrderline.getOrgId());
                                        PosOrderLineDetail itemSave = modelMapper.map(item, PosOrderLineDetail.class);
                                        itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                                        PosOrderLineDetail lineDetail = posOrderLineDetailRepository.save(itemSave);
                                        item = modelMapper.map(lineDetail, PosOrderLineDetailDto.class);

                                    }
                            );
                        }
                        element.setId(posOrderline.getId());
                        return posOrderline;
                    })
                    .collect(Collectors.toList());

            // chuyen ve dto
            PosOrderDto resultDto = modelMapper.map(finalEntitySave, PosOrderDto.class);
            List<PosOrderLineVAllDto> posOrderlineDtos = posOrderlines.stream()
                    .map(line -> {
                        PosOrderLineVAllDto lineDto = modelMapper.map(line, PosOrderLineVAllDto.class);
                        paramDto.getPosOrderLines().stream().filter(item -> item.getId().equals(lineDto.getId())).findFirst().ifPresent(
                                (item) -> {
                                    lineDto.setLineDetails(item.getLineDetails());
                                }
                        );
                        return lineDto;
                    })
                    .collect(Collectors.toList());
            tableRepository.updateStatusById(paramDto.getTableId(), Reference.TableStatus.TIU.name());
            resultDto.setPosOrderLines(posOrderlineDtos);
            response.setData(resultDto);
            response.setMessage(messageSource.getMessage("posorder_create", null, LocaleContextHolder.getLocale()));
        }
        response.setStatus(HttpStatus.OK.value());
        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    @Override
    public GlobalReponse findPosListById(Integer id) {


        PosOrderListView entity = completeViewRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
        PosOrderListViewDto dto = modelMapper.map(entity, PosOrderListViewDto.class);
        List<PosOrderLineCompleteVAllDto> line = posOrderLineVRepository.findByPosOrder_IdAndIsActive(entity.getPosOrderId(),"Y").stream()
                .map((element) -> {
                    PosOrderLineCompleteVAllDto lineDto = modelMapper.map(element, PosOrderLineCompleteVAllDto.class);
                    return lineDto;
                })
                .collect(Collectors.toList());
        dto.setPosOrderLines(line);
        dto.setOrderDate(DateHelper.fromInstantDateAndTime(entity.getOrderDate()));

        //Total Amount Receipt Other
        String sqlROSum = "SELECT round(sum(receipt_amount),2) as sum_receipt_amount FROM pos.d_pos_receipt_other WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId and is_cal = 'Y'";

        BigDecimal sumReceiptAmount = (BigDecimal) entityManager.createNativeQuery(sqlROSum)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", entity.getPosOrderId())
                .getSingleResult();


        //Total Amount Receipt Other
        String sqlTaxSum = "SELECT round(sum(tax_amount),2) as sum_tax_amount FROM pos.d_pos_taxline WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumTaxAmount = (BigDecimal) entityManager.createNativeQuery(sqlTaxSum)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", entity.getPosOrderId())
                .getSingleResult();

        //Total Amount Receipt Other
        String sqlFlatAmt = "SELECT flat_amt FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumFlatAmt = (BigDecimal) entityManager.createNativeQuery(sqlFlatAmt)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", entity.getPosOrderId())
                .getSingleResult();

        //Total Amount Deduction
        String sqlDeductionAmt = "SELECT deduction_amount FROM pos.d_pos_order WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        BigDecimal sumDeductionAmt = ParseHelper.BIGDECIMAL.parse(entityManager.createNativeQuery(sqlDeductionAmt)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", entity.getPosOrderId())
                .getSingleResult());

        sumFlatAmt = sumFlatAmt != null ? sumFlatAmt : BigDecimal.ZERO;
        sumDeductionAmt = sumDeductionAmt != null ? sumDeductionAmt : BigDecimal.ZERO;
        BigDecimal totalDiscount = sumFlatAmt.add(sumDeductionAmt);

        if (!"INC".equals(entity.getPriceCateCode())) {

            dto.setTaxAmount(sumTaxAmount == null ? BigDecimal.ZERO : sumTaxAmount);
        } else {

            dto.setTaxAmount(BigDecimal.ZERO);
        }
//        dto.setDiscountAmount(sumFlatAmt == null ? BigDecimal.ZERO : sumFlatAmt);
        dto.setDiscountAmount(totalDiscount);
        dto.setReceiptOtherAmount(sumReceiptAmount == null ? BigDecimal.ZERO : sumReceiptAmount);

        return GlobalReponse.builder()
                .data(dto)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale())).build();

    }

    @Override
    public GlobalReponsePagination findPosOrdersRetail(PosOrderListQueryRequest request) {

        Parameter parameter = new Parameter();
        String multiSource = request.getIsMultiSource() == null ? "N" : request.getIsMultiSource();
        parameter.add("order_status", request.getOrderStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("document_no", request.getDocumentNo(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("d_customer_id", request.getCustomerId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("customer_code", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("customer_name", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        parameter.add("phone1", request.getCustomerKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.END);
        parameter.add("source", "POS", multiSource.equals("N") ? Param.Logical.EQUAL : Param.Logical.NOT_EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("order_date",Param.getBetweenParam(request.getDateFrom(),request.getDateTo()),Param.Logical.BETWEEN,Param.Relational.NONE,Param.NONE);
        request.setSortBy("order_date");
        ResultSet rs = queryEngine.getRecords( "d_pos_order_retail_v",
                parameter, request);

        List<PosOrderRetailDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                PosOrderRetailDto dto = PosOrderRetailDto.builder()
                        .id(rs.getInt("d_pos_order_id"))
                        .documentNo(rs.getString("document_no"))
                        .billNo(rs.getString("document_no"))
                        .orderDate(rs.getObject("order_date") != null
                                ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                : null)
                        .description(rs.getString("description"))
                        .shiftControlId(rs.getInt("d_shift_control_id"))
                        .orderStatus(rs.getString("order_status"))
                        .orderStatusName(rs.getString("order_status_name"))
                        .org(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .address(rs.getString("address"))
                                .build())
                        .posTerminal(PosTerminalDto.builder()
                                .id(rs.getInt("d_pos_terminal_id"))
                                .name(rs.getString("terminal_name"))
                                .printerProductId(rs.getString("printer_product_id"))
                                .printerVendorId(rs.getString("printer_vendor_id"))
                                .printerName(rs.getString("printer_name"))
                                .printerPageSize(rs.getInt("printer_page_size"))
                                .printerPageSoQty(rs.getInt("printer_page_so_qty"))
                                .printerPageTempQty(rs.getInt("printer_page_temp_qty"))
                                .printerType(rs.getString("printer_type"))
                                .printerPort(rs.getBigDecimal("printer_port"))
                                .printerIp(rs.getString("printer_ip"))
                                .build())
                        .customer(CustomerDto.builder()
                                .id(rs.getInt("d_customer_id"))
                                .code(rs.getString("customer_code"))
                                .name(rs.getString("customer_name"))
                                .phone1(rs.getString("phone1"))
                                .build())
                        .user(UserDto.builder()
                                .userId(rs.getInt("d_user_id"))
                                .userName(rs.getString("user_name"))
                                .fullName(rs.getString("user_full_name"))
                                .build())
                        .flatDiscount(rs.getBigDecimal("flat_discount"))
                        .totalLine(rs.getBigDecimal("total_line"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .flatAmount(rs.getBigDecimal("flat_amt"))
                        .receiptOtherAmount(rs.getBigDecimal("receipt_other_amt"))
                        .taxAmount(rs.getBigDecimal("tax_amount"))
                        .isIssueEInvoice(rs.getString("is_issue_einvoice"))
                        .buyerName(rs.getString("buyer_name"))
                        .buyerTaxCode(rs.getString("buyer_tax_code"))
                        .buyerAddress(rs.getString("buyer_address"))
                        .buyerPhone(rs.getString("buyer_phone"))
                        .buyerEmail(rs.getString("buyer_email"))
                        .buyerCompany(rs.getString("buyer_company"))
                        .source(rs.getString("source"))
                        .build();
                data.add(dto);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("d_pos_order_retail_v", parameter, request);
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

    @Override
    public GlobalReponse findPosOrderRetailById(PosOrderReqDto dto) {


        PosOrderRetailDto data = null;

        try {
            if(dto.getIsBillInfo() != null && dto.getIsBillInfo().equals("Y")){
                Parameter parameter = new Parameter();
                parameter.add("d_pos_order_id", dto.getId(), Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
                ResultSet rs = queryEngine.getRecordsWithoutPaging( "d_pos_order_retail_v",parameter);
                while (rs.next()) {
                    data = PosOrderRetailDto.builder()
                            .id(rs.getInt("d_pos_order_id"))
                            .documentNo(rs.getString("document_no"))
                            .billNo(rs.getString("document_no"))
                            .orderDate(rs.getObject("order_date") != null
                                    ? DateHelper.fromTimestampStd(rs.getTimestamp("order_date"))
                                    : null)
                            .description(rs.getString("description"))
                            .shiftControlId(rs.getInt("d_shift_control_id"))
                            .orderStatus(rs.getString("order_status"))
                            .orderStatusName(rs.getString("order_status_name"))
                            .org(OrgDto.builder()
                                    .id(rs.getInt("d_org_id"))
                                    .name(rs.getString("org_name"))
                                    .address(rs.getString("address"))
                                    .build())
                            .posTerminal(PosTerminalDto.builder()
                                    .id(rs.getInt("d_pos_terminal_id"))
                                    .name(rs.getString("terminal_name"))
                                    .printerProductId(rs.getString("printer_product_id"))
                                    .printerVendorId(rs.getString("printer_vendor_id"))
                                    .printerName(rs.getString("printer_name"))
                                    .printerPageSize(rs.getInt("printer_page_size"))
                                    .printerPageSoQty(rs.getInt("printer_page_so_qty"))
                                    .printerPageTempQty(rs.getInt("printer_page_temp_qty"))
                                    .printerType(rs.getString("printer_type"))
                                    .printerPort(rs.getBigDecimal("printer_port"))
                                    .printerIp(rs.getString("printer_ip"))
                                    .build())
                            .customer(CustomerDto.builder()
                                    .id(rs.getInt("d_customer_id"))
                                    .name(rs.getString("customer_name"))
                                    .phone1(rs.getString("phone1"))
                                    .build())
                            .user(UserDto.builder()
                                    .userId(rs.getInt("d_user_id"))
                                    .userName(rs.getString("user_name"))
                                    .fullName(rs.getString("user_full_name"))
                                    .build())
                            .flatDiscount(rs.getBigDecimal("flat_discount"))
                            .totalLine(rs.getBigDecimal("total_line"))
                            .totalAmount(rs.getBigDecimal("total_amount"))
                            .flatAmount(rs.getBigDecimal("flat_amt"))
                            .receiptOtherAmount(rs.getBigDecimal("receipt_other_amt"))
                            .taxAmount(rs.getBigDecimal("tax_amount"))
                            .isIssueEInvoice(rs.getString("is_issue_einvoice"))
                            .buyerName(rs.getString("buyer_name"))
                            .buyerTaxCode(rs.getString("buyer_tax_code"))
                            .buyerAddress(rs.getString("buyer_address"))
                            .buyerPhone(rs.getString("buyer_phone"))
                            .buyerEmail(rs.getString("buyer_email"))
                            .buyerCompany(rs.getString("buyer_company"))
                            .source(rs.getString("source"))
                            .build();
                    //Total QTY Order Line
                    String sqlQtySum = "SELECT round(sum(qty),2) as sum_tax_amount FROM pos.d_pos_orderline WHERE d_tenant_id = :tenantId " +
                            " AND d_pos_order_id = :orderId ";

                    BigDecimal sumQtyAmount = (BigDecimal) entityManager.createNativeQuery(sqlQtySum)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .setParameter("orderId", dto.getId())
                            .getSingleResult();

                    data.setTotalQty(sumQtyAmount == null ? BigDecimal.ZERO : sumQtyAmount);
                    data.setPayment(getPosPaymentDto(dto));
                }
            }else{
                data = PosOrderRetailDto.builder().build();
            }

            data.setReceiptOther(getPosReceiptOther(dto));
            data.setTax(getPosTaxLineDto(dto));
            data.setPosOrderLine(getPosOrderLine(dto));



        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }



    public List<PosReceiptOtherDto> getPosReceiptOther(PosOrderReqDto dto) {
        try {
            String sql = "SELECT d_pos_receipt_other_id, " +
                    "d_tenant_id, " +
                    "d_org_id, " +
                    "d_pos_order_id, " +
                    "is_percent, " +
                    "d_tax_id, " +
                    "tax_name, " +
                    "d_receipt_other_id, " +
                    "receipt_name, " +
                    "receipt_wtax_name, " +
                    "code, " +
                    "amount_or_percent, " +
                    "total_amount, " +
                    "receipt_amount, " +
                    "tax_amount, " +
                    "receipt_wtax_amount " +
                    " FROM pos.d_pos_receipt_other_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId AND is_cal = 'Y'";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosReceiptOtherDto> posReceiptOtherList = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosReceiptOtherDto posReceiptOtherDto = PosReceiptOtherDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_receipt_other_id")))
                        .receiptOtherId(ParseHelper.INT.parse(row.get("d_receipt_other_id")))
                        .taxId(ParseHelper.INT.parse(row.get("d_tax_id")))
                        .receiptName(ParseHelper.STRING.parse(row.get("receipt_name")))
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .receiptWithTaxName(ParseHelper.STRING.parse(row.get("receipt_wtax_name")))
                        .amountOrPercent(ParseHelper.BIGDECIMAL.parse(row.get("amount_or_percent")))
                        .totalAmount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .receiptAmount(ParseHelper.BIGDECIMAL.parse(row.get("receipt_amount")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .receiptAmountWithTax(ParseHelper.BIGDECIMAL.parse(row.get("receipt_wtax_amount")))
                        .build();
                posReceiptOtherList.add(posReceiptOtherDto);
            }


            return posReceiptOtherList;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosTaxLineDto> getPosTaxLineDto(PosOrderReqDto dto) {
        try {
            String sql = "SELECT d_tax_id, " +
                    "name, " +
                    "tax_rate, " +
                    "tax_amount, " +
                    "tax_base_amount " +
                    " FROM pos.d_pos_taxline_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosTaxLineDto> posTaxLineDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosTaxLineDto posReceiptOtherDto = PosTaxLineDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_tax_id")))
                        .taxId(ParseHelper.INT.parse(row.get("d_tax_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .taxRate(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .taxBaseAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_base_amount")))
                        .build();
                posTaxLineDto.add(posReceiptOtherDto);
            }


            return posTaxLineDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosPaymentDto> getPosPaymentDto(PosOrderReqDto dto) {
        try {
            String sql = "SELECT d_pos_payment_id, " +
                    "name, " +
                    "payment_method,total_amount " +
                    " FROM pos.d_pos_payment_bill_info_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosPaymentDto> posPaymentDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosPaymentDto posReceiptOtherDto = PosPaymentDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_payment_id")))
                        .name(ParseHelper.STRING.parse(row.get("name")))
                        .paymentMethod(ParseHelper.STRING.parse(row.get("payment_method")))
                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                        .details(getPosPaymentDetailDto(ParseHelper.INT.parse(row.get("d_pos_payment_id"))))
                        .build();
                posPaymentDto.add(posReceiptOtherDto);
            }


            return posPaymentDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PaymentDetailDto> getPosPaymentDetailDto(Integer posPaymentId) {
        try {
            String sql = "SELECT code, " +
                    "amount " +
                    " FROM pos.d_pos_payment_dt WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_payment_id = :posPaymentId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posPaymentId", posPaymentId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PaymentDetailDto> posPaymentDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PaymentDetailDto posReceiptOtherDto = PaymentDetailDto.builder()
                        .code(ParseHelper.STRING.parse(row.get("code")))
                        .amount(ParseHelper.BIGDECIMAL.parse(row.get("amount")))
                        .build();
                posPaymentDto.add(posReceiptOtherDto);
            }


            return posPaymentDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosLotDto> getPosLOtDto(Integer posOrderLineId) {

        try {
            String sql = "SELECT " +
                    " d_pos_lot_id," +
                    " d_pos_order_id, " +
                    " d_pos_orderline_id, " +
                    " d_lot_id ," +
                    " qty, " +
                    " is_active, " +
                    " lot_code, " +
                    " expiry_date " +
                    " FROM pos.d_pos_lot_v WHERE 1 = 1 " +
                    " AND d_pos_orderline_id = :posOrderLineId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("posOrderLineId", posOrderLineId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosLotDto> data = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosLotDto dto = PosLotDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_lot_id")))
                        .posOrderId(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                        .posOrderLineId(ParseHelper.INT.parse(row.get("d_pos_orderline_id")))
                        .lotId(ParseHelper.INT.parse(row.get("d_lot_id")))
                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .isActive(ParseHelper.STRING.parse(row.get("is_active")))
                        .lotCode(ParseHelper.STRING.parse(row.get("lot_code")))
                        .expirationDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("expiry_date"))))
                        .build();
                data.add(dto);
            }


            return data;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    public List<PosOrderLineResDto> getPosOrderLine(PosOrderReqDto dto) {
        try {
            String sql = "SELECT " +
                    "d_pos_order_id , d_pos_orderline_id, d_product_id, " +
                    "product_name, " +
                    "product_wtax_name, " +
                    "salesprice,qty," +
                    "price_discount, " +
                    "discount_percent, " +
                    "discount_amount, " +
                    "tax_amount, " +
                    "linenet_amt," +
                    "grand_total, d_tax_id, tax_name, tax_rate,d_uom_id,uom_name,uom_code,d_image_id, image_url, description, " +
                    " d_kitchen_orderline_id, wds_status, wds_status_name " +
                    " FROM pos.d_pos_orderline_retail_v WHERE d_tenant_id = :tenantId " +
                    " AND d_pos_order_id = :orderId ";


            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orderId", dto.getId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            List<PosOrderLineResDto> posOrderLineRespDto = new ArrayList<>();

            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                PosOrderLineResDto posOrderLineRespDto1 = PosOrderLineResDto.builder()
                        .id(ParseHelper.INT.parse(row.get("d_pos_orderline_id")))
                        .product(ProductDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_product_id")))
                                .name(ParseHelper.STRING.parse(row.get("product_name")))
                                .nameWithTax(ParseHelper.STRING.parse(row.get("product_wtax_name")))
                                .uom(UomDto.builder()
                                        .id(ParseHelper.INT.parse(row.get("d_uom_id")))
                                        .name(ParseHelper.STRING.parse(row.get("uom_name")))
                                        .code(ParseHelper.STRING.parse(row.get("uom_code")))
                                        .build())
                                .image(ImageDto.builder()
                                        .id(ParseHelper.INT.parse(row.get("d_image_id")))
                                        .imageUrl(ParseHelper.STRING.parse(row.get("image_url")))
                                        .build())
                                .build())
                        .tax(TaxDto.builder()
                                .id(ParseHelper.INT.parse(row.get("d_tax_id")))
                                .name(ParseHelper.STRING.parse(row.get("tax_name")))
                                .taxRate(ParseHelper.BIGDECIMAL.parse(row.get("tax_rate")))
                                .build())
                        .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                        .salePrice(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                        .priceDiscount(ParseHelper.BIGDECIMAL.parse(row.get("price_discount")))
                        .taxAmount(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                        .discountAmount(ParseHelper.BIGDECIMAL.parse(row.get("discount_amount")))
                        .discountPercent(ParseHelper.BIGDECIMAL.parse(row.get("discount_percent")))
                        .lineNetAmount(ParseHelper.BIGDECIMAL.parse(row.get("linenet_amt")))
                        .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("grand_total")))
                        .lots(getPosLOtDto(ParseHelper.INT.parse(row.get("d_pos_orderline_id"))))
                        .description(ParseHelper.STRING.parse(row.get("description")))
                        .wdsLineId(ParseHelper.INT.parse(row.get("d_kitchen_orderline_id")))
                        .wdsLineStatus(ParseHelper.STRING.parse(row.get("wds_status")))
                        .wdsLineStatusName(ParseHelper.STRING.parse(row.get("wds_status_name")))
                        .build();
                posOrderLineRespDto.add(posOrderLineRespDto1);
            }

            return posOrderLineRespDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }

    private Page<Map<String, Object>> getReport(ReportPosQueryRequest request, Pageable pageable) {
        sql = new StringBuilder("\n" +
                "select d_pos_order_id\n" +
                ",d_shift_control_id\n" +
                ",d_tenant_id\n" +
                ",d_org_id\n" +
                ",created\n" +
                ",created_by\n" +
                ",is_active\n" +
                ",updated\n" +
                ",updated_by\n" +
                ",cashier\n" +
                ",document_no\n" +
                ",order_guests\n" +
                ",order_date\n " +
                ",bill_no\n " +
                ",name\n" +
                ",value\n" +
                ",cash\n" +
                ",visa\n" +
                ",deb\n" +
                ",loyalty\n" +
                ",bank\n" +
                ",coupon\n" +
                ",free\n" +
                ",qrcode\n" +
                ",voucher\n" +
                ", total_amount \n" +
                "from pos.d_pos_order_report2_v ");
        sql.append(" where d_tenant_id = :tenantId ");

        if (request.getReportDateFrom() != null)
            sql.append(" and DATE(order_date) >= :reportDateFrom ");

        if (request.getReportDateTo() != null)
            sql.append(" and DATE(order_date) <= :reportDateTo ");

        if (request.getShiftType() != null)
            sql.append(" and value = :value ");

        if (request.getCashier() != null)
            sql.append(" and lower(cashier) like  lower(:name)  ");

        if (request.getDocumentNo() != null)
            sql.append(" and document_no like :documentNo ");

        if (request.getCurrentDate() != null)
            sql.append(" and DATE(order_date) = :currentDate ");

        sql.append(" order by order_date desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if (request.getReportDateFrom() != null)
            query.setParameter("reportDateFrom", DateHelper.toLocalDate(request.getReportDateFrom()));
        if (request.getReportDateTo() != null)
            query.setParameter("reportDateTo", DateHelper.toLocalDate(request.getReportDateTo()));

        if (request.getCurrentDate() != null)
            query.setParameter("currentDate", DateHelper.toLocalDate(request.getCurrentDate()));

        if (request.getShiftType() != null)
            query.setParameter("value", request.getShiftType());

        if (request.getCashier() != null)
            query.setParameter("name", "%" + request.getCashier() + "%");

        if (request.getDocumentNo() != null)
            query.setParameter("documentNo", "%" + request.getDocumentNo() + "%");

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Lấy danh sách kết quả cho trang cụ thể
        List<Map<String, Object>> resultList = query.getResultList();

        // Lấy tổng số bản ghi cho phân trang
        String countQueryStr = "select count(*) from d_pos_order_report2_v where d_tenant_id = :tenantId ";
        if (request.getReportDateFrom() != null)
            countQueryStr += " and DATE(order_date) >= :reportDateFrom ";
        if (request.getReportDateTo() != null)
            countQueryStr += " and DATE(order_date) <= :reportDateTo ";
        if (request.getCurrentDate() != null)
            countQueryStr += " and DATE(order_date) = :currentDate ";
        if (request.getShiftType() != null)
            countQueryStr += " and value = :value ";
        if (request.getCashier() != null)
            countQueryStr += " and lower(cashier) like  lower(:name) ";

        if (request.getDocumentNo() != null)
            countQueryStr += " and document_no like  :documentNo ";

        Query countQuery = entityManager.createNativeQuery(countQueryStr);
        countQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if (request.getReportDateFrom() != null)
            countQuery.setParameter("reportDateFrom", DateHelper.toLocalDate(request.getReportDateFrom()));

        if (request.getReportDateTo() != null)
            countQuery.setParameter("reportDateTo", DateHelper.toLocalDate(request.getReportDateTo()));

        if (request.getCurrentDate() != null)
            countQuery.setParameter("currentDate", DateHelper.toLocalDate(request.getCurrentDate()));

        if (request.getShiftType() != null)
            countQuery.setParameter("value", request.getShiftType());

        if (request.getCashier() != null)
            countQuery.setParameter("name", "%" + request.getCashier() + "%");

        if (request.getDocumentNo() != null)
            countQuery.setParameter("documentNo", "%" + request.getDocumentNo() + "%");

        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);
    }


    public Integer handleImage(String image64, String imageCode) {

        ImageDto imageTransaction = null;

        // xu ly image
        imageTransaction = ImageDto.builder()
                .isActive("Y")
                .image64(image64)
                .imageCode(imageCode)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ImageDto> requestImage = new HttpEntity<>(imageTransaction, headers);
        GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_SAVE_IMAGE, requestImage, GlobalReponse.class);

        if(response.getStatus().intValue() != HttpStatus.OK.value()){
            throw new PosException(messageSource.getMessage("image.save.error", null, LocaleContextHolder.getLocale()));
        }

        ImageDto image = modelMapper.map(response.getData(), ImageDto.class);
        return  image.getId();
    }


    private PosOrderline saveOrderLine(PosOrderLineVAllDto element, Integer orderId) {
        PosOrderline posOrderline = modelMapper.map(element, PosOrderline.class);

        if(element.getId() == null){
            posOrderline = modelMapper.map(element, PosOrderline.class);
            posOrderline.setTenantId(AuditContext.getAuditInfo().getTenantId());
        }else{
            posOrderline = posOrderlineRepository.findById(element.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
            modelMapper.map(element, posOrderline);
        }
        posOrderline.setPosOrderId(orderId);
        return posOrderlineRepository.saveAndFlush(posOrderline);
    }

    private void saveLineProductTopping(PosOrderLineVAllDto element, PosOrderline posOrderline, Integer orderId) {
        if (element.getLineDetails() != null && !element.getLineDetails().isEmpty()) {
            Integer posOrderLineId = posOrderline.getId();
            Integer orgId = posOrderline.getOrgId();

            element.getLineDetails().forEach(item -> {
                PosOrderline itemSave;
                if(item.getId() != null){
                    itemSave = posOrderlineRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
                    modelMapper.map(item, itemSave);
                }else{
                    itemSave = modelMapper.map(item, PosOrderline.class);
                    itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                }
                itemSave.setParentId(posOrderLineId);
                itemSave.setOrgId(orgId);
                itemSave.setPosOrderId(orderId);
                itemSave = posOrderlineRepository.saveAndFlush(itemSave);
                item = modelMapper.map(itemSave, PosOrderLineDetailDto.class);
                item.setPosOrderLineId(posOrderLineId);
                item.setId(itemSave.getId());
            });
        }
    }


    private void saveLineProductLot(PosOrderLineVAllDto element, PosOrderline posOrderline, Integer orderId) {
        if (element.getLots() != null  && !element.getLots().isEmpty()) {
            Integer posOrderLineId = posOrderline.getId();
            Integer orgId = posOrderline.getOrgId();

            element.getLots().forEach(item -> {
                PosLot itemSave;
                if(item.getId() != null){
                    itemSave = posLotRepository.findById(item.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
                    modelMapper.map(item, itemSave);
                }else{
                    itemSave = modelMapper.map(item, PosLot.class);
                    itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                }
                itemSave.setOrgId(orgId);
                itemSave.setPosOrderId(orderId);
                itemSave.setPosOrderLineId(posOrderLineId);
                itemSave = posLotRepository.saveAndFlush(itemSave);
                item = modelMapper.map(itemSave, PosLotDto.class);
                item.setId(itemSave.getId());
            });
        }
    }

    public List<PosOrderline> processOrderLines(PosOrderDto paramDto, PosOrder finalEntitySave) {
        return paramDto.getPosOrderLines().stream()
                .map(element -> {
                    PosOrderline posOrderline = saveOrderLine(element, finalEntitySave.getId());
                    saveLineProductTopping(element, posOrderline, finalEntitySave.getId());
                    saveLineProductLot(element, posOrderline, finalEntitySave.getId());
                    element.setId(posOrderline.getId());
                    return posOrderline;
                })
                .collect(Collectors.toList());
    }


    public void processTable(PosOrderDto paramDto){

        if(paramDto.getTableId() != null){
            // cap nhat trang thai cua ban
            Table table = tableRepository.findById(paramDto.getTableId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));
            if (paramDto.getOrderStatus().equals(Reference.DocStatus.COM.name()) || paramDto.getOrderStatus().equals(Reference.DocStatus.VOD.name()) || paramDto.getOrderStatus().equals(Reference.DocStatus.MOV.name()))
                table.setTableStatus(Reference.TableStatus.ETB.name());
            else
                table.setTableStatus(Reference.TableStatus.TIU.name());
            tableRepository.save(table);

            if(paramDto.getIsSplit() == null) paramDto.setIsSplit("N");

            Integer rs = checkExistsTable(paramDto.getTableId());
            if (rs.intValue() > 0 && paramDto.getIsSplit().equals("N")) {
                throw new PosException(messageSource.getMessage("table_in_use", null, LocaleContextHolder.getLocale()));
            }
            log.info("Table not in use: " + paramDto.getTableId());

            tableRepository.updateStatusById(paramDto.getTableId(), Reference.TableStatus.TIU.name());
        }
    }


    public void processPosTaxLine(PosOrderDto paramDto){

        if(paramDto.getTax() != null && !paramDto.getTax().isEmpty()){
            posTaxLineRepository.updateStatusPosTaxLines("N",paramDto.getId());
            paramDto.getTax().forEach(item -> {
                PosTaxLine posTaxLine = posTaxLineRepository.findByPosOrderIdAndTaxId(paramDto.getId(),item.getTaxId());
                if(posTaxLine == null){
                    posTaxLine = modelMapper.map(item, PosTaxLine.class);
                    posTaxLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    posTaxLine.setOrgId(paramDto.getOrgId());
                    posTaxLine.setPosOrderId(paramDto.getId());
                }else{
                    modelMapper.map(item, posTaxLine);
                }
                posTaxLine.setIsActive("Y");
                posTaxLineRepository.saveAndFlush(posTaxLine);
            });
        }
    }

    public void processPosReceiptOther(PosOrderDto paramDto){
        if(paramDto.getReceiptOther() != null) {
            TotalPosOrderCostDto totalPosOrderCostDto =
                    TotalPosOrderCostDto.builder()
                            .posOrderId(paramDto.getId())
                            .org(OrgDto.builder().id(paramDto.getOrgId()).build())
                            .receiptOther(paramDto.getReceiptOther())
                            .build();
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TotalPosOrderCostDto> requestImage = new HttpEntity<>(totalPosOrderCostDto, headers);
            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_CREATE_POS_RO, requestImage, GlobalReponse.class);

            if (response.getStatus().intValue() != HttpStatus.OK.value()) {
                throw new PosException(messageSource.getMessage("receipt.other.save.error", null, LocaleContextHolder.getLocale()));
            }
        }
    }


    public void processPrePosOrder(PosOrderDto paramDto){

        processTable(paramDto);
        processPosTaxLine(paramDto);
        processPosReceiptOther(paramDto);
    }

    public Integer checkExistsTable(Integer tableId){
        String sql1 = "select count(1) from pos.d_pos_order where  d_tenant_id = :tenantId " +
                " and d_table_id = :tableId and order_status = 'DRA' and is_active = 'Y' ";
        Integer rs = ((Number) entityManager.createNativeQuery(sql1)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("tableId", tableId)
                .getSingleResult()).intValue();
        return rs ;
    }


    public String getPosCode(Integer tableId){
        String sql1 = "select document_no from pos.d_pos_order where  d_tenant_id = :tenantId " +
                " and d_table_id = :tableId and order_status = 'DRA' ";
        String rs = ((String) entityManager.createNativeQuery(sql1)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("tableId", tableId)
                .getSingleResult()).toString();
        return rs ;
    }

    @Transactional
    @Override
    public GlobalReponse deletePosOrder(Integer id) {
        StringBuilder sql = new StringBuilder("select count(1) from d_pos_orderline dpo " +
                "join d_kitchen_orderline dko on dpo.d_kitchen_orderline_id = dko.d_kitchen_orderline_id " +
                " where dko.orderline_status in ('WTP', 'BPR', 'PRD', 'PAI') ");

        sql.append(" and dpo.d_pos_order_id = :posOrderId limit 1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("posOrderId", id);

        Long count = ((Number) query.getSingleResult()).longValue();
        if(count > 0){ //Khoong cho phep xoa       p
            throw new PosException(messageSource.getMessage("posorder.line.not.delete", null, LocaleContextHolder.getLocale()));
        }else{
            PosOrder posOrder = entityRepository.findById(id)
                    .orElseThrow(() ->  new PosException(messageSource.getMessage("pos.order.not.found", null, LocaleContextHolder.getLocale())));
            if(posOrder.getOrderStatus().equals("DRA")){
                posOrder.setOrderStatus("VOD");
            }else {
                throw new PosException(messageSource.getMessage("pos.order.completed", null, LocaleContextHolder.getLocale()));
            }

            entityRepository.saveAndFlush(posOrder);
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Transactional
    @Override
    public GlobalReponse deleteWithReason(PosOrderDto paramDto) {

        if (paramDto.getId() == null) {

            // cap nhat trang thai cua ban
            Table table = tableRepository.findById(paramDto.getTableId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));

            if ("TIU".equals(table.getTableStatus())) {

                table.setTableStatus(Reference.TableStatus.ETB.name());
                tableRepository.save(table);

                return GlobalReponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                        .build();
            }
        }

        StringBuilder sql = new StringBuilder("select count(1) from d_pos_orderline dpo " +
                "join d_kitchen_orderline dko on dpo.d_kitchen_orderline_id = dko.d_kitchen_orderline_id " +
                " where dko.orderline_status in ('WTP', 'BPR', 'PRD', 'PAI') ");

        sql.append(" and dpo.d_pos_order_id = :posOrderId limit 1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("posOrderId", paramDto.getId());

        Long count = ((Number) query.getSingleResult()).longValue();

        if(count > 0){ //Khoong cho phep xoa

            throw new PosException(messageSource.getMessage("posorder.line.not.delete", null, LocaleContextHolder.getLocale()));
        }else{

            PosOrder posOrder = entityRepository.findById(paramDto.getId())
                    .orElseThrow(() ->  new PosException(messageSource.getMessage("pos.order.not.found", null, LocaleContextHolder.getLocale())));

            if(posOrder.getOrderStatus().equals("DRA")){

                posOrder.setOrderStatus("VOD");
                posOrder.setCancelReasonId(paramDto.getCancelReasonId());
                posOrder.setCancelReasonMessage(paramDto.getCancelReasonMessage());
            }else {

                throw new PosException(messageSource.getMessage("pos.order.completed", null, LocaleContextHolder.getLocale()));
            }

            try {

                entityRepository.saveAndFlush(posOrder);

                posOrderlineRepository.cancelAllLineByPosOrderId(posOrder.getId());

                // cap nhat trang thai cua ban
                Table table = tableRepository.findById(posOrder.getTableId()).orElseThrow(() -> new ObjectNotFoundException("Table not found"));
                table.setTableStatus(Reference.TableStatus.ETB.name());
                tableRepository.save(table);
            } catch (Exception e) {

                log.error("Error: {}", e.getMessage());
                throw new PosException(e.getMessage());
            }
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }

    @Transactional
    @Override
    public GlobalReponse updateOrderHeader(org.common.dbiz.dto.paymentDto.PosOrderDto dto) {


        PosOrder entitySave = null;
        entitySave = this.entityRepository.findById(dto.getId()).orElseThrow(() -> new ObjectNotFoundException("entity not found"));
        modelMapper.map(dto, entitySave);
        this.entityRepository.saveAndFlush(entitySave);

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();

    }

    @KafkaListener(groupId = GROUP_ID, topics = ORDER_MULTI_SOURCE_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void sendNotificationPaymentWithVoice(ConsumerRecord<String, OrderMultiSourceNotiDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        OrderMultiSourceNotiDto dto = consumerRecord.value();
        try {
            log.info("Topic: " + ORDER_MULTI_SOURCE_TOPIC);
            log.info("Received message with key: " + key);
            acknowledgment.acknowledge();
            dataSourceContextHolder.setCurrentTenantId(new Long(dto.getTenantId().toString()));
            setLocale("vi");

            String message = messageSource.getMessage("order.multi.source.notify", null, LocaleContextHolder.getLocale());
            String title = messageSource.getMessage("order.multi.source.title", null, LocaleContextHolder.getLocale());

            String userToken = getUserDeviceToken(dto.getUserId());

            if(userToken != null){
                log.info("User token: " + userToken);
                HttpHeaders headers = new HttpHeaders();
                headers.set("tenantId", dto.getTenantId().toString());
                //send thong bao
                SendNotification send = SendNotification.builder()
//                        .title("ORDER_MULTI_SOURCE")
                        .title(title)
                        .body(message)
                        .deviceTokens(List.of(userToken))
                        .router("ORDER_MULTI_SOURCE")
                        .type("ANN")
                        .speak(message)
                        .build();
                HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
                GlobalReponse exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);
            }else{
                log.info("User token is null" + dto.getUserId() + " - " + dto.getTenantId());
            }

        } catch (Exception e) {
            log.error("Caught error in updateImportFile(): ", e);
        }

    }

    public void setLocale(String lang) {
        Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
        LocaleContextHolder.setLocale(locale);
    }

    public String getUserDeviceToken(Integer userId){

        String sql1 = "select device_token from pos.d_user where 1 = 1 and d_user_id = :userId";

        List<?> resultList = entityManager.createNativeQuery(sql1)
                .setParameter("userId", userId)
                .getResultList();

        if (resultList.isEmpty()) {
            return null;
        }

        return  ((String) resultList.get(0));
    }
}
