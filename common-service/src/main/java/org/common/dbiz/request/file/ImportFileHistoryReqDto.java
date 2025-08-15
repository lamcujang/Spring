package org.common.dbiz.request.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportFileHistoryReqDto extends BaseQueryRequest implements Serializable {
    private String fileName;
    private String objectType;
    private String fromDate;
    private String toDate;
    private String fullName;
    private String importStatus;
}
