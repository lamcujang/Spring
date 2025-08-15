package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListSyncProduct {
    private  List<Integer> listProduct;
    private Integer tenantId;
}
