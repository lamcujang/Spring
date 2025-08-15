package com.dbiz.app.orderservice.service;

import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.TableQueryRequest;

import java.util.List;

public interface TableV2Service extends BaseServiceGeneric<TableDto, Integer, TableQueryRequest> {

    GlobalReponsePagination findAllTableAndReservationByDate(TableQueryRequest entityQueryRequest);
}
