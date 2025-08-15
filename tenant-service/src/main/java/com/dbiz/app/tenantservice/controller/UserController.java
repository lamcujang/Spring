package com.dbiz.app.tenantservice.controller;


import com.dbiz.app.tenantservice.dto.reponse.CreateUserResponseDto;
import com.dbiz.app.tenantservice.dto.request.CreateUserRequestDto;
import com.dbiz.app.tenantservice.service.db.User1Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static lombok.AccessLevel.PRIVATE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    User1Service userService;

    @PostMapping("/v2")
    @Operation(summary = "Create a new user")
    public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto userDto) {

        CreateUserResponseDto user = userService.create(userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
