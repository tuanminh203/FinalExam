package com.vti.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountUpdateDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDate birthDate;
    private String address;
}
