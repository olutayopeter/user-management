package com.fastcredit.usermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@ToString
@AllArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final int expiresin;
    private final String status;
    private final String message;

    public JwtResponse(String status, String message, String token, Integer expiresin) {
        this.status = status;
        this.message = message;
        this.jwttoken = token;
        this.expiresin = expiresin;
    }

}
