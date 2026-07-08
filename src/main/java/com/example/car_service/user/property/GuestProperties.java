package com.example.car_service.user.property;

import com.example.car_service.user.model.UserClass;
import com.example.car_service.user.model.UserRole;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "user.guest-user")
public class GuestProperties {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private UserClass classType;

    private boolean active;

}
