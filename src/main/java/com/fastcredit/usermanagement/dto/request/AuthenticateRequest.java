package com.fastcredit.usermanagement.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {

    private String usernameOrEmail;

    private String password;
}
