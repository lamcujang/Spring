package org.common.dbiz.request.tenantRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintReportQueryRequest extends BaseQueryRequest implements Serializable {

        private Integer tenantId;
        private String reportType;
        private Boolean isDefault;
}
