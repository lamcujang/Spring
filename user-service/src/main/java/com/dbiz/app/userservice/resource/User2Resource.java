package com.dbiz.app.userservice.resource;


import com.dbiz.app.userservice.service.UserV2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.AuthDto;
import org.common.dbiz.dto.userDto.password.ChangePasswordDto;
import org.common.dbiz.dto.userDto.password.EmailDto;
import org.common.dbiz.dto.userDto.password.VerifyCodeDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/api/v2/users"})
@Slf4j
@RequiredArgsConstructor
public class User2Resource {

    private final UserV2Service userService;


    @PostMapping("/login")
    public ResponseEntity<GlobalReponse> Login(
            @RequestBody
            UserDto userLoginDto) {
        log.info("*** UserDto, resource; login user ***");
        return ResponseEntity.ok(this.userService.login(userLoginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<GlobalReponse> register(
            @RequestBody
            UserDto userDto) {
        log.info("*** UserDto, resource; register user ***");
        return ResponseEntity.ok(this.userService.register(userDto));
    }

    @PostMapping("/sendVerifyEmail")
    public ResponseEntity<GlobalReponse> sendVerifyEmail(
            @RequestBody
            EmailDto email) {
        log.info("*** UserDto, resource; send verify email ***");
        return ResponseEntity.ok(this.userService.sendVerifyEmail(email));
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<GlobalReponse> verifyEmail(
            @RequestBody
            VerifyCodeDto code) {
        log.info("*** UserDto, resource; verify email ***");
        return ResponseEntity.ok(this.userService.verifyEmail(code));
    }

    @PostMapping("/cancelVerifyEmail")
    public ResponseEntity<GlobalReponse> cancelVerifyEmail(
            @RequestBody
            EmailDto email) {
        log.info("*** UserDto, resource; cancel verify email ***");
        return ResponseEntity.ok(this.userService.cancelVerifyEmail(email));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<GlobalReponse> changePassword(
            @RequestBody
            ChangePasswordDto code) {
        log.info("*** UserDto, resource; change password ***");
        return ResponseEntity.ok(this.userService.changePassword(code));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<GlobalReponse> forgotPassword(
            @RequestBody
            ChangePasswordDto code) {
        log.info("*** UserDto, resource; fotgot password ***");
        return ResponseEntity.ok(this.userService.forgotPassword(code));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(
            @RequestBody
            UserDto dto) {
        log.info("*** UserDto, resource; Update User ***");
        return ResponseEntity.ok(this.userService.update(dto));
    }

    @PostMapping("/auth")
    ResponseEntity<GlobalReponse> authentication(
            @RequestBody AuthDto userDto
    )
    {
        log.info("*** UserDto, resource; authentication user ***");
        return ResponseEntity.ok(this.userService.authentication(userDto));
    }

}
