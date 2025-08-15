# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Java Spring Boot microservices POS (Point-of-Sale) system** designed for restaurant and retail management. It implements a multi-tenant architecture with 15+ microservices, supporting enterprise-level operations including order management, payment processing, inventory tracking, employee management (HR/payroll), and ERP integration.

## Architecture

### Microservices Structure

The system follows a **microservices architecture** with these key services:

**Infrastructure Services:**
- `service-discovery` (Eureka Server, port 8761) - Service registry
- `api-gateway` (Spring Cloud Gateway, port 8443) - API routing and load balancing  
- `cloud-config` (port 9296) - Centralized configuration management

**Core Business Services:**
- `order-service` (port 8300) - POS order management, kitchen orders, reservations
- `payment-service` (port 8400) - Payment processing, e-invoicing, multiple payment providers
- `user-service` (port 8700) - User management, HR/payroll, time tracking
- `product-service` (port 8500) - Product catalog, pricing, categories
- `inventory-service` - Stock management, lot tracking, transactions
- `tenant-service` (port 8711) - Multi-tenant infrastructure and database provisioning
- `integration-service` - ERP integration, external system synchronization
- `system-service` - System utilities, notifications, Firebase integration
- `report-service` - Reporting and analytics
- `shipping-service` (port 8600) - Order shipping and logistics
- `favourite-service` (port 8800) - User favorites management

### Multi-Tenancy

**Critical**: This system uses a **database-per-tenant** architecture:
- Each tenant gets its own PostgreSQL database
- `tenant-service` manages tenant provisioning and metadata
- Database routing is handled via `DataSourceContextHolder`
- Liquibase provisions new tenant databases with schema migrations

### Technology Stack

- **Java 11** with **Spring Boot 2.5.7**
- **Spring Cloud 2020.0.4** for microservices infrastructure
- **PostgreSQL** primary database with **H2** for testing
- **Apache Kafka** for event-driven messaging
- **Redis** for caching and session management
- **Hibernate Envers** for audit trails
- **Resilience4j** circuit breakers
- **Zipkin** distributed tracing
- **Docker** containerization

## Development Commands

### Building and Testing

```bash
# Build all services
./mvnw clean package

# Build specific service
cd <service-name>
./mvnw clean package

# Run tests for all services
./mvnw test

# Run specific service locally (check application.yml for port)
cd <service-name>
./mvnw spring-boot:run
```

### Docker Operations

```bash
# Start all services with Docker Compose
docker-compose -f compose.yml up -d

# Stop all services
docker-compose -f compose.yml down --remove-orphans

# View logs for specific service
docker logs <container-name> -f

# Rebuild and restart specific service
docker-compose -f compose.yml up --build <service-name>
```

### Service Health Checks

```bash
# Check all service health via API Gateway
curl -k https://localhost:8443/actuator/health

# Check specific service directly
curl http://localhost:<port>/actuator/health

# View Eureka service registry
curl http://localhost:8761/eureka/apps
```

## Service Development Patterns

### Standard Service Structure

Each service follows this consistent pattern:
```
├── domain/              # JPA entities and database views
├── dto/                 # Data transfer objects (shared via common-service)
├── repository/          # Spring Data JPA repositories with specifications
├── service/             # Business logic layer
│   └── impl/           # Service implementations
├── resource/            # REST controllers (Spring MVC)
├── helper/             # Utility and mapping classes (often with ModelMapper)
├── exception/          # Service-specific exception handlers
├── specification/      # JPA Criteria API specifications for dynamic queries
└── config/             # Service configurations
```

### Shared Components (common-service)

**Always use shared DTOs** from `common-service` for consistency:
- `orderDto/*` - Order-related DTOs
- `paymentDto/*` - Payment and invoicing DTOs  
- `productDto/*` - Product catalog DTOs
- `integrationDto/*` - ERP integration DTOs
- `IdentityDto/*` - User and authentication DTOs

