package com.dbiz.app.tenantservice.service.db;

import com.dbiz.app.tenantservice.domain.db.Tenant1;

import com.dbiz.app.tenantservice.dto.request.CreateTenantRequestDto;
import com.dbiz.app.tenantservice.dto.request.RenameTenantRequestDto;
import com.dbiz.app.tenantservice.enums.DatabaseCreationStatus;

import com.dbiz.app.tenantservice.mapper.TenantMapper;
import com.dbiz.app.tenantservice.repository.db.Tenant1Repository;
import com.dbiz.app.tenantservice.repository.db.User1Repository;
import com.dbiz.app.tenantservice.repository.db.dao.TenantDao;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.reponse.TenantResponseDto;
import org.common.dbiz.exception.wrapper.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Service
@DependsOn("dataSourceRouting")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Tenant1Service {

    TenantDao tenantDao;
    User1Service userService;
    LiquibaseService liquibaseService;
    Tenant1Repository tenantRepository;
    DataSourceConfigService datasourceConfigService;
    DataSourceRoutingService dataSourceRoutingService;
    DataSourceContextHolder dataSourceContextHolder;
    PlatformTransactionManager transactionManager;
    User1Repository userRepository;

    public Tenant1Service(User1Service userService,
                          Tenant1Repository tenantRepository,
                          LiquibaseService liquibaseService,
                          @Qualifier("mainDataSource") DataSource mainDatasource,
                          DataSourceConfigService datasourceConfigService,
                          DataSourceRoutingService dataSourceRoutingService, DataSourceContextHolder dataSourceContextHolder, PlatformTransactionManager transactionManager, User1Repository userRepository) {
        this.userService = userService;
        this.tenantRepository = tenantRepository;
        this.liquibaseService = liquibaseService;
        this.tenantDao = new TenantDao(mainDatasource);
        this.datasourceConfigService = datasourceConfigService;
        this.dataSourceRoutingService = dataSourceRoutingService;
        this.dataSourceContextHolder = dataSourceContextHolder;
        this.transactionManager = transactionManager;
        this.userRepository = userRepository;
    }

    public Tenant1 getById(Long id) {

        return tenantRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Can't find tenant by id " + id));
    }

    public List<Tenant1> findAll() {

        return tenantRepository.findAll();
    }

    public TenantResponseDto create(CreateTenantRequestDto requestDto) {

        Tenant1 tenant = TenantMapper.INSTANCE.fromRequestDto(requestDto);

        tenant.setCreationStatus(DatabaseCreationStatus.IN_PROGRESS);
        tenant = saveAndFlush(tenant);

        try {

            tenantDao.createTenantDb(requestDto.getName(), requestDto.getUserName(), requestDto.getDbPassword(),"","");
            tenant.setCreationStatus(DatabaseCreationStatus.CREATED);

        } catch (Exception e) {

            log.error("Failed to create tenant db: " + e.getMessage());
            tenant.setCreationStatus(DatabaseCreationStatus.FAILED_TO_CREATE);

        } finally {

            tenant = saveAndFlush(tenant);
        }



//
//        TenantResponseDto responseDto = TenantMapper.INSTANCE.toResponseDto(tenant);
//
//        if (DatabaseCreationStatus.CREATED.equals(tenant.getCreationStatus())) {
//
//            liquibaseService.enableMigrationsToTenantDatasource(requestDto.getDbName(), requestDto.getUserName(), requestDto.getDbPassword());
//
////            Long userId = userService.create(requestDto.getUser(), tenant, List.of(Role.ADMIN)).getId();
//            User1 user = userService.create2(requestDto.getUser(), tenant, List.of(Role.ADMIN));
//            responseDto.setUserId(user.getId());
//
//            Map<Object, Object> configuredDataSources = datasourceConfigService
//                    .configureDataSources();
//
//            dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//
//            dataSourceContextHolder.setCurrentTenantId(tenant.getId());
//            tenant = saveAndFlush(tenant);
////            userRepository.saveAndFlush(user);
//        }



        return null;
    }

//    public TenantResponseDto create(CreateTenantRequestDto requestDto) {
//        TenantResponseDto responseDto = new TenantResponseDto();
//
//        // Step 1: Create tenant in the main database within its own transaction
//        DefaultTransactionDefinition mainTxDefinition = new DefaultTransactionDefinition();
//        mainTxDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        TransactionStatus mainTx = transactionManager.getTransaction(mainTxDefinition);
//
//        try {
//            // Step 1.1: Create tenant record in the main database
//            Tenant1 tenant = TenantMapper.INSTANCE.fromRequestDto(requestDto);
//            tenant.setCreationStatus(DatabaseCreationStatus.IN_PROGRESS);
//            tenant = tenantRepository.saveAndFlush(tenant);
//
//            // Step 1.2: Create tenant's database schema
//            tenantDao.createTenantDb(requestDto.getName(), requestDto.getUserName(), requestDto.getDbPassword());
//            tenant.setCreationStatus(DatabaseCreationStatus.CREATED);
//            tenantRepository.saveAndFlush(tenant);
//
//            transactionManager.commit(mainTx); // Commit the main database transaction
//
//            // Step 2: Enable migrations and switch to tenant database within a new transaction
//            DefaultTransactionDefinition tenantTxDefinition = new DefaultTransactionDefinition();
//            tenantTxDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//            TransactionStatus tenantTx = transactionManager.getTransaction(tenantTxDefinition);
//
//            try {
//                // Step 2.1: Add tenant datasource to routing and switch to tenant database
//                enableTenantDataSourceAndMigrations(tenant, requestDto);
//
//                // Step 2.2: Create an admin user in the tenant database
//                User1 user = userService.create2(requestDto.getUser(), tenant, List.of(Role.ADMIN));
//                responseDto.setUserId(user.getId());
//
//                // Step 2.3: Insert the tenant record into the tenant database
//                insertTenantRecordIntoTenantDatabase(tenant);
//
//                transactionManager.commit(tenantTx); // Commit the tenant database transaction
//
//            } catch (Exception tenantEx) {
//                transactionManager.rollback(tenantTx); // Rollback if tenant database fails
//                log.error("Failed to complete operations in tenant database: " + tenantEx.getMessage());
//                throw tenantEx;
//            }
//
//        } catch (Exception mainEx) {
//            transactionManager.rollback(mainTx); // Rollback if main database fails
//            log.error("Failed to complete operations in main database: " + mainEx.getMessage());
//            throw mainEx;
//        }
//
//        return responseDto;
//    }
//
//    private void enableTenantDataSourceAndMigrations(Tenant1 tenant, CreateTenantRequestDto requestDto) {
//        // Enable migrations on the tenant's datasource
//        liquibaseService.enableMigrationsToTenantDatasource(requestDto.getDbName(), requestDto.getUserName(), requestDto.getDbPassword());
//
//        // Add the new tenant's database to the routing map
//        Map<Object, Object> configuredDataSources = datasourceConfigService.configureDataSources();
//        dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//
//        // Switch to the newly created tenant's data source
//        dataSourceContextHolder.setCurrentTenantId(tenant.getId());
//    }
//
//    private void insertTenantRecordIntoTenantDatabase(Tenant1 tenant) {
//        // Save the tenant record in the tenant database
//        tenantRepository.save(tenant);
//        log.info("Inserted tenant record into tenant's database: " + tenant.getId());
//    }



    public Tenant1 saveAndFlush(Tenant1 tenant) {

        return tenantRepository.saveAndFlush(tenant);

    }

    @Transactional
    public void rename(RenameTenantRequestDto params) {

        tenantRepository.rename(params.getId(), params.getName());
    }

    @Transactional
    public void delete(Long id) {

        tenantRepository.deleteById(id);
    }
}
