package com.ecommerce.user_service.mapper;

import com.ecommerce.user_service.dto.RegisterUserDTO;
import com.ecommerce.user_service.dto.UserDTO;
import com.ecommerce.user_service.entity.User;

public class UserMapper {
    public static UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setToken(null);
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public static User convertToEntity(RegisterUserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