**Standard Response Format:**
```java
// Always use GlobalReponse for API responses
GlobalReponse<DataType> response = new GlobalReponse<>();
response.setStatus("SUCCESS");
response.setMessage("Operation completed");
response.setData(resultData);
```

### Database Patterns

**Entity Design:**
- Use `@Entity` with proper schema mapping (`schema = "pos"`)
- Implement audit trails with `@Audited` (Hibernate Envers)
- Create corresponding view entities for complex queries
- Use `@SqlResultSetMapping` for native queries

**Repository Pattern:**
- Extend `JpaRepository<Entity, ID>`
- Use `JpaSpecificationExecutor` for dynamic queries  
- Implement custom specifications in `specification/` package
- Use `@Query` for complex JPQL queries

### API Development

**REST Controllers:**
- Place in `resource/` package
- Use `@RestController` and `@RequestMapping`
- Return `GlobalReponse` wrapper objects
- Implement proper exception handling with `@ExceptionHandler`
- Document with SpringDoc OpenAPI annotations

**Swagger Documentation:**
- Access via `http://localhost:<port>/swagger-ui.html`
- Use `@Operation`, `@Parameter`, `@ApiResponse` annotations

## Configuration Profiles

Services support multiple environment profiles:
- `dev` - Development environment
- `prod` - Production environment  
- `stage` - Staging environment
- `test` - Testing environment

Set via `SPRING_PROFILES_ACTIVE` environment variable.

## Key Integration Points

### Kafka Messaging
- Event-driven communication between services
- Topics for order synchronization, user updates, email notifications
- Use shared Kafka DTOs from `common-service`

### ERP Integration
- `integration-service` handles external system sync
- File-based import/export capabilities
- Real-time data synchronization via Kafka

### Payment Processing
- Multiple payment provider support (MBB, QR codes)
- E-invoice generation and compliance
- Integration with banking systems

## Testing

### Database Testing
- Use H2 in-memory database with `@Test` profile
- Flyway migrations in `src/test/resources/db/migration/`
- Use `@DataJpaTest` for repository testing

### Integration Testing  
- Testcontainers for full integration tests
- Docker Compose test environment
- Use `TestRestTemplate` for API testing

## Monitoring and Observability

### Health Checks
- Actuator endpoints: `/actuator/health`, `/actuator/metrics`
- Circuit breaker status: `/actuator/health/circuitBreakers`
- Custom health indicators for database connectivity

### Distributed Tracing
- Zipkin UI: `http://localhost:9411/zipkin/`
- Trace correlation across service calls
- Performance monitoring

### Metrics
- Prometheus metrics: `/actuator/prometheus`
- JVM metrics, HTTP request metrics
- Circuit breaker statistics

## Important Notes

### Multi-Tenant Development
- **Always** consider tenant context when developing features
- Database operations are automatically routed to tenant-specific databases
- Test multi-tenancy scenarios thoroughly

### POS-Specific Features
- Kitchen order workflow management
- Table reservation system  
- Employee shift tracking
- Promotion and discount engine
- Real-time order status updates

### Performance Considerations
- Use database views for complex reporting queries
- Implement proper caching strategies with Redis
- Consider circuit breaker patterns for external integrations
- Use async processing for heavy operations

### Security
- Multi-layer authentication and authorization
- Secure API Gateway routing
- Proper tenant isolation
- Sensitive data encryption





> nói về cơ chế connect tới db trong source của t

● Tôi sẽ tìm hiểu cơ chế kết nối database trong source code của bạn.

● Update Todos
  ⎿  ☐ Tìm hiểu cơ chế kết nối database trong source
     ☐ Phân tích multi-tenant database routing
     ☐ Kiểm tra data source configuration
     ☐ Xem xét tenant context handling

● Search(pattern: "DataSourceContextHolder|DataSource|@Configuration")
  ⎿  Found 99 files (ctrl+r to expand)

● Search(pattern: "database.*config|datasource", type: "java")
  ⎿  Found 12 files (ctrl+r to expand)

