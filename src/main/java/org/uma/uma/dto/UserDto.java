package org.uma.uma.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.uma.uma.validation.CustomValidation;

@Setter
@Getter
@RequiredArgsConstructor
public class UserDto {
    @NotBlank(message = "Username is mandatory")
    @CustomValidation(valueType = CustomValidation.Type.USERNAME, message = "Username must be alphanumeric and between 5-20 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @CustomValidation(valueType = CustomValidation.Type.PASSWORD, message = "Password must be at least 8 characters, include an uppercase letter and a number")
    private String password;

    @NotBlank(message = "Role is mandatory")
    private String role;
}

