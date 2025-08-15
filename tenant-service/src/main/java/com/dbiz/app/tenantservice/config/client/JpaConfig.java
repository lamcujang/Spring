package com.dbiz.app.tenantservice.config.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class JpaConfig {

}
