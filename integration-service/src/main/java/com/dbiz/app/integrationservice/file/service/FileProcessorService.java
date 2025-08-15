package com.dbiz.app.integrationservice.file.service;

import com.dbiz.app.integrationservice.file.service.strategy.FileProcessorFactory;
import com.dbiz.app.integrationservice.file.service.strategy.FileProcessorStrategy;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileProcessorService {

    private final FileProcessorFactory fileProcessorFactory;

    public Map<String, Object> importData(FileIEDto dto) {
        FileProcessorStrategy processor = fileProcessorFactory.getProcessor(dto.getFileType());
        return processor.importFile(dto);
    }

    public GlobalReponse exportFile(FileIEDto dto) {
        FileProcessorStrategy processor = fileProcessorFactory.getProcessor(dto.getFileType());
        return processor.exportFile(dto);
    }

}
