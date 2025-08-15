package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.NoteGroupService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.JsonView.JsonViewNoteGroup;
import org.common.dbiz.dto.productDto.NoteGroupDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteGroupQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/noteGroups")
@Slf4j
@RequiredArgsConstructor
public class NoteGroupResource {
    private final NoteGroupService entityService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final NoteGroupQueryRequest  entityQueryRequest) {
        return ResponseEntity.ok(this.entityService.findAll(entityQueryRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody NoteGroupDto  entityDto)
    {
        return ResponseEntity.ok(this.entityService.save(entityDto));
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody NoteGroupDto entityDto)
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

    @JsonView(JsonViewNoteGroup.viewJsonNoteGroupAndNote.class)
    @GetMapping("/findAllNoteGroupAndNote")
    public ResponseEntity<GlobalReponsePagination> findAllNoteGroupAndNote(@ModelAttribute final NoteGroupQueryRequest entityQueryRequest) {
        return ResponseEntity.ok(this.entityService.findAllNoteGroupAndNote(entityQueryRequest));
    }
}
