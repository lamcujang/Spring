package com.dbiz.app.tenantservice.config.audit;

import com.dbiz.app.tenantservice.listener.AuditEntityInterceptor;
import com.dbiz.app.tenantservice.service.AuditLogProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class InterceptorBeanConfig {

    private final Environment environment;
    private final AuditLogProducer auditLogProducer;
    private final ObjectMapper objectMapper;

    @Bean
    public Interceptor auditEntityInterceptor() {
        System.out.println("Nó là một Bean ");
        return new AuditEntityInterceptor(environment,auditLogProducer,objectMapper);
    }

}
