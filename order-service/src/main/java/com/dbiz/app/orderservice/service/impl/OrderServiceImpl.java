package com.dbiz.app.orderservice.service.impl;



import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.dbiz.app.orderservice.constant.AppConstant;
import com.dbiz.app.orderservice.domain.PosOrder;
import com.dbiz.app.orderservice.domain.PosOrderline;
import com.dbiz.app.orderservice.domain.ReconcileDetail;
import com.dbiz.app.orderservice.repository.*;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.helper.KafkaAuditUserHelper;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.tenantservice.service.TenantService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.orderDto.*;
import org.common.dbiz.dto.paymentDto.BaseDto;
import org.common.dbiz.dto.paymentDto.ReceiptOther.EInvoiceInfoDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.NapasErrorException;
import org.common.dbiz.exception.wrapper.NapasOrderNotFoundException;
import org.common.dbiz.exception.wrapper.NapasSignatureInvalidException;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.helper.Encoder;
import org.common.dbiz.model.QRCodeDecoded;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.OrderQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.dbiz.app.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.json.simple.JSONObject;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderLineRepository orderLineRepository;

	private final OrderRepository orderRepository;
	private final ModelMapper modelMapper;
	private final RequestParamsUtils requestParamsUtils;
	private final RestTemplate restTemplate;
	private final PosOrderRepository posOrderRepository;
	private final EntityManager entityManager;
	private final ReconcileDetailRepository reconcileDetailRepository;
	private final PosOrderSerivceImpl posOrderSerivce;
	private final PosOrderlineRepository posOrderlineRepository;
	private final PosTaxLineRepository posTaxLineRepository;
	private final static String D_MBB_CHECKSUM = "D_MBB_KeyCheckSum";
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final static String TOPIC = "sync-order";
	private final DataSourceContextHolder dataSourceContextHolder;
	private final DataSourceConfigService dataSourceConfigService;
	private final DataSourceRoutingService dataSourceRoutingService;
	private final MessageSource messageSource;
	private final TenantService tenantService;
	private final ObjectMapper objectMapper;
	private final String GROUP_ID = "payment-service";
	private final String NOTIFY_PAYMENT_TOPIC = "NOTIFY_PAYMENT_TOPIC";
	private final String ISSUE_EINVOICE_TOPIC = "ISSUE_EINVOICE_TOPIC";
	private final KafkaAuditUserHelper kafkaAuditUserHelper;
	@Value("${spring.application.name:unknown-service}")
	private String serviceName;


	@Override
	public GlobalReponsePagination  findAll(OrderQueryRequest  queryRequest) {

//		log.info("*** Order List, service; fetch all Order *");
//		OrderQueryRequest query = (OrderQueryRequest) queryRequest;
//		Pageable pageable = requestParamsUtils.getPageRequest(query);
//
//		Specification<Order> spec = OrderSpecification.getSpecification(query);
//
//		Page<Order> entityList = orderRepository.findAll(spec, pageable);
//		List<OrderDto> listData = new ArrayList<>();
//		for (Order item : entityList.getContent()) {
//			OrderDto order = modelMapper.map(item, OrderDto.class);
//			order.setOrderDate(DateHelper.fromLocalDate(item.getOrderDate()));
//			listData.add(order);
//		}
//
//		GlobalReponsePagination response = new GlobalReponsePagination();
//		response.setMessage("Order fetched successfully");
//		response.setData(listData);
//		response.setCurrentPage(entityList.getNumber());
//		response.setPageSize(entityList.getSize());
//		response.setTotalPages(entityList.getTotalPages());
//		response.setTotalItems(entityList.getTotalElements());
//
//		return response;
		return null;
	}

	@Transactional
	@Override
	public GlobalReponse save(OrderDto orderDto ) {
		log.info("*** Order, service; save Order ***");
		GlobalReponse response = new GlobalReponse();

		int posOrderId = orderDto.getPosOrderId();
		//PosOrder
		PosOrder posOrder = this.posOrderRepository.findById(posOrderId)
				.orElseThrow(() -> new ObjectNotFoundException(String.format("POS Order with id: %d not found", posOrderId)));
		posOrder.setOrderStatus(AppConstant.DOC_STATUS_COMPLETED);



		//Free Order
//		if(orderDto.getPayments() != null && orderDto.getPayments().get(0).getPaymentRule().equals(AppConstant.PaymentRule.FREE)){
//			posOrder.setTotalAmount(BigDecimal.ZERO);
//			posOrder.setFlatAmt(BigDecimal.ZERO);
//			posOrder.setTotalLine(BigDecimal.ZERO);
//
//
//			List<PosOrderline> posOrderLines = posOrderlineRepository.findByPosOrderId(posOrderId);
//			posOrderLines.forEach(orderLine -> {
//				orderLine.setSalesPrice(BigDecimal.ZERO);
//				orderLine.setGrandTotal(BigDecimal.ZERO);
//				orderLine.setLineNetAmt(BigDecimal.ZERO);
//				orderLine.setTaxAmount(BigDecimal.ZERO);
//				orderLine.setDiscountPercent(BigDecimal.ZERO);
//				orderLine.setDiscountAmount(BigDecimal.ZERO);
//				posOrderlineRepository.save(orderLine);
//			});
//		}

		posOrder = posOrderRepository.save(posOrder);
		Map<Integer,TaxDto> mapPosTax = new HashMap<>();



		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setTenantId(posOrder.getTenantId());
		invoiceDto.setOrgId(posOrder.getOrgId());
		invoiceDto.setUserId(posOrder.getUserId());
		invoiceDto.setPosOrderId(posOrder.getId());
		invoiceDto.setDateInvoiced(DateHelper.fromInstantUTC(posOrder.getOrderDate()));
		invoiceDto.setCustomerId(posOrder.getCustomerId());
		invoiceDto.setCurrencyId(posOrder.getCurrencyId());
		invoiceDto.setPriceListId(posOrder.getPricelistId());
		invoiceDto.setPosTerminalId(posOrder.getPosTerminalId());
		invoiceDto.setTotalAmount(posOrder.getTotalAmount());
		invoiceDto.setCreatedBy(posOrder.getCreatedBy());
		invoiceDto.setUpdatedBy(posOrder.getUpdatedBy());
		invoiceDto.setOrderNo(posOrder.getDocumentNo());
		invoiceDto.setPayments(orderDto.getPayments());
		if(posOrder.getIsIssueEInvoice() != null && posOrder.getIsIssueEInvoice().equals("Y")){
			invoiceDto.setBuyerName(posOrder.getBuyerName());
			invoiceDto.setBuyerTaxCode(posOrder.getBuyerTaxCode());
			invoiceDto.setBuyerEmail(posOrder.getBuyerEmail());
			invoiceDto.setBuyerAddress(posOrder.getBuyerAddress());
			invoiceDto.setBuyerPhone(posOrder.getBuyerPhone());
			invoiceDto.setBuyerCompany(posOrder.getBuyerCompany());
			invoiceDto.setBuyerCitizenId(posOrder.getBuyerCitizenId());
			invoiceDto.setBuyerPassportNumber(posOrder.getBuyerPassportNumber());
			invoiceDto.setBuyerBudgetUnitCode(posOrder.getBuyerBudgetUnitCode());
		}


		List<InvoiceLineDto> invoiceLinesDto = new ArrayList<>();

		List<PosOrderline> posOrderLines = posOrderlineRepository.findByPosOrderId(posOrderId);

		posOrderLines.forEach(orderLineDto -> {
			//InvoiceLine

			InvoiceLineDto  invoiceLineDto = new InvoiceLineDto();
			invoiceLineDto.setPosOrderLineId(orderLineDto.getId());
			invoiceLineDto.setOrgId(orderLineDto.getOrgId());
			invoiceLineDto.setQty(orderLineDto.getQty());
			invoiceLineDto.setProductId(orderLineDto.getProductId());
			invoiceLineDto.setPriceEntered(orderLineDto.getPriceDiscount());
			invoiceLineDto.setGrandTotal(orderLineDto.getGrandTotal());
			invoiceLineDto.setLineNetAmt(orderLineDto.getLineNetAmt());
//			invoiceLineDto.setTaxAmount(orderLineDto.getTaxAmount());
//			invoiceLineDto.setDiscountPercent(orderLineDto.getDiscountPercent());
//			invoiceLineDto.setDiscountAmount(orderLineDto.getDiscountAmount());
			invoiceLineDto.setTaxId(orderLineDto.getTaxId());
			invoiceLinesDto.add(invoiceLineDto);
			if(!mapPosTax.containsKey(orderLineDto.getTaxId())){
				TaxDto taxDto = new TaxDto();
				taxDto.setTaxAmount(orderLineDto.getTaxAmount());
				taxDto.setTaxBaseAmount(orderLineDto.getLineNetAmt());
				mapPosTax.put(orderLineDto.getTaxId(), taxDto);
			}else{
				TaxDto taxDto = mapPosTax.get(orderLineDto.getTaxId());
				taxDto.setTaxAmount(taxDto.getTaxAmount().add(orderLineDto.getTaxAmount()));
				taxDto.setTaxBaseAmount(taxDto.getTaxBaseAmount().add(orderLineDto.getLineNetAmt()));
				mapPosTax.put(orderLineDto.getTaxId(), taxDto);
			}
		});


		//InvoiceLine
		invoiceDto.setInvoiceLines(invoiceLinesDto);

//		for(Map.Entry<Integer, TaxDto> entry : mapPosTax.entrySet()) {
//			PosTaxLine posTaxLine = new PosTaxLine();
//			posTaxLine.setTenantId(posOrder.getTenantId());
//			posTaxLine.setOrgId(posOrder.getOrgId());
//			posTaxLine.setTaxId(entry.getKey());
//			posTaxLine.setTaxAmount(entry.getValue().getTaxAmount());
//			posTaxLine.setTaxBaseAmount(entry.getValue().getTaxBaseAmount());
//			posTaxLine.setPosOrderId(posOrderId);
//			posTaxLine.setCreatedBy(posOrder.getCreatedBy());
//			posTaxLine.setUpdatedBy(posOrder.getUpdatedBy());
//			posTaxLineRepository.save(posTaxLine);
//		}

		log.info("Start Invoice");
		// Create headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
// For example, adding an Authorization header
		headers.set("orgId",AuditContext.getAuditInfo().getOrgId().toString());
		headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
		headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
		headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
		headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());

		HttpEntity<InvoiceDto> requestEntity = new HttpEntity<>(invoiceDto, headers);
		GlobalReponse reponseInvoice = this.restTemplate
				.postForEntity(AppConstant.DiscoveredDomainsApi.INVOICE_SERVICE_API_URL ,
						requestEntity,
						GlobalReponse.class)
				.getBody();
		if(reponseInvoice.getStatus().intValue() != HttpStatus.OK.value()
				&& reponseInvoice.getStatus().intValue() != HttpStatus.CREATED.value()) {
			throw new RuntimeException(reponseInvoice.getMessage());
		}


		log.info("End Invoice");

