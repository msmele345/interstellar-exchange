package com.mitchmele.interstellarexchange.ask;

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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Entity
@Table(name = "ASK")
public class Ask implements QuotePrice, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    private String symbol;

    @Column(name = "Askprice")
    private BigDecimal askPrice;

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
        return askPrice;
    }
}

