package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.LoginUserDTO;
import com.ecommerce.user_service.dto.RegisterUserDTO;
import com.ecommerce.user_service.dto.UserDTO;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.service.JWTService;
import com.ecommerce.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @QueryMapping
    public UserDTO login(@Argument("input") @Valid @RequestBody LoginUserDTO loginUserDTO) throws Exception {
        User authenticatedUser = userService.login(loginUserDTO);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        UserDTO userDTO = UserMapper.convertToDto(authenticatedUser);
        userDTO.setToken(jwtToken);
        return userDTO;
    }

    @MutationMapping
    public UserDTO signup(@Argument("input") @Valid RegisterUserDTO registerUserDTO) throws Exception {

        UserDTO userDTO = userService.register(registerUserDTO);
        return userDTO;
    }
}
