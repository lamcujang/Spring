package com.dbiz.app.userservice.helper;

import com.dbiz.app.userservice.domain.User;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    public User toUser(final UserDto userDto)
    {
        return modelMapper.map(userDto, User.class);
    }

    public User toUserInt(final UserIntDto userDto)
    {
        return modelMapper.map(userDto, User.class);
    }
    public User updateEntity(UserDto dto, User entity) {
        modelMapper.map(dto, entity);
        return entity;
    }
    public User updateEntityInt(UserIntDto dto, User entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public UserDto toUserDto(final User uom)
    {
        return modelMapper.map(uom, UserDto.class);
    }

    public UserLoginDto userLoginDto(final UserDto user) {
        return modelMapper.map(user, UserLoginDto.class);
    }
}
