package com.dbiz.app.userservice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EntityListeners(CustomAuditingEntityListener.class)
@Getter
@Setter
abstract public class AbstractMappedEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@CreatedDate
	@JsonFormat(shape = Shape.STRING)
	@Column(name = "created")
	private Instant created;

	@LastModifiedDate
	@JsonFormat(shape = Shape.STRING)
	@Column(name = "updated")
	private Instant updated;

	@Column(name="created_by")
	@CreatedBy
	private Integer createdBy;

	@Column(name="updated_by")
	@LastModifiedBy
	private Integer updatedBy;


	@Column(name="is_active",columnDefinition = "varchar(1) default 'Y'")
	private String isActive = "Y";

	@Column(name = "d_tenant_id", nullable = false, precision = 10)
	private Integer tenantId;
	@PrePersist
	protected void onCreate() {

//		updatedBy = 0;

		this.updated = Instant.now();
		this.created = Instant.now();



		if (this.createdBy == null) {
			this.createdBy = AuditContext.getAuditInfo().getUserId();
		}
		if (this.updatedBy == null) {
			this.updatedBy = AuditContext.getAuditInfo().getUserId();
		}

	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedBy = AuditContext.getAuditInfo().getUserId();
//		this.created = Instant.now();
		this.updated = Instant.now();
	}

}










