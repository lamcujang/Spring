package com.dbiz.app.proxyclient.business.product.service;

import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.productDto.ListProductDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PRODUCT-SERVICE", contextId = "imagesClientService", path = "/product-service/api/v1/images")
public interface ImageClientService {
	@PostMapping("/saveImage")
	public ResponseEntity<GlobalReponse> saveImage(@RequestBody ImageDto imageDto);

	@DeleteMapping("/deleteImage")
	 ResponseEntity<GlobalReponse> deleteImage(@RequestParam("imageId") final Integer imageId);
}










