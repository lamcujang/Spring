package org.common.dbiz.request.systemRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;


@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReasonQueryRequest extends BaseQueryRequest   {
    String keyword;
    String type;
    String id;
}
