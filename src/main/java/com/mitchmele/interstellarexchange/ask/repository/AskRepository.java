package com.mitchmele.interstellarexchange.ask.repository;

import com.mitchmele.interstellarexchange.ask.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {

    List<Ask> findAllBySymbol(String symbol);
}
