package com.dbiz.app.orderservice.service.impl;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.PurchaseOrder;
import com.dbiz.app.orderservice.domain.PurchaseOrderLine;
import com.dbiz.app.orderservice.domain.ReturnOrder;
import com.dbiz.app.orderservice.domain.view.POHeaderV;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.orderDto.PosLotDto;
import org.common.dbiz.dto.orderDto.ReturnOrderDto;
import org.common.dbiz.dto.productDto.TaxDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.helper.DateHelper;
import com.dbiz.app.orderservice.repository.PODetailVRepository;
import com.dbiz.app.orderservice.repository.POHeaderVRepository;
import com.dbiz.app.orderservice.repository.PurchaseOrderLineRepository;
import com.dbiz.app.orderservice.repository.PurchaseOrderRepository;
import com.dbiz.app.orderservice.service.PurchaseOrderService;
import com.dbiz.app.orderservice.domain.view.PODetailV;
import com.dbiz.app.orderservice.specification.PODetailVSpecification;
import com.dbiz.app.orderservice.specification.POHeaderVSpecification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PurchaseOrderDto;
import org.common.dbiz.dto.orderDto.dtoView.PODetailVDto;
import org.common.dbiz.dto.orderDto.dtoView.POHeaderVDto;
import org.common.dbiz.dto.orderDto.response.ProductDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DocHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.PODetailVRequest;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final ModelMapper modelMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderLineRepository purchaseOrderLineRepository;
    private final EntityManager entityManager;
    private  final RestTemplate restTemplate;
    private final RequestParamsUtils requestParamsUtils;
    private final POHeaderVRepository poHeaderVRepository;
    private final PODetailVRepository poDetailVRepository;
    private final MessageSource messageSource;


    @Override
    public GlobalReponsePagination findAll(POHeaderVRequest request) {
        log.info("*** PosOrder List, service; fetch all PosOrder *");

        Pageable pageable = requestParamsUtils.getPageRequest(request);
//        StringBuilder sql = new StringBuilder("select * from pos.d_pos_orderline where d_pos_order_id = ?1");


        Specification<POHeaderV> spec = POHeaderVSpecification.getSpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<POHeaderV> entityList = poHeaderVRepository.findAll( spec,pageable);
        List<POHeaderVDto > listData = new ArrayList<>();
        if(entityList != null && entityList.hasContent()) {
            entityList.getContent().forEach(entity -> {
                POHeaderVDto dto = modelMapper.map(entity, POHeaderVDto.class);
                dto.setOrderDate(DateHelper.fromInstantUTC(entity.getOrderDate()));
                listData.add(dto);
            });
        }
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse findById(Integer id) {

        log.info("*** PO, service; fetch PO by id *");

        GlobalReponse response = new GlobalReponse();
        POHeaderV po = this.poHeaderVRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("PO with id: %d not found", id)));

        POHeaderVDto dto = modelMapper.map(po, POHeaderVDto.class);
        response.setStatus(HttpStatus.OK.value());
        response.setData(dto);
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponse save(PurchaseOrderDto entityDto) {

        log.info("*** Purchase Order, service; save Purchase Order ***");
        GlobalReponse response = new GlobalReponse();


        PurchaseOrder entitySave;
        if(entityDto.getId() != null) {
            entitySave = this.purchaseOrderRepository.findById(entityDto.getId()).orElseThrow(()-> new ObjectNotFoundException("Purchase Order not found"));
            if(entitySave.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
                throw new PosException( messageSource.getMessage("po.not.delete", null, LocaleContextHolder.getLocale()));
            }

            if(entitySave.getOrderStatus().equals(AppConstant.DOC_STATUS_DRAFT)){
                modelMapper.map(entityDto, entitySave);
            }else {
                entitySave.setOrderStatus(AppConstant.DOC_STATUS_VOID);
            }

            entitySave  = purchaseOrderRepository.save(entitySave);
            response.setStatus(HttpStatus.OK.value());
        }else{
            entitySave = modelMapper.map(entityDto, PurchaseOrder.class);
            entitySave.setTenantId(AuditContext.getAuditInfo().getTenantId());

            Integer doctypeId = setDoctypePurchaseOrder();
            entitySave.setDoctypeId(doctypeId);

            if (entityDto.getDocumentNo() == null){
                do{
                    Integer maxId = purchaseOrderRepository.getMaxId() + 1;
                    String docNo = DocHelper.generateDocNo( "PO",maxId) ;
                    entitySave.setDocumentNo(docNo);
                }while (checkExistedDocumentNo(entitySave.getDocumentNo()).equals("FAI"));
            }else{
                String checkExistedDocumentNo = checkExistedDocumentNo(entityDto.getDocumentNo());
                if(checkExistedDocumentNo.equals("FAI")){
                    throw new PosException(messageSource.getMessage("document.no.exists", null, LocaleContextHolder.getLocale()));
                }
            }

            if(entityDto.getOrderStatus() == null){
                entitySave.setOrderStatus(AppConstant.DOC_STATUS_DRAFT);
            }

            if(entityDto.getOrderDate() != null){
                entitySave.setOrderDate(DateHelper.toInstantUTC(entityDto.getOrderDate()));
            }else {
                entitySave.setOrderDate(DateHelper.toInstantNowUTC());
            }

            entitySave  = purchaseOrderRepository.save(entitySave);
            entityDto.setId(entitySave.getId());
            response.setStatus(HttpStatus.CREATED.value());
        }

        Integer id = entitySave.getId();
        if(entityDto.getPurchaseOrderLines() != null && !entityDto.getPurchaseOrderLines().isEmpty()) {
            List<ProductDto> listProduct = new ArrayList<>();
            ProductDto pr = new ProductDto();
            entityDto.getPurchaseOrderLines().forEach(purchaseOrderLineDto -> {

//                pr.setId(purchaseOrderLineDto.getProductId());
//                pr.setOnHand(purchaseOrderLineDto.getQty());
//                listProduct.add(pr);
                purchaseOrderLineDto.setPurchaseOrderId(id);
                PurchaseOrderLine purchaseOrderLine = modelMapper.map(purchaseOrderLineDto, PurchaseOrderLine.class);
                purchaseOrderLine.setTenantId(AuditContext.getAuditInfo().getTenantId());
                purchaseOrderLineRepository.save(purchaseOrderLine);
            });

        }

        if (entitySave.getOrderStatus() != null && entitySave.getOrderStatus()
                .equals(AppConstant.DOC_STATUS_COMPLETED)) {
            purchaseOrderRepository.flush();
            purchaseOrderLineRepository.flush();
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
            TransactionDto transactionDto = TransactionDto.builder()
                    .orgId(entitySave.getOrgId())
                    .purchaseOrderIds(List.of(entitySave.getId()))
                    .build();

            HttpEntity<TransactionDto> requestEntity2 = new HttpEntity<>(transactionDto, headers);
            GlobalReponse responseTransaction = this.restTemplate
                    .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION,
                            requestEntity2,
                            GlobalReponse.class)
                    .getBody();
            if (responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                    && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                throw new RuntimeException(responseTransaction.getMessage());
            }

            entityDto.setTotalAmount(entitySave.getTotalAmount());
            entityDto.setOrgId(entitySave.getOrgId());

           // createPayment(entitySave,entityDto);
        }

        response.setData(entityDto);
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
//        log.info("Warehouse saved successfully with ID: {}", entitySave.getId());
        return response;
    }

    public String checkExistedDocumentNo(String documentNo) {
        String sql = "SELECT count(1) FROM pos.d_return_order WHERE document_no = :documentNo";
        Query query = entityManager.createNativeQuery(sql);
        query = query.setParameter("documentNo", documentNo);

        log.info("SQL:" + sql);
        Long count = ((Number) query.getSingleResult()).longValue();
        if (count > 0) {
            return "FAI";
        }
        return "COM";
    }

    private void createPayment(PurchaseOrder entity, PurchaseOrderDto entityDto){
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
        org.common.dbiz.dto.paymentDto.PaymentDto paymentDto = org.common.dbiz.dto.paymentDto.PaymentDto.builder()
                .docType("APP")
                .paymentAmount(entity.getTotalAmount())
                .paymentMethod(entityDto.getPaymentMethod())
                .paymentStatus("COM")
                .paymentDate(entityDto.getPaymentDate())
                .bankAccountId(entityDto.getBankAccountId())
                .purchaseOrderId(entity.getId())
                .build();
        paymentDto.setOrgId(entity.getOrgId());

        paymentDto.setResponsibleUserId(entityDto.getVendorId());
        paymentDto.setUserGroup("VEN");

        HttpEntity<org.common.dbiz.dto.paymentDto.PaymentDto> requestEntity1 = new HttpEntity<>(paymentDto, headers);
        GlobalReponse responseTransaction1 = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_URL,
                        requestEntity1,
                        GlobalReponse.class)
                .getBody();

        if (responseTransaction1.getStatus().intValue() != HttpStatus.OK.value()
                && responseTransaction1.getStatus().intValue() != HttpStatus.CREATED.value()) {
            throw new RuntimeException(responseTransaction1.getMessage());
        }
    }


    @Override
    public GlobalReponse deleteById(Integer id) {
        PurchaseOrder purchaseOrder= purchaseOrderRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException(
                        messageSource.getMessage("po.not.found", null, LocaleContextHolder.getLocale())
                        )
                );

        GlobalReponse response = new GlobalReponse();

        if(purchaseOrder.getOrderStatus().equals("DRA") ){  //Draft
            purchaseOrder.setOrderStatus("VOD"); //Chuyen trang thai huy
            purchaseOrderRepository.save(purchaseOrder);
        }else {
            throw new RuntimeException(
                    messageSource.getMessage("po.not.delete", null, LocaleContextHolder.getLocale())
            );
        }
        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        return response;
    }

    @Override
    public GlobalReponsePagination findAllDetail(PODetailVRequest request) {
        log.info("*** Purchase Order Detail List, service; fetch all Purchase Order Detail *");

        Pageable pageable = requestParamsUtils.getPageRequest(request);
//        StringBuilder sql = new StringBuilder("select * from pos.d_pos_orderline where d_pos_order_id = ?1");


        Specification<PODetailV> spec = PODetailVSpecification.getSpecification(request);
        GlobalReponsePagination response = new GlobalReponsePagination();

        Page<PODetailV> entityList = poDetailVRepository.findAll( spec,pageable);
        List<PODetailVDto> listData = new ArrayList<>();
        if(entityList != null && entityList.hasContent()) {
            entityList.getContent().forEach(entity -> {
                PODetailVDto dto = modelMapper.map(entity, PODetailVDto.class);
                TaxDto tax = TaxDto.builder()
                        .id(entity.getTaxId())
                        .name(entity.getTaxName())
                        .taxRate(entity.getTaxRate())
                        .build();
                UomDto uom = UomDto.builder()
                        .code(entity.getCode())
                        .id(entity.getUomId())
                        .name(entity.getUnitOfMeasure())
                        .build();
                PosLotDto lot = PosLotDto.builder()
                        .lotId(entity.getLotId())
                        .lotCode(entity.getLotCode())
                        .expirationDate(entity.getExpiryDate())
                        .build();
                dto.setLot(lot);
                dto.setTax(tax);
                dto.setUom(uom);
                listData.add(dto);
            });
        }

        response.setMessage( messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

//    @Override
//    public GlobalReponsePagination findAllV2(POHeaderVRequest request) {
//        Parameter parameter = new Parameter();
//        return null;
//    }

    private Integer setDoctypePurchaseOrder(){
        String doctypeCode = AppConstant.DocTypeCode.PO_ORDER;


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");
        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        GlobalReponse exRes = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE + "/"
                + doctypeCode, HttpMethod.GET, entityHeader, GlobalReponse.class).getBody();

        Integer doctypeId = modelMapper.map(exRes.getData(), Integer.class);
        return doctypeId;
    }
}
