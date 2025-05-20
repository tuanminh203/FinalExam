package com.vti.service;

import com.vti.dto.AccountRegisterRequestDTO;
import com.vti.dto.AccountUpdateDTO;
import com.vti.entity.Account;
import com.vti.reponsitory.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// Là annotation của Lombook
// Tự động tạo constructor cho các trường final
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(AccountRegisterRequestDTO accountRegisterRequestDTO) {
        // Kiểm tra xem tên tài khoản đã tồn tại hay chưa
        if(accountRepository.existsByUsername(accountRegisterRequestDTO.getUsername())){
            throw new RuntimeException("Username is already in use");
        }
        // Tạo một đối tượng Account từ DTO, lưu vào cơ sở dữ liệu
        Account account = new Account();
        account.setUsername(accountRegisterRequestDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountRegisterRequestDTO.getPassword()));
        account.setEmail(accountRegisterRequestDTO.getEmail());
        account.setRole(accountRegisterRequestDTO.getRole().toUpperCase());
        account.setBirthDate(accountRegisterRequestDTO.getBirthDate());
        account.setAddress(accountRegisterRequestDTO.getAddress());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);

    }

    @Override
    public void updateAccount(Integer id, AccountRegisterRequestDTO
                                          accountRegisterRequestDTO) {
        if (accountRepository.existsById(id)) {
            throw new RuntimeException("ID not found: " + id);
        }
        Optional<Account> accountOptional = accountRepository.findById(id);
        Account existingAccount = accountOptional.get();
        existingAccount.setUsername(accountRegisterRequestDTO.getUsername());
        existingAccount.setPassword(passwordEncoder.encode(accountRegisterRequestDTO.getPassword()));
        existingAccount.setEmail(accountRegisterRequestDTO.getEmail());
        existingAccount.setRole(accountRegisterRequestDTO.getRole().toUpperCase());
        existingAccount.setBirthDate(accountRegisterRequestDTO.getBirthDate());
        existingAccount.setAddress(accountRegisterRequestDTO.getAddress());

        accountRepository.save(existingAccount);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username not found: " +username);
        }
        //Collections.emptyList(): dang nhap thanh cong
        return  new User(username, account.getPassword(),
                AuthorityUtils.createAuthorityList(account.getRole()));
    }
}
