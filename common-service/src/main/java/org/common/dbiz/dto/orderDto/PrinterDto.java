package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.common.dbiz.dto.orderDto.response.KitchenOrderlineDtoV;
import org.common.dbiz.dto.orderDto.response.UserDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewKitchenOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PrinterDto implements Serializable {

    String orderDate;
    String documentNo;
    String productName;
    String uomName;
    String description;
    BigDecimal qty;
    String printerIp;
    BigDecimal printerPort;
    String printerType;
    String printerName;
    String printerProductId;
    String printerVendorId;
    Integer printerPageSize;
    Integer printerPageSoQty;
    Integer printerPageTempQty;
    String isMergeItem;
    String warehouseName;
    List<PrinterLineDetailDto> lineDetailPrinter;
    List<PrinterLineDto> linePrinters;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PrinterLineDto {
        String productName;
        BigDecimal qty;
        String description;
        String uomName;
        List<PrinterLineDetailDto> lineDetailPrinter;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PrinterLineDetailDto {
        String productName;
        BigDecimal qty;
    }
}