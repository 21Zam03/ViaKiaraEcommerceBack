package com.zam.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoggedResponse {

    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;

}
