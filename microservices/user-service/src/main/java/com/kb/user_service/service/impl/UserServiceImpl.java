package com.kb.user_service.service.impl;

import com.kb.user_service.dto.LoginRequest;
import com.kb.user_service.dto.LoginResponse;
import com.kb.user_service.dto.UserRegistrationRequest;
import com.kb.user_service.dto.UserResponse;
import com.kb.user_service.entity.User;
import com.kb.user_service.exception.InvalidCredentialsException;
import com.kb.user_service.exception.UserAlreadyExistsException;
import com.kb.user_service.repository.UserRepository;
import com.kb.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponse.class);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        // TODO: Replace with real JWT token generation
        String token = "dummy-jwt-token";
        return new LoginResponse(token, user.getEmail(), user.getName());
    }
}
