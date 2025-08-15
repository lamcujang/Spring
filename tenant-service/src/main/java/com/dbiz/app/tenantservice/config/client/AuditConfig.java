package com.dbiz.app.tenantservice.config.client;


import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Configuration
@Component("auditorAwareImpl")
public class AuditConfig  implements AuditorAware<AuditInfo> {
//    @Bean
//    public AuditorAware<Object> auditorProvider() {
//        AuditInfo auditInfo = AuditContext.getAuditInfo();
//        return (AuditorAware<Object>) auditInfo;
//    }

    @Override
    public Optional<AuditInfo> getCurrentAuditor() {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        return Optional.ofNullable(auditInfo);
    }
}
