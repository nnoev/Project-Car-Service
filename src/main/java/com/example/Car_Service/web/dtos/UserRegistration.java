package com.example.Car_Service.web.dtos;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistration {

    @Size(min = 6, max = 20 ,message = "Username must be between 6 and 20 characters")
    @NotBlank
    private String username;

    @Size(min = 6, max = 20 , message = "Password must be between 6 and 20 characters")
    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

}
