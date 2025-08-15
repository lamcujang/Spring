package com.dbiz.app.integrationservice.resource;


import com.dbiz.app.integrationservice.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.file.FileIEDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.file.ImportFileHistoryReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/file"})
@Slf4j
@RequiredArgsConstructor
public class FileResource {

     private final FileService service;

     @PostMapping("/import")
     public ResponseEntity<GlobalReponse> importFile(@RequestPart(value = "fileContent") MultipartFile fileContent,
                                                     @RequestParam(value = "orgOrAll") String orgOrAll,
                                                     @RequestParam(value = "orgIds") List<Integer> orgIds,
                                                     @RequestParam(value = "fileCode") String fileCode,
                                                     @RequestParam(value = "fileType") String fileType,
                                                     @RequestParam(value = "fileTail") String fileTail,
                                                     @RequestParam(value = "objectType") String objectType,
                                                     @RequestParam(value = "isUpdateInv") String isUpdateInv,
                                                     @RequestParam(value = "isUpdateCom") String isUpdateCom,
                                                     @RequestParam(value = "isSkipErrors") String isSkipErrors){
         log.info("*** File Service Resource: import file ***");
         return ResponseEntity.ok(service.importFile(fileContent,orgOrAll,orgIds,fileCode,fileType,fileTail,objectType,isUpdateInv,isUpdateCom,isSkipErrors));
     }

     @PostMapping("/export")
     public ResponseEntity<GlobalReponse> exportFile(@RequestBody FileIEDto dto){
         log.info("*** File Service Resource: export file ***");
         return ResponseEntity.ok(service.exportFile(dto));
     }

     @GetMapping("/history/findAll")
     public ResponseEntity<GlobalReponsePagination> getImportFileHistory(@ModelAttribute ImportFileHistoryReqDto req){
         log.info("*** File Service Resource: export file ***");
         return ResponseEntity.ok(service.getImportFileHistory(req));
     }
}
