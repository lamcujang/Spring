package com.dbiz.app.integrationservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {

	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";

	public static final String ERP_PLATFORM_IDEMPIERE = "Idempiere";
	public static final String ERP_PLATFORM_ERPNEXT = "ERPNext";

	public static final String FLOW_ERP_TO_POS="ETP";
	public static final String FLOW_POS_TO_ERP="PTE";

	public static final String KAFKA_GROUP_ID = "gr-sync-order";
	public static final class TopicKafka {

		// Ngăn chặn khởi tạo class
		private TopicKafka() {}

		public static final String GROUP_ID = "gr-sync-order";

		public static final String TOPIC_SEND_FLO = "sync-integration-to-floor-v1";
		public static final String TOPIC_SEND_TBL = "sync-integration-to-table-v1";
		public static final String TOPIC_SEND_PCG = "sync-integration-to-product-category";

		// Topics nội bộ
		public static final String TOPIC_INTERNAL_PRODUCT = "sync-integration-to-product-internal-v1"; // sai trong integration service
		public static final  String TOPIC_SEND_CAV_INT = "sync-int-customer-vendor";

		public static final String TOPIC_SEND_COUPON = "sync-integration-to-coupon-internal";
		public static final String TOPIC_INTERNAL_USER = "sync-int-user-internal";
		public  static final String TOPIC_INTERNAL_PROD = "sync-int-product-internal-v1"; // day qua service product

		public final static String TOPIC_PRICELIST = "sync-integration-to-pricelist-v1";

		public final static String	TOPIC_RECEIVE_CAV  = "receive-integration-customer-vendor";

		public static final String TPROD = "sync-integration-to-product-internal-v4";

		public static final String TOPIC_SEND_SYNC_IMAGE_PRODUCT= "sync-integration-image-product";

		public static final String TOPIC_SAVE_IMAGE_PRODUCT = "sync-save-image-product";
	}

	public static final class IntStatus {
		private IntStatus(){};
		public static final String COMPLETE = "COM";
		public static final String FAILURE = "FAI";
		public static final String INPROCESSING = "INP";

		public static final String NEW = "NEW";
	}
	public static final class DataTypeInt {
		private DataTypeInt() {}
		public static final String ORGANIZATION = "ORG";
		public static final String PRODUCT_CATEGORY= "PCG";
		public static final String PRODUCT = "PRO";
		public static final String PRICE_LIST = "PRL";
		public static final String CUSTOMER_VENDOR = "CAV";
		public static final String USER = "CUS";
		public static final String WAREHOUSE = "WHO";
		public static final String FLOOR = "FLO";
		public static final String TABLE = "TBL";
		public static final String COUPON = "COP";
		public static final String SALES_ORDER = "SOR";
		public static final String KITCHEN_ORDER = "KDS";
		public static final String POS_TERMINAL = "PTM";
		public static final String BPARTNER_GROUP = "BPG";
		public static final String VENDOR = "VEN";
		public static final String CUSTOMER = "CUS";
		public static final String IMAGE_PRODUCT ="IMG";
	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {
		
		public static final String USER_SERVICE_HOST = "http://USER-SERVICE/user-service";
		public static final String USER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/users";
		public static final String USER_SERVICE_API_INTEGRATION_PARTNER_GROUP = "http://USER-SERVICE/user-service/api/v1/partnerGroups/intSave";
		public static final String USER_SERVICE_API_INTEGRATION_PARTNER_GROUP_ERPNEXT = "http://USER-SERVICE/user-service/api/v1/partnerGroups/intSaveERPNext";
		public static final String USER_SERVICE_API_INTEGRATION_VENDOR = "http://USER-SERVICE/user-service/api/v1/vendors/intSave";
		public static final String USER_SERVICE_API_INTEGRATION_CUSTOMMER = "http://USER-SERVICE/user-service/api/v1/customers/intSave";
		public static final String USER_SERVICE_API_INTEGRATION_CUSTOMMER_ERPNEXT = "http://USER-SERVICE/user-service/api/v1/customers/intSaveERPNext";
		public static final String USER_SERVICE_API_INTEGRATION_USER = "http://USER-SERVICE/user-service/api/v1/users/intSave";
		public static final String USER_SERVICE_API_INTEGRATION_USER_ERPNEXT = "http://USER-SERVICE/user-service/api/v1/users/intSaveERPNext";
		public static final String PRODUCT_SERVICE_INT_WAREHOUSE = "http://PRODUCT-SERVICE/product-service/api/v1/warehouses/intSave";

		public static final String PRODUCT_SERVICE_API_INTEGRATION_UOM = "http://PRODUCT-SERVICE/product-service/api/v1/uom/intSave";

		public static final String PRODUCT_SERVICE_API_INTEGRATION_PRODUCT = "http://PRODUCT-SERVICE/product-service/api/v1/products/intSave";
		public static final String PRODUCT_SERVICE_API_SAVE_IMAGE = "http://PRODUCT-SERVICE/product-service/api/v1/products/saveImageProduct";
		public static final String PRODUCT_SERVICE_API_INTEGRATION_PRICELIST= "http://PRODUCT-SERVICE/product-service/api/v1/priceLists/intSave";
		public static final String PRODUCT_SERVICE_API_INTEGRATION_TAX_CATEGORY = "http://PRODUCT-SERVICE/product-service/api/v1/taxCategory/intSave";
		public static final String PRODUCT_SERVICE_API_INTEGRATION_PRODUCT_CATEGORY = "http://PRODUCT-SERVICE/product-service/api/v1/categoryProducts/intSave";
		public static final String PRODUCT_SERVICE_API_UPDATE_PRODUCT = "http://PRODUCT-SERVICE/product-service/api/v1/products/saveAll";

		public static final String ORDER_SERVICE_API_INTEGRATION_POS_ORDER = "http://ORDER-SERVICE/order-service/api/v1/posOrders/intSave";

		public static final String TENANT_SERVICE_API_INTEGRATION_ORG = "http://TENANT-SERVICE/tenant-service/api/v1/org/intSave";

		public static final String TENANT_SERVICE_API_INTEGRATION_POSTEMINAL = "http://TENANT-SERVICE/tenant-service/api/v1/terminal/intSave";

		public static final String ORDER_SERVICE_HOST = "http://ORDER-SERVICE/order-service";
		public static final String ORDER_SERVICE_API_URL = "http://ORDER-SERVICE/order-service/api/orders";
		public static final String ORDER_SERVICE_GET_BY_ID_URL = "http://ORDER-SERVICE/order-service/api/orders";

		public static final String ORDER_SERVICE_INT_FLOOR_SAVE = "http://ORDER-SERVICE/order-service/api/v1/floors/intSave";
		public static final String ORDER_SERVICE_INT_TABLE_SAVE = "http://ORDER-SERVICE/order-service/api/v1/tables/intSave";
		public static final String FAVOURITE_SERVICE_HOST = "http://FAVOURITE-SERVICE/favourite-service";
		public static final String FAVOURITE_SERVICE_API_URL = "http://FAVOURITE-SERVICE/favourite-service/api/favourites";
		
		public static final String PAYMENT_SERVICE_HOST = "http://PAYMENT-SERVICE/payment-service";
		public static final String PAYMENT_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/payments";

		public static final String PAYMENT_SERVICE_API_URL_INT_SAVE = "http://PAYMENT-SERVICE/payment-service/api/v1/coupon/intSave";
		public static final String SYSTEM_SERVICE_API_URL_GET_BY_NAME = "http://SYSTEM-SERVICE/system-service/api/v1/config/findByName";

		public static final String PAYMENT_SERVICE_SAVE_VOUCHER_TRANSACTION = "http://PAYMENT-SERVICE/payment-service/api/v1/payments/voucher/transaction";
		public static final String ORDER_SERVICE_API_INTEGRATION_KITCHEN_ORDER = "http://ORDER-SERVICE/order-service/api/v1/kitchenOrders/intSave";
		public static final String API_SEND_NOTIFY = "http://SYSTEM-SERVICE/system-service/api/v1/notify/sendNotify";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class ErpIntegrationEndPoint{

		public static final String name_GetConfig_INT_ORG = "D_END_POIN_INT_ORG";
		public static final String name_GetConfig_INT_POS = "D_END_POIN_INT_POS";
		public static final String name_GetConfig_INT_PC = "D_END_POIN_INT_PC";
		public static final String name_GetConfig_INT_FLO = "D_END_POIN_INT_FLO";
		public static final String name_GetConfig_INT_TBL = "D_END_POIN_INT_TBL";
		public static final String name_GetConfig_INT_BPN = "D_END_POIN_INT_BPN";
		public static final String name_GetConfig_INT_USR = "D_END_POIN_INT_USR";
		public static final String name_GetConfig_INT_WHO = "D_END_POIN_INT_WAREHOUSE";
		public static final String name_GetConfig_INT_PRO = "D_END_POIN_INT_PRODUCT";
		public static final String name_GetConfig_INT_COP = "D_END_POIN_INT_COUPON";
		public static final String name_GetConfig_INT_PRL = "D_END_POIN_INT_PRICE_LIST";
		public static final String name_GetConfig_GETTOKEn = "D_END_POIN_GETTOKEN";
		public static final String name_GetConfig_CREATE_PRODUCT = "D_END_POIN_CREATE_PRODUCT";
		public static final String D_GET_VOUCHER_INFO = "D_GET_VOUCHER_INFO";
		public static final String D_CHECK_VOUCHER_INFO = "D_CHECK_VOUCHER_INFO";
		public static final String D_GET_VOUCHER_SERVICES = "D_GET_VOUCHER_SERVICES";
		public static final String D_GET_VOUCHER_SERVICE_ORDER = "D_GET_VOUCHER_SERVICE_ORDER";
		public static final String D_CHECKIN_VOUCHER = "D_CHECKIN_VOUCHER";
		public static final String D_INT_CREATE_POS_ORDER = "D_INT_CREATE_POS_ORDER";
		public static final String D_INT_UDPATE_SHIFT = "D_INT_UDPATE_SHIFT";
		public static final String D_INT_CREATE_KITCHEN_ORDER = "D_INT_CREATE_KITCHEN_ORDER";
		public static final String D_ERPNEXT_END_INT_RESOURCE = "D_ERPNEXT_END_INT_RESOURCE";
		public static final String D_ERPNEXT_END_POINT_INT_SHIFT = "D_ERPNEXT_END_POINT_INT_SHIFT";
		public static final String D_ERPNEXT_END_INT_CUSTOMER = "D_ERPNEXT_END_INT_CUSTOMER";
		public static final String D_ERPNEXT_END_INT_METHOD = "D_ERPNEXT_END_INT_METHOD";
		public static final String D_ERPNEXT_END_INT_ORG = "D_ERPNEXT_END_INT_ORG";
		public static final String D_ERPNEXT_END_INT_PTM = "D_ERPNEXT_END_INT_PTM";
		public static final String D_ERPNEXT_END_INT_USER = "D_ERPNEXT_END_INT_USER";


		public static final String D_INT_CREATE_COUPON  = "D_INT_CREATE_COUPON";
	}
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class ERPNextDoctype{
		public static final String D_POS_PROFILE= "POS Profile";
		public static final String D_WAREHOUSE = "Warehouse";
		public static final String D_ITEM_GROUP = "Item Group";

	}
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class ErpIntegrationStatus{

		public static final String COMPLETE = "COM";
		public static final String FAIL = "FAI";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class ErpIntegrationFlow{

		public static final String FROM_ERP = "ETP";
		public static final String FROM_POS = "PTE";

	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class ErpIntegrationType{

		public static final String VOUCHER = "VOU";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class IntegrationType{
		public static final String ERP = "ERP";
		public static final String FIN = "FIN";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class FileType {
		public static final String EXCEL = "EXC";
		public static final String CSV = "CSV";
		public static final String JSON = "JSON";
		public static final String XML = "XML";
		public static final String PDF = "PDF";
	}
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class FileObjectType {
		public static final String CUSTOMER = "CUS";
		public static final String BUSINESS_PARTNER_GROUP = "BPG";
		public static final String PRODUCT = "PRO";
		public static final String PRICE_LIST = "PRL";
		public static final String PRICE_LIST_PRODUCT = "PLP";
		public static final String PRODUCT_CATEGORY = "PRC";
	}
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class ImportFileStatus {
		public static final String IN_PROGRESS = "INP";
		public static final String FAILED = "FAI";
		public static final String SUCCESS = "COM";
	}

}