//		orderDto = modelMapper.map(order, OrderDto.class);
//		orderDto.setId(order.getId());
//		response.setData(orderDto);

		IssueEInvoiceDto issueEInvoiceDto = null;
		EInvoiceInfoDto eInvoiceInfoDto = null;

		if(posOrder.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
			try {
				SyncOrderInfoDto syncOrderInfoDto = SyncOrderInfoDto.builder()
						.tenantId(AuditContext.getAuditInfo().getMainTenantId())
						.orgId(posOrder.getOrgId())
						.userId(posOrder.getUserId())
						.posOrderId(posOrder.getId())
						.build();
				kafkaTemplate.send(TOPIC, syncOrderInfoDto);


				if(posOrder.getIsIssueEInvoice() != null && posOrder.getIsIssueEInvoice().equals("Y")){
					BaseDto returnInvoice = modelMapper.map(reponseInvoice.getData(), BaseDto.class);
					issueEInvoiceDto = IssueEInvoiceDto.builder()
							.posOrderId(posOrder.getId())
							.invoiceId(returnInvoice.getId())
							.tenantId(AuditContext.getAuditInfo().getMainTenantId())
							.build();
//					kafkaTemplate.send(ISSUE_EINVOICE_TOPIC, issueEInvoiceDto);

					HttpEntity<IssueEInvoiceDto> requestEntity2 = new HttpEntity<>(issueEInvoiceDto, headers);
					GlobalReponse responseTransaction = this.restTemplate
							.postForEntity(AppConstant.DiscoveredDomainsApi.EINVOICE_SERVICE_API_ISSUE ,
									requestEntity2,
									GlobalReponse.class)
							.getBody();

					if(reponseInvoice != null && reponseInvoice.getData() != null){
						org.common.dbiz.dto.paymentDto.InvoiceDto invoiceDtoResponse = modelMapper.map(reponseInvoice.getData(), org.common.dbiz.dto.paymentDto.InvoiceDto.class);
						Integer id = invoiceDtoResponse.getId();
						eInvoiceInfoDto =  getEInvoice(id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		log.info("Start Transaction");
		if(posOrder.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
			// Create headers

			log.info("1.");
			TransactionDto transactionDto = TransactionDto.builder()
					.orgId(posOrder.getOrgId())
					.posOrderIds(List.of(posOrder.getId()))
					.build();
			log.info("dto: {}", transactionDto);
			log.info("2.");
			HttpEntity<TransactionDto> requestEntity2 = new HttpEntity<>(transactionDto, headers);
			log.info("headers: {}", headers);
			log.info("entity: {}", requestEntity2);
			GlobalReponse responseTransaction = this.restTemplate
					.postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION ,
							requestEntity2,
							GlobalReponse.class)
					.getBody();
			log.info("3.");
			if(responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
					&& responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
				throw new RuntimeException(responseTransaction.getMessage());
//				return responseTransaction;
			}
			log.info("4.");
		}
		log.info("End Transaction");
		org.common.dbiz.dto.paymentDto.PaymentDto paymentDto = new org.common.dbiz.dto.paymentDto.PaymentDto();
		if(eInvoiceInfoDto !=null){
			paymentDto.setEInvoice(eInvoiceInfoDto);
		}
		if(posOrder.getIsIssueEInvoice() != null && posOrder.getIsIssueEInvoice().equals("Y"))
			response.setData(paymentDto);
		else
			response.setData(null);
		response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));
		response.setStatus(HttpStatus.OK.value());
		log.info("Order saved successfully with ID: {}", posOrder.getId());

		if (posOrder.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)) {
			try { // Send Kafka save Audit for user
				Map<String, String> description = Map.of(
						"name", "Đơn hàng",
						"document_no", posOrder.getDocumentNo()
				);
				String descriptionJson = objectMapper.writeValueAsString(description);

				kafkaAuditUserHelper.sendKafkaSaveAuditUser(
						AuditContext.getAuditInfo().getMainTenantId(),
						AuditContext.getAuditInfo().getOrgId(),
						serviceName,
						DbMetadataHelper.getTableName(posOrder),
						posOrderId,
						"INSERT",
						AuditContext.getAuditInfo().getUserId(),
						descriptionJson);
			} catch (Exception e) {
				String errMes = "Error: OrderServiceImpl: save(): send Kafka save AuditLogUser";
				log.error("{}: {}", errMes, e.getMessage());
				throw new PosException(errMes); // thêm messageSource
			}
		}

		return response;
	}

	public class TaxDto {
		private BigDecimal taxAmount;
		private BigDecimal taxBaseAmount;

		public BigDecimal getTaxBaseAmount() {
			return taxBaseAmount;
		}

		public void setTaxBaseAmount(BigDecimal taxBaseAmount) {
			this.taxBaseAmount = taxBaseAmount;
		}

		public BigDecimal getTaxAmount() {
			return taxAmount;
		}

		public void setTaxAmount(BigDecimal taxAmount) {
			this.taxAmount = taxAmount;
		}
	}

	public EInvoiceInfoDto getEInvoice(Integer invoiceId){
		String sql = "SELECT invoice_form, invoice_sign, invoice_no, " +
				"search_code, search_link, einvoice_supplier_taxcode, einvoice_taxcode " +
				"FROM d_invoice where d_invoice_id = :invoiceId";

		List<Map<String, Object>> list = entityManager.createNativeQuery(sql)
				.setParameter("invoiceId", invoiceId)
				.unwrap(NativeQuery.class)
				.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
				.getResultList();

		EInvoiceInfoDto eInvoiceInfoDto = null;
		if(list != null && list.size() > 0){
			Map<String, Object> row = list.get(0);
			if(row != null){
				eInvoiceInfoDto = EInvoiceInfoDto.builder()
						.invoiceForm(ParseHelper.STRING.parse(row.get("invoice_form")))
						.invoiceSign(ParseHelper.STRING.parse(row.get("invoice_sign")))
						.invoiceNo(ParseHelper.STRING.parse(row.get("invoice_no")))
						.searchCode(ParseHelper.STRING.parse(row.get("search_code")))
						.searchLink(ParseHelper.STRING.parse(row.get("search_link")))
						.supplierTaxCode(ParseHelper.STRING.parse(row.get("einvoice_supplier_taxcode")))
						.taxOfCode(ParseHelper.STRING.parse(row.get("einvoice_taxcode")))
						.build();
			}
		}
		return eInvoiceInfoDto;
	}

	public String getValueByName (String name){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

		HttpEntity<String> entityHeader = new HttpEntity<>(headers);
		GlobalReponse  serviceDto  = restTemplate.exchange(
				AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_API_VALUE_BY_NAME_URL+"/" + name,
				HttpMethod.GET,entityHeader,
				GlobalReponse.class).getBody();
		return (String) serviceDto.getData();
	}

	@Override
	public String updateOrderMBB(Object Dto){

		UpdateOrderMBBDto updateOrderMBBDto = (UpdateOrderMBBDto) Dto;
		log.info("Update Order MBB");
		GlobalReponse response = new GlobalReponse();
		JSONObject qrcode = new JSONObject();
		String qrCode = null;
		String eCode = null;
		int myID = 0;
		String traceTransfer = null;
		String storeLabel = null;
		String terminalLabel = null;
		String debitAmount = null;
		String realAmount = null;
		String payDate = null;
		String checkSum = null;
		String rate = null;
		String billNumber = null;
		String consumerLabelTerm = null;
		String referenceLabelCode = null;
		List<String> referenceLabel = null;
		String userName = null;
		String ftCode = null;
		String endPointUrl = null;
		String m_ResCode = "";
		String m_ResDesc = "";
		List<MBBAddData > additionalData = null;

		try {
			traceTransfer = updateOrderMBBDto.getTraceTransfer();
		} catch (Exception e) {
			traceTransfer = "";
		}
		try {
			storeLabel = updateOrderMBBDto.getStoreLabel();
		} catch (Exception e) {
			storeLabel = "";
		}
		try {
			terminalLabel = updateOrderMBBDto.getTerminalLabel();
		} catch (Exception e) {
			terminalLabel = "";
		}
		try {
			debitAmount = updateOrderMBBDto.getDebitAmount();
		} catch (Exception e) {
			debitAmount = "";
		}
		try {
			realAmount = updateOrderMBBDto.getRealAmount();
		} catch (Exception e) {
			realAmount = "";
		}
		try {
			payDate = updateOrderMBBDto.getPayDate();
		} catch (Exception e) {
			payDate = "";
		}
		try {
			m_ResCode = updateOrderMBBDto.getRespCode();
		} catch (Exception e) {
			m_ResCode = "";
		}
		try {
			m_ResDesc = updateOrderMBBDto.getRespDesc();
		} catch (Exception e) {
			m_ResDesc = "";
		}
		try {
			checkSum = updateOrderMBBDto.getCheckSum();
		} catch (Exception e) {
			checkSum = "";
		}
		try {
			rate = updateOrderMBBDto.getRate();
		} catch (Exception e) {
			rate = "";
		}
		try {
			billNumber = updateOrderMBBDto.getBillNumber();
		} catch (Exception e) {
			billNumber = "";
		}
		try {
			consumerLabelTerm = updateOrderMBBDto.getConsumerLabelTerm();
		} catch (Exception e) {
			consumerLabelTerm = "";
		}
		try {
			referenceLabelCode = updateOrderMBBDto.getReferenceLabelCode();
		} catch (Exception e) {
			referenceLabelCode = "";
		}

		try {
			userName = updateOrderMBBDto.getUserName();
		} catch (Exception e) {
			userName = "";
		}
		try {
			ftCode = updateOrderMBBDto.getFtCode();
		} catch (Exception e) {
			ftCode = "";
		}
		try {
			endPointUrl = updateOrderMBBDto.getEndPointUrl();
		} catch (Exception e) {
			endPointUrl = "";
		}
		try {
			additionalData = updateOrderMBBDto.getAdditionalData();
		} catch (Exception e) {
			MBBAddData mydata = new MBBAddData();
			mydata.setValue("");
			mydata.setName("");
			additionalData.add(mydata);

		}

		log.info("traceTransfer: " + traceTransfer);
		log.info("storeLabel: " + storeLabel);
		log.info("terminalLabel: " + terminalLabel);
		log.info("debitAmount: " + debitAmount);
		log.info("m_ResCode: " + m_ResCode);
		log.info("m_ResDesc: " + m_ResDesc);
		log.info("payDate: " + payDate);
		log.info("checkSum: " + checkSum);
		log.info("rate: " + rate);
		log.info("billNumber: " + billNumber);
		log.info("userName: " + userName);
		log.info("consumerLabelTerm: " + consumerLabelTerm);
		log.info("referenceLabelCode: " + referenceLabelCode);
		log.info("ftCode: " + ftCode);
		log.info("endPointUrl: " + endPointUrl);

		String acccessKey = getValueByName(D_MBB_CHECKSUM);
//		String acccessKey = "SsgAccesskey";
		log.info("acccessKey: " + acccessKey);

		if(updateOrderMBBDto.getIsSkipCheckSum().equals("N")){
			if (!(acccessKey == null || acccessKey.length() == 0)) {
				String mychecksum = traceTransfer + billNumber + payDate + debitAmount + acccessKey;
				if (!getMD5(mychecksum).equals(checkSum)) {
					log.info("Responsecode 12");

					// return for ws json
					qrcode.put("resCode", "12");
					qrcode.put("resDesc", "False checksum");
					return qrcode.toString();
				}
			}
//			} else {
//				log.info("Responsecode 07 ");
//				qrcode.put("resCode", "07");
//				qrcode.put("resDesc", "Merchant is not exist");
//				return qrcode.toString();
//			}

		}
		if (m_ResCode.equals("00")) {

			//Decode reposone for multi tenant
			QRCodeDecoded qrCodeMBDecoded;
			try{
				qrCodeMBDecoded = Encoder.decode(referenceLabelCode);
				if (qrCodeMBDecoded == null) {
					throw new RuntimeException("QRCodeMBDecoded is null");
				}
			}catch (Exception e){
				log.info("Responsecode 12 ");
				qrcode.put("resCode", "12");
				qrcode.put("resDesc", "False checksum");
				return qrcode.toString();
			}

			log.info("Create QRCode MBB");
			log.info("tenantId: " + qrCodeMBDecoded.getTenantId());
			log.info("pos_order_id: " + qrCodeMBDecoded.getPosOrderId());
			log.info("timestamp: " + qrCodeMBDecoded.getTimestamp());
			log.info("dbiz timestamp: " +  DateHelper.generateTimeId());

			dataSourceContextHolder.setCurrentTenantId(new Long(qrCodeMBDecoded.getTenantId().toString()));

			AuditContext.setAuditInfo(new AuditInfo(0, 0,"0",
					"0",0,"vi",qrCodeMBDecoded.getTenantId()));

			int posOrderId = qrCodeMBDecoded.getPosOrderId();
			//PosOrder
			Optional<PosOrder> posOrderOP = this.posOrderRepository.findById(posOrderId);

			if (!posOrderOP.isPresent()) {
				log.info("Responsecode 02 for posOrderId: " + posOrderId);
				qrcode.put("resCode", "02");
				qrcode.put("resDesc", "Invalid order");
				return qrcode.toString();
			}


			PosOrder posOrder = posOrderOP.get();
			log.info("posOrder no: " + posOrder.getDocumentNo());
			if(posOrder.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
				log.info("Completed order");
				qrcode.put("resCode", "00");
				qrcode.put("resDesc", "Success");
				log.info("order ftcode : " + posOrder.getFtCode());
				log.info("ftcode : " + ftCode);
				if(posOrder.getFtCode() == null) {
					posOrder.setFtCode("123");
				}
				if(!posOrder.getFtCode().equals(ftCode)){
					sendNotification(new BigDecimal(debitAmount),posOrder.getDeviceToken(),posOrder.getDocumentNo());
				}
				return qrcode.toString();
			}

			log.info("Not Completed Order");
			AuditContext.getAuditInfo().setTenantId(posOrder.getTenantId());

			ReconcileDetail reconcileDetail = new ReconcileDetail();
			reconcileDetail.setTenantId(posOrder.getTenantId());
			reconcileDetail.setOrgId(posOrder.getOrgId());
			reconcileDetail.setPaymentStatus("COM");
			// missing CusPaymentMethod
			reconcileDetail.setBankId(posOrder.getBankId());
			reconcileDetail.setBankAccountId(posOrder.getBankAccountId());
			reconcileDetail.setPosOrderId(posOrderId);
			reconcileDetail.setCustomerId(posOrder.getCustomerId());
			reconcileDetail.setQrcodePayment(posOrder.getQrcodePayment());
			reconcileDetail.setReferenceCode(referenceLabelCode);
			reconcileDetail.setPaymentAmount(new BigDecimal(debitAmount));
			reconcileDetail.setTransactionNo(traceTransfer);
			reconcileDetail.setErrorCode(m_ResCode);
			reconcileDetail.setMerchantCode(consumerLabelTerm);
			reconcileDetail.setCheckSum(checkSum);
			reconcileDetail.setFtCode(ftCode);
			reconcileDetail.setTerminalId(terminalLabel);
			reconcileDetail = reconcileDetailRepository.save(reconcileDetail);


			if(reconcileDetail.getId() != null) {
				posOrder.setFtCode(ftCode);
				posOrder.setBillNo(posOrder.getDocumentNo());
				posOrder.setReconcileDetailId(reconcileDetail.getId());
				posOrderRepository.save(posOrder);

				OrderDto orderDto = new OrderDto();
				orderDto.setPosOrderId(posOrderId);
				orderDto.setOrgId(posOrder.getOrgId());
//				BigDecimal qrcodeAmount = getQRCodeNapasAmount(posOrder.getId());
				BigDecimal qrcodeAmount = new BigDecimal(debitAmount);
				GlobalReponse reponse =  save(orderDto);
				if (reponse.getStatus() == HttpStatus.OK.value() || reponse.getStatus() == HttpStatus.CREATED.value()) {
					qrcode.put("resCode", "00");
					qrcode.put("resDesc", "Success");
					sendNotification(qrcodeAmount,posOrder.getDeviceToken(),posOrder.getDocumentNo());
					return qrcode.toString();
				}
				qrcode.put("resCode", "02");
				qrcode.put("resDesc", "Invalid order");
				return qrcode.toString();
			}
		}else{
			qrcode.put("resCode", m_ResCode);
			qrcode.put("resDesc", m_ResDesc);
			return qrcode.toString();
		}

		qrcode.put("resCode", "02");
		qrcode.put("resDesc", "Invalid order");
		return qrcode.toString();
	}

	@Override
	public NapasResponseDto updateNapasOrder(PayloadNapasDto req) {

		log.info("Update Order NAPAS");

		NotificationDto dto = null;
		try {
			dto = objectMapper.convertValue(req.getPayload(), NotificationDto.class);
			log.info("payload: " + objectMapper.writeValueAsString(req));
			changeTenant(dto.getId());
		} catch (Exception e) {
			log.error("Error while changing tenant: {}", e.getMessage());
			throw new NapasErrorException("Invalid order");
		}

		verifySignature(req);

		// GET 7 last characters
		String last7 = dto.getId().substring(dto.getId().length() - 7);
		Integer npOrderId = Integer.parseInt(last7);
//		Optional<PosOrder> posOrderOP = this.posOrderRepository.findById(posOrderId);
		Optional<PosOrder> posOrderOP = this.posOrderRepository.findByNpOrderId(npOrderId);

		if (!posOrderOP.isPresent()) {
			throw new NapasOrderNotFoundException( "Order not found");
		}

		//Create Napas Transaction
		Integer napasTransactionId = processNapasTransaction(dto);

		PosOrder posOrder = posOrderOP.get();

		if(posOrder.getOrderStatus().equals(AppConstant.DOC_STATUS_COMPLETED)){
			log.info("Completed order");
			sendNotification(dto.getAmount(),posOrder.getDeviceToken(),posOrder.getDocumentNo());
			return getNapasResponseDto("success", "Message is successfully");
		}

		BigDecimal qrcodeAmount = getQRCodeNapasAmount(posOrder.getId());
		if(dto.getAmount().compareTo(qrcodeAmount) != 0){
			throw new NapasOrderNotFoundException( "Order not found");
		}

		posOrder.setBillNo(posOrder.getDocumentNo());
		posOrder.setNpTransactionId(napasTransactionId);
		posOrderRepository.save(posOrder);

		OrderDto orderDto = new OrderDto();
		orderDto.setPosOrderId(posOrder.getId());
		orderDto.setOrgId(posOrder.getOrgId());

		try {
			GlobalReponse response =  save(orderDto);
			if (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value()) {
				sendNotification(qrcodeAmount,posOrder.getDeviceToken(),posOrder.getDocumentNo());
				return getNapasResponseDto("success", "Message is successfully");
			}
		}catch (Exception e){
			log.error("Error while saving order: {}", e.getMessage());
		}
		throw new NapasErrorException("Invalid order");
	}

	public int getTenantNumbers(){
		return dataSourceConfigService.getTenantNumbersRedis();
	}


	public void sendNotification(BigDecimal qrcodeAmount,String deviceToken,String documentNo){
		PaymentNotficationKafkaDto payment = PaymentNotficationKafkaDto.builder()
				.tenantId(AuditContext.getAuditInfo().getMainTenantId())
				.amount(qrcodeAmount)
				.deviceToken(deviceToken)
				.code(documentNo).build();
		kafkaTemplate.send(NOTIFY_PAYMENT_TOPIC, payment);
	}

	@Override
	public GlobalReponse deleteById(Integer id) {
		return null;
	}

	@Override
	public GlobalReponse findById(Integer id) {
		return null;
	}

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private static final String OUTPUT_FORMAT = "%-20s:%s";

	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static String getMD5(String p_text) {
		byte[] md5InBytes = digest(p_text.getBytes(UTF_8));
		System.out.println(String.format(OUTPUT_FORMAT, "MD5 (hex) ", bytesToHex(md5InBytes)));
		return bytesToHex(md5InBytes);
	}

	private static byte[] digest(byte[] input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		byte[] result = md.digest(input);
		return result;
	}


	public void changeTenant(String code){
		String newCode = code.substring(4,7);
		tenantService.changeTenantByMerchantCode(newCode);
	}

	public NapasResponseDto getNapasResponseDto(String code, String message){
		NapasResponseDto napasResponseDto = new NapasResponseDto();
		napasResponseDto.setCode(code);
		napasResponseDto.setMessage(message);
		return napasResponseDto;
	}

	public Integer processNapasTransaction(NotificationDto paramDto){


		HttpHeaders headers = new HttpHeaders();
		headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
		headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
		headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<NotificationDto> requestImage = new HttpEntity<>(paramDto, headers);
		GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_CREATE_NAPAS_TRX_RO, requestImage, GlobalReponse.class);

		if(response.getStatus().intValue() != HttpStatus.OK.value()){
			return null;
		}
		return (Integer) response.getData();
	}


	public void verifySignature(PayloadNapasDto dto) {

		log.info("verifySignature");
		// Create headers
		HttpHeaders headers = new HttpHeaders();
		// Add headers as needed
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
		headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());


		// Create an HttpEntity with the headers and the request body
		HttpEntity<PayloadNapasDto> requestEntity = new HttpEntity<>(dto, headers);
		//Response from Invoice Service
		GlobalReponse response = this.restTemplate
				.postForEntity(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_VERIFY_NAPAS_SIGNATURE ,
						requestEntity,
						GlobalReponse.class)
				.getBody();

		if(response == null || response.getStatus() == HttpStatus.MULTIPLE_CHOICES.value()) {
			throw new NapasSignatureInvalidException(response.getMessage());
		}
	}


	public BigDecimal getQRCodeNapasAmount(Integer orderId) {

		BigDecimal amount = BigDecimal.ZERO;
		try {
			String sql = "SELECT " +
					" total_amount  " +
					" FROM pos.d_pos_payment WHERE 1 = 1 " +
					" AND d_pos_order_id = :orderId and payment_method = :method" ;

			// Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
			List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
					.setParameter("orderId", orderId)
					.setParameter("method", AppConstant.PaymentRule.QRCODE_NAPAS)
					.unwrap(NativeQuery.class)
					.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
					.getResultList();

			for (Map<String, Object> row : results) {
				log.info("Row: {}", row);
				amount 	= ParseHelper.BIGDECIMAL.parse(row.get("total_amount"));
			}

		} catch (Exception e) {
			log.error("Error: {}", e.getMessage());
		}
		return amount;
	}


	@KafkaListener(groupId = GROUP_ID, topics = NOTIFY_PAYMENT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
	public void sendNotificationPaymentWithVoice(ConsumerRecord<String, PaymentNotficationKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
		String key = consumerRecord.key(); // could be null
		PaymentNotficationKafkaDto dto = consumerRecord.value();
		try {
			log.info("Topic: " + NOTIFY_PAYMENT_TOPIC);
			log.info("Received message with key: " + key);

			acknowledgment.acknowledge();
			setLocale("vi");
			String message = messageSource.getMessage("payment.qrcode.success", null, LocaleContextHolder.getLocale());
			String title = messageSource.getMessage("order.payment.title", null, LocaleContextHolder.getLocale());
			BigDecimal amount = dto.getAmount().setScale(2, RoundingMode.HALF_UP);
			String f = String.format("%.0f", amount);
			message = message.replace("@amount@", f);
			log.info("message: " + message);

			HttpHeaders headers = new HttpHeaders();
			headers.set("tenantId", dto.getTenantId().toString());

			//send thong bao
			SendNotification send = SendNotification.builder()
//					.title("PAYMENT_NOTIFICATION")
					.title(title)
					.body(message)
					.deviceTokens(List.of(dto.getDeviceToken()))
					.code(null)
					.router("PAYMENT_NOTIFICATION")
					.type("ANN")
					.speak(message)
					.build();
			HttpEntity<SendNotification> requestEntity = new HttpEntity<>(send, headers);
			GlobalReponse exService = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.API_SEND_NOTIFY, requestEntity, GlobalReponse.class);

		} catch (Exception e) {
			log.error("Caught error in updateImportFile(): ", e);
		}

	}

	public void setLocale(String lang) {
		Locale locale = Locale.lookup(Locale.LanguageRange.parse(lang), Arrays.asList(Locale.getAvailableLocales()));
		LocaleContextHolder.setLocale(locale);
	}

}










