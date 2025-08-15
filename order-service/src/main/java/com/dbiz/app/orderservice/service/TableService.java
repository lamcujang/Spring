package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;

import java.util.List;

public interface TableService extends BaseServiceGeneric<TableDto, Integer, TableQueryRequest> {

    GlobalReponsePagination findAllTableAndReservationByDate(TableQueryRequest entityQueryRequest);
    GlobalReponse intSave(List<TableDto> entityDto);
    GlobalReponse getByIdFromCashier(Integer id);
}
