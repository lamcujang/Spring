package com.dbiz.app.systemservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AppConstant {
	
	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final LocalDateTime CURRENT_DATETIME = LocalDateTime.now();

	public static final String D_URL_GET_PROVINCES = "D_URL_GET_PROVINCES";
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class DiscoveredDomainsApi {
		
		public static final String USER_SERVICE_HOST = "http://USER-SERVICE/user-service";
		public static final String USER_SERVICE_API_URL = "http://USER-SERVICE/user-service/api/users";
		
		public static final String PRODUCT_SERVICE_HOST = "http://PRODUCT-SERVICE/product-service";
		public static final String PRODUCT_SERVICE_API_URL = "http://PRODUCT-SERVICE/product-service/api/products";
		
		public static final String ORDER_SERVICE_HOST = "http://ORDER-SERVICE/order-service";
		public static final String ORDER_SERVICE_API_URL = "http://ORDER-SERVICE/order-service/api/orders";
		public static final String ORDER_SERVICE_GET_BY_ID_URL = "http://ORDER-SERVICE/order-service/api/orders";

		public static final String FAVOURITE_SERVICE_HOST = "http://FAVOURITE-SERVICE/favourite-service";
		public static final String FAVOURITE_SERVICE_API_URL = "http://FAVOURITE-SERVICE/favourite-service/api/favourites";
		
		public static final String PAYMENT_SERVICE_HOST = "http://PAYMENT-SERVICE/payment-service";
		public static final String PAYMENT_SERVICE_API_URL = "http://PAYMENT-SERVICE/payment-service/api/payments";
		
		public static final String SHIPPING_SERVICE_HOST = "http://SHIPPING-SERVICE/shipping-service";
		public static final String SHIPPING_SERVICE_API_URL = "http://SHIPPING-SERVICE/shipping-service/api/shippings";


	}



	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class Napas {

		public static final String D_NAPAS_URL_PRE = "D_NAPAS_URL_PRE";
		public static final String D_NAPAS_URL_GETTOKEN = "D_NAPAS_URL_GETTOKEN";
		public static final String D_NAPAS_URL_INVESTIGATION = "D_NAPAS_URL_INVESTIGATION";
		public static final String D_NAPAS_CRD_CLIENT_ID = "D_NAPAS_CRD_CLIENT_ID";
		public static final String D_NAPAS_CRD_CLIENT_SECRET = "D_NAPAS_CRD_CLIENT_SECRET";
		public static final String D_NAPAS_MASTER_MERCHANT = "D_NAPAS_MASTER_MERCHANT";
		public static final String D_NAPAS_GRANT_TYPE = "D_NAPAS_GRANT_TYPE";
		public static final String D_NAPAS_CODE = "D_NAPAS_CODE";
		public static final String D_NAPAS_PUBLIC_KEY = "D_NAPAS_PUBLIC_KEY";
		public static final String D_SYSTEM_PRIVATE_KEY = "D_SYSTEM_PRIVATE_KEY";
		public static final String D_NAPAS_RECEIVER_ID = "D_NAPAS_RECEIVER_ID";
		public static final String D_NAPAS_SENDER_ID = "D_NAPAS_SENDER_ID";

	}


	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class NapasMessageIdentifier {

		public static final String PAYMENT_NOTIFICATION = "paymentnotification";
		public static final String INVEST_REQUEST = "investrequest";
		public static final String INVEST_RESPONSE = "investresponse";
		public static final String RECONCILIATION_REPORT = "reconciliationreport";

	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract class NapasServiceType {

		public static final String PAYMENT_NOTIFICATION = "01";
		public static final String INVESTIGATION = "02";
		public static final String RECONCILIATION_REPORT = "03";

	}
	
	
}









