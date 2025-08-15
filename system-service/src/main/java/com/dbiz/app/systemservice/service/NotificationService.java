package com.dbiz.app.systemservice.service;

import org.common.dbiz.dto.systemDto.ClearCacheDto;
import org.common.dbiz.dto.systemDto.NotificationDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.systemRequest.NotificationQueryRequest;
import org.common.dbiz.request.systemRequest.SendNotification;

public interface NotificationService extends BaseServiceGeneric<NotificationDto, Integer, NotificationQueryRequest>{

    GlobalReponse sendNotify(SendNotification request);

    GlobalReponse getMessage(String key);

    GlobalReponse clearCacheFE(ClearCacheDto dto);

    GlobalReponse updateStatusNotification(NotificationDto dto);

}
