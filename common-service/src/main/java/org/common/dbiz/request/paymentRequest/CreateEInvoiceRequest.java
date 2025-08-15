package org.common.dbiz.request.paymentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEInvoiceRequest implements Serializable {
    private Integer invoiceId;
}
