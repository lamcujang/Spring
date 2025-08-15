package org.common.dbiz.dto.paymentDto;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceLineListDto extends BaseDto implements Serializable {

    List<InvoiceLineDto> lines;
}
