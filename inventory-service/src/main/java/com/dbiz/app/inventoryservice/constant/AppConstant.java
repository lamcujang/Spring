package com.dbiz.app.inventoryservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public abstract class DiscoveredDomainsApi{

            public static final String PRODUCT_SERVICE_API_CREATE_PRODUCT_LOCATION_URL = "http://PRODUCT-SERVICE/product-service/api/v1/productLocation/internal";

    }
}
