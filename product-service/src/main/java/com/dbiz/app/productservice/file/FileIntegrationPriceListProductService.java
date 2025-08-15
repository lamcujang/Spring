package com.dbiz.app.productservice.file;

import com.dbiz.app.productservice.constant.AppConstant;
import com.dbiz.app.productservice.domain.PriceList;
import com.dbiz.app.productservice.domain.PriceListProduct;
import com.dbiz.app.productservice.domain.Product;
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
import org.common.dbiz.exception.PosException;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileIntegrationPriceListProductService {

    private final JdbcTemplate jdbcTemplate;
    private final MessageSource messageSource;
    private final QueryEngine queryEngine;
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;
    private final PriceListRepository priceListRepository;
    private final PriceListProductRepository priceListProductRepository;

    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;

    private final static String GROUP_ID = "file-import-group";
    private final static String IMPORT_PRICE_LIST_PRODUCT_TOPIC = "import-file-price-list-product-topic";
    private final static String UPDATE_IMPORT_TOPIC = "update-import-file-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static String defaultN = "0";

    @KafkaListener(groupId = GROUP_ID, topics = IMPORT_PRICE_LIST_PRODUCT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void integratePriceListProduct(ConsumerRecord<String, FileIEDto> consumerRecord, Acknowledgment acknowledgment) {
        String key = consumerRecord.key(); // could be null
        FileIEDto fileIEDto = consumerRecord.value();
        try {
            log.info("ProductService: Received message from Kafka topic: " + IMPORT_PRICE_LIST_PRODUCT_TOPIC);
            log.info("Received message with key: " + key);
            log.info("Received message with value: " + fileIEDto);

            acknowledgment.acknowledge();
            changeTenantAndLocale(fileIEDto);
            log.info("Audit:\nTenant: {}\nUser: {}\nMainTenant: {}",
                    AuditContext.getAuditInfo().getTenantId(),
                    AuditContext.getAuditInfo().getUserId(),
                    AuditContext.getAuditInfo().getMainTenantId());

            String sql = "SELECT * FROM pos.d_i_file_prl_product ORDER BY row_number";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            log.info(results.stream()
                    .map(Map::toString)
                    .collect(Collectors.joining("\n\t")));

            Map<String, String> headers = getHeaders(fileIEDto);
            List<String> errorMessages = new ArrayList<>();
            boolean hasIntegrate = false;
            for (Map<String, Object> row : results) {
                int index = ParseHelper.INT.parse(row.get("row_number"));
                String productName = ParseHelper.STRING.parse(row.get("product_name"));
                BigDecimal costPrice = ParseHelper.BIGDECIMAL.parse(row.get("cost_price"));
                BigDecimal salesPrice = ParseHelper.BIGDECIMAL.parse(row.get("sales_price"));
                String priceListName = ParseHelper.STRING.parse(row.get("pricelist_name"));
                String isActive = ParseHelper.STRING.parse(row.get("is_prlproduct_active"));

                List<Product> products = productRepository.findByName(productName);
                Product product;
                if (products.isEmpty()) {
                    errorMessages.add(messageSource.getMessage(
                            "file.validation.wrong_product",
                            new Object[]{index},
                            LocaleContextHolder.getLocale()));
                    fileIEDto.getErrorRows().add(index);
                    if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                        break;
                    } else {
                        continue;
                    }
                } else {
                    product = products.get(0);
                }

                PriceList priceList = priceListRepository.findByName(priceListName);
                if (priceList == null) {
                    errorMessages.add(messageSource.getMessage(
                            "file.validation.wrong_pricelist",
                            new Object[]{index},
                            LocaleContextHolder.getLocale()));
                    fileIEDto.getErrorRows().add(index);
                    if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                        break;
                    } else {
                        continue;
                    }
                }

                PriceListProduct priceListProductSave = PriceListProduct.builder()
                        .tenantId(AuditContext.getAuditInfo().getTenantId())
                        .orgId(0)
                        .priceListId(priceList.getId())
                        .productId(product.getId())
                        .costPrice(costPrice)
                        .salesPrice(salesPrice)
                        .build();
                priceListProductSave.setIsActive(defaultN.equals(isActive) ? "N" : "Y");

                PriceListProduct priceListProduct = priceListProductRepository.findByPriceListAndProductId(priceList.getId(), product.getId()).orElse(null);
                if (priceListProduct == null) {
                    priceListProduct = priceListProductSave;
                } else {
                    modelMapper.map(priceListProductSave, priceListProduct);
                }
                priceListProductRepository.save(priceListProduct);

                // update standardPrice
                PriceList generalPriceList = priceListRepository.findByGeneralPriceList("Y").get(0);
                if (priceList.getId().equals(generalPriceList.getId())) {
                    priceListProduct.setStandardPrice(priceListProduct.getSalesPrice());

                    List<PriceListProduct> notGeneralPriceListPriceListProducts = priceListProductRepository.findAllByProductIdAndGeneralPriceList(product.getId(), "N");
                    for (PriceListProduct otherPriceListProduct : notGeneralPriceListPriceListProducts) {
                        otherPriceListProduct.setStandardPrice(priceListProduct.getSalesPrice());
                        priceListProductRepository.save(otherPriceListProduct);
                    }
                } else {
                    List<PriceListProduct> otherPriceListProducts = priceListProductRepository.findAllByProductIdAndGeneralPriceList(product.getId(), "Y");
                    if (!otherPriceListProducts.isEmpty()) {
                        priceListProduct.setStandardPrice(otherPriceListProducts.get(0).getSalesPrice());
                    }
                }
                priceListProductRepository.save(priceListProduct);
                hasIntegrate = true;
            }

            if (!errorMessages.isEmpty()) {
                fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                        fileIEDto.getErrorMessage() + "<br>" + String.join("<br>", errorMessages) :
                        String.join("<br>", errorMessages));
            }
            // HasIntegrate && ( No Error || case SKIP )
            if (hasIntegrate && ( fileIEDto.getErrorMessage() == null || "Y".equals(fileIEDto.getIsSkipErrors()) )) {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.SUCCESS);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            } else {
                fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
                kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
            }

        } catch (Exception e) {
            log.error("Caught error in integratePriceListProduct(): ", e);
            fileIEDto.setImportStatus(AppConstant.ImportFileStatus.FAILED);
            fileIEDto.setErrorMessage(fileIEDto.getErrorMessage() != null ?
                    fileIEDto.getErrorMessage() + "<br>" + "Caught error in integrateProduct(): " + e.getMessage() :
                    "Caught error in integratePriceListProduct(): " + e.getMessage()
            );
            kafkaTemplate.send(UPDATE_IMPORT_TOPIC, fileIEDto);
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
}
