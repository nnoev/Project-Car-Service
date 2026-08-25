package com.example.car_service.web;

import com.example.car_service.client.ServiceRecordClient;
import com.example.car_service.client.dto.ServiceRecordRequest;
import com.example.car_service.security.UserData;
import com.example.car_service.user.model.User;
import com.example.car_service.user.model.UserRole;
import com.example.car_service.user.service.UserService;
import com.example.car_service.vehicle.model.Vehicle;
import com.example.car_service.vehicle.service.VehicleService;
import com.example.car_service.web.controllers.ServiceRecordController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceRecordController.class)
public class ServiceRecordControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ServiceRecordClient serviceRecordClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAddServiceRecord_shouldReturnFormWithUserVehicles()
            throws Exception {
        UUID userId = UUID.randomUUID();
        UserData principal = new UserData(
                userId,
                "john",
                "password",
                UserRole.USER,
                true
        );
        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID())
                .make("Mercedes")
                .model("E350")
                .productionYear(2010)
                .vin("WDD21200000000001")
                .mileage(150000)
                .build();
        User user = User.builder()
                .id(userId)
                .username("john")
                .role(UserRole.USER)
                .vehicles(List.of(vehicle))
                .build();
        when(userService.getById(userId))
                .thenReturn(user);
        mockMvc.perform(get("/service-records/add")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("service-record-form"))
                .andExpect(model().attributeExists("serviceRecord"))
                .andExpect(model().attribute(
                        "vehicles",
                        user.getVehicles()
                ))
                .andExpect(model().attribute("isEdit", false));
        verify(userService).getById(userId);
        verifyNoInteractions(vehicleService);
        verifyNoInteractions(serviceRecordClient);
    }
    @Test
    void postAddServiceRecord_withValidData_shouldCreateRecordAndRedirect()
            throws Exception {

        UUID userId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        LocalDate serviceDate = LocalDate.of(2026, 8, 25);

        UserData principal = new UserData(
                userId,
                "john",
                "password",
                UserRole.USER,
                true
        );

        User user = User.builder()
                .id(userId)
                .username("john")
                .role(UserRole.USER)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .make("Mercedes")
                .model("E350")
                .productionYear(2010)
                .vin("WDD21200000000001")
                .mileage(150000)
                .owner(user)
                .build();

        when(userService.getById(userId))
                .thenReturn(user);
        when(vehicleService.getById(vehicleId))
                .thenReturn(vehicle);

        mockMvc.perform(post("/service-records/add")
                        .with(user(principal))
                        .with(csrf())
                        .param("vehicleId", vehicleId.toString())
                        .param("date", serviceDate.toString())
                        .param("serviceType", "Brake service")
                        .param("description", "Brake pads replaced")
                        .param("cost", "220.00")
                        .param("mileageAtService", "150000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/service-records"))
                .andExpect(redirectedUrl("/service-records"))
                .andExpect(flash().attribute(
                        "message",
                        "Service record added successfully"
                ));

        ArgumentCaptor<ServiceRecordRequest> requestCaptor =
                ArgumentCaptor.forClass(ServiceRecordRequest.class);

        verify(serviceRecordClient).create(requestCaptor.capture());

        ServiceRecordRequest capturedRequest = requestCaptor.getValue();

        assertEquals(vehicleId, capturedRequest.getVehicleId());
        assertEquals(userId, capturedRequest.getUserId());
        assertEquals(serviceDate, capturedRequest.getServiceDate());
        assertEquals(
                "Brake service",
                capturedRequest.getServiceType()
        );
        assertEquals(
                "Brake pads replaced",
                capturedRequest.getDescription()
        );
        assertEquals(
                new BigDecimal("220.00"),
                capturedRequest.getCost()
        );
        assertEquals(
                150000,
                capturedRequest.getMileageAtService()
        );

        verify(userService).getById(userId);
        verify(vehicleService).getById(vehicleId);
        verify(serviceRecordClient, never())
                .getAllByUserId(any(UUID.class));
    }

}
