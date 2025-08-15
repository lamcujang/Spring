package org.common.dbiz.dto.integrationDto.voucher;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VoucherParamDto implements Serializable {

    private int page=0;
    private int pageSize=15;

    private Integer orgId;
    private Integer userId;
    private String documentNo;


}
