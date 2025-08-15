package com.dbiz.app.proxyclient.business.product.controller;


import com.dbiz.app.proxyclient.business.product.service.ImageClientService;
import com.dbiz.app.proxyclient.business.product.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.NoteDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.NoteQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageClientService service;

    @PostMapping("/saveImage")
    public ResponseEntity<GlobalReponse> saveImage(@RequestBody ImageDto imageDto)
    {
        log.info("*** saveImage, controller; fetch all image *");
        return ResponseEntity.ok(this.service.saveImage(imageDto).getBody());
    }

    @DeleteMapping("/deleteImage")
    public ResponseEntity<GlobalReponse> deleteImage(@RequestParam("imageId") final Integer imageId) {
        log.info("*** Delete, controller; delete image  *");
        return ResponseEntity.ok(this.service.deleteImage(imageId).getBody());
    }
}
