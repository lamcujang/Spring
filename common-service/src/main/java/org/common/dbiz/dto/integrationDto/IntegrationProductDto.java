package org.common.dbiz.dto.integrationDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IntegrationProductDto {

    private Integer tenantId;
    private String statusIntegration;
    private String payload;
    private String response;
    private String error = "";
    private String lastPage;

    private SyncIntegrationCredential syncIntegrationCredential;

}
