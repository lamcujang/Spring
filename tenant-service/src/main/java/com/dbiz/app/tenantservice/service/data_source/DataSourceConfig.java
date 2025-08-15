package com.dbiz.app.tenantservice.service.data_source;

import org.hibernate.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@DependsOn("dataSourceRouting")
public class DataSourceConfig {

    private DataSourceRoutingService dataSourceRouting;

    public DataSourceConfig(DataSourceRoutingService dataSourceRouting) {
        this.dataSourceRouting = dataSourceRouting;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return dataSourceRouting;
    }

    @Primary
    @Bean(name="customEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerBean(EntityManagerFactoryBuilder builder, @Qualifier("auditEntityInterceptor") Interceptor auditEntityInterceptor) {
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.session_factory.interceptor", auditEntityInterceptor);
        return builder.dataSource(dataSource()).packages("com.dbiz.app.tenantservice"
                ,"com.dbiz.app.userservice"
                ,"com.dbiz.app.productservice"
                ,"com.dbiz.app.orderservice"
                ,"com.dbiz.app.paymentservice"
                ,"com.dbiz.app.systemservice"
                ,"com.dbiz.app.integrationservice"
                ,"com.dbiz.app.inventoryservice"
                ,"com.dbiz.app.reportservice"
        ) .properties(jpaProperties).build();
    }

    @Bean(name = "customTransactionManager")
    public JpaTransactionManager transactionManager(
        @Autowired @Qualifier("customEntityManager") LocalContainerEntityManagerFactoryBean customEntityManagerFactoryBean) {
        return new JpaTransactionManager(customEntityManagerFactoryBean.getObject());
    }
}

