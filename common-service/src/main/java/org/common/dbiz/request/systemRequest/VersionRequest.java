package org.common.dbiz.request.systemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VersionRequest extends BaseQueryRequest {
    private String version;
    private String platform;
}
