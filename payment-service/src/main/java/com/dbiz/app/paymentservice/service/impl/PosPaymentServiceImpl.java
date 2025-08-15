package com.dbiz.app.paymentservice.service.impl;

import com.dbiz.app.paymentservice.repository.PosPaymentRepository;
import com.dbiz.app.paymentservice.service.PosPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PosPaymentServiceImpl implements PosPaymentService{

    private final PosPaymentRepository posPaymentRepository;

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
}
