package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductIntKafkaDto implements Serializable {
    Integer tenantId;
    List<ProductIntDto> productIntDtoList;
    SyncIntegrationCredential syncIntegrationCredential;
    String lastPage;
}