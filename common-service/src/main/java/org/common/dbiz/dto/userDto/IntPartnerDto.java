package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IntPartnerDto implements Serializable {
    String platform ;
    Integer tenantId;
    String status = "COM";
    String error = "";
    String sendKafka = "N";
    List<CustomerDto> customers =new ArrayList<>();
    List<VendorDto> vendors =new ArrayList<>();
    List<PartnerGroupDto> partnerGroups =new ArrayList<>();
    SyncIntegrationCredential syncIntegrationCredential = new SyncIntegrationCredential();

}