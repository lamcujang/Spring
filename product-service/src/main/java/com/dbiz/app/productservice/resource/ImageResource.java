package com.dbiz.app.productservice.resource;

import com.dbiz.app.productservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImageResource {

    private final ImageService imageService;

    @PostMapping("/saveImage")
    public ResponseEntity<GlobalReponse> saveImage(@RequestBody ImageDto imageDto)
    {

        log.info("*** ProductDto List, controller; fetch all product *");
        GlobalReponse response = GlobalReponse.builder()
                .data(this.imageService.save(imageDto))
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/findById/{imageId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("imageId") final Integer imageId) {
        log.info("*** Find by id, controller; find product by id *");
        GlobalReponse response = new GlobalReponse();
        response.setData(this.imageService.findById(imageId));
        response.setMessage("success");
        response.setStatus(HttpStatus.OK.value());
        response.setErrors("");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteImage")
    public ResponseEntity<GlobalReponse> deleteImage(@RequestParam("imageId") final Integer imageId) {
        log.info("*** Delete, controller; delete product  *");
        GlobalReponse response = new GlobalReponse();
        response.setData(null);
        response.setMessage("Image deleted successfully");
        return ResponseEntity.ok(response);
    }

}
