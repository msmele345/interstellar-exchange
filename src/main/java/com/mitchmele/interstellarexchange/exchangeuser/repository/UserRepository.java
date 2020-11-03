package com.mitchmele.interstellarexchange.exchangeuser.repository;

import com.mitchmele.interstellarexchange.exchangeuser.ExchangeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ExchangeUser,Long> {

    Optional<ExchangeUser> findByUsername(String username);
}
