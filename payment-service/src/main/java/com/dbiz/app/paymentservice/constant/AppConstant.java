package com.dbiz.app.paymentservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {
	
	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final LocalDate CURRENT_DATE = LocalDate.now();
	public static final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();
	public static final String DOC_STATUS_COMPLETED = "COM";
	public static final String DOC_STATUS_DRAFT = "DRA";
	public static final String DOC_STATUS_INPROGRESS = "IPR";
	public static final String DOC_STATUS_VOID = "VOD";
	public static final String DOC_STATUS_CLOSED = "CLL";
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {
		
		public static final String USER_SERVICE_HOST = "http://USER-SERVICE/user-service";
		public static final String USER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/users";
		
		public static final String PRODUCT_SERVICE_HOST = "http://PRODUCT-SERVICE/product-service";
		public static final String PRODUCT_SERVICE_API_URL = "http://PRODUCT-SERVICE/product-service/api/products";
		
		public static final String ORDER_SERVICE_HOST = "http://ORDER-SERVICE/order-service";
		public static final String ORDER_SERVICE_API_URL = "http://ORDER-SERVICE/order-service/api/orders";
		
		public static final String FAVOURITE_SERVICE_HOST = "http://FAVOURITE-SERVICE/favourite-service";
		public static final String FAVOURITE_SERVICE_API_URL = "http://FAVOURITE-SERVICE/favourite-service/api/favourites";
		
		public static final String PAYMENT_SERVICE_HOST = "http://PAYMENT-SERVICE/payment-service";
		public static final String PAYMENT_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/payments";

		public static final String SHIPPING_SERVICE_HOST = "http://SHIPPING-SERVICE/shipping-service";
		public static final String SHIPPING_SERVICE_API_URL = "http://SHIPPING-SERVICE/shipping-service/api/shippings";

		public static final String INVOICE_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/v1/invoices";
		public static final String INVOICE_SERVICE_API_VALUE_BY_ID_URL = "http://PAYMENT-SERVICE/payment-service/api/v1/invoices/findById";

		public static final String TENANT_SERVICE_API_HOST = "http://TENANT-SERVICE/tenant-service";
		public static final String TENANT_SERVICE_API_TERMINAL_BY_ID_URL = "http://TENANT-SERVICE/tenant-service/api/v1/terminal";
		public static final String SYSTEM_SERVICE_API_VALUE_BY_NAME_URL = "http://SYSTEM-SERVICE/system-service/api/v1/config/findByName";
		public static final String ORDER_SERVICE_API_SAVE_URL = "http://ORDER-SERVICE/order-service/api/v1/posOrders/save";
		public static final String ORDER_SERVICE_API_UPDATE_HEADER_URL = "http://ORDER-SERVICE/order-service/api/v1/posOrders/header/update";
		public static final String TENANT_SERVICE_API_TERMINAL_SAVE_URL = "http://TENANT-SERVICE/tenant-service/api/v1/terminal";
		public static final String ORDER_SERVICE_API_UPDATE_MBB_URL = "http://ORDER-SERVICE/order-service/api/v1/orders/updateOrderMBB";
		public static final String CUSTOMER_SERVICE_API_UPDATE = "http://USER-SERVICE/user-service/api/v1/customers/updateDebit";
		public static final String PRODUCT_SERVICE_UPDATEALL_PR_API_URL = "http://PRODUCT-SERVICE/product-service/api/v1/products/saveAll2";
		public static final String SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE = "http://SYSTEM-SERVICE/system-service/api/v1/docType";
		public static final String SYSTEM_SERVICE_GET_NAPAS_CONFIG = "http://SYSTEM-SERVICE/system-service/api/v1/config/napas";
		public static final String SYSTEM_SERVICE_GET_NAPAS_HEADER = "http://SYSTEM-SERVICE/system-service/api/v1/signature/napas/sign";
		public static final String SYSTEM_SERVICE_VERIFY_NAPAS_SIGNATURE = "http://SYSTEM-SERVICE/system-service/api/v1/signature/napas/verify";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class PaymentRule {
		public static final String CASH = "CAS";
		public static final String BANK = "BAN";
		public static final String QRCODE_MBB = "QRC";
		public static final String VISA = "VIS";
		public static final String FREE = "FRE";
		public static final String DEBT = "DEB";
		public static final String VOUCHER = "VOU";
		public static final String COUPON = "COU";
		public static final String QRCODE_NAPAS = "QRC";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DocTypeCode {
		public static final String REQUEST_ORDER = "REQUEST_ORDER";
		public static final String AR_INVOICE = "AR_INVOICE";
		public static final String AP_INVOICE = "AP_INVOICE";
		public static final String PRODUCTION = "PRODUCTION";
		public static final String PO_ORDER = "PO_ORDER";
		public static final String POS_ORDER = "POS_ORDER";
		public static final String AR_PAYMENT = "AR_PAYMENT";
		public static final String AP_PAYMENT = "AP_PAYMENT";
		public static final String KITCHEN_ORDER = "KITCHEN_ORDER";
		public static final String POS_CLOSED_CASH = "POS_CLOSED_CASH";
	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class QRCodeType {
		public static final String MBB = "MBB_QRCODE";
		public static final String NAPAS = "NAPAS_QRCODE";
	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class EInvoice {
		public static final String D_EINVOICE_VIETTEL_SIGN = "D_EINVOICE_VIETTEL_SIGN";
		public static final String D_EINVOICE_VIETTEL_FORM = "D_EINVOICE_VIETTEL_FORM";
		public static final String D_EINVOICE_VIETTEL_URL_PRE = "D_EINVOICE_VIETTEL_URL_PRE";
		public static final String D_EINVOICE_VIETTEL_URL_ISSUE = "D_EINVOICE_VIETTEL_URL_ISSUE";
		public static final String D_EINVOICE_VIETTEL_RESCUE_URL = "D_EINVOICE_VIETTEL_RESCUE_URL";

		public static final String D_EINVOICE_HILO_URL_PRE = "D_EINVOICE_HILO_URL_PRE";
		public static final String D_EINVOICE_HILO_UR_CREATE = "D_EINVOICE_HILO_URL_CREATE";
		public static final String D_EINVOICE_HILO_URL_ISSUE = "D_EINVOICE_HILO_URL_ISSUE";
		public static final String D_EINVOICE_HILO_URL_REPLACE = "D_EINVOICE_HILO_URL_REPLACE";
		public static final String D_EINVOICE_HILO_RESCUE_URL = "D_EINVOICE_HILO_RESCUE_URL";
		public static final String D_EINVOICE_HILO_URL_EDIT = "D_EINVOICE_HILO_URL_EDIT";
		public static final String D_EINVOICE_HILO_URL_PDF = "D_EINVOICE_HILO_URL_PDF";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class PriceCategory {
		public static final String INC = "INC";
		public static final String EXC = "EXC";
	}
}









