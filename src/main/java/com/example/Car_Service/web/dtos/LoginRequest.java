package com.example.Car_Service.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Size(min = 6, max = 20)
    @NotBlank
    private String username;

    @Size(min = 6, max = 20)
    @NotBlank
    private String password;
}
