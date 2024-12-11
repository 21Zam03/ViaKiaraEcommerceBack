package com.zam.security.services;

import com.zam.security.entities.UserEntity;
import com.zam.security.payload.request.SignInRequest;
import com.zam.security.payload.request.SignUpRequest;
import com.zam.security.payload.response.MessageResponse;
import com.zam.security.payload.response.SignInResponse;
import com.zam.security.payload.response.SignUpResponse;
import com.zam.security.payload.response.UserLoggedResponse;

public interface AuthService {

    public SignUpResponse signUp(SignUpRequest signUpRequest);
    public SignInResponse signIn(SignInRequest signInRequest);
    public MessageResponse changePassword(String oldPassword, String newPassword);
    public UserLoggedResponse validateSession(String token);
}
