package com.dbiz.app.proxyclient.business.product.define;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public enum ProductGroupType {

    RG, // Regular Goods hh thuong
    PG, // processed goods hang che bien
    SVC, // service dich vu
    CP,// combo-packaged combo-donggoi
    NONE
    }