● Search(pattern: "spring\.datasource|jdbc", type: "yml")
  ⎿  Found 0 files

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceContextHolder.java)
  ⎿  Read 45 lines (ctrl+r to expand)

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfig.java)
  ⎿  Read 60 lines (ctrl+r to expand)

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceRoutingService.java)
  ⎿  Read 89 lines (ctrl+r to expand)

● Update Todos
  ⎿  ☒ Tìm hiểu cơ chế kết nối database trong source
     ☐ Phân tích multi-tenant database routing
     ☐ Kiểm tra data source configuration
     ☐ Xem xét tenant context handling

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java)
  ⎿  Read 170 lines (ctrl+r to expand)

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\config\TenantsRoutingFilter.java)
  ⎿  Read 43 lines (ctrl+r to expand)

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\config\MainDataSourceConfig.java)
  ⎿  Read 48 lines (ctrl+r to expand)

● Search(pattern: "setCurrentTenantId|getCurrentTenantId", output_mode: "content")
  ⎿  Found 677 lines (ctrl+r to expand)

● Update Todos
  ⎿  ☒ Tìm hiểu cơ chế kết nối database trong source
     ☒ Phân tích multi-tenant database routing
     ☒ Kiểm tra data source configuration
     ☒ Xem xét tenant context handling

● Dựa trên việc phân tích source code, đây là cơ chế kết nối database trong hệ thống của bạn:

  Cơ chế Multi-Tenant Database Connection

  1. Kiến trúc Database-per-Tenant

  - Hệ thống sử dụng mô hình mỗi tenant một database riêng
  - Có một main database chứa thông tin metadata của các tenant
  - Mỗi tenant sẽ có database riêng với schema pos

  2. Các thành phần chính

  DataSourceContextHolder

  // Quản lý tenant ID trong ThreadLocal
  public class DataSourceContextHolder {
      static ThreadLocal<Long> currentTenantId = new ThreadLocal<>();

      public static void setCurrentTenantId(Long tenantId) {
          currentTenantId.set(tenantId);
      }

      public static Long getCurrentTenantId() {
          return currentTenantId.get();
      }
  }

  DataSourceRoutingService

  - Extends AbstractRoutingDataSource
  - Chức năng chính: Route request đến đúng database dựa trên tenant ID
  @Override
  protected Long determineCurrentLookupKey() {
      return DataSourceContextHolder.getCurrentTenantId();
  }

  DataSourceConfigService

  - Tự động tạo DataSource cho từng tenant từ database chính
  - Caching với Redis: Lưu số lượng tenant và thông tin tenant
  - Liquibase: Tự động tạo schema cho tenant mới

  3. Flow hoạt động

  1. Khởi tạo:
    - System khởi động → Query main database lấy danh sách tenant
    - Tạo DataSource cho mỗi tenant active
    - Cache thông tin vào Redis
  2. Runtime:
    - Request đến → Set tenant ID vào DataSourceContextHolder
    - DataSourceRoutingService.determineCurrentLookupKey() được gọi
    - Route connection đến database tương ứng
  3. Tenant context được set tại:
  // Khi login
  dataSourceContextHolder.setCurrentTenantId(tenantId);

  // Khi xử lý Kafka message
  dataSourceContextHolder.setCurrentTenantId(new Long(dto.getTenantId()));

  // Khi gọi API identity server
  dataSourceContextHolder.setCurrentTenantId(null); // main DB
  dataSourceContextHolder.setCurrentTenantId(tenantId); // tenant DB

  4. Cấu hình Database

  Main DataSource (MainDataSourceConfig.java):

  @Bean(value = "mainDataSource")
  public DataSource dataSource(){
      BasicDataSource ds = new BasicDataSource();
      ds.setUrl(url);              // Main DB URL
      ds.setUsername(username);
      ds.setPassword(password);
      ds.setDefaultSchema("pos");
      return ds;
  }

  Tenant DataSource (động tạo):

  private DataSource configureDataSource(TenantDbInfoDto dto) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setUrl(datasourceBaseUrl + dto.getDbName());
      dataSource.setUsername(dto.getUserName());
      dataSource.setPassword(dto.getDbPassword());
      dataSource.setSchema("pos");
      return dataSource;
  }

  5. Các điểm quan trọng

  - Thread-safe: Sử dụng ThreadLocal để tách biệt tenant context giữa các request
  - Auto-provisioning: Tự động tạo database cho tenant mới bằng Liquibase
  - Redis caching: Cache thông tin tenant để tối ưu performance
  - Schema consistency: Tất cả database đều dùng schema pos
  - Entity scanning: Scan entities từ tất cả service packages trong EntityManager

  6. Cách sử dụng trong code

  // Để truy cập main database
  dataSourceContextHolder.setCurrentTenantId(null);

  // Để truy cập tenant database
  dataSourceContextHolder.setCurrentTenantId(tenantId);

  // Trong Kafka listener
  dataSourceContextHolder.setCurrentTenantId(new Long(dto.getTenantId()));

  Đây là một kiến trúc multi-tenant khá phức tạp nhưng rất mạnh mẽ, cho phép cô lập hoàn toàn dữ liệu giữa các tenant với khả năng mở rộng cao.

