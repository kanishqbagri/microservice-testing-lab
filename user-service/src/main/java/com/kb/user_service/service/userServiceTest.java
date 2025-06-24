package com.kb.user_service.service;

import com.kb.user_service.dto.UserRegistrationRequest;
import com.kb.user_service.entity.User;
import com.kb.user_service.repository.UserRepository;
import com.kb.user_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder(), new ModelMapper());
    }

    @Test
    void registerUser_ShouldSucceed_WhenEmailIsNew() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@example.com");
        request.setName("Test");
        request.setPassword("pass");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        var response = userService.registerUser(request);
        assertEquals("test@example.com", response.getEmail());
    }
}
