package com.dbiz.app.tenantservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_summary_today_v2_v", schema = "pos")
public class SummaryTodayV {
    @Id
    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "total_amount_today")
    private BigDecimal totalAmountToday;

    @Column(name = "total_amount_yesterday")
    private BigDecimal totalAmountYesterday;

    @Column(name = "amount_percent_diff")
    private BigDecimal amountPercentDiff;

//    @Column(name = "count_order_processing")
//    private Long countOrderProcessing;
//
//    @Column(name = "amount_order_processing")
//    private BigDecimal amountOrderProcessing;

    @Column(name = "total_order_today")
    private BigDecimal totalOrderToday;

    @Column(name = "total_order_yesterday")
    private BigDecimal totalOrderYesterday;

    @Column(name = "order_percent_diff")
    private BigDecimal orderPercentDiff;

    @Column(name = "count_customer_today")
    private Long countCustomerToday;

    @Column(name = "count_customer_yesterday")
    private Long countCustomerYesterday;

    @Column(name = "customer_percent_diff")
    private BigDecimal customerPercentDiff;

    @Column(name = "other_amt_today")
    private BigDecimal otherAmtToday;

    @Column(name = "other_amt_yesterday")
    private BigDecimal otherAmtYesterday;

    @Column(name = "other_amt_percent_diff")
    private BigDecimal otherAmtPercentDiff;


    @Column(name = "discount_amt_today")
    private BigDecimal discountAmtToday;

    @Column(name = "discount_amt_yesterday")
    private BigDecimal discountAmtYesterday;

    @Column(name = "discount_amt_percent_diff")
    private BigDecimal discountAmtPercentDiff;


    @Column(name = "qty_cancel_today")
    private Long qtyCancelToday;

    @Column(name = "qty_cancel_yesterday")
    private Long qtyCancelYesterday;

    @Column(name = "qty_cancel_percent_diff")
    private BigDecimal qtyCancelPercentDiff;

}