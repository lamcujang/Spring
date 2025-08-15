package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Embeddable
@Data
public class VendorPOV {

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Size(max = 255)
    @Column(name = "vendorname")
    private String vendorName;

    @Column(name = "vendordebt")
    private BigDecimal vendorDebt;

    @Column(name = "vendorpaid")
    private BigDecimal vendorPaid;
}
