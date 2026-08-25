package com.example.car_service.web;

import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.controllers.ProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private UserData principal;
    private User existingUser;
    private UUID userId;

    @Test
    void changePassword_withValidData_shouldUpdatePasswordAndRedirect() throws Exception {


        when(userService.getById(userId)).thenReturn(existingUser);
        when(passwordEncoder.matches(
                "current123",
                "encoded-password"
        )).thenReturn(true);
        when(passwordEncoder.encode("newPassword123"))
                .thenReturn("new-encoded-password");

        mockMvc.perform(post("/profile/change-password")
                        .with(user(principal))
                        .with(csrf())
                        .param("currentPassword", "current123")
                        .param("newPassword", "newPassword123")
                        .param("confirmPassword", "newPassword123"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/profile"))
                .andExpect(redirectedUrl("/profile"));


        assertEquals(
                "new-encoded-password",
                existingUser.getPassword()
        );

        verify(passwordEncoder)
                .matches("current123", "encoded-password");
        verify(passwordEncoder).encode("newPassword123");
        verify(userService).save(existingUser);
    }
    @Test
    void changePassword_withIncorrectCurrentPassword_shouldReturnProfile()
            throws Exception {

        when(userService.getById(userId)).thenReturn(existingUser);
        when(passwordEncoder.matches(
                "incorrect-password",
                "encoded-password"
        )).thenReturn(false);

        mockMvc.perform(post("/profile/change-password")
                        .with(user(principal))
                        .with(csrf())
                        .param("currentPassword", "incorrect-password")
                        .param("newPassword", "newPassword123")
                        .param("confirmPassword", "newPassword123"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute(
                        "activeTab",
                        "change-password"
                ))
                .andExpect(model().attribute(
                        "passwordError",
                        "Current password is incorrect"
                ));

        verify(passwordEncoder)
                .matches("incorrect-password", "encoded-password");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userService, never()).save(any(User.class));
    }
    @Test
    void changePassword_withInvalidData_shouldReturnValidationErrors()
            throws Exception {

        when(userService.getById(userId)).thenReturn(existingUser);

        mockMvc.perform(post("/profile/change-password")
                        .with(user(principal))
                        .with(csrf())
                        .param("currentPassword", "")
                        .param("newPassword", "123")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute(
                        "activeTab",
                        "change-password"
                ))
                .andExpect(model().attributeHasFieldErrors(
                        "changePassword",
                        "currentPassword",
                        "newPassword",
                        "confirmPassword"
                ));

        verify(userService, never()).save(any(User.class));
    }
    @Test
    void changeProfile_withInvalidData_shouldReturnValidationErrors()
            throws Exception {

        when(userService.getById(userId)).thenReturn(existingUser);

        mockMvc.perform(post("/profile/edit")
                        .with(user(principal))
                        .with(csrf())
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "invalid-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("activeTab", "edit-profile"))
                .andExpect(model().attributeHasFieldErrors(
                        "changeProfile",
                        "firstName",
                        "lastName",
                        "email"
                ));

        verify(userService, never()).checkForEmail(anyString());
        verify(userService, never()).save(any(User.class));
    }



    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        principal = new UserData(
                userId,
                "john",
                "encoded-password",
                UserRole.USER,
                true
        );

        existingUser = User.builder()
                .id(userId)
                .username("john")
                .password("encoded-password")
                .firstName("John")
                .lastName("Smith")
                .email("john@car-service.com")
                .role(UserRole.USER)
                .active(true)
                .build();
    }
}
