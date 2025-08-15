package com.dbiz.app.paymentservice.service.impl;


import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import org.common.dbiz.exception.wrapper.PerstingObjectException;
import com.dbiz.app.paymentservice.service.InvoiceLineService;
import com.dbiz.app.paymentservice.specification.InvoiceLineSpecification;
import com.dbiz.app.paymentservice.domain.InvoiceLine;
import org.common.dbiz.dto.paymentDto.InvoiceLineDto;
import org.common.dbiz.dto.paymentDto.InvoiceLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceLineQueryRequest;
import com.dbiz.app.paymentservice.repository.InvoiceLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class InvoiceLineServiceImp implements InvoiceLineService {

    private final InvoiceLineRepository invoiceLineRepository;
    private final RequestParamsUtils requestParamsUtils;
    private final ModelMapper modelMapper;


    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {
        log.info("*** InvoiceLine List, service; fetch all InvoiceLine *");
        InvoiceLineQueryRequest query = (InvoiceLineQueryRequest) queryRequest;
        Pageable pageable = requestParamsUtils.getPageRequest(query);
        Specification<InvoiceLine> spec = InvoiceLineSpecification.getSpecification(query);
        Page<InvoiceLine> entityList = invoiceLineRepository.findAll(spec, pageable);
        List<InvoiceLineDto> listData = new ArrayList<>();
        for(InvoiceLine item : entityList.getContent()) {
            listData.add(modelMapper.map(item, InvoiceLineDto.class));
        }

        GlobalReponsePagination response = new GlobalReponsePagination();
        response.setMessage("Invoice Lines fetched successfully");
        response.setData(listData);
        response.setCurrentPage(entityList.getNumber());
        response.setPageSize(entityList.getSize());
        response.setTotalPages(entityList.getTotalPages());
        response.setTotalItems(entityList.getTotalElements());

        return response;
    }

    @Override
    public GlobalReponse save(Object Dto) {
        InvoiceLineListDto invoicelineListDto = (InvoiceLineListDto) Dto;
        log.info("*** Invoice Line, service; save Invoice Line ***");
        try{
            InvoiceLine invoiceLine = null;
            for(InvoiceLineDto item : invoicelineListDto.getLines()) {
                invoiceLine = invoiceLineRepository.save(modelMapper.map(item, InvoiceLine.class));
            }
        }catch (Exception e) {
            throw new PerstingObjectException("Error while saving Invoice Line");
        }
        GlobalReponse response = new GlobalReponse();
        response.setMessage("Invoice Line saved successfully");
        response.setStatus(HttpStatus.CREATED.value());
//        response.setData(modelMapper.map(invoiceLine, InvoiceLineDto.class));
        return response;
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
