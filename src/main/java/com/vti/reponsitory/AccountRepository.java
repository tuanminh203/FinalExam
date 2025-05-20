package com.vti.reponsitory;

import com.vti.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsById(Integer id);
}
