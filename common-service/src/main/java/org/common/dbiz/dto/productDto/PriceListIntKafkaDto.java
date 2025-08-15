package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link PriceList}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListIntKafkaDto implements Serializable {

    private Integer tenantId;
    private Integer integrationHistoryId;
    private String statusIntegration;
    private String payload;
    private String response;
    private String error = "";
    private String lastPage;
    SyncIntegrationCredential syncIntegrationCredential;
    List<PriceListIntDto> priceListIntDtoList;
}