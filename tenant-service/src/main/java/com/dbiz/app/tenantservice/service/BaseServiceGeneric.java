package com.dbiz.app.tenantservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

public interface BaseServiceGeneric<T, ID,R> {
    GlobalReponsePagination findAll(R request);
    GlobalReponse findById(ID id);
    GlobalReponse save(T entity);
    GlobalReponse deleteById(ID id);
}
