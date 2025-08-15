package com.dbiz.app.tenantservice.repository.db.dao;

import com.dbiz.app.tenantservice.dto.datasource.TenantDbInfoDto;
import com.dbiz.app.tenantservice.enums.DatabaseCreationStatus;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.InputStream;
import java.net.URL;
import java.util.List;



@Slf4j
@Repository
public class TenantDao extends AbstractDao {

    @Value("${datasource.main.driver}")
    String driver;

    @Value("${datasource.base-url}")
    String url;

    @Value("${datasource.main.username}")
    String userNameMain;

    @Value("${datasource.main.password}")
    String passwordMain;


    @Value("${tablespace.location}")
    String tablespaceLocation;

    @Value("${datasource.main.host-ip}")
    String dbHostIp;

    @Value("${datasource.main.host-port}")
    String dbHostPort;

    @Value("${datasource.main.host-username}")
    String dbHostUsername;

    @Value("${datasource.main.host-password}")
    String dbHostPassword;

    @Value("${env.name}")
    String envName;


    @Autowired
    public TenantDao(@Qualifier("mainDataSource") DataSource mainDataSource) {
        super(mainDataSource);
    }

    public List<TenantDbInfoDto> getTenantDbInfo(DatabaseCreationStatus creationStatus) {

        String query = "select d_tenant_id, db_name, db_user_name, db_password " +
                "from d_tenant " +
                "where creation_status = :creationStatus";

        MapSqlParameterSource params = new MapSqlParameterSource("creationStatus", creationStatus.getValue());

        return namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> {

            TenantDbInfoDto dto = new TenantDbInfoDto();

            dto.setId(rs.getLong("d_tenant_id"));
            dto.setDbName(rs.getString("db_name"));
            dto.setUserName(rs.getString("db_user_name"));
            dto.setDbPassword(rs.getString("db_password"));

            return dto;
        });
    }

    public List<TenantDbInfoDto> getTenantDbInfo(String creationStatus) {

        String query = "select d_tenant_id, db_name, db_user_name, db_password " +
                "from d_tenant " +
                "where creation_status = :creationStatus";

        MapSqlParameterSource params = new MapSqlParameterSource("creationStatus", creationStatus);

        return namedParameterJdbcTemplate.query(query, params, (rs, rowNum) -> {

            TenantDbInfoDto dto = new TenantDbInfoDto();

            dto.setId(rs.getLong("d_tenant_id"));
            dto.setDbName(rs.getString("db_name"));
            dto.setUserName(rs.getString("db_user_name"));
            dto.setDbPassword(rs.getString("db_password"));

            return dto;
        });
    }

    public void createTenantDb(String dbName, String userName, String password,String industryCode,String tenantCode) {
        String tablespaceName = industryCode + "_" + tenantCode;
        String pathTableSpace =  createFolderForTableSpace(tablespaceName);

        jdbcTemplate.setDataSource(mainDataSource);
        createUserIfMissing(userName, password);

        String deleteCurrentUser = "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity " +
                " WHERE pg_stat_activity.datname = 'fnb_template' AND pid <> pg_backend_pid()";
        jdbcTemplate.execute(deleteCurrentUser);


        // Create tablespace
        String createTablespaceQuery = String.format("CREATE TABLESPACE %s OWNER %s LOCATION '%s'", tablespaceName, userNameMain, pathTableSpace);
//        String createTablespaceQuery = String.format("CREATE TABLESPACE %s  LOCATION '%s'", tablespaceName, pathTableSpace);
        jdbcTemplate.execute(createTablespaceQuery);
        log.info("Created tablespace: " + pathTableSpace);

        // Create database with specified tablespace
//        String createDbQuery = String.format("CREATE DATABASE %s WITH TEMPLATE fnb_template ", dbName);
        String createDbQuery = String.format(
                "CREATE DATABASE %s WITH TEMPLATE fnb_template TABLESPACE %s",
                dbName,
                tablespaceName
        );
        log.info("Created database: " + dbName);
//        String createDbQuery = "CREATE DATABASE " + dbName ;


        jdbcTemplate.execute(createDbQuery);
        log.info("Created database: " + dbName);

        jdbcTemplate.setDataSource(DataSourceBuilder.create()
                .url(url + dbName) // Adjust to your host and port
                .username(userNameMain)
                .password(passwordMain)
                .build());

        String grantOwnerDBQuery = String.format("ALTER DATABASE %s OWNER TO %s", dbName,userName);
        jdbcTemplate.execute(grantOwnerDBQuery);

        String grantPrivilegesQuery = String.format("GRANT ALL PRIVILEGES ON SCHEMA %s TO %s", "pos", userName);
        jdbcTemplate.execute(grantPrivilegesQuery);

//        String grantSchemasageQuery1 = String.format("GRANT ALL ON schema %s TO %s", "pos",userName);
//        jdbcTemplate.execute(grantSchemasageQuery1);


        String grantSchemasageQuery = String.format("GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA %s TO %s", "pos",userName);
        jdbcTemplate.execute(grantSchemasageQuery);

        // Grant access to all sequences in the 'pos' schema
        String grantSequencePrivilegesQuery = String.format("GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA pos TO %s", userName);
        jdbcTemplate.execute(grantSequencePrivilegesQuery);


        jdbcTemplate.setDataSource(mainDataSource);
    }

    private void createUserIfMissing(String userName, String password) {

        try {

            String createUserQuery = String.format(
                    "DO\n" +
                            "$do$\n" +
                            "BEGIN\n" +
                            "    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '%s') THEN\n" +
                            "        ALTER USER \"%s\" WITH PASSWORD '%s';\n" +
                            "    ELSE\n" +
                            "        CREATE USER \"%s\" WITH CREATEDB CREATEROLE PASSWORD '%s';\n" +
                            "    END IF;\n" +
                            "END\n" +
                            "$do$;",
                    userName, userName, password, userName, password
            );

            jdbcTemplate.execute(createUserQuery);

        } catch (Exception exception) {

            log.error("Error during creation user : {}", exception.getMessage());
        }
    }

    public String createFolderForTableSpace(String code){
        String folderPath = tablespaceLocation + "/" + code;
        try {
            JSch jsch = new JSch();
            Session session = null;
            if(envName != null && envName.equalsIgnoreCase("prod")){
                session = jsch.getSession(dbHostUsername, dbHostIp, Integer.parseInt(dbHostPort));
                session.setPassword(dbHostPassword);
            }else{
                // Key-based authentication for development
//                URL resource = TenantDao.class.getClassLoader().getResource("ssh");
//                if (resource == null) {
//                    throw new IllegalArgumentException("Private key file not found in resources!");
//                }
//                String privateKeyPath = resource.getPath();
//                jsch.addIdentity(privateKeyPath);
                // Load the private key file from the classpath
                ClassPathResource resource = new ClassPathResource("ssh");
                if (!resource.exists()) {
                    throw new IllegalArgumentException("Private key file not found in resources!");
                }

                // Get the InputStream for the private key
                try (InputStream privateKeyStream = resource.getInputStream()) {
                    // Read the private key into a byte array
                    byte[] privateKey = privateKeyStream.readAllBytes();

                    // Add the private key to JSch
                    jsch.addIdentity("PrivateKey", privateKey, null, null);
                }
                session = jsch.getSession(dbHostUsername, dbHostIp, Integer.parseInt(dbHostPort));
                System.out.println("Using key-based authentication for development.");
            }

            // Disable host key checking for simplicity
            session.setConfig("StrictHostKeyChecking", "no");

            System.out.println("Connecting to the server...");
            session.connect();

            System.out.println("Connected. Creating folder for tablespace...");
            String command = String.format("mkdir -p %s && chown -R postgres:postgres %s ",
                    folderPath, folderPath,folderPath);

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            // Capture output (if any)
            StringBuilder output = new StringBuilder();
            int readByte;
            while ((readByte = inputStream.read()) != -1) {
                output.append((char) readByte);
            }

            System.out.println("Command output: " + output);

            int exitStatus = channelExec.getExitStatus();
            if (exitStatus == 0) {
                System.out.println("Folder created successfully at: " + folderPath);
            } else {
                System.err.println("Error creating folder. Exit status: " + exitStatus);
            }

            channelExec.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return folderPath;
    }

}
