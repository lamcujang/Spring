package com.dbiz.app.tenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@DependsOn("dataSourceRouting")
@EntityScan(basePackages = {"com.dbiz.app.tenantservice"})
@EnableJpaRepositories(basePackages = {"com.dbiz.app.tenantservice"},
        transactionManagerRef = "customTransactionManager",
        entityManagerFactoryRef = "customEntityManager")
@SpringBootApplication(
            exclude={
                    DataSourceAutoConfiguration.class,
                    SqlInitializationAutoConfiguration.class,
                    LiquibaseAutoConfiguration.class
            },
            scanBasePackages = {"com.dbiz.app.tenantservice"})
public class TenantServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TenantServiceApplication.class, args);
    }

}
