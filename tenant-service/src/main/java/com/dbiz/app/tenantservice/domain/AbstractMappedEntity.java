package com.dbiz.app.tenantservice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;

import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import org.common.dbiz.helper.DateHelper;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EntityListeners(CustomAuditingEntityListener.class)
abstract public class   AbstractMappedEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@CreatedDate
	@JsonFormat(shape = Shape.STRING)
	@Column(name = "created",updatable = false)
	private Instant created;
	
	@LastModifiedDate
	@JsonFormat(shape = Shape.STRING)
	@Column(name = "updated")
	private Instant updated;

    @Column(name="created_by",updatable = false)
    @CreatedBy
    private Integer createdBy;

    @Column(name="updated_by")
    @LastModifiedBy
    private Integer updatedBy;

	@Column(name="is_active",columnDefinition = "varchar(1) default 'Y'", insertable = true, updatable = true)
	private String isActive = "Y";


	@PrePersist
	protected void onCreate() {
        if (this instanceof PrintReport) {
            PrintReport printReport = (PrintReport) this;
            if (printReport.getTenantId() == null) {
                printReport.setTenantId(AuditContext.getAuditInfo().getTenantId());
            }
        } else if (this instanceof PosTerminal) {
            PosTerminal posTerminal = (PosTerminal) this;
            if (posTerminal.getTenantId() == null) {
                posTerminal.setTenantId(AuditContext.getAuditInfo().getTenantId());
            }
        }

        this.created = DateHelper.toInstantNowUTC();
        this.updated = DateHelper.toInstantNowUTC();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
        this.createdBy = AuditContext.getAuditInfo().getUserId();

    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = DateHelper.toInstantNowUTC();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
    }


}










