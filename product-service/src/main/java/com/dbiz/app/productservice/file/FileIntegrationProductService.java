package com.dbiz.app.productservice.file;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.*;
import com.dbiz.app.productservice.repository.*;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.FileTemplateDto;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.productDto.ProductLocationDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationProductService {

    private final JdbcTemplate jdbcTemplate;
    private final MessageSource messageSource;
    private final QueryEngine queryEngine;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final TransactionTemplate txTemplate;
//    @Qualifier("customTransactionManager")
//    private final PlatformTransactionManager customTransactionManager;
//    private TransactionTemplate txTemplate;
//    @PostConstruct
//    public void init() {
//        this.txTemplate = new TransactionTemplate(customTransactionManager);
////        txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
////        txTemplate.setTimeout(30);
//    }

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductComboRepository productComboRepository;
    private final AssignOrgRepository assignOrgRepository;
    private final UomRepository uomRepository;
    private final PriceListRepository priceListRepository;
    private final PriceListProductRepository priceListProductRepository;
    private final TaxRepository taxRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductLocationRepository productLocationRepository;
    private final PcTerminalAccessRepository pcTerminalAccessRepository;
    private final LocatorRepository locatorRepository;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;

    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_PRODUCT_TOPIC = "import-file-product-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static String DEFAULT_GROUP_TYPE = "Hàng chế biến";
    private final static String GROUP_TYPE_COMBO_CODE = "Combo - Đóng gói";
    private final static String DEFAULT_PRODUCT_CATEGORY_CODE = "DEFAULT";
    private final static BigDecimal DEFAULT_TAX_RATE = BigDecimal.valueOf(0);
    private final static String defaultN = "0";

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integrateProduct(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("ProductService: Received message from Kafka topic: " + IMPORT_PRODUCT_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sqlProduct = "SELECT * FROM pos.d_i_file_product ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlProduct);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

//            Integer orgId = fileIEDto.getOrgId(); //Integer orgId = ParseHelper.INT.parse(row.get("d_org_id")) // in loop
            Map<String, String> headers = getHeaders(fileIEDto);

            Set<String> processedProductCodes = new HashSet<>();
            boolean hasIntegrate = false;
            List<String> errorMessages = new ArrayList<>();
            // Save row
            for (Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));

                // Original Save product row
//                Product product = saveProduct(row, index, headers, errorMessages, processedProductCodes, fileIEDto);
//                if (product == null) {
//                    fileIEDto.getErrorRows().add(index);
//                    if (!fileIEDto.getIsSkipErrors()) { // stop integrate if case STOP
//                        break;
//                    } continue;
//                }
//                if (!processInventory(product, row, index, headers, errorMessages, processedProductCodes, fileIEDto)) {
//                    fileIEDto.getErrorRows().add(index);
//                    if (!fileIEDto.getIsSkipErrors()) { // stop integrate if case STOP
//                        break;
//                    } continue;
//                }
//                processedProductCodes.add(product.getCode());
//                hasIntegrate = true;

                // Save product row as a transaction
                if(!saveRowProdTx(row, index, headers, errorMessages, processedProductCodes, fileIEDto)) {
                    fileIEDto.getErrorRows().add(index);
                    if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                        break;
                    } continue;
                }
                hasIntegrate = true;

            }

            if (!errorMessages.isEmpty()) {
                fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                        fileIEDto.getErrorMessage() + "<br>" + String.join("<br>", errorMessages) :
                        String.join("<br>", errorMessages));
            }

            log.info("before calling update\nprocessedProductCodes: {}\nhasIntegrate: {}\nfileIEDto.getErrorMessage(): {}, fileIEDto.getIsSkipErrors(): {}",
                    processedProductCodes,
                    hasIntegrate,
                    fileIEDto.getErrorMessage(),
                    fileIEDto.getIsSkipErrors());
            // HasIntegrate && ( No Error || case SKIP )
            if (hasIntegrate && ( fileIEDto.getErrorMessage() == null || "Y".equals(fileIEDto.getIsSkipErrors()) )) {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.SUCCESS);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            } else {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            }

        } catch (Exception e) {
            log.error("Caught error in integrateProduct(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integrateProduct(): " + e.getMessage() :
                    "Caught error in integrateProduct(): " + e.getMessage()
            );
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
        }
    }

    private void logDs() {
        Map<Object, DataSource> dsMap = dataSourceRoutingService.getResolvedDataSources();
        if (dsMap.isEmpty()) {
            log.warn("No DataSources resolved");
        } else {
            List<String> infos = new ArrayList<>();
            dsMap.forEach((keyDs, ds) -> {
                String info;
                if (ds instanceof DriverManagerDataSource) {
                    DriverManagerDataSource dmds = (DriverManagerDataSource) ds;
                    info = String.format("url=%s, user=%s", dmds.getUrl(), dmds.getUsername());
                } else {
                    info = ds.toString();
                }
                infos.add(" • Key-Value: [{" + keyDs + "}] → {" + info + "}");
            });
            log.info("\nDataSource:\n{}", String.join("\n", infos));
        }
    }

    public void changeTenantAndLocale(FileIEDto fileIEDto) {
        try {
            int tenantNumbers = getTenantNumbers();
            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                Map<Object, Object> configuredDataSources = dataSourceConfigService
                        .configureDataSources();

                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
            }
            if (fileIEDto.getTenantId() != 0) {
                dataSourceContextHolder.setCurrentTenantId(Long.valueOf(fileIEDto.getTenantId()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);
            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, fileIEDto.getLanguage(), fileIEDto.getTenantId()));
            LocaleContextHolder.setLocale(Locale.forLanguageTag(fileIEDto.getLanguage()));

        } catch (Exception e) {
            log.error("Caught error in changeTenantAndLocale(): ", e);
            throw new PosException("Caught error in changeTenantAndLocale(): " + e.getMessage(), e);
        }
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private Map<String, String> getHeaders(FileIEDto fileIEDto){
        FileTemplateDto fileTemplate;

        Parameter parameter = new Parameter();
        parameter.add("file_type", fileIEDto.getFileType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("object_type", fileIEDto.getObjectType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_file_template", parameter);

        try {
            if (rs.next()) {
                fileTemplate = FileTemplateDto.builder()
                        .templateJson(rs.getString("template_json"))
                        .iTableName(rs.getString("i_table_name"))
                        .name(rs.getString("name"))
                        .build();

                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> templateColumns = objectMapper.readValue(fileTemplate.getTemplateJson(), new TypeReference<List<Map<String, Object>>>() {});

                Map<String, String> headerNames = new HashMap<>();

                for (Map<String, Object> item : templateColumns) {
                    String header = ParseHelper.STRING.parse(item.get("Header_Name_" + AuditContext.getAuditInfo().getLanguage()));
                    headerNames.put( ParseHelper.STRING.parse(item.get(("Column_Name"))), header);
                }
                log.info("{}", AuditContext.getAuditInfo().getLanguage());
                log.info("headerNames: {}", headerNames);

                return headerNames;
            }
        } catch (Exception e) {
            log.error("Caught error in getHeaders(): ", e);
            throw new PosException("Caught error in getHeaders(): " + e.getMessage(), e);
        }

        return null;
    }

    private Boolean saveRowProdTx(
            Map<String, Object> row,
            Integer index,
            Map<String, String> headers,
            List<String> errorMessages,
            Set<String> processedProductCodes,
            FileIEDto fileIEDto) {

        return txTemplate.execute(status -> {
            try {
                Product product = saveProduct(row, index, headers, errorMessages, processedProductCodes, fileIEDto);
                if (product == null) {
                    status.setRollbackOnly();
                    return false;
                }
                if (!processInventory(product, row, index, headers, errorMessages, processedProductCodes, fileIEDto)) {
                    status.setRollbackOnly();
                    return false;
                }
                processedProductCodes.add(product.getCode());
                return true;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("Row {}: Unexpected error!", index, e);
                errorMessages.add("Row " + index + ": Unexpected error!");
                return false;
            }
        });

    }

    private Product saveProduct(Map<String, Object> row, Integer index, Map<String, String> headers, List<String> errorMessages, Set<String> processedProductCodes, FileIEDto fileIEDto) {
        Integer priceListId = priceListRepository.findPriceListByGeneralPriceList("Y", AuditContext.getAuditInfo().getTenantId(), 0);
        int id = productRepository.getMaxId() + 1;
        String docNo = row.get("product_code") == null ?
                "PROD" + id :
                ParseHelper.STRING.parse(row.get("product_code"));
        Product productSave = Product.builder()
                .tenantId(ParseHelper.INT.parse(row.get("d_tenant_id")))
                .orgId(0)
                .code(docNo)
                .name(ParseHelper.STRING.parse(row.get("product_name")))
                .isShowPos(defaultN.equals(ParseHelper.STRING.parse(row.get("is_direct_sale"))) ? "N" : "Y") // default Y
                .isSales("Y")
                .costprice(ParseHelper.BIGDECIMAL.parse(row.get("cost_price")) == null ? BigDecimal.ZERO //default 0
                        : ParseHelper.BIGDECIMAL.parse(row.get("cost_price")))
                .saleprice(ParseHelper.BIGDECIMAL.parse(row.get("sale_price")) == null ? BigDecimal.ZERO //default 0
                        : ParseHelper.BIGDECIMAL.parse(row.get("sale_price")))
                .isStocked(ParseHelper.STRING.parse(row.get("is_stocked")) == null ? "N" : "Y") //default N
                .preparationTime(ParseHelper.BIGDECIMAL.parse(row.get("max_cooking_wait_time")) == null ? BigDecimal.ZERO //default 0
                        : ParseHelper.BIGDECIMAL.parse(row.get("max_cooking_wait_time")))
                .cookingTime(ParseHelper.BIGDECIMAL.parse(row.get("max_cooking_completion_time")) == null ? BigDecimal.ZERO //default 0
                        : ParseHelper.BIGDECIMAL.parse(row.get("max_cooking_completion_time")))
                .build();
        productSave.setIsActive(defaultN.equals(ParseHelper.STRING.parse(row.get("is_product_active"))) ? "N" : "Y");
        log.info("productSave: {}", productSave);

        Product product = productRepository.findByCode(productSave.getCode());
        if (product != null) {
            log.info("Case update");
            modelMapper.map(productSave, product);
        } else {
            log.info("Case insert");
            product = productSave;
            String errorMessage = validateProduct(product, headers, index);
            if (errorMessage != null) {
                errorMessages.add(errorMessage);
                return null;
            }
        }

        // Tax - default 0%
        BigDecimal taxRate = ParseHelper.BIGDECIMAL.parse(row.get("tax_rate"));
        Tax entityCheck;
        if(taxRate == null){
            entityCheck = this.taxRepository.findByTaxRate(DEFAULT_TAX_RATE);
        }else{
            entityCheck = this.taxRepository.findByTaxRate(taxRate);
            if(entityCheck == null){
                String errorMessage = messageSource.getMessage(
                        "file.validation.wrong_tax",
                        new Object[]{index},
                        LocaleContextHolder.getLocale());
                errorMessages.add(errorMessage);
                return null;
            }
        }
        product.setTaxId(entityCheck.getId());

        //Category
        String productCategoryName = ParseHelper.STRING.parse(row.get("product_category_name"));
        ProductCategory productCategory;
        if(productCategoryName == null){
            productCategory = productCategoryRepository.findByCode(DEFAULT_PRODUCT_CATEGORY_CODE);
        }else{ // Trương hợp không nhập define
            productCategory = productCategoryRepository.findByName(productCategoryName);
            if (productCategory == null) { // Trường hợp nhập sai
                String code = "CAT" + productCategoryRepository.getMaxId() + 1;
                productCategory = ProductCategory.builder()
                        .orgId(0)
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .code(code)
                        .name(productCategoryName)
                        .isMenu("Y")
                        .isSummary("N")
                        .build();
                productCategory = productCategoryRepository.save(productCategory);

                //assign
                pcTerminalAccess(productCategory.getId(), fileIEDto);
            }
        }
        product.setProductCategoryId(productCategory.getId());

        // UoM
        String uomName = ParseHelper.STRING.parse(row.get("uom_name"));

        if(uomName != null) {
            Uom uom = uomRepository.findByName(uomName).orElse(null);
            if (uom == null) {
                uom = Uom.builder()
                        .code(uomName)
                        .name(uomName.substring(0, Math.min(uomName.length(), 5)).strip())
                        .orgId(product.getOrgId())
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .build();
                uom = uomRepository.save(uom);
            }
            product.setUom(uom);
        }else{
            String errorMessage = messageSource.getMessage(
                    "file.validation.missing_uom",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
            errorMessages.add(errorMessage);
            return null;
        }

        // Group Type
        String productGroupType = ParseHelper.STRING.parse(row.get("group_type"));

        String sqlGroupType = "SELECT * FROM pos.d_reference_get_v WHERE name_reference = 'Goods Type'";
        List<Map<String, Object>> groupTypeResults = jdbcTemplate.queryForList(sqlGroupType);
        Map<String, String> groupTypes = new HashMap<>();
        for (Map<String, Object> row1 : groupTypeResults) {
            groupTypes.put(
                    ParseHelper.STRING.parse(row1.get("name")),
                    ParseHelper.STRING.parse(row1.get("value"))
            );
        }

        if(productGroupType == null){
            product.setGroupType(groupTypes.get(DEFAULT_GROUP_TYPE));
        }else if (groupTypes.containsKey(productGroupType)){
            product.setGroupType(groupTypes.get(productGroupType));
        }else{
            String errorMessage = messageSource.getMessage(
                    "file.validation.wrong_group_type",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
            errorMessages.add(errorMessage);
            return null;
        }

        // Save Product
        product = productRepository.save(product);

        // Assign Org
        for (Integer orgAssignId : fileIEDto.getOrgIds()) {
            AssignOrgProduct assignOrg = assignOrgRepository.getByOrgIdAndProductId(orgAssignId, product.getId()).orElse(null);
            if (assignOrg == null) {
                assignOrg = AssignOrgProduct.builder()
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .productId(product.getId())
                        .orgId(orgAssignId).build();
            } else {
                assignOrg.setIsActive("Y");
            }
            assignOrgRepository.save(assignOrg);
        }

        //Mac dinh la bang gia trung tam
        PriceListProduct priceListProduct = priceListProductRepository.findByPriceListAndProductId(priceListId, product.getId()).orElse(null);
        if (priceListProduct == null) {
            priceListProduct = PriceListProduct.builder()
                    .productId(product.getId())
                    .priceListId(priceListId)
                    .costPrice(product.getCostprice())
                    .salesPrice(product.getSaleprice())
                    .lastOrderPrice(BigDecimal.ZERO)
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(product.getOrgId())
                    .build();
            priceListProductRepository.save(priceListProduct);
        }

        if ("Y".equals(fileIEDto.getIsUpdateCom())) {
            // Component
            if (!processedProductCodes.contains(product.getCode())) {
                productComboRepository.updateAllByProductId(product.getId(), "Y");
                productComboRepository.updateAllByProductId(product.getId(), "N");
            }

            String componentName = ParseHelper.STRING.parse(row.get("component_name"));
            BigDecimal componentQty = ParseHelper.BIGDECIMAL.parse(row.get("component_qty"));
            String isComponentActive = ParseHelper.STRING.parse(row.get("is_component_active"));

            if (componentName != null) {
                List<Product> productComponent = productRepository.findByName(componentName);
                if (!productComponent.isEmpty()) {
                    if (componentQty != null) {
                        ProductCombo productCombo = productComboRepository.findByProductIdAndProductComponentIdAndIsItem(
                                product.getId(),
                                productComponent.get(0).getId(),
                                GROUP_TYPE_COMBO_CODE.equalsIgnoreCase(productGroupType) ? "Y" : "N").orElse(null);
                        if (productCombo == null) {
                            productCombo = ProductCombo.builder()
                                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                                    .orgId(0)
                                    .productId(product.getId())
                                    .productComponentId(productComponent.get(0).getId())
                                    .qty(componentQty)
                                    .isItem(GROUP_TYPE_COMBO_CODE.equalsIgnoreCase(productGroupType) ? "Y" : "N")
                                    .build();
                            productCombo.setIsActive(defaultN.equals(isComponentActive) ? "N" : "Y");
                        } else {
                            productCombo.setIsActive(defaultN.equals(isComponentActive) ? "N" : "Y");
                            productCombo.setQty(componentQty);
                        }
                        productComboRepository.save(productCombo);
                    } else {
                        String errorMessage = messageSource.getMessage(
                                "file.validation.missing_bom_quantity",
                                new Object[]{index},
                                LocaleContextHolder.getLocale());
                        errorMessages.add(errorMessage);
                        return null;
                    }
                } else {
                    String errorMessage = messageSource.getMessage(
                            "file.validation.wrong_bom_name",
                            new Object[]{index},
                            LocaleContextHolder.getLocale());
                    errorMessages.add(errorMessage);
                    return null;
                }
            }
        }

        return product;
    }

    private String validateProduct(Product product, Map<String, String> headers, Integer index) {
//        if (product.getCode() == null || product.getCode().isEmpty()) {
//            return messageSource.getMessage(
//                    "file.validation.empty",
//                    new Object[]{index, headers.get("product_code")},
//                    LocaleContextHolder.getLocale());
//        }

        // Validate Name
        if (product.getName() == null || product.getName().isEmpty()) {
            return messageSource.getMessage(
                    "file.validation.missing_product_name",
                    new Object[]{index},
                    LocaleContextHolder.getLocale());
        }

        return null;
    }

    private Boolean processInventory(Product product, Map<String, Object> row, Integer index, Map<String, String> header, List<String> errorMessages, Set<String> processedProductCodes, FileIEDto fileIEDto){

        if(!processedProductCodes.contains(product.getCode())) {
            String warehouseName = ParseHelper.STRING.parse(row.get("warehouse_name"));
            BigDecimal stockQty = ParseHelper.BIGDECIMAL.parse(row.get("stock_qty"));

            Optional<Warehouse> warehouse;
            if (warehouseName == null) {
                log.info("No Warehouse provided, default to center Warehouse");
                warehouse = warehouseRepository.findById(0);
            } else {
                warehouse = warehouseRepository.findByName(warehouseName);
                if (warehouse.isEmpty()) { //Truong hop nhap sai ten warehouse
                    log.info("Warehouse name is not in database");
                    String errorMessage = messageSource.getMessage(
                            "file.validation.wrong_warehouse",
                            new Object[]{index},
                            LocaleContextHolder.getLocale());
                    errorMessages.add(errorMessage);
                    return false;
                }
            }
            log.info("Warehouse: {}", warehouse);

            //Lay ra kho mac dinh
            Optional<Locator> locator = locatorRepository.findByWarehouseIdAndIsDefault(warehouse.get().getId(), "Y");
            log.info("location {}", locator);
            ProductLocation productLocation = null;

            if(locator.isPresent()) {
                //Da ton tai san pham nay thuoc kho nay, locator va org nay chua
                productLocation = productLocationRepository.findByProductIdAndWarehouseIdAndLocatorIdAndOrgId(
                        product.getId(),
                        warehouse.get().getId(),
                        locator.get().getId(),
                        warehouse.get().getOrgId()).orElse(null);

                log.info("Already exists in warehouse {}", productLocation);
                if(productLocation == null) {
                    log.info("Create product location");
                    productLocation = ProductLocation.builder()
                            .productId(product.getId())
                            .warehouseId(warehouse.get().getId())//ID  warehouse
                            .orgId(warehouse.get().getOrgId()) //Org cua warehouse
                            .locatorId(locator.get().getId())
                            .stockQty(stockQty)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .isDefault("N")
                            .build();
                    productLocation.setIsActive("Y");
                    Long existProductLocationDefault = productLocationRepository.countDefaultLocationByProductId(product.getId());
                    if (existProductLocationDefault <= 0) {
                        productLocation.setIsDefault("Y");
                    }
                    productLocation = productLocationRepository.saveAndFlush(productLocation);
                    log.info("product location: {}", productLocation);
                }else{
                    productLocation.setStockQty(stockQty);
                    productLocation = productLocationRepository.saveAndFlush(productLocation);
                    log.info("product location: {}", productLocation);
                }
            }else{
                String errorMessage = "Warehouse does not have Locator!";
                errorMessages.add(errorMessage);
                return false;
            }

            if ("Y".equals(product.getIsStocked()) && "Y".equals(fileIEDto.getIsUpdateInv())) {
                log.info("Call INVENTORY-SERVICE");

                //Ghi nhận số lượng tồn kho, Ghi nhận giao dịch xuất / nhập kho của mặt hàng
                HttpHeaders headers = new HttpHeaders();
                headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
                headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
                headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
                headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
                headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
                List<ProductLocationDto> arr = new ArrayList<>();
                ProductLocationDto productLocationDto = modelMapper.map(productLocation, ProductLocationDto.class);
                productLocationDto.setIsSysDefault("N");
                log.info("productLocationDto {}", productLocationDto);
                arr.add(productLocationDto);
                TransactionDto transactionDto = TransactionDto.builder()
                        .productLocationDtos(arr)
                        .build();

                HttpEntity<TransactionDto> requestEntity = new HttpEntity<>(transactionDto, headers);
                GlobalReponse responseTransaction = this.restTemplate
                        .postForEntity(AppConstant.DiscoveredDomainsApi.INVENTORY_SERVICE_API_CREATE_TRANSACTION,
                                requestEntity,
                                GlobalReponse.class)
                        .getBody();

                if (responseTransaction.getStatus().intValue() != HttpStatus.OK.value()
                        && responseTransaction.getStatus().intValue() != HttpStatus.CREATED.value()) {
                    throw new RuntimeException(responseTransaction.getMessage());
                }
            }

        }
        return true;
    }

    //Lấy ra các diem ban tai 1 chi nhanh
    private List<Integer> getTerminalIdAccessInOrg(Integer orgId){
        String sql = "SELECT d_pos_terminal_id FROM pos.d_pos_org_access where is_active = 'Y' and d_org_id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,orgId);
        List<Integer> posTerminalAccessId = new ArrayList<>();
        for(Map<String, Object> item: result){
            posTerminalAccessId.add(ParseHelper.INT.parse(item.get("d_pos_terminal_id")));
        }

        log.info("ID terminal access Org {}", posTerminalAccessId);

        return posTerminalAccessId;
    }

    private void pcTerminalAccess(Integer productCategoryId, FileIEDto fileIEDto){
        log.info("Assign product category in pos terminal");

        pcTerminalAccessRepository.updateIsActiveByTenantIdAndProductCategoryId("N", AuditContext.getAuditInfo().getTenantId(), productCategoryId);
        fileIEDto.getOrgIds().forEach(orgId -> {
            List<Integer> pcTerminalAccessId = getTerminalIdAccessInOrg(orgId);

            pcTerminalAccessId.forEach(terminalId -> {
                PcTerminalAccess pcCheck = pcTerminalAccessRepository.findByProductCategoryIdAndOrgIdAndPosTerminalId(productCategoryId, orgId, terminalId);
                if (pcCheck == null) {
                    PcTerminalAccess pcTerminalAccess = PcTerminalAccess.builder()
                            .productCategoryId(productCategoryId)
                            .posTerminalId(terminalId)
                            .orgId(orgId)
                            .tenantId(AuditContext.getAuditInfo().getTenantId())
                            .build();
                    pcTerminalAccess.setIsActive("Y");
                    pcTerminalAccessRepository.save(pcTerminalAccess);
                } else {
                    pcCheck.setIsActive("Y");
                    pcTerminalAccessRepository.save(pcCheck);
                }
            });
        });
    }
}

