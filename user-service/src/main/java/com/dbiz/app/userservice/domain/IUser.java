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
@Table(name = "i_user", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IUser {
    @Id
    @Column(name = "i_user_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i_user_sq")
    @SequenceGenerator(name = "i_user_sq", sequenceName = "i_user_sq", allocationSize = 1)
    private Integer iUserId;

    @Size(max = 64)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 64)
    private String userName;

    @Size(max = 128)
    @Column(name = "full_name", length = 128)
    private String fullName;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @Size(max = 64)
    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @Size(max = 15)
    @Column(name = "user_pin", length = 15)
    private String userPin;

    @Size(max = 10)
    @Column(name = "is_locked", length = 10)
    private String isLocked;

    @Column(name = "date_locked")
    private LocalDate dateLocked;

    @Column(name = "date_last_login")
    private LocalDate dateLastLogin;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private BigDecimal createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private BigDecimal updatedBy;



    @Column(name = "erp_user_id")
    private BigDecimal erpUserId;

    @Size(max = 500)
    @Column(name = "device_token", length = 500)
    private String deviceToken;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Size(max = 1)
    @Column(name = "is_view_cost", length = 1)
    private String isViewCost;

    @Size(max = 1)
    @Column(name = "is_view_other", length = 1)
    private String isViewOther;

    @Size(max = 255)
    @Column(name = "googleid")
    private String googleid;

    @Size(max = 255)
    @Column(name = "zaloid")
    private String zaloid;

    @Size(max = 255)
    @Column(name = "facebookid")
    private String facebookid;

    @Size(max = 255)
    @Column(name = "tiktokid")
    private String tiktokid;

    @Size(max = 255)
    @Column(name = "iserverid")
    private String iserverid;

    @Size(max = 15)
    @Column(name = "verify_code", length = 15)
    private String verifyCode;

    @Size(max = 20)
    @Column(name = "gender", length = 20)
    private String gender;

    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;

    @Size(max = 512)
    @Column(name = "city", length = 512)
    private String city;

    @Size(max = 100)
    @Column(name = "wards", length = 100)
    private String wards;

    @Size(max = 255)
    @Column(name = "appleid")
    private String appleid;

    @Size(max = 255)
    @Column(name = "erp_user_name")
    private String erpUserName;

    @Column(name = "error_message")
    @Type(type = "org.hibernate.type.TextType")
    private String errorMessage;

    @Column(name="d_tenant_id")
    private Integer tenantId;

}