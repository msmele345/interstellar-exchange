package com.mitchmele.interstellarexchange.trade.repository;

import com.mitchmele.interstellarexchange.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<List<Trade>> findAllBySymbol(String symbol);

    Optional<Trade> findById(Integer id);

    List<Trade> findAllByTimeStampBetween(Date startDate, Date endDate);
}