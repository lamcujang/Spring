package com.dbiz.app.integrationservice.file.excel;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.file.service.strategy.FileProcessorStrategy;
import com.dbiz.app.integrationservice.file.validation.Validator;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.common.dbiz.dto.integrationDto.FileTemplateDto;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component(AppConstant.FileType.EXCEL)
@Slf4j
@RequiredArgsConstructor
public class ExcelProcessor implements FileProcessorStrategy {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;
    private final Validator validator;

    @Override
    public Map<String, Object> importFile(FileIEDto fileIEDto) {

        Map<String, Object> output = new HashMap<>();
        {
            output.put("hasImport", false);
            output.put("errorMessage", null);
        }

        try (InputStream inputStream = Files.newInputStream(Paths.get(fileIEDto.getFileUrl()));
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            FileTemplateDto fileTemplate;
            try {
                fileTemplate = getFileTemplate(fileIEDto.getFileType(), fileIEDto.getObjectType());
            } catch (Exception e) {
                log.error("Caught error from calling getFileTemplate() in importFile(): ", e);
                output.put("errorMessage", "Caught getFileTemplate() exception: " + e.getMessage());
                return output;
            }
            if (fileTemplate == null) {
                output.put("errorMessage", messageSource.getMessage("file.file_template.not_found", null, LocaleContextHolder.getLocale()));
                return output;
                //throw new PosException(messageSource.getMessage("file.file_template.not_found", null, LocaleContextHolder.getLocale()));
            }

            // Parse JSON template to get column mapping
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> templateColumns = objectMapper.readValue(fileTemplate.getTemplateJson(), new TypeReference<List<Map<String, Object>>>() {});
            // Sort the list on "Index" in case templateJson is not written in Index order so that columnNames is ordered for FilteredIndexDataTypes and columns
            templateColumns.sort(Comparator.comparingInt(item -> (Integer) item.get("Index")));

            Map<Integer, ColumnMetaDto> indexMeta = new LinkedHashMap<>(); // LinkedHashMap để giữ thứ tự

            // Build index-to-columnName mapping
            Map<Integer, String> indexRequired = new TreeMap<>();
            List<String> columnNames = new ArrayList<>();
            for (Map<String, Object> item : templateColumns) {
                columnNames.add(ParseHelper.STRING.parse(item.get("Column_Name")));
                Integer index = (Integer) item.get("Index");
                String isRequired = (String) item.get("is_required");
                indexRequired.put(index, isRequired);
                indexMeta.put(index, ColumnMetaDto.builder().isRequired(isRequired).build());
            }
            log.info("indexRequired: {}", indexRequired);

            Map<Integer, String> indexDataTypes = getDataTypeMapping(fileTemplate, columnNames);
            getIndexColumnMeta(fileTemplate, indexMeta, columnNames);

            int totalColumns = columnNames.size();

            // Extract Excel header names to notify validation violations
            List<String> headerNames = new ArrayList<>();
            Row headerRow = sheet.getRow(0); // Get the first row (header row)
            if (!isRowEmpty(headerRow)) {
                for (int i = 0; i < totalColumns; i++) {
                    Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    headerNames.add(cell.getStringCellValue().strip()); // Convert cell to string and add to list
                }
            }

            columnNames.add("d_tenant_id");
            columnNames.add("d_org_id");
            columnNames.add("created");
            columnNames.add("created_by");
            columnNames.add("updated");
            columnNames.add("updated_by");
            columnNames.add("is_active");
            columnNames.add("row_number");

            // Prepare column name string for SQL
            StringJoiner columns = new StringJoiner(", ");
            for (String column : columnNames) {
                columns.add(column);
            }

            // Process Excel file
            List<Object[]> values = new ArrayList<>();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            List<String> errorMessages = new ArrayList<>();
            int rowCount = 0;
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row)) continue;

