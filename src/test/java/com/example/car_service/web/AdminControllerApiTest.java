package com.example.car_service.web;

import com.example.car_service.admin.AdminService;
import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.security.UserData;
import com.example.car_service.service_reminder.service.ServiceReminderService;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.controllers.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ServiceReminderService serviceReminderService;

    @MockitoBean
    private ServiceRecordClient serviceRecordClient;

    @MockitoBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postMapping_fromAdmin_shouldReturn302() throws Exception {
        UserData user = new UserData(UUID.randomUUID(), "username", "password", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder requestBuilder = post("/admin/users/{id}/role", UUID.randomUUID()).param("role", "USER").with(user(user)).with(csrf());
        mockMvc.perform(requestBuilder).andExpect(status().is(302));

    }

    @Test
    void getRecordsFromAdmin_shouldReturn302() throws Exception {
        UserData user = new UserData(UUID.randomUUID(), "username", "password", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder requestBuilder = get("/admin/services").with(user(user)).with(csrf());
        mockMvc.perform(requestBuilder).andExpect(status().is(200));
    }

    @Test
    void getRecordsFromNonAdmin_shouldReturn500() throws Exception {
        UserData user = new UserData(UUID.randomUUID(), "username", "password", UserRole.USER, true);
        MockHttpServletRequestBuilder requestBuilder = get("/admin/services").with(user(user)).with(csrf());
        mockMvc.perform(requestBuilder).andExpect(status().is(403));
    }
    @Test
    void getServiceRecords_shouldReturn200() throws Exception {
        UserData user = new UserData(UUID.randomUUID(), "username", "password", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder requestBuilder = get("/admin/services").with(user(user)).with(csrf());
        mockMvc.perform(requestBuilder).andExpect(status().is(200));
    }
@Test
    void getUsers_shouldReturn200() throws Exception {
        UserData user = new UserData(UUID.randomUUID(), "username", "password", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder requestBuilder = get("/admin/users").with(user(user)).with(csrf());
    }

}
