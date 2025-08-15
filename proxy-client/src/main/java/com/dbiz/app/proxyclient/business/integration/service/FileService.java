package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.file.ImportFileHistoryReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "fileService", path = "/integration-service/api/v1/file", decode404 = true, configuration = FeignClientConfig.class )
public interface FileService {

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<GlobalReponse> importFile(@RequestPart(value = "fileContent") MultipartFile fileContent,
                                             @RequestParam(value = "orgOrAll") String orgOrAll,
                                             @RequestParam(value = "orgIds") List<Integer> orgIds,
                                             @RequestParam(value = "fileCode") String fileCode,
                                             @RequestParam(value = "fileType") String fileType,
                                             @RequestParam(value = "fileTail") String fileTail,
                                             @RequestParam(value = "objectType") String objectType,
                                             @RequestParam(value = "isUpdateInv") String isUpdateInv,
                                             @RequestParam(value = "isUpdateCom") String isUpdateCom,
                                             @RequestParam(value = "isSkipErrors") String isSkipErrors);

    @PostMapping("/export")
    ResponseEntity<GlobalReponse> exportFile(@RequestBody FileIEDto dto);

    @GetMapping("/history/findAll")
    ResponseEntity<GlobalReponsePagination> getImportFileHistory(@SpringQueryMap ImportFileHistoryReqDto req);
}
