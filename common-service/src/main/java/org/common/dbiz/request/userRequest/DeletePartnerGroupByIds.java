package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePartnerGroupByIds {
    Integer[] ids;
}
