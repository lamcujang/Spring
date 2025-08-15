package com.dbiz.app.productservice.service;

import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;

public interface NoteGroupService extends BaseServiceGeneric<NoteGroupDto,Integer, NoteGroupQueryRequest> {

    GlobalReponsePagination findAllNoteGroupAndNote(NoteGroupQueryRequest request);
}
