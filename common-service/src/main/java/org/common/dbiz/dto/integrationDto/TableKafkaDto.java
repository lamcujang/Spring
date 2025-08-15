package org.common.dbiz.dto.integrationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.TableDto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.ProductCategory}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TableKafkaDto implements Serializable {
    Integer tenantId;
    String status;
    String error;
    String lastPage;
    List<TableDto> tableDtos;
}