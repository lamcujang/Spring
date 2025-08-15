package com.dbiz.app.orderservice.service;


import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface BaseService {
    GlobalReponsePagination  findAll(Object queryRequest);

    GlobalReponse  save(Object Dto);

    GlobalReponse deleteById(Integer id);

    GlobalReponse findById(Integer id);

}