                rowCount++;
                if (validator.validateOneErrorForEachCell(row, indexDataTypes, indexRequired, indexMeta, headerNames, errorMessages)) { // if row is valid
                    List<Object> rowValues = new ArrayList<>();
                    for (int colIndex = 0; colIndex < totalColumns ; colIndex++) {
                        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Object value = processCellValue(cell, evaluator);
                        rowValues.add(value);
                        //log.info("Row: {} - Column header: {} - Value: {} - Cell type: {}", rowIndex + 1, headerNames.get(colIndex), value, cell.getCellType());
                    }
                    //log.info("---Next row---");

                    rowValues.add(AuditContext.getAuditInfo().getTenantId()); // d_tenant_id
                    rowValues.add(0); // d_org_id
                    rowValues.add(Timestamp.from(Instant.now())); // created
                    rowValues.add(AuditContext.getAuditInfo().getUserId()); // created_by
                    rowValues.add(Timestamp.from(Instant.now())); // updated
                    rowValues.add(AuditContext.getAuditInfo().getUserId()); // updated_by
                    rowValues.add("Y"); // is_active
                    rowValues.add(rowIndex+1); // row_number
                    // can use LocalDateTime.now() instead of Timestamp.from(Instant.now())

                    values.add(rowValues.toArray());

                    output.put("hasImport", true);
                } else {
                    fileIEDto.getErrorRows().add(rowIndex+1);
                    if ("N".equals(fileIEDto.getIsSkipErrors())) { // stop integrate if case STOP
                        break;
                    }
                }
            }
            fileIEDto.setRowCount(rowCount);

            log.info("Row count: {}", rowCount);
            // Throw error if sheet has no data
            if (rowCount == 0) {
                output.put("errorMessage", messageSource.getMessage("file.excel_data.not_found", null, LocaleContextHolder.getLocale()));
                log.info("errorMessage: {}", messageSource.getMessage("file.excel_data.not_found", null, LocaleContextHolder.getLocale()));
                return output;
                //throw new PosException(messageSource.getMessage("file.excel_data.not_found", null, LocaleContextHolder.getLocale()));
            }

            log.info("All Errors:\n{}", String.join("\n", errorMessages));
            // Throw error if any validation fails
            if (!errorMessages.isEmpty()) {
                output.put("errorMessage", String.join("<br>", errorMessages));
                log.info("errorMessage: {}", String.join("<br>", errorMessages));
                //throw new PosException(validator.getErrorMessages().toString());
            }

            // Insert data
            log.info("All Columns: {}", columns);
            log.info("All Values:\n{}", values.stream()
                    .map(Arrays::toString)
                    .collect(Collectors.joining("\n")));
            deleteRows(fileTemplate.getITableName()); // Delete old data
            if (!values.isEmpty()) {
                saveRows(fileTemplate.getITableName(), columns.toString(), values);
            }

        } catch (Exception e) {
            log.error("Caught error in importFile(): ", e);
            output.put("errorMessage", "Caught error in importFile(): " + e.getMessage());
            return output;
            //throw new PosException(e.getMessage());
        }

        return output;

