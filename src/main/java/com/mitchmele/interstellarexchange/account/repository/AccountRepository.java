package com.mitchmele.interstellarexchange.account.repository;

import com.mitchmele.interstellarexchange.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
}
