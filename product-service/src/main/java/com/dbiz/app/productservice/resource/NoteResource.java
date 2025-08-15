package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
@Slf4j
@RequiredArgsConstructor
public class NoteResource {
    private final NoteService entityService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final NoteQueryRequest entityQueryRequest) {
        log.info("*** Note, controller; fetch Note all *");
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody NoteDto entityDto)
    {
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody NoteDto entityDto)
    {
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse>deleteById(@PathVariable("id") final Integer id)
    {
        return ResponseEntity.ok(this.entityService.deleteById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id)
    {
        return ResponseEntity.ok(this.entityService.findById(id));
    }
}
