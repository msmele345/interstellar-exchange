package com.mitchmele.interstellarexchange.trade.repository;

import com.mitchmele.interstellarexchange.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> { }