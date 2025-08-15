package com.dbiz.app.paymentservice.service;


import org.common.dbiz.dto.paymentDto.CreateEInvoiceOrgDto;
import org.common.dbiz.dto.paymentDto.einvoice.IssueEInvoiceDto;
import org.common.dbiz.dto.paymentDto.request.EInvoiceSetUpReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.CreateEInvoiceRequest;

public interface EInvoiceService{

    GlobalReponse  createEInvoice(CreateEInvoiceRequest rq);

    GlobalReponse  createEInvoiceSetupByOrg(CreateEInvoiceOrgDto rq);

    GlobalReponse  getEInvoiceOrgBySetUpIdAndOrgId(CreateEInvoiceOrgDto rq);

    GlobalReponsePagination getEInvoiceSetUp(EInvoiceSetUpReqDto rq);

    GlobalReponse setDefault(Integer id);

    GlobalReponse  issueEInvoice(IssueEInvoiceDto rq);

    GlobalReponse issueHiloEInvoice(IssueEInvoiceDto rq);

    GlobalReponse replaceHiloEInvoice(IssueEInvoiceDto rq);

    GlobalReponse adjustHiloEInvoice(IssueEInvoiceDto rq);

    GlobalReponse getHiloInvInfo(Integer id);
}
