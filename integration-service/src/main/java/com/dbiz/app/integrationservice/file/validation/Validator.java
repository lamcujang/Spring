package com.dbiz.app.integrationservice.file.validation;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Validator {

    private final List<ValidationStrategy> strategies;

    public void validate(Row row,
                         Map<Integer, String> dataTypeMapping,
                         Map<Integer, String> requiredFieldMapping,
                         Map<Integer, ColumnMetaDto> indexMeta,
                         List<String> headerNames,
                         List<String> errorMessages) {
        for (ValidationStrategy strategy : strategies) {
            strategy.validate(row, dataTypeMapping, requiredFieldMapping, indexMeta, headerNames, errorMessages);
        }
    }

    public Boolean validateOneErrorForEachCell(Row row,
                                               Map<Integer, String> dataTypeMapping,
                                               Map<Integer, String> requiredFieldMapping,
                                               Map<Integer, ColumnMetaDto> indexMeta,
                                               List<String> headerNames,
                                               List<String> errorMessages) {
        Set<Integer> errorColumns = new HashSet<>(); // Track columns with errors
        boolean valid = true;

        for (ValidationStrategy strategy : strategies) {
            List<String> newErrorMessages = new ArrayList<>(); // Store errors from this strategy only

            strategy.validate(row, dataTypeMapping, requiredFieldMapping, indexMeta, headerNames, newErrorMessages);

            // Add only errors for columns that haven't been marked already
            for (String newErrorMessage : newErrorMessages) {
                Integer columnIndex = extractColumnIndex(newErrorMessage, headerNames);
                if (columnIndex != null && !errorColumns.contains(columnIndex)) {
                    errorMessages.add(newErrorMessage);
                    errorColumns.add(columnIndex); // Mark column as having an error
                }
                valid = false;
            }

//            // If all columns already have errors, Stop validation early
//            if (errorColumns.size() == headerNames.size()) {
//                break;
//            }
        }
        return valid;
    }

    private Integer extractColumnIndex(String errorMessage, List<String> headerNames) {
        for (int i = 0; i < headerNames.size(); i++) {
            if (errorMessage.contains(headerNames.get(i))) {
                return i;
            }
        }
        return null;
    }

    public void validateFirstErrorTypeOnly(Row row,
                                           Map<Integer, String> dataTypeMapping,
                                           Map<Integer, String> requiredFieldMapping,
                                           Map<Integer, ColumnMetaDto> indexMeta,
                                           List<String> headerNames,
                                           List<String> errorMessages) {
        int initialErrorCount = errorMessages.size(); // Track error initial size for all validation strategies

        for (ValidationStrategy strategy : strategies) {
            strategy.validate(row, dataTypeMapping, requiredFieldMapping, indexMeta, headerNames, errorMessages);

            if (errorMessages.size() > initialErrorCount) { // Check if an error was added
                break; // Stop further validation for this row
            }
        }
    }

}
