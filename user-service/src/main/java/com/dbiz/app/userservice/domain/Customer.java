package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

@Getter
@Setter
@Entity
@Table(name = "d_customer", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_customer_sq")
    @SequenceGenerator(name = "d_customer_sq", sequenceName = "d_customer_sq", allocationSize = 1)
    @Column(name = "d_customer_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "phone1", length = 15)
    private String phone1;

    @Column(name = "phone2", length = 15)
    private String phone2;

    @Size(max = 255)
    @Column(name = "address1")
    private String address1;

    @Size(max = 255)
    @Column(name = "address2")
    private String address2;

    @Column(name = "customer_point")
    private Long customerPoint;

    @Size(max = 15)
    @Column(name = "tax_code", length = 15)
    private String taxCode;

    @Size(max = 64)
    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    @Size(max = 255)
    @Column(name = "company")
    private String company;



    @Column(name = "birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "d_image_id")
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)// dung CascadeType.ALL de khi luu va xoa image tu xoa theo
//    @JoinColumn(name = "d_image_id", referencedColumnName = "d_image_id", nullable = false)
    private Image image;



    @Size(max = 36)
//    @NotNull
    @Column(name = "d_customer_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String customerUu;

    @Size(max = 1)
    @Column(name = "is_customer_type", length = 1)
    private String isCustomerType;

    @Size(max = 100)
    @Column(name = "area", length = 100)
    private String area;

    @Size(max = 100)
    @Column(name = "wards", length = 100)
    private String wards;


    @Column(name = "d_partner_group_id", precision = 10)
    private Integer partnerGroupId;

    @Column(name = "gender")
    private String gender;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private BigDecimal discount;



    @Column(name="d_tenant_id")
    private Integer tenantId;

    @Column(name = "erp_customer_id", precision = 10)
    private Integer erpCustomerId;

    @Size(max = 1)
    @Column(name = "is_pos_vip", length = 1)
    private String isPosVip;

    @Size(max = 500)
    @Column(name = "partnername", length = 500)
    private String partnerName;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Size(max = 1)
    @Column(name = "is_debt", length = 1)
    private String isDebt;



    @Column(name = "erp_customer_name")
    private String erpCustomerName;

    @Column(name = "city")
    private String city;

}