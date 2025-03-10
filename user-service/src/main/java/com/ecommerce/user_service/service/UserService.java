package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.LoginUserDTO;
import com.ecommerce.user_service.dto.RegisterUserDTO;
import com.ecommerce.user_service.dto.UserDTO;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public UserDTO register(RegisterUserDTO userDTO) throws Exception {
        if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new Exception("Passwords does not match");
        }
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if(existingUser.isPresent()) throw new Exception("User Already Exists!!");
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User newUser = UserMapper.convertToEntity(userDTO);
        return UserMapper.convertToDto(userRepository.save(newUser));
    }

    public User login(LoginUserDTO userDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getEmail(),
                            userDTO.getPassword()
                    )
            );
        } catch(BadCredentialsException ex) {
            throw new Exception(ex.getMessage());
        }

        return userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDTO.getEmail()));
    }
}
