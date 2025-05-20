package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String email;
    private String role;
    private String address;

    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
