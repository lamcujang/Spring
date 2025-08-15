package org.common.dbiz.dto.integrationDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IntegrationHistoryInfoDto {

    private String statusIntegration;
    private String payload;
    private String response;
    private String error = "";

}
