package com.dbiz.app.orderservice.domain.view;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "shift_control_get_payment_v", schema = "pos")
public class ShiftControlGetPaymentV extends AbstractMappedEntity implements Serializable {
    
    @Id
    @Column(name = "d_payment_id", precision = 10)
    private Integer paymentId;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Size(max = 32)
    @Column(name = "document_no", length = 32)
    private String documentNo;

    @Column(name = "d_shift_control_id", precision = 10)
    private Integer shiftControlId;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @Size(max = 128)
    @Column(name = "user_name", length = 128)
    private String userName;

//    @Size(max = 3)
//    @Column(name = "payment_method", length = 3)
//    private String paymentMethod;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

//    @Column(name = "payment_type")
//    @Type(type = "org.hibernate.type.TextType")
//    private String paymentType;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}