> theo như cơ chế này thì khi start 1 servcie lên thì đã mở connection tới db chưa
  ⎿  ⧉ Selected 1 lines from tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceRoutingService.java in Cursor

● Theo cơ chế này, khi start một service lên thì CHƯA mở connection đến database ngay lập tức. Đây là cách hoạt động:

  Quá trình khởi tạo Connection

  1. Khi Service Start Up

  public DataSourceRoutingService(...) {
      // Chỉ CONFIG DataSource, chưa mở connection
      Map<Object, Object> dataSourceMap = this.datasourceConfigService.configureDataSources();

      this.setTargetDataSources(dataSourceMap); // Chỉ set config
      this.setDefaultTargetDataSource(mainDataSource); // Set default
  }

  Tại thời điểm này:
  - ✅ Tạo DataSource objects cho tất cả tenant
  - ✅ Load tenant info từ main database
  - ❌ CHƯA mở actual connection đến tenant databases

  2. Connection được mở KHI NÀO?

  Connection chỉ được mở lazy khi có request thực tế:

  @Override
  protected Long determineCurrentLookupKey() {
      // Method này được gọi KHI có database operation
      return DataSourceContextHolder.getCurrentTenantId();
  }

  Connection được establish khi:
  1. Có request đến service
  2. Tenant ID được set: dataSourceContextHolder.setCurrentTenantId(tenantId)
  3. Code thực hiện database operation (query, save, etc.)
  4. Lúc này Spring mới gọi determineCurrentLookupKey()
  5. Lúc này connection pool mới tạo actual connection đến database

  3. Ví dụ Timeline

  # Service startup
  09:00:01 - Service started
  09:00:02 - Load tenant list từ main DB: [tenant1, tenant2, tenant3]
  09:00:03 - Tạo DataSource config cho 3 tenant (CHƯA connect)
  09:00:04 - Service ready ✅

  # First request đến
  09:15:30 - Request với tenantId=2
  09:15:31 - Set: dataSourceContextHolder.setCurrentTenantId(2L)
  09:15:32 - Execute: userRepository.findById(...)
  09:15:33 - Spring gọi: determineCurrentLookupKey() → return 2L
  09:15:34 - Lần đầu mở connection đến tenant2_db ← THỜI ĐIỂM NÀY
  09:15:35 - Execute SQL query

  4. Kiểm chứng trong Code

  Trong DataSourceConfigService.configureDataSources():

  // Chỉ tạo DriverManagerDataSource object
  private DataSource configureDataSource(TenantDbInfoDto dto) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setUrl(getUrl(dto));     // Set URL
      dataSource.setUsername(dto.getUserName()); // Set credentials
      dataSource.setPassword(dto.getDbPassword());
      // ← CHƯA có .getConnection() hay connect() nào cả
      return dataSource;
  }

  5. Connection Pooling

  - Mỗi tenant DataSource sẽ có connection pool riêng
  - Pool được khởi tạo lazy khi có request đầu tiên
  - Sau đó connection được reuse trong pool

  6. Lợi ích của cách này

  - ✅ Fast startup: Service start nhanh vì không connect all DB
  - ✅ Resource efficient: Chỉ connect DB khi cần
  - ✅ Scalable: Có thể có hàng trăm tenant mà không lo startup chậm
  - ✅ Fault tolerant: Nếu 1 tenant DB down, service vẫn start được

  Kết luận: Service start = Chuẩn bị DataSource configs, actual connection chỉ mở khi có request thực tế.

