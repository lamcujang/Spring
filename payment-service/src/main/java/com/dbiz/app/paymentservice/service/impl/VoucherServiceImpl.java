package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.PosPaymentDT;
import com.dbiz.app.paymentservice.domain.VoucherTransaction;
import com.dbiz.app.paymentservice.repository.PosPaymentDTRepository;
import com.dbiz.app.paymentservice.repository.PosPaymentRepository;
import com.dbiz.app.paymentservice.repository.VoucherTransactionRepository;
import com.dbiz.app.paymentservice.service.VoucherService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PaymentDetailDto;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final ModelMapper modelMapper;
    private final VoucherTransactionRepository voucherTransactionRepository;
    private final PosPaymentDTRepository posPaymentDTRepository;

    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {
        return null;
    }

    @Override
    public GlobalReponse save(Object Dto) {

        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse saveVoucherTransaction(VoucherTransactionDto Dto) {
        log.info("VoucherTransactionServiceImpl save");
        VoucherTransactionDto dto = (VoucherTransactionDto) Dto;
        VoucherTransaction voucherTransaction = modelMapper.map(dto, VoucherTransaction.class);
        try {
            voucherTransaction.setTransactionDate(AppConstant.CURRENT_DATE_TIME);
            voucherTransaction.setTenantId(AuditContext.getAuditInfo().getTenantId());
            voucherTransaction = voucherTransactionRepository.save(voucherTransaction);
            dto = modelMapper.map(voucherTransaction, VoucherTransactionDto.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new PosException("Failed to save voucher transaction");
        }
        GlobalReponse globalReponse = new GlobalReponse();
        globalReponse.setMessage("Voucher transaction saved successfully");
        globalReponse.setStatus( HttpStatus.OK.value());
        globalReponse.setData(dto);
        return globalReponse;
    }

    @Override
    public String saveVoucherCouponPayment(List<PaymentDetailDto> Dto,
                                                  Integer orgId,
                                                  Integer posPaymentId,String paymentMethod) {

        log.info("Save voucher coupon payment");

        if(Dto == null || Dto.isEmpty()){
            throw new PosException("Payment details is required");
        }
        for(PaymentDetailDto paymentDetailDto : Dto){
            PosPaymentDT posPaymentDT = new PosPaymentDT();
            posPaymentDT.setTenantId(AuditContext.getAuditInfo().getTenantId());
            posPaymentDT.setOrgId(orgId);
            posPaymentDT.setPosPaymentId(posPaymentId);
            posPaymentDT.setAmount(paymentDetailDto.getAmount());
            posPaymentDT.setPaymentMethod(paymentMethod);
            posPaymentDT.setCode(paymentDetailDto.getCode());

            posPaymentDTRepository.save(posPaymentDT);

        }
        return "COM";
    }
}
