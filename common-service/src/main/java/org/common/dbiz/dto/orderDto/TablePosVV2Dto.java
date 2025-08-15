package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TablePosVV2Dto implements Serializable {

    private String isActive;
    private Integer tenantId;
    private Integer orgId;
    private Integer id;

    @Size(max = 32)
    private String name;

    @Size(max = 3)
    private String tableStatus;

    private Integer displayIndex;
    private Integer numberSeats;

    @Size(max = 5)
    private String tableNo;

    private Integer floorId;

    @Size(max = 1)
    private String isDefault;

    private String isBuffet;
    private String isRoom;
    private BigDecimal numberGuests;

    private String floorName;

    @Size(max = 1)
    private String isOccupied;

    private Short totalGuests;

    private List<String> customerNames;

    // Các trường bổ sung từ DTO gốc (nếu cần cho FE)
    private Integer posOrderId; // Không có trong view, cần kiểm tra nếu dùng
    private FloorDto floor;     // Không có trong view, nhưng có thể ánh xạ từ floorName
    private String customerName; // Đã thay bằng customerNames
    private Integer customerId;  // Không có trong view

    private Integer erpTableId;  // Không có trong view
    private String isLocked;     // Không có trong view
    private String orderGuest;   // Đã thay bằng totalGuests
}
