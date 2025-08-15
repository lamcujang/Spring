package com.dbiz.app.integrationservice.resource;

import com.dbiz.app.integrationservice.file.service.FileProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/data")
@Slf4j
@RequiredArgsConstructor
public class FileTestResource {

    private final FileProcessorService fileProcessorService;

    @PostMapping("/import")
    public ResponseEntity<GlobalReponse> importData(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("code") String code,
                                                    @RequestParam("orgId") Integer orgId){
        log.info("*** DataProcessor Resource: import Data ***");
        String fileType = code.split("_")[0];
        String objectType = code.split("_")[1];
//        return ResponseEntity.ok(fileProcessorService.importData(file, fileType, objectType, orgId));
        return null;
    }

    @PostMapping("/export")
    public ResponseEntity<GlobalReponse> exportFormData(@RequestParam("code") String code){
        log.info("*** DataProcessor Resource: export Data ***");
        String fileType = code.split("_")[0];
        String objectType = code.split("_")[1];
//        return ResponseEntity.ok(fileProcessorService.exportFormData(fileType, objectType));
        return null;
    }
}
