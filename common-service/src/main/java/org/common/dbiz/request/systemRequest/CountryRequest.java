package org.common.dbiz.request.systemRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;


@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryRequest  extends BaseQueryRequest   {
    String code;
    String name;
    String isDefault;
}
