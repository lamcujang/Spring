package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.request.productRequest.NoteQueryRequest;

public interface NoteService extends BaseServiceGeneric<NoteDto
        , Integer, NoteQueryRequest> {
}
