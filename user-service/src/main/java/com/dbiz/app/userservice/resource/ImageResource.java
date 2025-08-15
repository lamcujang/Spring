package com.dbiz.app.userservice.resource;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.userservice.helper.UserMapper;
import com.dbiz.app.userservice.service.ImageService;
import com.dbiz.app.userservice.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.jsonView.JsonViewUserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.UserQueryRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/image"})
@Slf4j
@RequiredArgsConstructor
public class ImageResource {
	
	private final ImageService imageService;

	@PostMapping("/save")
	public ResponseEntity<GlobalReponse> delete(@RequestBody ImageDto imageDto) {
		log.info("save image");
		ImageDto image = imageService.save(imageDto);
		return ResponseEntity.ok(GlobalReponse.builder().data(image).build());
	}

	@DeleteMapping("/delete/{imageId}")
	public ResponseEntity<GlobalReponse> delete(@PathVariable Integer imageId) {
		log.info("delete image");
		imageService.deleteById(imageId);
		return ResponseEntity.ok(GlobalReponse.builder().message("Image deleted successfully").build());
	}

	@GetMapping("/{imageId}")
	public ResponseEntity<GlobalReponse> findById(@PathVariable Integer imageId) {
		log.info("find image by id");
		ImageDto image = imageService.findById(imageId);
		return ResponseEntity.ok(GlobalReponse.builder().data(image).build());
	}
}










