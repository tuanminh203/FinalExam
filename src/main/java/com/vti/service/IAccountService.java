package com.vti.service;

import com.vti.dto.AccountRegisterRequestDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAccountService extends UserDetailsService {
    void register(AccountRegisterRequestDTO accountRegisterRequestDTO);
    void updateAccount(Integer id, AccountRegisterRequestDTO accountRegisterRequestDTO);
}
