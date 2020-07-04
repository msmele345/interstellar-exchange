package com.mitchmele.interstellarexchange.bid.repository;

import com.mitchmele.interstellarexchange.bid.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllBySymbol(String symbol);
}
