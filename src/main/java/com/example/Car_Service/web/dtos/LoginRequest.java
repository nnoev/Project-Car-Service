package com.example.Car_Service.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Size(min = 6, max = 20 ,message = "Username must be between 6 and 20 characters")
    @NotBlank
    private String  username;

    @Size(min = 6, max = 20 , message = "Password must be between 6 and 20 characters")
    @NotBlank
    private String password;
}
