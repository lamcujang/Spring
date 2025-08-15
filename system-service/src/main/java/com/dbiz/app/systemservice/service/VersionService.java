package com.dbiz.app.systemservice.service;

import org.common.dbiz.dto.systemDto.VersionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.VersionRequest;

public interface VersionService {
    GlobalReponsePagination findAll(VersionRequest req);
}
