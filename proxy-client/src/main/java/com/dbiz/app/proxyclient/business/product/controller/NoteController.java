package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final NoteQueryRequest NoteQueryRequest
            ){
        return ResponseEntity.ok(this.entityClientService.findAll(NoteQueryRequest).getBody());
    }


    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final NoteDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }


    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final NoteDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
