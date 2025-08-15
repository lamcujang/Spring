package org.common.dbiz.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GlobalReponsePagination {

    private Integer status = 200;
    private String message="Success";
    private Object data;
    private String errors = "";
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private Long totalItems;

}
