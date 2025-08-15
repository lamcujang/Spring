package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_sc_posorder_get_v", schema = "pos")
public class SCPosOrderGetV extends AbstractMappedEntity implements Serializable {


    @Column(name = "visa")
    private BigDecimal visa;

    @Column(name = "qty_visa")
    private Integer qtyVisa;

    @Column(name = "card")
    private BigDecimal card;

    @Column(name = "qty_card")
    private Integer qtyCard;

    @Column(name = "cheque")
    private BigDecimal cheque;

    @Column(name = "qty_cheque")
    private Integer qtyCheque;

    @Column(name = "free")
    private BigDecimal free;

    @Column(name = "qty_free")
    private Integer qtyFree;

    @Column(name = "mix")
    private BigDecimal mix;

    @Column(name = "qty_mix")
    private Integer qtyMix;

    @Column(name = "qrcode")
    private BigDecimal qrcode;

    @Column(name = "qty_qrcode")
    private Integer qtyQrcode;

    @Column(name = "slip")
    private BigDecimal slip;

    @Column(name = "qty_slip")
    private Integer qtySlip;

    @Column(name = "voucher")
    private BigDecimal voucher;

    @Column(name = "qty_voucher")
    private Integer qtyVoucher;
    @Id
    @Column(name = "d_shift_control_id", precision = 10)
    private Integer shiftControlId;

    @Column(name = "cash")
    private BigDecimal Cash;

    @Column(name = "bank")
    private BigDecimal Bank;

    @Column(name = "deb")
    private BigDecimal Debt;

    @Column(name = "loyalty")
    private BigDecimal Loyalty;

    @Column(name = "qty_cash")
    private Integer qtyCash;

    @Column(name = "qty_bank")
    private Integer qtyBank;

    @Column(name = "qty_deb")
    private Integer qtyDeb;

    @Column(name = "qty_loyalty")
    private Integer qtyLoyalty;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    public SCPosOrderGetV() {
    }
    public SCPosOrderGetV (Integer shiftControlId, BigDecimal Cash, BigDecimal Bank, BigDecimal Debt, BigDecimal Loyalty, Integer qtyCash, Integer qtyBank, Integer qtyDeb, Integer qtyLoyalty, BigDecimal visa, Integer qtyVisa)
    {
        this.shiftControlId = shiftControlId;
        this.Cash = Cash;
        this.Bank = Bank;
        this.Debt = Debt;
        this.Loyalty = Loyalty;
        this.qtyCash = qtyCash;
        this.qtyBank = qtyBank;
        this.qtyDeb = qtyDeb;
        this.qtyLoyalty = qtyLoyalty;
    }
    public SCPosOrderGetV(Integer shiftControlId) {
        this.shiftControlId = shiftControlId;
        this.visa = BigDecimal.ZERO;
        this.qtyVisa = 0;
        this.card = BigDecimal.ZERO;
        this.qtyCard = 0;
        this.cheque = BigDecimal.ZERO;
        this.qtyCheque = 0;
        this.free = BigDecimal.ZERO;
        this.qtyFree = 0;
        this.mix = BigDecimal.ZERO;
        this.qtyMix = 0;
        this.qrcode = BigDecimal.ZERO;
        this.qtyQrcode = 0;
        this.slip = BigDecimal.ZERO;
        this.qtySlip = 0;
        this.voucher = BigDecimal.ZERO;
        this.qtyVoucher = 0;
        this.shiftControlId = 0;
        this.Cash = BigDecimal.ZERO;
        this.Bank = BigDecimal.ZERO;
        this.Debt = BigDecimal.ZERO;
        this.Loyalty = BigDecimal.ZERO;
        this.qtyCash = 0;
        this.qtyBank = 0;
        this.qtyDeb = 0;
        this.qtyLoyalty = 0;
    }
}