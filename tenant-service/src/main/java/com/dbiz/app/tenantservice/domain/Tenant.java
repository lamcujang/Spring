package com.dbiz.app.tenantservice.domain;

import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(CustomAuditingEntityListener.class)
@Table(name = "d_tenant", schema = "pos")
public class Tenant extends AbstractMappedEntity  implements Serializable {
    @Id
    @Column(name = "d_tenant_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tenant_sq")
    @SequenceGenerator(name = "d_tenant_sq", sequenceName = "d_tenant_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "domain_url")
    private String domainUrl;

    @Column(name = "d_industry_id")
    private Integer industryId;

    @Column(name = "industry_code")
    private String industryCode;

    @Column(name = "owner_username")
    private String ownerUserName;

    @Column(name = "owner_password")
    private String ownerPassword;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;


    @Size(max = 15)
    @Column(name = "tax_code", length = 15)
    private String taxCode;

    @Column(name = "d_image_id", precision = 10)
    private Integer imageId;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "db_user_name")
    private String dbUserName;

    @Column(name = "db_password")
    private String dbPassword;

    @Column(name = "creation_status")
    private String creationStatus;

    @Column(name = "is_first_login")
    private String isFirstLogin;

    @Column(name = "production_mgmt")
    private String productionMgmt;

    @Column(name = "shift_mgmt")
    private String shiftMgmt;

    @Column(name = "notification_kitchen")
    private String notificationKitchen;

    @Column(name = "inventory_alter")
    private String inventoryAlter;

    @Column(name = "bill_merge_item")
    private String billMergeItem;

    @Column(name = "number_of_payments")
    private Integer numberOfPayments;

    @Size(max = 36)
    @Column(name = "d_tenant_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String tenantUu;

    @Size(max = 1)
    @Column(name = "is_promotion_enabled", length = 1)
    private String isPromotionEnabled;

    @Size(max = 1)
    @Column(name = "allow_promotion_merge", length = 1)
    private String allowPromotionMerge;

    @Size(max = 1)
    @Column(name = "apply_promotion_on_order", length = 1)
    private String applyPromotionOnOrder;

    @Size(max = 1)
    @Column(name = "apply_auto_promotion", length = 1)
    private String applyAutoPromotion;


    @Column(name = "dish_recall_time")
    private BigDecimal dishRecallTime;

    @Column(name = "agent_code")
    private String agentCode;


}