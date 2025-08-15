package com.dbiz.app.userservice.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "i_customer", schema = "pos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ICustomer {
    @Id
    @Column(name = "i_customer_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i_customer_sq")
    @SequenceGenerator(name = "i_customer_sq", sequenceName = "i_customer_sq", allocationSize = 1)
    private Integer iCustomerId;

    @Column(name = "code" )
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "phone1" )
    private String phone1;

    @Column(name = "phone2" )
    private String phone2;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "customer_point")
    private Long customerPoint;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "email")
    private String email;

    @Column(name = "debit_amount")
    private Integer debitAmount;

    @Column(name = "company")
    private String company;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "is_customer_type", length = 1)
    private String isCustomerType;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "wards")
    private String wards;

    @Column(name = "d_partner_group_id")
    private Integer partnerGroupId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "erp_customer_id")
    private Integer erpCustomerId;

    @Size(max = 1)
    @Column(name = "is_pos_vip", length = 1)
    private String isPosVip;

    @Size(max = 500)
    @Column(name = "partnername", length = 500)
    private String partnername;

    @Column(name = "credit_limit")
    private Integer creditLimit;

    @Size(max = 1)
    @Column(name = "is_debt", length = 1)
    private String isDebt;

    @Size(max = 512)
    @Column(name = "city", length = 512)
    private String city;

    @Size(max = 255)
    @Column(name = "erp_customer_name")
    private String erpCustomerName;

    @Column(name = "error_message")
    @Type(type = "org.hibernate.type.TextType")
    private String errorMessage;

    @Column(name="d_tenant_id")
    private Integer tenantId;

}