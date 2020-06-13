package com.mitchmele.interstellarexchange.repository;

import com.mitchmele.interstellarexchange.model.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {
}
