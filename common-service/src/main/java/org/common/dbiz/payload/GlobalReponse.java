package org.common.dbiz.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GlobalReponse  {
    private Integer status =200;
    private String message ="Success";
    private Object data ;
    private String errors = "";

}
