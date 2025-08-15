package com.dbiz.app.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@DependsOn("dataSourceRouting")
@EntityScan(basePackages = {"com.dbiz.app.paymentservice","com.dbiz.app.tenantervice"})
@EnableJpaRepositories(basePackages = {"com.dbiz.app.paymentservice","com.dbiz.app.tenantservice"},
		transactionManagerRef = "customTransactionManager",
		entityManagerFactoryRef = "customEntityManager")
@SpringBootApplication(
		exclude={
				DataSourceAutoConfiguration.class,
				SqlInitializationAutoConfiguration.class,
				LiquibaseAutoConfiguration.class
		},
		scanBasePackages = {"com.dbiz.app.paymentservice","com.dbiz.app.tenantservice"})
@EnableEurekaClient
public class PaymentServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
	
	
	
}

@RestController
class PaymentController {
	
	@GetMapping
	public String msg() {
		return "Payment controller responding!!";
	}
	
}






