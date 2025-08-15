package com.dbiz.app.integrationservice.file.service;

import com.dbiz.app.integrationservice.domain.ImportFileHistory;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.file.ImportFileHistoryReqDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    GlobalReponse importFile(MultipartFile fileContent,
                             String orgOrAll,
                             List<Integer> orgIds,
                             String fileCode,
                             String fileType,
                             String fileTail,
                             String objectType,
                             String isUpdateInv,
                             String isUpdateCom,
                             String isSkipErrors);

    GlobalReponse exportFile(FileIEDto fileIEDto);

    ImportFileHistory saveImportFileHistory(Integer orgId,
                                            String fileCode,
                                            String fileType,
                                            String fileTail,
                                            String objectType);

    GlobalReponsePagination getImportFileHistory(ImportFileHistoryReqDto req);
}