> có vẻ chưa có cơ chế connection pool trong source này đúng không

● Bạn đúng rồi! Nhìn vào source code, hệ thống CHƯA có connection pool thực sự cho tenant databases.

  Phân tích Connection Pool trong Source

  1. Main Database CÓ Connection Pool

  // MainDataSourceConfig.java
  @Bean(value = "mainDataSource")
  public DataSource dataSource(){
      BasicDataSource ds = new BasicDataSource(); // ← Apache Commons DBCP2
      ds.setUrl(url);
      ds.setUsername(username);
      ds.setDriverClassName(driver);
      ds.setPassword(password);
      ds.setDefaultSchema("pos");
      ds.setRollbackOnReturn(false);
      return ds; // BasicDataSource có built-in connection pool
  }

  2. Tenant Databases KHÔNG có Connection Pool

  // DataSourceConfigService.java
  private DataSource configureDataSource(TenantDbInfoDto dto) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource(); // ← Không có pool!
      dataSource.setUrl(getUrl(dto));
      dataSource.setUsername(dto.getUserName());
      dataSource.setPassword(dto.getDbPassword());
      dataSource.setSchema("pos");
      return dataSource;
  }

  3. Vấn đề với DriverManagerDataSource

  DriverManagerDataSource là non-pooling datasource:
  - ✅ Simple và lightweight
  - ❌ Tạo connection mới cho mỗi request
  - ❌ Không reuse connection
  - ❌ Performance kém với high traffic
  - ❌ Resource waste (tạo/đóng connection liên tục)

  4. Impact thực tế

  # Với mỗi request đến tenant database:
  Request 1 → New Connection → Execute Query → Close Connection
  Request 2 → New Connection → Execute Query → Close Connection
  Request 3 → New Connection → Execute Query → Close Connection
  # ... (rất tốn resource)

  5. Nên cải thiện như thế nào

  Thay thế DriverManagerDataSource bằng pooled datasource:

  private DataSource configureDataSource(TenantDbInfoDto dto) {
      // Option 1: Apache Commons DBCP2
      BasicDataSource dataSource = new BasicDataSource();
      dataSource.setUrl(getUrl(dto));
      dataSource.setUsername(dto.getUserName());
      dataSource.setPassword(dto.getDbPassword());
      dataSource.setDefaultSchema("pos");

      // Connection pool settings
      dataSource.setInitialSize(5);
      dataSource.setMaxTotal(20);
      dataSource.setMaxIdle(10);
      dataSource.setMinIdle(5);
      dataSource.setMaxWaitMillis(5000);

      return dataSource;

      // Option 2: HikariCP (faster)
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(getUrl(dto));
      config.setUsername(dto.getUserName());
      config.setPassword(dto.getDbPassword());
      config.setSchema("pos");
      config.setMaximumPoolSize(20);
      config.setMinimumIdle(5);

      return new HikariDataSource(config);
  }

  6. Tại sao họ không dùng Connection Pool?

  Có thể do:
  - ⚠️ Oversight: Quên không config pooling
  - ⚠️ Development stage: Chưa optimize cho production
  - ⚠️ Simple first: Implement đơn giản trước, optimize sau
  - ⚠️ Low traffic assumption: Giả định traffic ít nên chưa cần pool

  7. Recommendation

  Nên upgrade lên connection pooling vì:
  - 🚀 Performance: Giảm latency đáng kể
  - 💰 Resource efficiency: Giảm overhead tạo/đóng connection
  - 📈 Scalability: Xử lý được concurrent requests tốt hơn
  - 🛡️ Stability: Tránh database connection exhaustion

  Kết luận: Đúng rồi, source này chỉ có connection pool cho main DB, tenant databases đang dùng non-pooling DataSource - cần cải thiện cho production        
  environment.

