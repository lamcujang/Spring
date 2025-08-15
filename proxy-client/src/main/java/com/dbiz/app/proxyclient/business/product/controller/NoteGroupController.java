package com.dbiz.app.proxyclient.business.product.controller;

import com.dbiz.app.proxyclient.business.product.service.NoteGroupService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/noteGroups")
@RequiredArgsConstructor
public class NoteGroupController {
    private final NoteGroupService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final NoteGroupQueryRequest NoteGroupQueryRequest
            ){
        return ResponseEntity.ok(this.entityClientService.findAll(NoteGroupQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final NoteGroupDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final NoteGroupDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }

    @GetMapping("/findAllNoteGroupAndNote")
    public ResponseEntity<GlobalReponsePagination> findAllNoteGroupAndNote(@SpringQueryMap final NoteGroupQueryRequest entityQueryRequest) {
        return ResponseEntity.ok(this.entityClientService.findAllNoteGroupAndNote(entityQueryRequest).getBody());
    }
}
