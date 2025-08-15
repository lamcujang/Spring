package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.AuthDto;
import org.common.dbiz.dto.userDto.password.ChangePasswordDto;
import org.common.dbiz.dto.userDto.password.EmailDto;
import org.common.dbiz.dto.userDto.password.VerifyCodeDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;

public interface UserV2Service {

    GlobalReponse login(UserDto userLoginDto);

    GlobalReponse register(UserDto userDto);

    GlobalReponse sendVerifyEmail(EmailDto emailDto);

    GlobalReponse verifyEmail(VerifyCodeDto verifyCodeDto);

    GlobalReponse changePassword(ChangePasswordDto dto);

    GlobalReponse cancelVerifyEmail(EmailDto emailDto);

    GlobalReponse forgotPassword(ChangePasswordDto dto);

    GlobalReponse update(UserDto dto );

    GlobalReponse authentication(AuthDto dto);

}
