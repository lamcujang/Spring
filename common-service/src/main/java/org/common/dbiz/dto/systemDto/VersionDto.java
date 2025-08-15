package org.common.dbiz.dto.systemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VersionDto {
    String version;
    String minVersion;
    String forceUpdate;
    String storeUrl;
    String message;
    String platform;
}
