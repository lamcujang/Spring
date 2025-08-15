package com.dbiz.app.integrationservice.file.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class RequiredFieldValidation implements ValidationStrategy {

    private final MessageSource messageSource;

    @Override
    public Boolean validate(Row row,
                            Map<Integer, String> indexDataType,
                            Map<Integer, String> indexRequired,
                            Map<Integer, ColumnMetaDto> indexMeta,
                            List<String> headerNames,
                            List<String> errorMessages) {
        //log.info("Require validated for row: {}", row.getRowNum() + 1);

        boolean valid = true;
        for (Map.Entry<Integer, String> entry : indexRequired.entrySet()) {
            Integer index = entry.getKey();
            String required = entry.getValue() == null ? "N" : entry.getValue();
            Cell cell = row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            // If required, check for null, blank, or empty cells
            boolean isRequired = required.equals("Y");
            boolean hasValue = cell.getCellType() != CellType.BLANK
                    && !(cell.getCellType() == CellType.STRING && cell.getStringCellValue().isBlank());
            if (isRequired && !hasValue) {
                String errorMessage = messageSource.getMessage(
                        "file.validation.required",
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
