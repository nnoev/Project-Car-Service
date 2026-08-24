package com.example.car_service.web;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ServiceRecordClient serviceRecordClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRegister_shouldReturnStatus200andRedirectToLoginAndRegisterUserInvoke() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .formField("username", "username")
                .formField("password", "password")
                .formField("email", "username@car.com")
                .formField("firstName", "firstName")
                .formField("lastName", "lastName")
                .with(csrf());
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/login"));
                verify(userService).registerUser(any());
    } @Test
    void postRegisterWithInvalidForm_shouldReturnStatus200andRedirectToRegister() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/register")
                .formField("username", "username")
                .formField("password", "123")
                .formField("email", "username@car.com")
                .formField("firstName", "firstName")
                .formField("lastName", "lastName")
                .with(csrf());
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(view().name("register"));
    }

}
