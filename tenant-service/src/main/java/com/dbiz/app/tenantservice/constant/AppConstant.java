package com.dbiz.app.tenantservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {
	public static final String ERP_PLATFORM_IDEMPIERE = "Idempiere";
	public static final String ERP_PLATFORM_ERPNEXT = "ERPNext";
	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final Locale LOCALE_VI = Locale.lookup(Locale.LanguageRange.parse("vi"), Arrays.asList(Locale.getAvailableLocales()));

	public static final String ORG_AVT = "ORG_AVT";

	public static final String EMENU_BANNER = "EMENU_BANNER";
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {

		public static final String USER_SERVICE_HOST = "http://USER-SERVICE/user-service";

		public static final String SAVE_IMAGE = "http://USER-SERVICE/user-service/api/v1/image/save";

		public static final String USER_SERVICE_SAVE_ORGACCESS = "http://USER-SERVICE/user-service/api/v1/users/saveOrgAccess";
		public static final String GET_IMAGE_ID = "http://USER-SERVICE/user-service/api/v1/image/";
		public static final String GET_CUSTOMER_BY_PHONE1 = "http://USER-SERVICE/user-service/api/v1/customers/findByPhone1";
		public static final String USER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/users";
		public static final String USER_SERVICE_CREATE_API_URL = "http://USER-SERVICE/user-service/api/v1/users/register";

		public static final String USER_SERVICE_GET_BY_ERPUSER_ID = "http://USER-SERVICE/user-service/api/v1/users/getByErpUserId";

		public static final String USER_SERVICE_API_URL_GET_ORG_ACCESS = "http://USER-SERVICE/user-service/api/v1/users/getOrgAccess";
		public static final String PRODUCT_SERVICE_HOST = "http://PRODUCT-SERVICE/product-service";
		public static final String PRODUCT_SERVICE_API_URL = "http://PRODUCT-SERVICE/product-service/api/products";
		public static final String PRODUCT_SERVICE_GET_WAREHOUSE_BY_ERP_ID = "http://PRODUCT-SERVICE/product-service/api/v1/warehouses/getByErpId";
		public static final String ORDER_SERVICE_HOST = "http://ORDER-SERVICE/order-service";
		public static final String ORDER_SERVICE_API_URL = "http://ORDER-SERVICE/order-service/api/orders";
		
		public static final String FAVOURITE_SERVICE_HOST = "http://FAVOURITE-SERVICE/favourite-service";
		public static final String FAVOURITE_SERVICE_API_URL = "http://FAVOURITE-SERVICE/favourite-service/api/favourites";
		
		public static final String PAYMENT_SERVICE_HOST = "http://PAYMENT-SERVICE/payment-service";
		public static final String PAYMENT_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/payments";
		
		public static final String SHIPPING_SERVICE_HOST = "http://SHIPPING-SERVICE/shipping-service";
		public static final String SHIPPING_SERVICE_API_URL = "http://SHIPPING-SERVICE/shipping-service/api/shippings";

		public static final String PRODUCT_SERVICE_GET_PRICELIST_ERP_ID = "http://PRODUCT-SERVICE/product-service/api/v1/priceLists/findErpId";
		public static final String SYSTEM_SERVICE_API_URL_GET_BY_NAME = "http://SYSTEM-SERVICE/system-service/api/v1/config/findByName";

		public static final String PRODUCT_SERVICE_API_URL_SAVE_PRICELIST = "http://PRODUCT-SERVICE/product-service/api/v1/priceLists/saveIntPosterminal";

		public static final String TENANT_SERVICE_API_AUDIT_LOG = "http://TENANT-SERVICE/tenant-service/api/v1/audit-log/save";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class SYS_VALUE{

		public static final String ID_USER_ADMIN = "ID_USER_ADMIN";
		public static final String ID_PASSWORD_ADMIN = "ID_PASSWORD_ADMIN";
		public static final String ID_DOMAIN_URL = "ID_DOMAIN_URL";
		public static final String ID_CREATE_TENANT_URL = "ID_CREATE_TENANT_URL";
		public static final String ID_CREATE_USER_URL = "ID_CREATE_USER_URL";
		public static final String MDM_DOMAIN_ASSET_URL = "MDM_DOMAIN_ASSET_URL";
		public static final String MDM_URL_AGENT_LOGO = "MDM_URL_AGENT_LOGO";
		public static final String MDM_CREATE_BPARTNER_URL = "MDM_CREATE_BPARTNER_URL";
		public static final String MDM_CREATE_CONTRACT_URL = "MDM_CREATE_CONTRACT_URL";
		public static final String D_HAS_TO_CREATE_MDM = "D_HAS_TO_CREATE_MDM";
		public static final String D_POS_WEB_DOMAIN_URL = "D_POS_WEB_DOMAIN_URL";
		public static final String D_EXPIRY_TIME_TENANT = "D_EXPIRY_TIME_TENANT";
	}
	
	
	
}