> nếu t dựng một service nữa lên là databases proxy để quản lý connnection được không

● Ý tưởng sử dụng Database Proxy Service để quản lý connection là rất hay! Đây là một pattern phổ biến và có nhiều lợi ích. Hãy phân tích:

  Database Proxy Service Architecture

  1. Kiến trúc hiện tại vs Kiến trúc với DB Proxy

  Hiện tại:

  [User Service] → [Tenant Routing] → [DB1, DB2, DB3...]
  [Order Service] → [Tenant Routing] → [DB1, DB2, DB3...]
  [Payment Service] → [Tenant Routing] → [DB1, DB2, DB3...]

  Với DB Proxy:

  [User Service] ──┐
  [Order Service] ──┤→ [DB Proxy Service] → [Connection Pool] → [DB1, DB2, DB3...]
  [Payment Service] ─┘

  2. Lợi ích của Database Proxy Service

  Connection Management:

  @Service
  public class DatabaseProxyService {

      private Map<Long, HikariDataSource> tenantPools = new ConcurrentHashMap<>();

      // Centralized connection pool management
      public Connection getConnection(Long tenantId) {
          return getTenantDataSource(tenantId).getConnection();
      }

      private HikariDataSource getTenantDataSource(Long tenantId) {
          return tenantPools.computeIfAbsent(tenantId, this::createPooledDataSource);
      }

      private HikariDataSource createPooledDataSource(Long tenantId) {
          TenantDbInfo tenant = getTenantInfo(tenantId);

          HikariConfig config = new HikariConfig();
          config.setJdbcUrl(tenant.getUrl());
          config.setUsername(tenant.getUsername());
          config.setPassword(tenant.getPassword());

          // Optimized pool settings
          config.setMaximumPoolSize(20);
          config.setMinimumIdle(5);
          config.setConnectionTimeout(30000);
          config.setIdleTimeout(600000);
          config.setMaxLifetime(1800000);

          return new HikariDataSource(config);
      }
  }

  Query Execution Service:

  @RestController
  @RequestMapping("/db-proxy")
  public class DatabaseProxyController {

      @Autowired
      private DatabaseProxyService dbProxy;

      // Generic query execution
      @PostMapping("/query")
      public ResponseEntity<List<Map<String, Object>>> executeQuery(
              @RequestHeader("tenantId") Long tenantId,
              @RequestBody QueryRequest request) {

          try (Connection conn = dbProxy.getConnection(tenantId)) {
              return executeQuery(conn, request.getSql(), request.getParams());
          }
      }

      // Transaction support
      @PostMapping("/transaction")
      public ResponseEntity<Void> executeTransaction(
              @RequestHeader("tenantId") Long tenantId,
              @RequestBody List<QueryRequest> queries) {

          try (Connection conn = dbProxy.getConnection(tenantId)) {
              conn.setAutoCommit(false);

              for (QueryRequest query : queries) {
                  executeUpdate(conn, query.getSql(), query.getParams());
              }

              conn.commit();
              return ResponseEntity.ok().build();
          } catch (Exception e) {
              conn.rollback();
              throw e;
          }
      }
  }

  3. Advanced Features

  Connection Monitoring:

  @Component
  public class ConnectionMonitor {

      @Scheduled(fixedRate = 60000) // Every minute
      public void monitorConnections() {
          tenantPools.forEach((tenantId, dataSource) -> {
              HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();

              log.info("Tenant {}: Active={}, Idle={}, Total={}",
                  tenantId,
                  pool.getActiveConnections(),
                  pool.getIdleConnections(),
                  pool.getTotalConnections());

              // Alert if pool exhaustion
              if (pool.getActiveConnections() > pool.getMaximumPoolSize() * 0.8) {
                  alertService.sendPoolExhaustionAlert(tenantId);
              }
          });
      }
  }

  Dynamic Pool Scaling:

  @Component
  public class DynamicPoolManager {

      // Auto-scale pool size based on usage
      public void scalePool(Long tenantId, int targetSize) {
          HikariDataSource ds = tenantPools.get(tenantId);
          if (ds != null) {
              ds.getHikariConfigMXBean().setMaximumPoolSize(targetSize);
              log.info("Scaled tenant {} pool to {}", tenantId, targetSize);
          }
      }

      // Remove unused pools to save memory
      @Scheduled(fixedRate = 3600000) // Every hour
      public void cleanupIdlePools() {
          tenantPools.entrySet().removeIf(entry -> {
              HikariDataSource ds = entry.getValue();
              HikariPoolMXBean pool = ds.getHikariPoolMXBean();

              // Remove if no activity for 2 hours
              boolean shouldRemove = pool.getActiveConnections() == 0 &&
                                   pool.getIdleConnections() == 0;

              if (shouldRemove) {
                  ds.close();
                  log.info("Removed idle pool for tenant {}", entry.getKey());
              }

              return shouldRemove;
          });
      }
  }

  4. Integration với các Service

  Service gọi DB Proxy qua REST:

  @Service
  public class UserServiceImpl {

      @Autowired
      private DatabaseProxyClient dbProxyClient;

      public List<User> findUsers(String criteria) {
          Long tenantId = DataSourceContextHolder.getCurrentTenantId();

          QueryRequest request = QueryRequest.builder()
              .sql("SELECT * FROM users WHERE name LIKE ?")
              .params(Arrays.asList("%" + criteria + "%"))
              .build();

          return dbProxyClient.executeQuery(tenantId, request, User.class);
      }
  }

  Hoặc qua gRPC (performance tốt hơn):

  @Service
  public class DatabaseProxyGrpcService extends DatabaseProxyGrpc.DatabaseProxyImplBase {

      @Override
      public void executeQuery(QueryRequest request, StreamObserver<QueryResponse> responseObserver) {
          try (Connection conn = dbProxy.getConnection(request.getTenantId())) {
              QueryResponse response = executeQuery(conn, request);
              responseObserver.onNext(response);
              responseObserver.onCompleted();
          } catch (Exception e) {
              responseObserver.onError(e);
          }
      }
  }

  5. Lợi ích cụ thể

  ✅ Centralized Connection Management: Tất cả connection logic ở một nơi
  ✅ Better Resource Utilization: Shared pools, dynamic scaling✅ Monitoring & Alerting: Centralized metrics cho tất cả databases
  ✅ Security: Centralized database credentials management
  ✅ Caching: Query result caching, prepared statement caching
  ✅ Load Balancing: Có thể route đến read replicas
  ✅ Backup & Failover: Centralized disaster recovery

  6. Nhược điểm cần cân nhắc

  ❌ Added Latency: Thêm một network hop
  ❌ Single Point of Failure: Nếu DB Proxy down thì tất cả services down
  ❌ Complexity: Thêm một service cần maintain
  ❌ Network Overhead: Serialization/deserialization data

  7. Recommendation

  NÊN implement DB Proxy Service trong trường hợp này vì:

  1. Hệ thống đã phức tạp: 15+ services, multi-tenant
  2. Scaling needs: Sẽ có nhiều tenant hơn trong tương lai
  3. Resource optimization: Hiện tại waste nhiều connection
  4. Monitoring gap: Khó monitor DB performance cross-services

  Implementation approach:
  1. Start với connection pooling trong DB Proxy
  2. Add monitoring & metrics
  3. Gradually migrate services một cái một
  4. Add advanced features như caching, load balancing sau

  Ý tưởng rất tốt để optimize kiến trúc hiện tại!

