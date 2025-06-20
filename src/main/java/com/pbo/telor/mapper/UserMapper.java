package com.pbo.telor.mapper;

import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.model.UserEntity;

public class UserMapper {
    public static UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                user.getRole(),
                user.getOrmawa() != null
                        ? OrmawaMapper.toResponseWithoutUser(user.getOrmawa())
                        : null);
    }

    public static UserResponse toResponseWithoutOrmawa(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                user.getRole(),
                null);
    }

}
