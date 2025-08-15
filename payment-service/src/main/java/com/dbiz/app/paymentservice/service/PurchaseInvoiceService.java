package com.dbiz.app.paymentservice.service;

import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;

public interface PurchaseInvoiceService extends BaseService{
    GlobalReponsePagination getPO(POHeaderVRequest req);
}
