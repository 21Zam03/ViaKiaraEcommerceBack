package com.zam.security.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    private String email;
    private String password;
    private Set<Integer> roleList;
    private String fistName;
    private String lastName;

}
