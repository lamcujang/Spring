package com.dbiz.app.integrationservice.file.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(3)
@Slf4j
@RequiredArgsConstructor
public class DateFieldValidation implements ValidationStrategy {

    private final MessageSource messageSource;

    @Override
    public Boolean validate(Row row,
                            Map<Integer, String> indexDataType,
                            Map<Integer, String> indexRequired,
                            Map<Integer, ColumnMetaDto> indexMeta,
                            List<String> headerNames,
                            List<String> errorMessages) {
        //log.info("Date validated for row: {}", row.getRowNum() + 1);

        boolean valid = true;
        for (Map.Entry<Integer, String> entry : indexDataType.entrySet()) {
            Integer index = entry.getKey();
            String dataType = entry.getValue();
            String required = indexRequired.get(index) == null ? "N" : indexRequired.get(index);
            Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            // if dataType:Time && ( Req:Y && !Time ) || ( Req:N && !Blank && !Time )
            boolean isTypeTime = dataType.equalsIgnoreCase("timestamp without time zone") || dataType.equalsIgnoreCase("date");
            boolean isRequired = required.equals("Y");
            boolean hasValue = cell.getCellType() != CellType.BLANK
                    && !(cell.getCellType() == CellType.STRING && cell.getStringCellValue().isBlank());
            boolean isNotTime = cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell);
            if (isTypeTime && ( isRequired || hasValue ) && isNotTime) {
                String errorMessage = messageSource.getMessage(
                        "file.validation.date",
                        new Object[]{row.getRowNum() + 1, headerNames.get(index)},
                        LocaleContextHolder.getLocale());
                log.error(errorMessage);
                errorMessages.add(errorMessage);
                valid = false;
            }
        }
        return valid;
    }

}