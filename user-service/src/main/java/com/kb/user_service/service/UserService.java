package com.kb.user_service.service;

import com.kb.user_service.dto.UserRegistrationRequest;
import com.kb.user_service.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
}
