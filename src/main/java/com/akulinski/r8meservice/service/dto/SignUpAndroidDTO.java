package com.akulinski.r8meservice.service.dto;

import com.akulinski.r8meservice.config.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class SignUpAndroidDTO implements Serializable {

    private final String password;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private final String username;

    @Email
    @Size(min = 5, max = 254)
    private final String email;

    public SignUpAndroidDTO(String password, String username, String email) {
        this.password = password;
        this.username = username;
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
