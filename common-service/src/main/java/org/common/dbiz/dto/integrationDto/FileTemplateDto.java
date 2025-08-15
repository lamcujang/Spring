package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileTemplateDto {
     Integer FileTemplateId;
     Integer TenantId;
     Integer OrgId;
     String name;
//     String fileName;
     String code;
     String fileType;
     String fileTail;
     String objectType;
     String templateJson;
     String iTableName;
}
