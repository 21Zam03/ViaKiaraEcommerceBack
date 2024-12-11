package com.zam.security.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {

    private String email;
    private String fistName;
    private String lastName;
    private String message;
    private String token;
    private Integer status;

}
