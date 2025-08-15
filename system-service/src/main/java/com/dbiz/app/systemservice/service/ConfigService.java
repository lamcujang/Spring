package com.dbiz.app.systemservice.service;

import liquibase.pro.packaged.G;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.dto.systemDto.ConfigDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ConfigQueryRequest;
import org.common.dbiz.request.systemRequest.EMenuGetUrlQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;

public interface ConfigService extends BaseServiceGeneric<ConfigDto, Integer, ConfigQueryRequest>{

    GlobalReponse findValueByName(String name);

    GlobalReponsePagination getEMenuConfig(EMenuGetUrlQueryRequest request);

    GlobalReponsePagination getEMenuConfigV2(EMenuGetUrlQueryRequest request);

    GlobalReponse sendNotify(SendNotification request);

    GlobalReponse getImageAsset(String name);

    GlobalReponse getParamEmenu(String param);

    GlobalReponse getNapasConfig(NapasConfigReqDto dto);
}
