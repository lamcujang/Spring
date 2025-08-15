package com.dbiz.app.productservice.service;


import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;

import java.util.List;
import java.util.Optional;

public interface BaseServiceGeneric<T, ID,R> {
    GlobalReponsePagination findAll(R request);
    GlobalReponse findById(ID id);
    GlobalReponse save(T entity);
    GlobalReponse deleteById(ID id);
}
