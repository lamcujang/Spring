package com.dbiz.app.tenantservice.config.client;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class CustomAuditingEntityListener {

    @PrePersist
    public void setAuditInformationOnCreate(AbstractMappedEntity entity) {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        if(entity.getCreated() == null ) {
            entity.setCreated(Instant.now()) ;
        }
        if(entity.getUpdated() == null) {
            entity.setUpdated( Instant.now());
        }
        if (auditInfo != null) {
            if (entity.getCreatedBy() == null) {
                entity.setCreatedBy(auditInfo.getUserId());
            }

        }
    }

    @PreUpdate
    public void setAuditInformationOnUpdate(AbstractMappedEntity entity) {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        if (auditInfo != null) {
            entity.setUpdatedBy(auditInfo.getUserId());
        }
        entity.setUpdated(Instant.now());
    }


}
