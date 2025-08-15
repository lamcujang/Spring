package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.Note}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NoteDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    Integer id;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Integer noteGroupId;
    NoteGroupDto noteGroup;
    @Size(max = 32)
    String name;
    @Size(max = 255)
    String description;
    String productCategoryIds;
    List<ProductCategoryDto> productCategorys;
}