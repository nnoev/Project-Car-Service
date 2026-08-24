package com.example.car_service.web;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.web.controllers.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ServiceRecordClient serviceRecordClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getInder_shouldReturnStatus200_andIndexView() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");
        mockMvc.perform(requestBuilder)
                .andExpect(view().name("index"))
                .andExpect(status().is(200));

    }

    @Test
    void getDashboard_shouldReturnStatus200_andDashboardView_andUserServiceRecordTotalCostObjects() throws Exception {
        User user = User.builder().id(UUID.randomUUID()).role(UserRole.USER).build();
        UserData authenticated = new UserData(user.getId(),user.getUsername(),user.getPassword(),user.getRole(),user.isActive());
        when(userService.getById(any())).thenReturn(user);
        MockHttpServletRequestBuilder requestBuilder = get("/dashboard").with(user(authenticated));
        mockMvc.perform(requestBuilder)
                .andExpect(view().name("dashboard"))
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("user", "serviceRecords", "totalCost"));

    }

}