//        return GlobalReponse.builder()
//                .status(HttpStatus.OK.value())
//                .message(messageSource.getMessage("file.import.success", null, LocaleContextHolder.getLocale()))
//                .errors("").build();
    }

    @Override
    public GlobalReponse exportFile(FileIEDto dto) {

        String excelBase64;
        FileTemplateDto fileTemplate;
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("ExcelData");

            fileTemplate = getFileTemplate(dto.getFileType(), dto.getObjectType());
            if (fileTemplate == null) {
                throw new PosException(messageSource.getMessage("file.file_template.not_found", null, LocaleContextHolder.getLocale()));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> templateColumns = objectMapper.readValue(fileTemplate.getTemplateJson(), new TypeReference<List<Map<String, Object>>>() {
            });
            // Sort the list on "Index" in case templateJson is not written in Index order
            templateColumns.sort(Comparator.comparingInt(item -> (Integer) item.get("Index")));

            List<String> headerNames = new ArrayList<>();
            for (Map<String, Object> item : templateColumns) {
                log.info("{}", AuditContext.getAuditInfo().getLanguage());
                String header = ParseHelper.STRING.parse(item.get("Header_Name_" + AuditContext.getAuditInfo().getLanguage()));
                headerNames.add(header);
            }
            log.info("headerNames: {}", headerNames);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Nền xám nhẹ
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headerNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headerNames.get(i)); // Set header text
                cell.setCellStyle(headerStyle);
            }

            Row emptyRow = sheet.createRow(1);
            for (int i = 0; i < headerNames.size(); i++) {
                emptyRow.createCell(i).setCellValue("");
            }

            // Define table range (A1 to last row and last column)
            String rangeRef = "A1:" + CellReference.convertNumToColString(headerNames.size() - 1) + (sheet.getLastRowNum() + 1);
            log.info("RangeRef: {}", rangeRef);
            AreaReference areaRef = new AreaReference(rangeRef, workbook.getSpreadsheetVersion());
            // Create table and set reference
            XSSFTable table = ((XSSFSheet) sheet).createTable(areaRef);
            table.setDisplayName("ExportTable");
            table.setName("ExportData");

            // Get the underlying XML table and Set table style and columns
            CTTable cttable = table.getCTTable();
            CTTableStyleInfo ctstyle = cttable.addNewTableStyleInfo();
            ctstyle.setName("TableStyleMedium2"); // Built-in table style
            ctstyle.setShowColumnStripes(false);
            ctstyle.setShowRowStripes(true);

            for (int i = 0; i < headerNames.size(); i++) {
                cttable.getTableColumns().getTableColumnArray(i).setId(i + 1);
            }

            // Add auto filter
            cttable.addNewAutoFilter().setRef(rangeRef);

            // Auto-size columns after writing all data
//            for (int colIndex = 0; colIndex < headerNames.size(); colIndex++) {
//                sheet.autoSizeColumn(colIndex);
//            }

            // Convert workbook to Base64
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream); // Write the workbook content to the output stream
                byte[] excelBytes = outputStream.toByteArray(); // Convert to byte array
                excelBase64 = Base64.getEncoder().encodeToString(excelBytes);
            }
        } catch (Exception e) {
            log.error("Caught error in exportFile(): ", e);
            throw new PosException(e.getMessage());
        }

//        String fileName = fileTemplate.getFileName() != null ?
//                fileTemplate.getFileName() :
//                fileTemplate.getName();
        String fileName = fileTemplate.getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String currentDate = LocalDate.now().format(formatter);

        String fileNameDate = new StringJoiner("_")
                .add(fileName)
                .add(currentDate)
                .toString();

        Map<String, String> excelForm = new HashMap<>();
        excelForm.put("name", fileNameDate);
        excelForm.put("content", excelBase64);

        return GlobalReponse.builder()
                .data(excelForm)
                .status(HttpStatus.OK.value())
                .message(messageSource.getMessage("file.export.success", null, LocaleContextHolder.getLocale()))
                .errors("").build();
    }

    private FileTemplateDto getFileTemplate(String fileType, String objectType) {
        Parameter parameter = new Parameter();
        parameter.add("file_type", fileType, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("object_type", objectType, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_file_template", parameter);

        try {
            if (rs.next()) {
//                String fileName = null;
//                try {
//                    fileName = rs.getString("file_name");
//                } catch (SQLException ignored) {}

                return FileTemplateDto.builder()
                        .templateJson(rs.getString("template_json"))
                        .iTableName(rs.getString("i_table_name"))
                        .name(rs.getString("name"))
//                        .fileName(fileName)
                        .build();
            }
        } catch (Exception e) {
            log.error("Caught error in getFileTemplate(): ", e);
            throw new PosException(e.getMessage());
        }
        return null;
    }

    private Map<Integer, String> getDataTypeMapping(FileTemplateDto fileTemplate, List<String> columnNames) {
        // Query datatypes of each column
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "column_name, " +
                        "data_type " +
                "FROM information_schema.columns " +
                "WHERE table_name = :tableName");
        List<Object[]> result = entityManager.createNativeQuery(sql.toString())
                .setParameter("tableName", fileTemplate.getITableName())
                .getResultList();

        // Chuyển đổi danh sách Object[] thành Map
        Map<String, String> columnDataTypes = new HashMap<>();
        for (Object[] row : result) {
            String columnName = ParseHelper.STRING.parse(row[0]) ; // COLUMN_NAME
            String dataType = ParseHelper.STRING.parse(row[1]);  // DATA_TYPE
            columnDataTypes.put(columnName, dataType);
        }

        // Take a ColumnName to DataType map and return an Index to DataType map filtered on requiredColumns
        // Lọc các entry có tên cột nằm trong danh sách requiredColumns
        Map<Integer, String> indexDataTypes = new LinkedHashMap<>(); // LinkedHashMap để giữ thứ tự
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            if (columnDataTypes.containsKey(columnName)) {
                indexDataTypes.put(i , columnDataTypes.get(columnName));
            }
        }

        log.info("Column Names: {}", columnNames);
        log.info("Column DataTypes: {}", columnDataTypes);
        log.info("Filtered Index DataTypes: {}", indexDataTypes);

        return indexDataTypes;
    }
