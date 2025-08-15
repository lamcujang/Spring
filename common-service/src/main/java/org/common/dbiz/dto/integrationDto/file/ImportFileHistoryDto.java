package org.common.dbiz.dto.integrationDto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImportFileHistoryDto implements Serializable {
    Integer id;
    String code;
    Integer orgId;
    Integer userId;
    String fullName;
    String fileName;
    String isActive;
    String importDate;
    String importStatus;
    String importStatusName;
    String fileType;
    String objectType;
    String objectTypeName; // ko có trg db
    String errorMessage;
}
