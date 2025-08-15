package org.common.dbiz.dto.integrationDto.file;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileIEDto {

    Integer tenantId;
    String orgOrAll; // ALL - ORG
    List<Integer> orgIds; // Các chi nhánh truyền vào
    String language;

    String fileCode;
    String fileName;
    String fileType;
    String fileTail;
    String objectType;
    String fileUrl;
    String fileContentBase64; // not used, save local file and use fileUrl instead
    MultipartFile fileContent; // not used, save local file and use fileUrl instead

    String isUpdateInv;
    String isUpdateCom;
    String isSkipErrors;

    String importStatus;
    String errorMessage;
    Integer rowCount;
    @Builder.Default
    Set<Integer> errorRows = new HashSet<>();

}