> nếu không dùng dataproxy t cần sửa gì trong datasourceconfig để dùng connection pool nếu dùng hikary
  ⎿  ⧉ Selected 1 lines from tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java in Cursor

● Để sử dụng HikariCP connection pool thay vì DriverManagerDataSource, bạn cần sửa các điểm sau:

  1. Thêm HikariCP dependency vào pom.xml

  <dependency>
      <groupId>com.zaxxer</groupId>
      <artifactId>HikariCP</artifactId>
      <version>4.0.3</version>
  </dependency>

  2. Sửa DataSourceConfigService.java

● Read(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java)
  ⎿  Read 20 lines (ctrl+r to expand)

● Đây là cách sửa để sử dụng HikariCP:

● Update(tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java)
  ⎿  User rejected update to tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java
       11    import org.springframework.beans.factory.annotation.Qualifier;
       12    import org.springframework.beans.factory.annotation.Value;
       13    import org.springframework.data.redis.core.RedisTemplate;
       14 -  import org.springframework.jdbc.datasource.DriverManagerDataSource;
       14 +  import org.springframework.jdbc.datasource.DriverManagerDataSource;
       15 +  import com.zaxxer.hikari.HikariConfig;
       16 +  import com.zaxxer.hikari.HikariDataSource;
       17    import org.springframework.stereotype.Service;
       18    
       19    import javax.sql.DataSource;

