package org.common.dbiz.dto.tenantDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.userDto.UserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TenantDto implements Serializable {
    Integer id;
    String code;
    String name;
    String domainUrl;
    String taxCode;
    Integer imageId;
    Integer industryId;
    String industryCode;
    String googleId;
    String facebookId;
    String zaloId;
    String lineId;
    String tiktokId;
    String appleId;
    String city;
    String address;
    String country;
    String logo;
    String salesRepresentative;
    String status;
    String productPackage; // SELECTION
    String contractTermPackage; // SELECTION
    List<String> typeOfBusiness; // SELECTION
    List<String> industrySector; // SELECTION
    String numberOfTables; // SELECTION
    String numberOfEmployees;
    String iServerId;
    String isFirstLogin;
    List<CreateOrgDto> orgs;
    UserDto user;
    private String productionMgmt;
    private String shiftMgmt;
    private String inventoryAlter;
    private String notificationKitchen;
    private String billMergeItem;
    private Integer numberOfPayments;
    String isPromotionEnabled;
    String allowPromotionMerge;
    String applyPromotionOnOrder;
    String applyAutoPromotion;
    BigDecimal dishRecallTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate expiredDate;

    String logoUrl;
    String agentCode;
}