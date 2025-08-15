package com.dbiz.app.productservice.service;


import org.common.dbiz.dto.productDto.ListWarehouseDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.WarehouseIntDto;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;

import java.util.List;

public interface WarehouseService extends BaseServiceGeneric<WarehouseDto , Integer, WarehouseQueryRequest >{
    GlobalReponse intSave(WarehouseIntDto param);

    GlobalReponse deleteById(Integer id);

    GlobalReponse findByErpId(Integer erpId);

    GlobalReponse updateAll(ListWarehouseDto param);
}
