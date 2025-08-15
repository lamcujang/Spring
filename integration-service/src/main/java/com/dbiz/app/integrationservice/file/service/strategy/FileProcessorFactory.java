package com.dbiz.app.integrationservice.file.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileProcessorFactory {

    private final Map<String, FileProcessorStrategy> strategyMap;

    public FileProcessorStrategy getProcessor(String fileType) {
        return strategyMap.get(fileType.toUpperCase());
    }

}
