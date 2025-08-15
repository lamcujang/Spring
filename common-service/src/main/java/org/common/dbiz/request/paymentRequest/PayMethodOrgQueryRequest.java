package org.common.dbiz.request.paymentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayMethodOrgQueryRequest extends BaseQueryRequest  implements Serializable {

    private Integer id;

    private Integer orgId;

}
