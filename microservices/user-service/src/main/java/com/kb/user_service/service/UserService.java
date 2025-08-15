package com.kb.user_service.service;

import com.kb.user_service.dto.UserRegistrationRequest;
import com.kb.user_service.dto.UserResponse;
import com.kb.user_service.dto.LoginRequest;
import com.kb.user_service.dto.LoginResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    LoginResponse login(LoginRequest request);
}
