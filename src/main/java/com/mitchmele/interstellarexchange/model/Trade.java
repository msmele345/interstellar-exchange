package com.mitchmele.interstellarexchange.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRADE")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRADE_ID", nullable = false, unique = true)
    private Integer id;

    @Column(name = "BID_ID")
    private Integer bidId;

    @Column(name = "ASK_ID")
    private Integer askId;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "Tradeprice")
    private BigDecimal tradePrice;

    @Column(name = "CREATED_TS", updatable = false)
    @CreationTimestamp
    private Date timeStamp;
}
