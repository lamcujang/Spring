package com.dbiz.app.productservice.service.impl;


import com.dbiz.app.productservice.service.StorageOnhandService;
import org.common.dbiz.dto.productDto.StorageOnhandDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.StorageQueryRequest;

public class StorageOnhandServiceImpl implements StorageOnhandService {
    @Override
    public GlobalReponsePagination  findAll(StorageQueryRequest  paramReques) {
        return null;
    }

    @Override
    public GlobalReponse  save(StorageOnhandDto  dto) {
        return null;
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }
}
