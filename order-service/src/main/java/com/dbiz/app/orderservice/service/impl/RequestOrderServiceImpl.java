package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.*;
import com.dbiz.app.orderservice.domain.view.PosOrderLineVAll;
import com.dbiz.app.orderservice.service.*;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.orderDto.dtoView.PosOrderListViewDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.helper.RequestOrderViewMapper;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.orderservice.specification.RequestOrderGetAllVSpecification;
import com.dbiz.app.orderservice.domain.view.RequestOrderGetAllV;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.dtoView.RequestOrderGetAllVDto;
import org.common.dbiz.dto.orderDto.dtoView.RequestOrderLineGetAllVDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.common.dbiz.request.orderRequest.SendNotifycationRq;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RequestOrderServiceImpl implements RequestOrderService {

    private final RequestParamsUtils requestParamsUtils;

    private final RequestOrderRepository requestOrderRepository;

    private final RequestOrderGetAllVRepository requestOrderGetAllVRepository;

    private final RequestOrderLineRepository requestOrderLineRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final RequestOrderViewMapper requestOrderViewMapper;

    private final RestTemplate restTemplate;

    private final DoctypeRepository doctypeRepository;

    private final EntityManager entityManager;

    private final TableRepository tableRepository;

    private final PosOrderService posOrderService;

    private final KitchenOrderService kitchenOrderService;

    private final PosOrderLineVAllRepository posOrderLineVRepository;

    private final RqOrderlineDetailRepository rqOrderlineDetailRepository;

    private final FloorRepository floorRepository;

    private final PosOrderRepository posOrderRepository;

    @Override
    public GlobalReponsePagination findAll(RequestOrderGetALlVQueryRequest request) {
        GlobalReponsePagination respone = new GlobalReponsePagination();
        Pageable page = requestParamsUtils.getPageRequest(request);
        Specification<RequestOrderGetAllV> spec = RequestOrderGetAllVSpecification.getEntitySpecification(request);
        Page<RequestOrderGetAllV> data = requestOrderGetAllVRepository.findAll(spec, page);
        List<RequestOrderGetAllVDto> response = new ArrayList<>();

        data.getContent().forEach(item ->
        {
            RequestOrderGetAllVDto dto = requestOrderViewMapper.convertToDto(item);
            dto.setOrderTime(DateHelper.fromInstantUTC(item.getOrderTime()));
            List<RequestOrderLineGetAllVDto> lineDtos = new ArrayList<>();
            item.getRequestOrderLines().forEach(line ->
            {
                RequestOrderLineGetAllVDto lineDto = modelMapper.map(line, RequestOrderLineGetAllVDto.class);
                lineDto.setRequestOrder(null);
                List<RqOrderlineDetail> lineDetail = this.rqOrderlineDetailRepository.getAllByRequestOrderLineId(lineDto.getId());
                if(!lineDetail.isEmpty())
                {
                    List<RqOrderLineDetailDto> lineDetailDto =
                            lineDetail.stream().map(i->{
                               return modelMapper.map(i, RqOrderLineDetailDto.class);
                            }).collect(Collectors.toList());
                    lineDto.setLineDetail(lineDetailDto);
                }
                lineDtos.add(lineDto);
            });

            dto.setRequestOrderLines(lineDtos);
            response.add(dto);
        });


        respone.setData(response);
        respone.setErrors("");
        respone.setMessage(messageSource.getMessage("request.order.getAll.success", null, LocaleContextHolder.getLocale()));
        respone.setStatus(HttpStatus.OK.value());
        respone.setTotalPages(data.getTotalPages());
        respone.setCurrentPage(data.getNumber());
        respone.setTotalItems(data.getTotalElements());
        return respone;
    }

    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse save(RequestOrderDto entity) {
        log.info("RequestOrderServiceImpl::save");
        log.info("params: {}", entity);
        if (entity.getId() != null) {
            RequestOrder rq = requestOrderRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("entity not found"));
            modelMapper.map(entity, rq);
            if (entity.getOrderTime() != null)
                rq.setOrderTime(DateHelper.toInstantUTC(entity.getOrderTime()));
            requestOrderRepository.save(rq);
            entity = modelMapper.map(rq, RequestOrderDto.class);
            entity.setOrderTime(DateHelper.fromInstantUTC(rq.getOrderTime()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("request.order.update.success", null, LocaleContextHolder.getLocale()))
                .data(entity)
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Transactional
    @Override
    public GlobalReponse saveAll(RequestOrderDto requestOrderDto) {
        log.info("RequestOrderServiceImpl::saveAll");
        // save RequestOrder, RequestOrderLine, RqOrderlineDetail
        GlobalReponse reponse = new GlobalReponse();
        RequestOrder rqSave = modelMapper.map(requestOrderDto, RequestOrder.class);
        rqSave.setOrderTime(DateHelper.toInstantUTC(requestOrderDto.getOrderTime()));
        rqSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
        Doctype doctype = doctypeRepository.findByCode(AppConstant.DocTypeCode.REQUEST_ORDER);
        rqSave.setDoctypeId(doctype.getId());
        rqSave.setDocumentNo("RQ" + (requestOrderRepository.getMaxId() + 1));
        rqSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
        rqSave.setCustomerPhone(requestOrderDto.getCusPhone());
        rqSave.setCustomerName(requestOrderDto.getCusName());

        rqSave = requestOrderRepository.save(rqSave);
        Integer rqId = rqSave.getId();

        List<RequestOrderLineDto> lineDtosSave = requestOrderDto.getRequestOrderLineDto().stream().map(
                lineDto -> {
                    lineDto.setRequestOrderId(rqId);
                    RequestOrderLine itemSave = modelMapper.map(lineDto, RequestOrderLine.class);
                    itemSave.setTenantId(AuditContext.getAuditInfo().getTenantId());
                    requestOrderLineRepository.save(itemSave);
                    RequestOrderLineDto lineDtoSave = modelMapper.map(itemSave, RequestOrderLineDto.class);
                    if (lineDto.getLineDetail() != null && !lineDto.getLineDetail().isEmpty()) {
                        List<RqOrderLineDetailDto> lineDetail = lineDto.getLineDetail().stream().map(itemDetail -> {
                            RqOrderlineDetail rqOrderlineDetail = modelMapper.map(itemDetail, RqOrderlineDetail.class);
                            rqOrderlineDetail.setRequestOrderLineId(itemSave.getId());
                            rqOrderlineDetail.setTenantId(AuditContext.getAuditInfo().getTenantId());
                            rqOrderlineDetailRepository.save(rqOrderlineDetail);
                            return modelMapper.map(rqOrderlineDetail, RqOrderLineDetailDto.class);
                        }).collect(Collectors.toList());
                        lineDtoSave.setLineDetail(lineDetail);
                    }
                    return lineDtoSave;
                }).collect(Collectors.toList());
        requestOrderDto = modelMapper.map(rqSave, RequestOrderDto.class);
        requestOrderDto.setOrderTime(DateHelper.fromInstantDateAndTime(rqSave.getOrderTime()));
        requestOrderDto.setRequestOrderLineDto(lineDtosSave);
        reponse.setData(requestOrderDto);
        reponse.setErrors("");
        reponse.setMessage(messageSource.getMessage("request.order.save.success", null, LocaleContextHolder.getLocale()));
        reponse.setStatus(HttpStatus.OK.value());
        reponse.setData(requestOrderDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entityHeader = new HttpEntity<>(headers);

        try {
            //send notify
            SendNotification sendNotification = new SendNotification();
            sendNotification.setType("ANN");
            String title = getMessage(AppConstant.notify_Request_Order_TITLE);
            String body = getMessage(AppConstant.notify_Request_Order_BODY);
            GlobalReponse exResponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_GET_POSTERMINAL_BY_ID + "/"
                    + rqSave.getPosTerminalId(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
            PosTerminalDto posTerminalDto = modelMapper.map(exResponse.getData(), PosTerminalDto.class);
            body = body.replace("@terminal_name@", posTerminalDto.getName());
            body = body.replace("@table_name@", tableRepository.findById(requestOrderDto.getTableId()).get().getName());
            sendNotification.setTitle(title);
            sendNotification.setBody(body);
            sendNotification.setCode(null);
            sendNotification.setDeviceTokens(getListDeviceToken(rqSave.getOrgId(), rqSave.getPosTerminalId()));
            sendNotification.setRouterFunction("E-request");
            sendNotification.setRecordId(rqSave.getId());
            HttpEntity<SendNotification> requestEntity = new HttpEntity<>(sendNotification, headers);
            exResponse = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, HttpMethod.POST, requestEntity, GlobalReponse.class).getBody();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

        return reponse;
    }

    @Override
    @Transactional
    public GlobalReponse saveV2(RequestOrderDto entity) {

        HttpHeaders headersMain = new HttpHeaders();
        headersMain.setContentType(MediaType.APPLICATION_JSON);
        headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);

        log.info("RequestOrderServiceImpl::save");
        log.info("params: {}", entity);
        if (entity.getId() != null) {
            RequestOrder rq = requestOrderRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("entity not found"));
            modelMapper.map(entity, rq);
            if (entity.getOrderStatus() != null && entity.getOrderStatus().equals("CNF")) {

                String sql ="select d_customer_id, name from pos.d_customer where d_tenant_id  = :tenantId " +
                        " and  phone1 = :phone ";

                List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                        .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                        .setParameter("phone", rq.getCustomerPhone())
                        .unwrap(NativeQuery.class)
                        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                        .getResultList();

                if(results == null || results.isEmpty()) {
                    StringBuilder urlBuilder = new StringBuilder(AppConstant.DiscoveredDomainsApi.GET_CUSTOMER_BY_PHONE2);

                    List<String> params = new ArrayList<>();
                    if (rq.getCustomerPhone() != null) {
                        params.add("phone1=" + rq.getCustomerPhone());
                    }
                    if (rq.getCustomerName() != null) {
                        params.add("fullName=" + rq.getCustomerName());
                    }

                    if (!params.isEmpty()) {
                        urlBuilder.append("?").append(String.join("&", params));
                    }

                    String url = urlBuilder.toString();

                    GlobalReponse exRes = restTemplate.exchange(url
                            ,HttpMethod.GET ,entityHeader, GlobalReponse.class).getBody();

//                    GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_CUSTOMER_BY_PHONE1 + "/" + rq.getCustomerPhone() + "/" + rq.getCustomerName(), HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                    org.common.dbiz.dto.userDto.CustomerDto cus = modelMapper.map(exRes.getData(), org.common.dbiz.dto.userDto.CustomerDto.class);
                    rq.setCustomerId(cus.getId());
                    rq.setCustomerName(cus.getName());
                }else{

                    for ( Map<String, Object> row : results ) {

                        log.info("Row: {}", row);
                        rq.setCustomerId(ParseHelper.INT.parse(row.get("d_customer_id")));
                        rq.setCustomerName(ParseHelper.STRING.parse(row.get("name")));
                    }
                }

                requestOrderRepository.save(rq);
                List<RequestOrderLine> listRqOrderLine = requestOrderLineRepository.findByTenantIdAndRequestOrderId(
                        AuditContext.getAuditInfo().getTenantId(), entity.getId());

                if (listRqOrderLine != null && !listRqOrderLine.isEmpty()) {
                    GlobalReponse exRes = createPosOrder(rq, listRqOrderLine);
                    if (exRes != null) {
                        if (exRes.getStatus().intValue() == HttpStatus.OK.value()
                                || exRes.getStatus().intValue() == HttpStatus.CREATED.value()) {
                            PosOrderDto posOrderDto = modelMapper.map(exRes.getData(), PosOrderDto.class);
                            rq.setPosOrderId(posOrderDto.getId());
                            requestOrderRepository.save(rq);
                            List<RequestOrderLine> listRqOrderLineUpdate = requestOrderLineRepository.findByTenantIdAndRequestOrderId(
                                    AuditContext.getAuditInfo().getTenantId(), rq.getId());
                            posOrderDto.getPosOrderLines().stream().forEach(line -> {
                                listRqOrderLineUpdate.stream().forEach(rqLine -> {
                                    if (line.getProductId().equals(rqLine.getProductId())) {
                                        rqLine.setPosOrderlineId(line.getId());
                                        requestOrderLineRepository.save(rqLine);
                                    }
                                });
                            });
                            if (entity.getIsSendKitchen() != null && entity.getIsSendKitchen().equals("Y")) {
                                GlobalReponse exResKitchen = createKitchenOrder(posOrderDto);
                                return exResKitchen;
                            }
                        } else {
                            return exRes;
                        }
                    }
                }
            } else {
                requestOrderRepository.save(rq);
            }


            entity = modelMapper.map(rq, RequestOrderDto.class);
            entity.setOrderTime(DateHelper.fromInstantDateAndTime(rq.getOrderTime()));
        }
        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("request.order.update.success", null, LocaleContextHolder.getLocale()))
                .data(entity)
                .errors("")
                .build();
    }

    /**
     * @param param
     * @return
     */
    @Override
    public GlobalReponse getHistoryRequestOrder(RequestOrderDto param) {
        log.info("*** getHistoryRequestOrder ***");
        StringBuilder message = new StringBuilder();
        List<PosOrderLineVAllDto> listPosOrderLines = new ArrayList<>();
        Integer customerId = 0;
        if (param.getCusPhone() != null && !param.getCusPhone().isBlank())
        {
            List<Tuple> rsCheck = entityManager.createNativeQuery("select d_customer_id from d_customer where phone1 = :phone  ", Tuple.class)
                    .setParameter("phone", param.getCusPhone())
                    .getResultList();
            if (rsCheck != null && !rsCheck.isEmpty()) {
                customerId = ParseHelper.INT.parse(rsCheck.get(0).get("d_customer_id"));
            }
        }
        StringBuilder sql = new StringBuilder("SELECT d_pos_order_id FROM pos.d_pos_order WHERE order_status = 'DRA' AND d_org_id = :orgId AND d_table_id = :tableId AND d_floor_id = :floorId");

        if (customerId > 0) {
            sql.append(" AND d_customer_id = :customerId");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class)
                .setParameter("orgId", param.getOrgId())
                .setParameter("tableId", param.getTableId())
                .setParameter("floorId", param.getFloorId());

        if (customerId > 0) {
            query.setParameter("customerId", customerId);
        }

        List<Tuple> rs = query.getResultList();
        if (rs == null || rs.isEmpty())
            message = new StringBuilder(messageSource.getMessage("not.yet.request.order", null, LocaleContextHolder.getLocale()));
        else {
            Integer posOrderId = ParseHelper.INT.parse(rs.get(0).get("d_pos_order_id"));
            //get pos order line
            List<PosOrderLineVAll> lineQuery = posOrderLineVRepository.findByPosOrder_IdAndIsActiveAndParentId(posOrderId, "Y", null);
            if (!lineQuery.isEmpty()) {
                listPosOrderLines = lineQuery.stream()
                        .map(item2 -> {
                            PosOrderLineVAllDto itemDto = modelMapper.map(item2, PosOrderLineVAllDto.class);
                            itemDto.setPosOrderId(item2.getPosOrder().getId());
                            itemDto.setPosOrderLineId(itemDto.getId());
                            itemDto.setTimeWaiting(Duration.between(item2.getCreated(), DateHelper.toInstantNowUTC()).toMinutes() + "");
                            List<PosOrderLineDetailDto> lisDetail = new ArrayList<>();
                            List<PosOrderLineVAll> lineDetailQuery
                                    = posOrderLineVRepository.findByPosOrder_IdAndIsActiveAndParentId(posOrderId, "Y", item2.getId());
                            BigDecimal extraAmount = BigDecimal.ZERO;
                            itemDto.setExtraAmount(extraAmount);
                            if (!lineDetailQuery.isEmpty()) {
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
                                                    .taxRate(item3.getTaxRate())
                                                    .taxName(item3.getTaxName())
                                                    .build();
                                            itemDto.setExtraAmount(itemDto.getExtraAmount().add(item3.getSalesPrice().multiply(item3.getQty())));
                                            return itemDetail;
                                        })
                                        .collect(Collectors.toList());
                            }
                            itemDto.setLineDetails(lisDetail);
                            if (itemDto.getStatus() == null || itemDto.getStatus().isBlank()) {
                                itemDto.setStatus(this.getRef("CNF"));
                                itemDto.setValueStatus("CNF");
                            }
                            return itemDto;
                        })
                        .collect(Collectors.toList());
            }
        }
        List<RequestOrderGetAllV> listRq = requestOrderGetAllVRepository.getAllByCustomerPhone1AndOrderStatus(param.getCusPhone(), "PND");
        if (listRq != null && !listRq.isEmpty()) {
            List<PosOrderLineVAllDto> finalListPosOrderLines = listPosOrderLines;
            listRq.stream().forEach(item -> {
                item.getRequestOrderLines().forEach(lineDto -> {
                    PosOrderLineVAllDto itemDto = PosOrderLineVAllDto.builder()
                            .tenantId(lineDto.getTenantId())
                            .orgId(lineDto.getOrgId())
                            .qty(lineDto.getQty())
                            .status(item.getStatusName())
                            .valueStatus(item.getOrderStatus())
                            .description(lineDto.getDescription())
                            .created(DateHelper.castInstantToDateAndMonth(item.getCreated()))
                            .productDto(ProductPosOrderLineVAllDto.builder()
                                    .id(lineDto.getProduct().getId())
                                    .name(lineDto.getProduct().getName())
                                    .code(lineDto.getProduct().getCode())
                                    .imageUrl(lineDto.getImageUrl() == null ? "" : lineDto.getImageUrl())
                                    .build())
                            .salesPrice(lineDto.getSaleprice())
                            .taxId(lineDto.getTaxId())
                            .taxRate(lineDto.getTaxRate())
                            .taxName(lineDto.getTaxName())
                            .timeWaiting(Duration.between(item.getOrderTime(), DateHelper.toInstantNowUTC()).toMinutes() + "")
                            .id(lineDto.getId())
                            .build();
                    List<RqOrderlineDetail> lineDetail = rqOrderlineDetailRepository.getAllByRequestOrderLineId(lineDto.getId());
                    if (!lineDetail.isEmpty()) {
                        List<PosOrderLineDetailDto> listDetail = new ArrayList<>();
                        lineDetail.stream().forEach(itemDetail -> {
                            PosOrderLineDetailDto itemDetailDto = PosOrderLineDetailDto.builder()
                                    .id(itemDetail.getId())
                                    .posOrderLineId(itemDto.getId())
                                    .qty(itemDetail.getQty())
                                    .salesPrice(itemDetail.getSaleprice())
                                    .description(itemDetail.getDescription())
                                    .isActive(itemDetail.getIsActive())
                                    .productId(itemDetail.getProductId())
                                    .productName(getProductName(itemDetail.getProductId()))
                                    .build();
                            listDetail.add(itemDetailDto);
                        });
                        itemDto.setLineDetails(listDetail);
                    }
                    finalListPosOrderLines.add(itemDto);
                });
            });


        }
        return GlobalReponse.builder()
                .message(message.toString())
                .data(listPosOrderLines)
                .status(HttpStatus.OK.value())
                .errors("").build();
    }

    private String getProductName(Integer productId) {
        String sql = "SELECT name FROM d_product WHERE d_product_id = :productId";

        try {
            Object result = entityManager
                    .createNativeQuery(sql)
                    .setParameter("productId", productId)
                    .getSingleResult();

            return result != null ? result.toString() : "";
        } catch (Exception e) {
            return ""; // Không có kết quả → trả về chuỗi rỗng
        }
    }

    /**
     * @param request
     * @return
     */
    @Override
    public GlobalReponse sendNotify(SendNotifycationRq request) {
        log.info("RequestOrderServiceImpl::sendNotify");
        StringBuilder messgaseResponse = new StringBuilder("");
        String NOTIFICATION_TITLE = "";
        String NOTIFICATION_BODY = "";
        String MSG_SPEAK = "";
        String Router = "NOTIFI_E_REQUEST";
        List<String> deviceToken = new ArrayList<>();
        Table table;
        Floor floor;
        switch (request.getNotifyType()) {
            case "RQ_PAYMENT":
                PosOrderDto lineCall = modelMapper.map(posOrderService.findById(request.getPosOrderId()).getData(), PosOrderDto.class);
                table = tableRepository.findById(lineCall.getTableId()).get();
                floor = floorRepository.findById(table.getFloorId()).get();
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_RQ_PAYMENT_NOTIFY_TITLE);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_RQ_PAYMENT_NOTIFY_BODY);
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@table_name@", table.getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@floor_name@", floor.getName());
                deviceToken = this.getListDeviceToken(lineCall.getOrgId(), lineCall.getPosTerminalId());
                messgaseResponse = new StringBuilder(messageSource.getMessage("rq.notify.payment",null,LocaleContextHolder.getLocale()));
                break;
            case "RQ_STAFF":
                table = tableRepository.findById(request.getTableId()).get();
                floor = floorRepository.findById(request.getFloorId()).get();
                NOTIFICATION_TITLE = getMessage(AppConstant.notify_RQ_STAFF_TITLE);
                NOTIFICATION_BODY = getMessage(AppConstant.notify_RQ_STAFF_BODY);
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@table_name@", table.getName());
                NOTIFICATION_BODY = NOTIFICATION_BODY.replace("@floor_name@", floor.getName());
                deviceToken = this.getListDeviceToken(request.getOrgId(), request.getPosTerminalId());
                messgaseResponse = new StringBuilder(messageSource.getMessage("rq.notify.staff",null,LocaleContextHolder.getLocale()));
                break;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!deviceToken.isEmpty()) {
            for (String item : deviceToken) {
                SendNotification send = SendNotification.builder()
                        .title(NOTIFICATION_TITLE)
                        .body(NOTIFICATION_BODY)
                        .code(null)
                        .deviceToken(item)
                        .router(Router)
                        .type("ANN")
                        .build();
                HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
                GlobalReponse exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);
            }
        }

        return GlobalReponse.builder()
                .message(messgaseResponse.toString())
                .data(null)
                .errors("")
                .status(HttpStatus.OK.value())
                .build();
    }


    public String getRef(String value) {
        List<Tuple> sql = entityManager.createNativeQuery("select drl.name,drl.value from d_reference dr left join d_reference_list drl on dr.d_reference_id = drl.d_reference_id\n" +
                        "        where dr.name = 'Request Order Status' and drl.value = :valueStatus", Tuple.class)
                .setParameter("valueStatus", value)
                .getResultList();
        if (sql != null || !sql.isEmpty()) {
            return sql.get(0).get("name").toString();
        }
        return "";
    }

    public String getMessage(String value) {
        String sql = "SELECT msg_text FROM pos.d_message WHERE value = :value";

        Map<String, Object> result = (Map<String, Object>) entityManager.createNativeQuery(sql)
                .setParameter("value", value)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getSingleResult();

        return ParseHelper.STRING.parse(result.get("msg_text"));

    }

    public List<String> getListDeviceToken(Integer orgId, Integer posTerminalId) {
        String checkPosMng = "N";
        List<String> result = new ArrayList<>();

        List<Tuple> rsCheck = entityManager.createNativeQuery("select * from d_org where d_org_id = :orgId ", Tuple.class)
                .setParameter("orgId", orgId)
                .getResultList();
        for (Tuple row : rsCheck) {
            if (row.get("is_pos_mng", String.class).equals("Y"))
                checkPosMng = "Y";
        }

        if (checkPosMng.equals("Y")) {

            String sql = "select du.device_token as device_token from d_user du join   d_posterminal_org_access_v dpoa  on dpoa.d_user_id = du.d_user_id \n" +
                    "                       where dpoa.d_pos_terminal_id = :posterminalId and du.device_token is not null ";
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("posterminalId", posTerminalId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            for (Map<String, Object> row : results) {
                result.add(ParseHelper.STRING.parse(row.get("device_token")));
            }
        } else {
            String sql = "select du.device_token from d_user du join d_userorg_access doa on du.d_user_id = doa.d_user_id\n" +
                    "       where du.device_token is not null and doa.d_org_id = :orgId";
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("orgId", orgId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            for (Map<String, Object> row : results) {
                result.add(ParseHelper.STRING.parse(row.get("device_token")));
            }
        }

        return result;
    }


    //Create customer if not exist by phone
    public Integer createCustomer(String name, String phone) {

        if (phone != null && !phone.equals("")) {
            String sql = "select d_customer_id from pos.d_customer where d_tenant_id  = :tenantId " +
                    " and  phone1 = :phone ";
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("phone", phone)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            if (results == null || results.isEmpty()) {
                HttpHeaders headersMain = new HttpHeaders();
                headersMain.setContentType(MediaType.APPLICATION_JSON);
                headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

                HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_CUSTOMER_BY_PHONE1
                        + "/" + phone + "/" + name, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                if (exRes.getData() != null) {
                    CustomerDto cus = modelMapper.map(exRes.getData(), CustomerDto.class);
                    return cus.getId();
                }
            } else {
                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    return ParseHelper.INT.parse(row.get("d_customer_id"));
                }
                return null;
            }
        } else {
            String sql = "select d_customer_id from pos.d_customer where d_tenant_id  = :tenantId " +
                    " and  code = 'KHACHLE' ";
            List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
            if (results != null && !results.isEmpty()) {
                for (Map<String, Object> row : results) {
                    log.info("Row: {}", row);
                    return ParseHelper.INT.parse(row.get("d_customer_id"));
                }
            } else {
                HttpHeaders headersMain = new HttpHeaders();
                headersMain.setContentType(MediaType.APPLICATION_JSON);
                headersMain.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
                HttpEntity<String> entityHeader = new HttpEntity<>(headersMain);
                GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.GET_CUSTOMER_BY_PHONE1
                                + "/" + phone + "/" + name,
                        HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();
                if (exRes.getData() != null) {
                    CustomerDto cus = modelMapper.map(exRes.getData(), CustomerDto.class);
                    return cus.getId();
                }
            }
            return null;
        }
        return null;
    }


    //Get current pos order id
    public Integer getCurrentPosOrderId(String phone, Integer tableId, Integer floorId) {

        String sql = "select d_pos_order_id from pos.d_pos_order where d_tenant_id  = :tenantId " +
                " and  d_table_id = :tableId and d_floor_id = :floorId and (phone = :phone  or phone = '' or phone is null) and order_status = 'DRA' ";
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("tableId", tableId)
                .setParameter("floorId", floorId)
                .setParameter("phone", phone)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return ParseHelper.INT.parse(row.get("d_pos_order_id"));
            }

        }
        return null;

    }

    public String isPosManagement(Integer orgId) {

        String sql = "select is_pos_mng from pos.d_org where d_tenant_id  = :tenantId " +
                " and  d_org_id = :orgId ";
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", orgId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return ParseHelper.STRING.parse(row.get("is_pos_mng"));
            }

        }
        return "N";
    }

    public Integer getPriceListId(Integer orgId, Integer posTerminalId) {

        String sql = null;
        List<Map<String, Object>> results = null;
        if (isPosManagement(orgId).equals("Y")) {
            sql = "select d_pricelist_id from pos.d_org where d_tenant_id  = :tenantId " +
                    " and  d_org_id = :orgId ";
            results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orgId", orgId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

        } else {
            sql = "select d_pricelist_id from pos.d_pos_terminal where d_tenant_id  = :tenantId " +
                    " and  d_pos_terminal_id = :posTerminalId";
            results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posTerminalId", posTerminalId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
        }

        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return ParseHelper.INT.parse(row.get("d_pricelist_id"));
            }

        }
        return null;
    }

    public Integer getCurrentShiftControlId(Integer orgId, Integer posTerminalId) {

        String sql = null;
        List<Map<String, Object>> results = null;
        if (isPosManagement(orgId).equals("Y")) {
            sql = "select d_shift_control_id from pos.d_shift_control where d_tenant_id  = :tenantId " +
                    " and  d_org_id = :orgId order by sequence_no desc limit 1";
            results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("orgId", orgId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

        } else {
            sql = "select d_shift_control_id from pos.d_shift_control where d_tenant_id  = :tenantId " +
                    " and  d_pos_terminal_id = :posTerminalId order by sequence_no desc limit 1";
            results = entityManager.createNativeQuery(sql)
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .setParameter("posTerminalId", posTerminalId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();
        }

        if (results != null && !results.isEmpty()) {
            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);
                return ParseHelper.INT.parse(row.get("d_shift_control_id"));
            }

        }
        return null;
    }


    public GlobalReponse createPosOrder(RequestOrder order, List<RequestOrderLine> listRqOrderLine) {
        PosOrderDto posOrderDto = null;
        Integer posOrderId = getCurrentPosOrderId(order.getCustomerPhone(), order.getTableId(), order.getFloorId());
        if (posOrderId != null) {
            posOrderDto = PosOrderDto.builder()
                    .id(posOrderId)
                    .build();
        } else {
            posOrderDto = PosOrderDto.builder()
                    .orgId(order.getOrgId())
                    .posTerminalId(order.getPosTerminalId())
                    .isActive("Y")
                    .customerId(order.getCustomerId())
                    .userId(AuditContext.getAuditInfo().getUserId())
                    .phone(order.getCustomerPhone())
                    .customerName(order.getCustomerName())
                    .shiftControlId(getCurrentShiftControlId(order.getOrgId(), order.getPosTerminalId()))
                    .source("POS")
                    .tableId(order.getTableId())
                    .floorId(order.getFloorId())
                    .orderStatus("DRA")
                    .build();
        }

        if (listRqOrderLine != null && !listRqOrderLine.isEmpty()) {
            List<PosOrderLineVAllDto> posOrderLineDtos = listRqOrderLine.stream().map(
                    line -> {
                        PosOrderLineVAllDto posOrderLineDto = PosOrderLineVAllDto.builder()
                                .productId(line.getProductId())
                                .qty(line.getQty())
                                .salesPrice(line.getSaleprice())
                                .qty(line.getQty())
                                .taxId(line.getTaxId())
                                .description(line.getDescription())
                                .orgId(line.getOrgId())
                                .requestOrderLineId(line.getId())
                                .build();
                        List<RqOrderlineDetail> lineDetail = this.rqOrderlineDetailRepository.getAllByRequestOrderLineId(line.getId());
                        if(!lineDetail.isEmpty())
                        {
                            List<PosOrderLineDetailDto> listLinePos = new ArrayList<>();
                            lineDetail.stream().forEach(i ->{
                                PosOrderLineDetailDto posLineDetail = PosOrderLineDetailDto.builder()
                                        .productId(i.getProductId())
                                        .qty(i.getQty())
                                        .salesPrice(i.getSaleprice())
                                        .isActive("Y")
                                        .taxId(i.getTaxId())
                                        .orgId(i.getOrgId()).build();
                                listLinePos.add(posLineDetail);
                            });
                            posOrderLineDto.setLineDetails(listLinePos);
                        }
                        return posOrderLineDto;
                    }).collect(Collectors.toList());
            posOrderDto.setPosOrderLines(posOrderLineDtos);
        }

        if (posOrderDto != null) {
            return posOrderService.save(posOrderDto);
        }
        return null;

    }


    public GlobalReponse createKitchenOrder(PosOrderDto posOrderDto) {


        KitchenOrderDto kitchenOrderDto = KitchenOrderDto.builder()
                .orgId(posOrderDto.getOrgId())
                .posOrderId(posOrderDto.getId())
                .posTerminalId(posOrderDto.getPosTerminalId())
                .isActive("Y")
                .orderStatus("WTP")
                .tableId(posOrderDto.getTableId())
                .floorId(posOrderDto.getFloorId())
                .userId(AuditContext.getAuditInfo().getUserId())
                .build();
        List<KitchenOrderlineDto> listKitchenOrderline = posOrderDto.getPosOrderLines().stream().map(
                line -> {
                    KitchenOrderlineDto kitchenOrderlineDto = KitchenOrderlineDto.builder()
                            .orderlineStatus("WTP")
                            .orgId(line.getOrgId())
                            .productId(line.getProductId())
                            .qty(line.getQty())
                            .posOrderLineId(line.getId())
                            .description(line.getDescription())
                            .build();
                    return kitchenOrderlineDto;
                }).collect(Collectors.toList());
        kitchenOrderDto.setListKitchenOrderline(listKitchenOrderline);
        return kitchenOrderService.save(kitchenOrderDto);
    }


}
