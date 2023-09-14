package com.fastcredit.usermanagement.dto.request;


import com.fastcredit.usermanagement.utils.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @Size(min = 4, message = "Username must be at least 4 characters long.")
    private String username;

    @Email(message = "Invalid email format.")
    private String email;

    @StrongPassword
    private String password;

}
