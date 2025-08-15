package com.dbiz.app.orderservice.constant;

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
	public static final String DOC_STATUS_COMPLETED = "COM";
	public static final String DOC_STATUS_INPROGRESS = "IPR";
	public static final String DOC_STATUS_DRAFT = "DRA";
	public static final String DOC_STATUS_VOID = "VOD";
	public static final String DOC_STATUS_CLOSED = "CLS";
	public static final LocalDate CURRENT_DATE = LocalDate.now();
	public static final LocalDateTime CURRENT_DATETIME = LocalDateTime.now();
	// notify
	public static final String notify_RQ_PAYMENT_NOTIFY_TITLE = "D_RQ_PAYMENT_NOTIFY_TITLE";
	public static final String notify_RQ_PAYMENT_NOTIFY_BODY = "D_RQ_PAYMENT_NOTIFY_BODY";
	public static final String notify_RQ_STAFF_TITLE = "D_RQ_STAFF_NOTIFY_TITLE";
	public static final String notify_RQ_STAFF_BODY = "D_RQ_STAFF_NOTIFY_BODY";
	public static final String notify_Request_Order_TITLE = "D_EMENU_NOTIFY_TITLE";
	public static final String notify_Request_Order_BODY = "D_EMENU_NOTIFY_BODY";
	public static final String notify_Cooking_Success_Title= "D_KITCHENORDER_SUCCESS_NOTIFY_TITLE";
	public static final String notify_Cooking_Success_Body= "D_KITCHENORDER_SUCCESS_NOTIFY_BODY";
	public static final String notify_Send_KDS_Title = "D_KITCHENORDER_SENDKDS_NOTIFY_TITLE";
	public static final String notify_Send_KDS_Body="D_KITCHENORDER_SENDKDS_NOTIFY_BODY";
	public static final String notify_Remind_Title="D_KITCHENORDER_REMIND_NOTIFY_TITLE";
	public static final String notify_Remind_Body="D_KITCHENORDER_REMIND_NOTIFY_BODY";
	public static final String notify_RemindPreTime_Title="D_KITCHENORDER_REMINDPRETIME_NOTIFY_TITLE";
	public static final String notify_RemindPreTime_Body="D_KITCHENORDER_REMINDPRETIME_NOTIFY_BODY";
	public static final String notify_RemindCookTime_Title="D_KITCHENORDER_REMINDCOOKTIME_NOTIFY_TITLE";
	public static final String notify_RemindCookTime_Body="D_KITCHENORDER_REMINDCOOKTIME_NOTIFY_BODY";

    public static final String speak_RemindKDS = "D_MSG_SPEAK_REMIND_KDS";

    public static final String speak_SendKDS = "D_MSG_SPEAK_SEND_KDS";
	// end notify
	public static final String CURRENCY_CODE_VND = "VND";

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {

		public static final String GET_CUSTOMER_BY_PHONE1 = "http://USER-SERVICE/user-service/api/v1/customers/findByPhone1";
		public static final String GET_CUSTOMER_BY_PHONE2 = "http://USER-SERVICE/user-service/api/v1/customers/findByPhone2";
		public static final String USER_SERVICE_HOST = "http://USER-SERVICE/user-service";
		public static final String USER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/v1/users";
		public static final String CUSTOMER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/v1/customers";

		public static final String REFERENCELIST_SERVICE_API_URL_VALUE = "http://SYSTEM-SERVICE/system-service/api/v1/referenceList/findByValue";

		public static final String REFERENCELIST_SERVICE_API_FIND_REF_VALUE = "http://SYSTEM-SERVICE/system-service/api/v1/referenceList/findByRefValue";

		public static final String REFERENCELIST_SERVICE_API_FIND_NAMEREF_VALUE = "http://SYSTEM-SERVICE/system-service/api/v1/referenceList/findByReferenceNameAndValue";

		public static final String PRODUCT_SERVICE_API_URL = "http://PRODUCT-SERVICE/product-service/api/v1/products";

		public static final String API_SEND_NOTIFY = "http://SYSTEM-SERVICE/system-service/api/v1/notify/sendNotify";

		public static final String PRODUCT_SERVICE_HOST = "http://PRODUCT-SERVICE/product-service/";

		public static final String ORDER_SERVICE_HOST = "http://ORDER-SERVICE/order-service";
		public static final String ORDER_SERVICE_API_URL = "http://ORDER-SERVICE/order-service/api/orders";
		
		public static final String FAVOURITE_SERVICE_HOST = "http://FAVOURITE-SERVICE/favourite-service";
		public static final String FAVOURITE_SERVICE_API_URL = "http://FAVOURITE-SERVICE/favourite-service/api/favourites";
		
		public static final String PAYMENT_SERVICE_HOST = "http://PAYMENT-SERVICE/payment-service";
		public static final String PAYMENT_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/v1/payments";
		public static final String INVOICE_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/v1/invoices";
		public static final String SHIPPING_SERVICE_HOST = "http://SHIPPING-SERVICE/shipping-service";
		public static final String SHIPPING_SERVICE_API_URL = "http://SHIPPING-SERVICE/shipping-service/api/shippings";
		public static final String PRODUCT_SERVICE_UPDATEALL_PR_API_URL = "http://PRODUCT-SERVICE/product-service/api/v1/products/saveAll2";

		public static final String PRODUCT_SERVICE_FIND_PRODUCTCOMBO_BYPRODUCID = "http://PRODUCT-SERVICE/product-service/api/v1/productCombo/findByProductId";

		public static final String PRODUCT_SERVICE_FIND_PRODUCTISCOMBO_BYPRODUCID = "http://PRODUCT-SERVICE/product-service/api/v1/productCombo/component";

		public static final String SYSTEM_SERVICE_API_VALUE_BY_NAME_URL = "http://SYSTEM-SERVICE/system-service/api/v1/config/findByName";


		public static final String TENANT_SERVICE_API_GET_BY_ORGERP = "http://TENANT-SERVICE/tenant-service/api/v1/org/getByErpId";

		public static final String TENANT_SERVICE_API_GET_POSTERMINAL_BY_ID = "http://TENANT-SERVICE/tenant-service/api/v1/terminal";

		public static final String TENANT_SERVICE_API_GET_POSTERMINAL_BY_ERP_ID = "http://TENANT-SERVICE/tenant-service/api/v1/terminal/getByErpId";


		public static final String PRODUCT_SERVICE_API_SAVE_IMAGE = "http://PRODUCT-SERVICE/product-service/api/v1/images/saveImage";

		public static final String PRODUCT_SERVICE_API_GET_IMAGE_BY_ID = "http://PRODUCT-SERVICE/product-service/api/v1/images/findById";

		public static final String INVENTORY_SERVICE_API_CREATE_TRANSACTION = "http://INVENTORY-SERVICE/inventory-service/api/v1/transaction/create";

		public static final String PAYMENT_SERVICE_API_CREATE_POS_RO = "http://PAYMENT-SERVICE/payment-service/api/v1/receiptOther/pos";
		public static final String SYSTEM_SERVICE_GET_DOCTYPE_BY_CODE = "http://SYSTEM-SERVICE/system-service/api/v1/docType";
		public static final String PAYMENT_SERVICE_API_CREATE_NAPAS_TRX_RO = "http://PAYMENT-SERVICE/payment-service/api/v1/napas/internal/transaction";
		public static final String SYSTEM_SERVICE_VERIFY_NAPAS_SIGNATURE = "http://SYSTEM-SERVICE/system-service/api/v1/signature/napas/verify";

		public static final String EINVOICE_SERVICE_API_ISSUE = "http://PAYMENT-SERVICE/payment-service/api/v1/einvoice/issue";

	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DocTypeCode {
		public static final String REQUEST_ORDER = "REQUEST_ORDER";
		public static final String AR_INVOICE = "AR_INVOICE";
		public static final String PRODUCTION = "PRODUCTION";
		public static final String PO_ORDER = "PO_ORDER";
		public static final String POS_ORDER = "POS_ORDER";
		public static final String AR_PAYMENT = "AR_PAYMENT";
		public static final String KITCHEN_ORDER = "KITCHEN_ORDER";
		public static final String POS_CLOSED_CASH = "POS_CLOSED_CASH";
		public static final String RETURN_CUSTOMER_ORDER = "RETURN_CUSTOMER_ORDER";
		public static final String RETURN_VENDOR_ORDER = "RETURN_VENDOR_ORDER";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class KitchenStatus {
		public  static final String KITCHEN_SEND = "KOS";
		public  static final String KITCHEN_CANCEL = "DMC";
		public  static final String KITCHEN_WAITING= "WTP";
		public  static final String KITCHEN_DONE = "PRD";

		public  static final String KITCHEN_PROCESSING = "BPR";
		public  static final String KITCHEN_NOT_SEND = "NSK";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class WDSStatus {
		public static final String ADJUSTMENT_PENDING = "ADP";
		public static final String ADJUSTING = "ADJ";
		public static final String COMPLETE = "COM";

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
		public static final String QRCODE_NAPAS = "QRC";


	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class TableStatus {
		public static final String TABLE_STATUS_EMPTY = "ETB";

		public static final String TABLE_STATUS_TABLE_IN_USE = "TIU";

		public static final String TABLE_STATUS_BOOKing = "TBD";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class PromotionBasedOn {
		public static final String PRODUCT = "PRO";
		public static final String SALES_ORDER = "SOR";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class PromotionType {
		public static final String GIVE_WITH_PURCHASE = "GWP";
		public static final String DISCOUNT_ON_SALES = "DOS";

		public static final String FREE_WITH_GIFT = "FWG";
		public static final String BUY_ONE_DISCOUNT = "BOD";
		public static final String PRODUCT_DISCOUNT_ORDER = "PDO";
		public static final String POINT_EARN_REDEEM = "PER";
	}
}









