package com.dbiz.app.orderservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

/*
    * author: thanhnc
    * This is a generic interface for the service layer
 */
public interface BaseServiceGeneric<T, ID,R> {
    GlobalReponsePagination  findAll(R request);
    GlobalReponse  findById(ID id);
    GlobalReponse save(T entity);
    GlobalReponse deleteById(ID id);
}
