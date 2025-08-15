package com.dbiz.app.systemservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.dto.systemDto.ReferenceListDto;
import org.common.dbiz.request.systemRequest.ReferenceListQueryRequest;

public interface ReferenceListService extends BaseServiceGeneric<ReferenceListDto,Integer, ReferenceListQueryRequest> {

    GlobalReponse findByValue(String value);

    GlobalReponse findRefValue(String value, String domain, String column);

    GlobalReponse findByReferenceNameAndValue(String nameReference, String value);

    GlobalReponse findByReferenceName(String nameReference);
}
