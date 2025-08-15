package com.dbiz.app.integrationservice.file.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Order(4)
@Slf4j
@RequiredArgsConstructor
public class FieldLengthValidation implements ValidationStrategy {

    private final MessageSource messageSource;

    @Override
    public Boolean validate(Row row,
                            Map<Integer, String> indexDataType,
                            Map<Integer, String> indexRequired,
                            Map<Integer, ColumnMetaDto> indexMeta,
                            List<String> headerNames,
                            List<String> errorMessages) {
        //log.info("String validated for row: {}", row.getRowNum() + 1);

        boolean valid = true;
        for (Map.Entry<Integer, String> entry : indexDataType.entrySet()) {
            Integer index = entry.getKey();
            String dataType = entry.getValue();
            ColumnMetaDto meta = indexMeta.get(index);
            Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

//            if (dataType.equalsIgnoreCase("character varying") && required.equals("Y")) {
//                if (cell.getCellType() != CellType.STRING) {
//                    String errorMessage = messageSource.getMessage(
//                            "file.validation.string",
//                            new Object[]{row.getRowNum() + 1, headerNames.get(index)},
//                            LocaleContextHolder.getLocale());
//                    log.error(errorMessage);
//                    errorMessages.add(errorMessage);
//                    valid = false;
//                }
//            }

            if (cell.getCellType() == CellType.BLANK) {
                continue;
            }

            // --- VARCHAR/VARYING: length check ---
            if (dataType.equalsIgnoreCase("character varying")) {

                DataFormatter formatter = new DataFormatter();
                String text;
                switch (cell.getCellType()) {
                    case STRING:
                        text = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                    case BOOLEAN:
                    case FORMULA:
                        // convert non-string cell to string for length-check
                        text = formatter.formatCellValue(cell);
                        break;
                    default:
                        text = "";
                        break;
                }
                // Length check
                Integer maxLen = meta.getCharMaxLength();
                if (maxLen != null && text.length() > maxLen) {
                    String msg = messageSource.getMessage(
                            "file.validation.maxlength",
                            new Object[]{row.getRowNum() + 1, headerNames.get(index), maxLen},
                            LocaleContextHolder.getLocale()
                    );
                    log.error(msg);
                    errorMessages.add(msg);
                    valid = false;
                }
            }
            // --- NUMERIC: precision/scale check ---
            else if (dataType.equalsIgnoreCase("numeric")) {
                BigDecimal bd = null;
                if (cell.getCellType() == CellType.NUMERIC && !DateUtil.isCellDateFormatted(cell)) {
                    bd = BigDecimal.valueOf(cell.getNumericCellValue());
                }

                if (bd != null && meta.getNumericPrecision() != null && meta.getNumericScale() != null) {
                    int precision = meta.getNumericPrecision();
                    int scale     = meta.getNumericScale();
                    int actualDigits = bd.precision();
                    int actualScale  = Math.max(bd.scale(), 0);

                    if (actualDigits > precision || actualScale > scale) {
                        String msg = messageSource.getMessage(
                                "file.validation.numeric_precision",
                                new Object[]{row.getRowNum() + 1, headerNames.get(index), precision, scale},
                                LocaleContextHolder.getLocale()
                        );
                        log.error(msg);
                        errorMessages.add(msg);
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

}