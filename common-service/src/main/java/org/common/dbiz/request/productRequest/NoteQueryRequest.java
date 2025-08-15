package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteQueryRequest extends BaseQueryRequest implements Serializable {
        private Integer orgId;
        private Integer noteGroupId;
        private Integer productCategoryId;
        private String name;
}
