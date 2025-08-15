package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dbiz.app.domain.view.IntegrationHistoryGetV}
 */

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegrationHistoryGetVDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer integrationHistoryId;
    Instant intDate;
    @Size(max = 128)
    String fullName;
    @Size(max = 15)
    String typeValue;
    @Size(max = 64)
    String intType;
    @Size(max = 15)
    String flowValue;
    @Size(max = 64)
    String intFlow;
    @Size(max = 15)
    String statusValue;
    @Size(max = 64)
    String intStatus;
}