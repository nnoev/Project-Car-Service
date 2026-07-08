package com.example.car_service.user.model;

import com.example.car_service.service_record.model.ServiceRecord;
import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column
    @Enumerated(EnumType.STRING)
    private UserClass classType;

    @Column
    private boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<ServiceRecord> serviceRecords = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<ServiceReminder> serviceReminders = new ArrayList<>();

    public BigDecimal getTotalCost() {
        return serviceRecords.stream()
                .map(ServiceRecord::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
