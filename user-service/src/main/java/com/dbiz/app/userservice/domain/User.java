package com.dbiz.app.userservice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity

@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true, exclude = {"tenant","image"})
@Data
@Builder
@Table(name = "d_user", schema = "pos")
public final class User extends AbstractMappedEntity  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_user_sq")
	@SequenceGenerator(name = "d_user_sq", sequenceName = "d_user_sq", allocationSize = 1)
	@Column(name = "d_user_id", unique = true, nullable = false, updatable = false)
	private Integer userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "full_name",columnDefinition = "varchar(128) default 'Thanh'")
	private String fullName;

	@Column(name="phone")
	private String phone;

	@Column(name = "password")
	private String password;

//	@Column(name = "d_image_id")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_image_id", referencedColumnName = "d_image_id", nullable = false)
	private Image image;

	@Email(message = "*Input must be in Email format!**")
	private String email;

	@Column(name ="birth_day" )
	private LocalDate birthDay;


	@Column(name = "user_pin")
	private String userPin;

	@Column(name = "is_locked",columnDefinition = "varchar(1) default 'N'")
	private String isLocked ="N";

	@Column(name = "date_locked")
	private LocalDate dateLocked;

	@Column(name = "date_last_login")
	private LocalDate dateLastLogin;

	@Column(name="d_tenant_id")
	private Integer tenantId;

	@Column(name = "erp_user_id")
	private Integer erpUserId;


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

	@Column(name = "googleid")
	private String googleId;

	@Column(name = "zaloid")
	private String zaloId;

	@Column(name = "facebookid")
	private String facebookId;

	@Column(name = "tiktokid")
	private String tiktokId;

	@Column(name = "appleid")
	private String appleId;

	@Column(name = "iserverid")
	private String iServerId;

	@Column(name = "verify_code")
	private String verifyCode;


	@Column(name = "gender")
	private String gender;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;


	@Size(max = 100)
	@Column(name = "wards", length = 100)
	private String wards;

	@Column(name="erp_user_name")
	private String erpUserName;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "grant_type")
	private String grantType;


}









