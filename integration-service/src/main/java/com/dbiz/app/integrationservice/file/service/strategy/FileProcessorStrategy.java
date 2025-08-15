package com.dbiz.app.integrationservice.file.service.strategy;

import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileProcessorStrategy {

    Map<String, Object> importFile(FileIEDto dto);

    GlobalReponse exportFile(FileIEDto dto);

}
