package com.dbiz.app.productservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {
	public static final String ERP_PLATFORM_IDEMPIERE = "Idempiere";
	public static final String ERP_PLATFORM_ERPNEXT = "ERPNext";
	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class NameSystemConfig {

		public static final String MDM_URL_SAVE_IMAGE = "MDM_URL_SAVE_IMAGE";

	}
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {

		public static final String GET_ORG_BY_ID = "http://TENANT-SERVICE/tenant-service/api/v1/org";
		public static final String GET_ORG_TENANT_CODE = "http://TENANT-SERVICE/tenant-service/api/v1/tenant/getInforTenantOrg";

		public static final String GET_ORG_BY_ERP_ID =  "http://TENANT-SERVICE/tenant-service/api/v1/org/getByErpId";

		public static final String GET_POSTERMINAL_BY_ERP_ID =  "http://TENANT-SERVICE/tenant-service/api/v1/terminal/getByErpId";
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


		public  static final String SYSTEM_SERVICE_HOST_FINDBYNAME = "http://SYSTEM-SERVICE/system-service/api/v1/config/findByName/";


		public static final String TENANT_SERVICE_API_GET_BY_ORGERP = "http://TENANT-SERVICE/tenant-service/api/v1/org/getByErpId";

		public static final String INVENTORY_SERVICE_API_CREATE_TRANSACTION = "http://INVENTORY-SERVICE/inventory-service/api/v1/transaction/create";
		
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class ImportFileStatus {
		public static final String IN_PROGRESS = "INP";
		public static final String FAILED = "FAI";
		public static final String SUCCESS = "COM";
	}
	
}









