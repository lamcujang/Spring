package org.common.dbiz.dto.tenantDto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PrintReportDto implements Serializable {
    Integer id;
    String reportType;
    String reportSource;
    String isDefault;
}