//    // Take a ColumnName to DataType map and return an Index to DataType map filtered on requiredColumns
//    private Map<Integer, String> getFilteredIndexDataType(Map<String, String> columnDataTypes, List<String> columnNames) {
//        // Lọc các entry có tên cột nằm trong danh sách requiredColumns
//        Map<Integer, String> result = new LinkedHashMap<>(); // LinkedHashMap để giữ thứ tự
//
//        for (int i = 0; i < columnNames.size(); i++) {
//            String columnName = columnNames.get(i);
//            if (columnDataTypes.containsKey(columnName)) {
//                result.put(i , columnDataTypes.get(columnName)); // STT bắt đầu từ 0
//            }
//        }
//        return result;
//    }

    private void getIndexColumnMeta(
            FileTemplateDto fileTemplate,
            Map<Integer, ColumnMetaDto> indexMeta,
            List<String> columnNames) {
        // Query meta data of each column
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                    "column_name, " +
                    "data_type, " +
                    "character_maximum_length, " +
                    "numeric_precision, " +
                    "numeric_scale " +
                "FROM information_schema.columns " +
                "WHERE table_name = :tableName");
        List<Object[]> result = entityManager.createNativeQuery(sql.toString())
                .setParameter("tableName", fileTemplate.getITableName())
                .getResultList();

        // Chuyển đổi danh sách Object[] thành Map
        Map<String, ColumnMetaDto> columnMeta = new HashMap<>();
        for (Object[] row : result) {
            String columnName = ParseHelper.STRING.parse(row[0]) ;
            String dataType = ParseHelper.STRING.parse(row[1]);
            Integer charMaxLength = ParseHelper.INT.parse(row[2]);
            Integer numericPrecision = ParseHelper.INT.parse(row[3]);
            Integer numericScale = ParseHelper.INT.parse(row[4]);
            columnMeta.put(columnName, ColumnMetaDto.builder()
                    .dataType(dataType)
                    .charMaxLength(charMaxLength)
                    .numericPrecision(numericPrecision)
                    .numericScale(numericScale)
                    .build());
        }

        // Lọc các entry có tên cột nằm trong danh sách requiredColumns
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            if (columnMeta.containsKey(columnName)) {
                // indexMeta.put(i , columnMeta.get(columnName));
                modelMapper.map(columnMeta.get(columnName), indexMeta.get(i));
            }
        }

        log.info("Column Names: {}", columnNames);
        log.info("Column Meta: {}", columnMeta);
        log.info("Filtered Index Meta: {}", indexMeta);
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null
                    && cell.getCellType() != CellType.BLANK
                    && !(cell.getCellType() == CellType.STRING && cell.getStringCellValue().isBlank())) {
                return false; // Found a not null, blank or empty cell
            }
        }
        return true;
    }

    private Object processCellValue(Cell cell, FormulaEvaluator evaluator) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().isBlank() ? null : cell.getStringCellValue();
            case BLANK:
                return null;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return Timestamp.from(cell.getDateCellValue().toInstant());
                }
                double numericValue = cell.getNumericCellValue();
                return (numericValue % 1 == 0) ? (long) numericValue : numericValue;
            case FORMULA:
                return evaluateFormulaCell(cell, evaluator); // Process formula result
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return cell.toString().strip();
        }
    }

    private Object evaluateFormulaCell(Cell cell, FormulaEvaluator evaluator) {
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case STRING:
                return cellValue.getStringValue().isBlank() ? null : cellValue.getStringValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // DateUtil.isCellDateFormatted(cell) works with formula results
                    return Timestamp.from(DateUtil.getJavaDate(cellValue.getNumberValue()).toInstant());
                }
                double numericValue = cellValue.getNumberValue();
                return (numericValue % 1 == 0) ? (long) numericValue : numericValue;
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case BLANK:
                return null;
            default:
                return cell.toString().strip();
        }
    }

    public void saveRows(String tableName, String columns, List<Object[]> values) {
        String placeholders = "(" + "?, ".repeat(values.get(0).length - 1) + "?)";
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES " + placeholders;

        jdbcTemplate.batchUpdate(sql, values);
    }

    public void deleteRows(String tableName) {
        String sql = "DELETE FROM " + tableName + " where 1 = 1";
        jdbcTemplate.batchUpdate(sql);
    }

    private void exportDataAndCreateTable(Workbook workbook, Sheet sheet, List<String> columnNames, FileTemplateDto fileTemplate) {
        // columnNames is from templateColumns: ColumnName

        // Data rows
        Query query = entityManager.createNativeQuery("SELECT* FROM " + fileTemplate.getITableName());
        List<Map<String, Object>> data = query.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        // Create a cell style for numbers with a thousand separator
        CellStyle thousandSeparatorStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        thousandSeparatorStyle.setDataFormat(format.getFormat("#,##0.0")); // Thousands separator format

        // Create a date cell style
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));

        // Populate table starting from the second row (index 1)
        for (int rowIndex = 1; rowIndex <= data.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex);
            // Handle null values
            for (int colIndex = 0; colIndex < columnNames.size(); colIndex++) {
                Object value = data.get(rowIndex - 1).get(columnNames.get(colIndex));

                Cell cell = row.createCell(colIndex);
                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue()); // Keep numeric values as numbers
                    cell.setCellStyle(thousandSeparatorStyle); // Apply the formatting
                    //log.info("Number: {}", ((Number) value).doubleValue());
                } else if (value instanceof java.util.Date) {
                    cell.setCellValue((java.util.Date) value); // Store date correctly
                    cell.setCellStyle(dateCellStyle); // Apply date formatting
                    //log.info("Date: {}", value);
                } else {
                    cell.setCellValue(value != null ? value.toString() : ""); // Default to string
                    //log.info("Default String: {}", value);
                }
                //log.info("Row: {} - Column header: {} - Value: {} - Cell type: {}", rowIndex + 1, headerNames.get(colIndex), value, cell.getCellType());
            }
            //log.info("---Next row---");
        }

        // Define table range (A1 to last row and last column)
        String rangeRef = "A1:" + CellReference.convertNumToColString(columnNames.size() - 1) + (data.size() + 1);
        log.info("RangeRef: {}", rangeRef);
        AreaReference areaRef = new AreaReference(rangeRef, workbook.getSpreadsheetVersion());
        // Create table and set reference
        XSSFTable table = ((XSSFSheet) sheet).createTable(areaRef);
        table.setDisplayName("ExportTable");
        table.setName("ExportData");

        // Get the underlying XML table and Set table style and columns
        CTTable cttable = table.getCTTable();
        CTTableStyleInfo ctstyle = cttable.addNewTableStyleInfo();
        ctstyle.setName("TableStyleMedium2"); // Built-in table style
        ctstyle.setShowColumnStripes(false);
        ctstyle.setShowRowStripes(true);

        for (int i = 0; i < columnNames.size(); i++) {
            cttable.getTableColumns().getTableColumnArray(i).setId(i + 1);
        }

        // Add auto filter
        cttable.addNewAutoFilter().setRef(rangeRef);
    }

}