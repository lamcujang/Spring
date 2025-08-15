package com.dbiz.app.tenantservice.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Data
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.yml")
public class MainDataSourceConfig {

    @Value("${datasource.main.driver}")
    String driver;

    @Value("${datasource.main.url}")
    String url;

    @Value("${datasource.main.username}")
    String username;

    @Value("${datasource.main.password}")
    String password;

    @Bean(value = "mainDataSource")
    public DataSource dataSource(){

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setDriverClassName(driver);
        ds.setPassword(password);
        ds.setDefaultSchema("pos");
        ds.setRollbackOnReturn(false);

        return ds;
    }
}