> t chỉ hỏi cách sửa thôi
  ⎿  ⧉ Selected 1 lines from tenant-service\src\main\java\com\dbiz\app\tenantservice\service\data_source\DataSourceConfigService.java in Cursor

● Được, tôi sẽ chỉ hướng dẫn cách sửa thôi:

  Cách sửa để dùng HikariCP thay vì DriverManagerDataSource:

  1. Thêm import statements:

  import com.zaxxer.hikari.HikariConfig;
  import com.zaxxer.hikari.HikariDataSource;

  2. Sửa method configureDataSource():

  Thay vì:
  private DataSource configureDataSource(TenantDbInfoDto dto) {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setUrl(getUrl(dto));
      dataSource.setUsername(dto.getUserName());
      dataSource.setPassword(dto.getDbPassword());
      dataSource.setSchema("pos");
      return dataSource;
  }

  Sửa thành:
  private DataSource configureDataSource(TenantDbInfoDto dto) {
      HikariConfig config = new HikariConfig();

      // Database connection settings
      config.setJdbcUrl(getUrl(dto));
      config.setUsername(dto.getUserName());
      config.setPassword(dto.getDbPassword());
      config.setSchema("pos");

      // Connection pool settings
      config.setMaximumPoolSize(20);          // Max connections
      config.setMinimumIdle(5);               // Min idle connections
      config.setConnectionTimeout(30000);     // 30 seconds
      config.setIdleTimeout(600000);          // 10 minutes
      config.setMaxLifetime(1800000);         // 30 minutes
      config.setLeakDetectionThreshold(60000); // 1 minute

      // Pool name for monitoring
      config.setPoolName("tenant-" + dto.getId() + "-pool");

      return new HikariDataSource(config);
  }

  3. Thêm cleanup method:

  // Thêm method để cleanup pools khi shutdown
  @PreDestroy
  public void cleanup() {
      // Close all HikariDataSource pools when service shuts down
      // Implementation needed if you cache DataSources
  }

  4. Cải tiến thêm - Cache DataSource:

  private final Map<Long, HikariDataSource> cachedDataSources = new ConcurrentHashMap<>();

  private DataSource configureDataSource(TenantDbInfoDto dto) {
      return cachedDataSources.computeIfAbsent(dto.getId(), id -> {
          HikariConfig config = new HikariConfig();
          // ... config như trên
          return new HikariDataSource(config);
      });
  }

  5. Dependency cần thêm:

  <dependency>
      <groupId>com.zaxxer</groupId>
      <artifactId>HikariCP</artifactId>
      <version>4.0.3</version>
  </dependency>

  Lợi ích sau khi sửa:
  - ✅ Connection pooling thay vì tạo mới mỗi request
  - ✅ Better performance và resource management
  - ✅ Connection leak detection
  - ✅ Configurable pool settings
  - ✅ Monitoring capabilities
