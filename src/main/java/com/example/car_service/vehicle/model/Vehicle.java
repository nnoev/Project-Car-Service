package com.example.car_service.vehicle.model;

import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Make is required")
    @Column(nullable = false)
    private String make;

    @Min(value = 1900, message = "Year must be between 1900 and 2100")
    @Max(value = 2100, message = "Year must be between 1900 and 2100")
    @Column(nullable = false)
    private int productionYear;

    @NotBlank(message = "VIN is required")
    @Column(nullable = false)
    private String vin;

    @Min(value = 0, message = "Mileage must be positive")
    @Column(nullable = false)
    private int mileage;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Builder.Default
    @OneToMany(mappedBy = "vehicle")
    private List<ServiceReminder> serviceReminders = new ArrayList<>();

}
