package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId); // DB 데이터 가공해서 전달
    Iterable<UserEntity> getUserByAll(); // DB 데이터 바로 전달
}
