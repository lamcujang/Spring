package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.Warehouse}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDto implements Serializable {

    @Size(max = 1)
    String isActive;
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String description;
    @Size(max = 255)
    String address;
    @Size(max = 1)
    String isNegative;
    @Size(max = 1)
    String isMergeItem;
    Integer orgId;
    String orgName;
    private String printerIp;
    private String deviceToken;
    Integer erpWarehouseId;
    String printerType;
    String printerName;
    String printerProductId;
    String printerVendorId;
    Integer printerPageSize;
    Integer printerPageSoQty;
    Integer printerPageTempQty;
    Integer printerPort;
    String ErpWarehouseName;
    List<LocatorDto> locatorDtos;

}