package com.dbiz.app.systemservice.service.impl;

import com.dbiz.app.systemservice.service.SQLService;
import com.dbiz.app.tenantservice.dto.datasource.TenantDbInfoDto;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class SQLServiceImpl implements SQLService {

    private final DataSourceConfigService dataSourceConfigService;


    @Value("${datasource.main.name}")
    String mainDatasourceName;

    @Value("${datasource.main.username}")
    String mainDatasourceUsername;

    @Value("${datasource.main.password}")
    String mainDatasourcePassword;

    @Value("${datasource.base-url}")
    String datasourceBaseUrl;

    private final EntityManager entityManager;
    private final MessageSource messageSource;

    @Override
    public GlobalReponse modifyAll(MultipartFile file) {

        try{
            String sql = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<TenantDbInfoDto> dtos = dataSourceConfigService
                    .configureDataSources2();
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            for (TenantDbInfoDto dto : dtos) {
                dataSource.setUrl(datasourceBaseUrl + dto.getDbName());
                dataSource.setUsername(mainDatasourceUsername);
                dataSource.setPassword(mainDatasourcePassword);
                dataSource.setSchema("pos");
                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.execute(sql);
                } catch (SQLException e) {
                    // Log and handle failure for this tenant
                    System.err.println("Tenant: " + dto.getDbName() + "_" + dto.getId());
                    System.err.println("Migration failed for tenant: " + e.getMessage());
                }
            }
        }catch (Exception e) {
            log.error("Error modifyAll: ", e);

        }

        return GlobalReponse.builder().status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, new Locale("vi"))).build();
    }

    @Override
    public GlobalReponse grantAccessAll(MultipartFile file) {


        try{
            String sql = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<TenantDbInfoDto> dtos = dataSourceConfigService
                    .configureDataSources2();
            String userName = "";
            String sqlQuery = "";
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(datasourceBaseUrl + mainDatasourceName);
            for (TenantDbInfoDto dto : dtos) {
                if(dto.getDbName().equals(mainDatasourceName) || dto.getDbName().equals("fnb_template")){
                    continue;
                }
                sqlQuery = sql.replace("{{username}}", dto.getUserName()).replace("{{dbname}}", dto.getDbName());
                dataSource.setUrl(datasourceBaseUrl + dto.getDbName());
                dataSource.setUsername(mainDatasourceUsername);
                dataSource.setPassword(mainDatasourcePassword);
                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.execute(sqlQuery);
                } catch (SQLException e) {
                    // Log and handle failure for this tenant
                    System.err.println("Tenant: " + dto.getDbName() + "_" + dto.getId());
                    System.err.println("Migration failed for tenant: " + e.getMessage());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.error("Error modifyAll: ", e);

        }

        return GlobalReponse.builder().status(HttpStatus.OK.value())
                .message(messageSource.getMessage("successfully", null, new Locale("vi"))).build();
    }

    @Override
    @Transactional
    public GlobalReponse clearTrialData() {
        try {
            // Load the SQL file from the resources folder
            ClassPathResource resource = new ClassPathResource("sql/clearTrialData.sql"); // Adjust the path as needed
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Fetch tenant database information
            try {
                // Execute the SQL code
                entityManager.createNativeQuery(sql).executeUpdate();
            } catch (Exception e) {
                // Log and handle failure for this tenant
                e.printStackTrace();
                throw  new PosException(e.getMessage());
            }
        } catch (Exception e) {
            log.error("Error modifyAll: ", e);
            e.printStackTrace();
            throw  new PosException(e.getMessage());
        }

        return GlobalReponse.builder()
                .status(HttpStatus.OK.value())
                .message (messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .build();
    }


}
