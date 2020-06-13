package com.mitchmele.interstellarexchange.repository;

import com.mitchmele.interstellarexchange.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
}
