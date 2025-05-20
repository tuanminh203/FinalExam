package com.vti.service;

import com.vti.dto.AccountRegisterRequestDTO;
import com.vti.entity.Account;
import com.vti.reponsitory.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public void register(AccountRegisterRequestDTO accountRegisterRequestDTO) {
        // Kiểm tra xem tên tài khoản đã tồn tại hay chưa
        if(accountRepository.existsByUsername(accountRegisterRequestDTO.getUsername())){
            throw new RuntimeException("Username is already in use");
        }

        Account account = modelMapper.map(accountRegisterRequestDTO, Account.class);

        // Gán các trường cần xử lý đặc biệt
        account.setPassword(passwordEncoder.encode(accountRegisterRequestDTO.getPassword()));
        account.setRole(accountRegisterRequestDTO.getRole().toUpperCase());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);

    }

    @Override
    public void updateAccount(Integer id, AccountRegisterRequestDTO accountRegisterRequestDTO) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("ID not found: " + id);
        }

        Account existingAccount = accountOptional.get();

        // Map các trường từ DTO sang đối tượng đã có
        modelMapper.map(accountRegisterRequestDTO, existingAccount);

        // Gán lại các trường cần xử lý đặc biệt
        existingAccount.setPassword(passwordEncoder.encode(accountRegisterRequestDTO.getPassword()));
        existingAccount.setRole(accountRegisterRequestDTO.getRole().toUpperCase());
        existingAccount.setUpdatedAt(LocalDateTime.now());

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
