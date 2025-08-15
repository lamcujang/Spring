package com.dbiz.app.productservice.resource;

import javax.validation.Valid;

import com.dbiz.app.productservice.service.ProductIntService;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dbiz.app.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
@RequiredArgsConstructor
public class ProductResource {

    private final ProductService productService;

    private final ProductIntService productIntService;

    @GetMapping("/{productId}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("productId") final Integer productId) {
        log.info("*** ProductDto, controller; fetch product by id *");
        return ResponseEntity.ok(this.productService.findById(productId));
    }


    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute ProductQueryRequest request) {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findAll(request));
    }

    @PostMapping("/saveProduct")
    public ResponseEntity<GlobalReponse> saveProduct(@RequestBody @Valid final ProductDto productDto) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.productService.save(productDto));
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("productId") final Integer productId) {
        log.info("*** Delete, controller; delete product  *");
        return ResponseEntity.ok(this.productService.deleteById(productId));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<GlobalReponse> updateProduct(@RequestBody @Valid final ProductDto productDto) {
        log.info("*** Save, controller; save product  *");
        return ResponseEntity.ok(this.productService.save(productDto));
    }


    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody final SaveAllProductAttr productDtos) {
        log.info("*** Save All, controller; save all products  *");
        return new ResponseEntity<>(this.productService.saveAll(productDtos), HttpStatus.CREATED);
    }

    @PostMapping("/savePart")
    public ResponseEntity<GlobalReponse> savePart(@RequestBody final SaveAllProductAttr productDto) {
        log.info("*** Save All, controller; save all products  *");
        return ResponseEntity.ok(this.productService.savePart(productDto));
    }


    @PostMapping("/saveAll2")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody final ListProductDto productDtos) {
        log.info("*** Save All, controller; save all products  *");
        return new ResponseEntity<>(this.productService.saveAllProduct(productDtos), HttpStatus.OK);
    }

    @GetMapping("/KcGetProduct")
    public ResponseEntity<GlobalReponsePagination> KcGetProduct(@ModelAttribute ProductQueryRequest request) {
        log.info("*** KcGetProduct, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.KcGetProduct(request));
    }


    @PostMapping("/intSave")
    public ResponseEntity<GlobalReponse> intSave(@RequestBody final List<ProductIntDto> productDto) {
        log.info("*** Save, controller; save product  *");
        GlobalReponse response = new GlobalReponse();
//		try {
        response = this.productService.intSave(productDto);

//		} catch (Exception e) {
//			response.setMessage("Error: " + e.getMessage())	;
//			response.setErrors(e.getMessage())	;
//			e.printStackTrace();
//			log.error("Error: ", e);
//			response.setErrors(e.getMessage());
//			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		}
        return ResponseEntity.ok(response);

    }

    @GetMapping("/findByIdOrgAccess")
    public ResponseEntity<GlobalReponse> findByIdOrgAccess(@ModelAttribute ProductQueryRequest request) {
        log.info("*** findByIdOrgAccess, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findByIdOrgAccess(request));
    }

    @GetMapping("/findByIdLocator")
    public ResponseEntity<GlobalReponse> findByIdLocator(@ModelAttribute ProductQueryRequest request) {
        log.info("*** findByIdLocator, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findByIdLocator(request));
    }

    @GetMapping("/findAllByWarehouse")
    public ResponseEntity<GlobalReponsePagination> findAllByWarehouse(@ModelAttribute ProductQueryRequest request) {
        log.info("*** findAllByWarehouse, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findAllByWarehouse(request));
    }


    @GetMapping("/getOnHand")
    public ResponseEntity<GlobalReponsePagination> getOnHand(@ModelAttribute ProductQueryRequest request) {
        log.info("*** findAllByWarehouse, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.getOnHand(request));
    }

    @PostMapping("/org")
    public ResponseEntity<GlobalReponsePagination> getOrgAssign(@RequestBody final ProductQueryRequest productDto) {
        log.info("*** Save All, controller; save all products  *");
        return ResponseEntity.ok(this.productService.getOrgAssign(productDto));
    }


    @PostMapping("/saveImageProduct")
    public ResponseEntity<GlobalReponse> saveImage(@RequestBody ProductDto productDto) {
        log.info("*** save image, controller;");

        return ResponseEntity.ok(this.productService.saveImage(productDto.getId(), productDto.getImage()));
    }

    @PostMapping("/saveIntefaceProduct")
    public ResponseEntity<GlobalReponse> saveIntefaceProduct(@RequestBody ProductIntDto productIntDto) {
        log.info("*** save interface product, controller;");
        return ResponseEntity.ok(this.productIntService.saveInterfaceProduct(productIntDto));
    }


    @GetMapping("/findAllSimple")
    public ResponseEntity<GlobalReponsePagination> findAllSimple(@ModelAttribute ProductQueryRequest request) {
        log.info("*** findAllSimple, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findAllSimple(request));
    }

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll2(@ModelAttribute ProductQueryRequest request) {
        log.info("*** ProductDto List, controller; fetch all product *");
        return ResponseEntity.ok(this.productService.findAllV2(request));
    }
}










