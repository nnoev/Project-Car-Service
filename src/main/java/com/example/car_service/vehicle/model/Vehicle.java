package com.example.car_service.vehicle.model;

import com.example.car_service.service_reminder.model.ServiceReminder;
import com.example.car_service.user.model.User;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String vin;

    @Column(nullable = false)
    private int mileage;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Builder.Default
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<ServiceReminder> serviceReminders = new ArrayList<>();

}
