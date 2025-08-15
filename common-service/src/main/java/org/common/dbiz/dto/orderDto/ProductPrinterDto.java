package org.common.dbiz.dto.orderDto;

import lombok.*;

import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductPrinterDto implements Serializable {
     String productName;
     String description;
     String qty;
     String printerIp;

}