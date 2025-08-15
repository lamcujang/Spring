package com.dbiz.app.tenantservice.service.data_source;

import com.dbiz.app.tenantservice.dto.datasource.TenantDbInfoDto;
import com.dbiz.app.tenantservice.enums.DatabaseCreationStatus;
import com.dbiz.app.tenantservice.repository.db.dao.TenantDao;
import com.dbiz.app.tenantservice.service.db.LiquibaseService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataSourceConfigService {

    @NonFinal
    @Value("${datasource.main.name}")
    String mainDatasourceName;

    @NonFinal
    @Value("${datasource.main.username}")
    String mainDatasourceUsername;

    @NonFinal
    @Value("${datasource.main.password}")
    String mainDatasourcePassword;

    @NonFinal
    @Value("${datasource.base-url}")
    String datasourceBaseUrl;

    @NonFinal
    Boolean wasMainDatasourceConfigured = false;

    @NonFinal
    @Value("${spring.application.name}")
    String appName;

    DataSource mainDataSource;
    LiquibaseService liquibaseService;
    private final RedisTemplate redisTemplate;
    static int countTenant = 0;


    public DataSourceConfigService(@Qualifier("mainDataSource") DataSource mainDataSource,
                                   LiquibaseService liquibaseService, RedisTemplate redisTemplate) {
        this.mainDataSource = mainDataSource;
        this.liquibaseService = liquibaseService;
        this.redisTemplate = redisTemplate;
    }

    public Map<Object, Object> configureDataSources() {

        Map<Object, Object> dataSources = new HashMap<>();

        if (!wasMainDatasourceConfigured)  {
            liquibaseService.enableMigrationsToMainDatasource(mainDatasourceName,
                    mainDatasourceUsername, mainDatasourcePassword);
            wasMainDatasourceConfigured = true;
        }

        List<TenantDbInfoDto> dtos = new TenantDao(mainDataSource).getTenantDbInfo(DatabaseCreationStatus.CREATED.getValue());

        dataSources.put(null, mainDataSource);
        for (TenantDbInfoDto dto : dtos) {

            dataSources.put(dto.getId(), configureDataSource(dto));
//            if(appName.equalsIgnoreCase("TENANT-SERVICE")){
//                updateTenantId(dto.getId().intValue());
//            }
        }
        int count = dtos != null ? dtos.size() : 0;
        log.info("count tenant number: " + count);
        log.info("info of tenant: " + dtos.toString());
        log.info("size of tenant: " + dtos.size());
        if(appName.equalsIgnoreCase("TENANT-SERVICE")){
            try{
                updateNumberTenant(count);
            }catch (Exception e) {
                log.error("Error in updating tenant count in redis");

            }
        }
        countTenant = count;
        return dataSources;
    }


    public List<TenantDbInfoDto> configureDataSources2() {


        List<TenantDbInfoDto> dtos = new TenantDao(mainDataSource).getTenantDbInfo(DatabaseCreationStatus.CREATED.getValue());

        TenantDbInfoDto dto = new TenantDbInfoDto();
        dto.setDbName("pos");
        dto.setDbName(mainDatasourceName);
        dtos.add(dto);

        TenantDbInfoDto dtoFnbTemplate = new TenantDbInfoDto();
        dtoFnbTemplate.setDbName("pos");
        dtoFnbTemplate.setDbName("fnb_template");
        dtos.add(dtoFnbTemplate);

        return dtos;
    }

    public void updateNumberTenant(int count){
        redisTemplate.opsForValue().set("tenant", String.valueOf(count));
    }
    public void updateTenantId(int id){
        redisTemplate.opsForValue().set(String.valueOf(id), String.valueOf(id));
    }

    public int getTenantNumbers(){
        return countTenant;
    }

    public int getTenantNumbersRedis(){
        Object ob =redisTemplate.opsForValue().get("tenant");

        if(ob != null){
            String count = String.valueOf(ob);
            return Integer.parseInt(count);
        }

        return 0;
    }

    public int checkExistTenantRedis(int id){
        Object ob =redisTemplate.opsForValue().get(String.valueOf(id));

        if(ob != null){
            String count = String.valueOf(ob);
            return Integer.parseInt(count);
        }

        return 0;
    }

    private DataSource configureDataSource(TenantDbInfoDto dto) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(getUrl(dto));
        dataSource.setUsername(dto.getUserName());
        dataSource.setPassword(dto.getDbPassword());
        dataSource.setSchema("pos");

        return dataSource;
    }

    private String getUrl(TenantDbInfoDto dto) {

        return datasourceBaseUrl + dto.getDbName();
    }
}
