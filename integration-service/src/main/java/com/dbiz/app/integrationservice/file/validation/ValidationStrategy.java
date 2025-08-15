package com.dbiz.app.integrationservice.file.validation;

import org.apache.poi.ss.usermodel.Row;
import org.common.dbiz.dto.integrationDto.file.ColumnMetaDto;

import java.util.List;
import java.util.Map;

public interface ValidationStrategy {

    Boolean validate(Row row,
                     Map<Integer, String> indexDataType,
                     Map<Integer, String> indexRequired,
                     Map<Integer, ColumnMetaDto> indexMeta,
                     List<String> headerNames,
                     List<String> errorMessages);

}
