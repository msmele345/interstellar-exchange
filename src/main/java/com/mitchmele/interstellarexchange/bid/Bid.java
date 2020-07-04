package com.mitchmele.interstellarexchange.bid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mitchmele.interstellarexchange.quote.QuotePrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Entity
@Table(name = "BID")
public class Bid implements QuotePrice, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    private String symbol;

    @Column(name = "Bidprice")
    private BigDecimal bidPrice;

    @Column(name = "CREATED_TS", updatable = false)
    @CreationTimestamp
    private Date timeStamp;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return bidPrice;
    }
}