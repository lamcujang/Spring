package com.dbiz.app.tenantservice.service.db;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConnectionService {

    @NonFinal
    @Value("${datasource.base-url}")
    String datasourceBaseUrl;

    @NonFinal
    @Value("${datasource.main.driver}")
    String mainDatasourceDriverClassName;

    static String USER = "user";
    static String PASSWORD = "password";

//    public static Connection getConnection(PostgreSQLContainer container) throws ConnectException {
//
//        try {
//
//            Properties dbProperties = new Properties();
//            Class.forName(container.getDriverClassName());
//            dbProperties.put(USER, container.getUsername());
//            dbProperties.put(PASSWORD, container.getPassword());
//
//            return DriverManager
//                    .getConnection(container.getJdbcUrl(),
//                            dbProperties);
//
//        } catch (SQLException | ClassNotFoundException e) {
//
//            log.error(e.getMessage());
//
//            throw new ConnectException("Can't connect to DB");
//        }
//    }

    public Connection getConnection(String dbName, String userName, String dbPassword) throws ConnectException {

        try {

            Properties dbProperties = new Properties();

            Class.forName(mainDatasourceDriverClassName);
            dbProperties.put(USER, userName);
            dbProperties.put(PASSWORD, dbPassword);
            dbProperties.put("schema", "pos");
            dbProperties.put("defaultSchema", "pos");

            return DriverManager.getConnection(datasourceBaseUrl + dbName,
                    dbProperties);

        } catch (SQLException | ClassNotFoundException e) {

            log.error(e.getMessage());

            throw new ConnectException("Can't connect to DB");
        }
    }
}
