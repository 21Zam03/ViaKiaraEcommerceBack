package com.zam.security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequest {

    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be an email address with correct format")
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

}
