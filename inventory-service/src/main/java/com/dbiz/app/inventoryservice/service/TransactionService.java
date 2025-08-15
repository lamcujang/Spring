package com.dbiz.app.inventoryservice.service;

import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.inventoryDto.TransactionParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface TransactionService {

    GlobalReponse createTransaction(TransactionDto transactionDto);

    GlobalReponsePagination findAllTransaction(TransactionParamDto transactionParamDto);

}
