package com.vti.controller;

import com.vti.dto.AccountRegisterRequestDTO;
import com.vti.dto.AccountUpdateDTO;
import com.vti.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AccountRegisterRequestDTO
                                                   accountRegisterRequestDTO) {
        accountService.register(accountRegisterRequestDTO);
        return ResponseEntity.ok("Account registered successfully");
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Integer id,
                                            @RequestBody AccountRegisterRequestDTO
                                                    accountRegisterRequestDTO) {
        accountService.updateAccount(id, accountRegisterRequestDTO);
        return ResponseEntity.ok("Account updated successfully");
    }
}
