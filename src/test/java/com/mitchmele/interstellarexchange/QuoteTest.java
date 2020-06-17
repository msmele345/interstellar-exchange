package com.mitchmele.interstellarexchange;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;

import java.math.BigDecimal;

abstract public class QuoteTest {

//    public Bid inputBid = Bid.builder().id(22).symbol("OGC").bidPrice(BigDecimal.valueOf(113.08)).build();
    public Bid inputBid1 = Bid.builder().id(162).symbol("ABC").bidPrice(BigDecimal.valueOf(23.05)).build();
    public Bid inputBid2 = Bid.builder().id(163).symbol("ABC").bidPrice(BigDecimal.valueOf(23.07)).build();
    public Bid inputBid3 = Bid.builder().id(164).symbol("BBN").bidPrice(BigDecimal.valueOf(13.25)).build();
    public Bid inputBid4 = Bid.builder().id(172).symbol("CAZ").bidPrice(BigDecimal.valueOf(3.06)).build();
    public Bid inputBid5 = Bid.builder().id(22).symbol("OGC").bidPrice(BigDecimal.valueOf(113.03)).build();

    public Ask inputAsk = Ask.builder().id(19).symbol("ABC").askPrice(BigDecimal.valueOf(23.05)).build();
    public Ask inputAsk2 = Ask.builder().id(20).symbol("BBN").askPrice(BigDecimal.valueOf(13.25)).build();
    public Ask inputAsk3 = Ask.builder().id(21).symbol("CAZ").askPrice(BigDecimal.valueOf(3.07)).build();
    public Ask inputAsk4 = Ask.builder().id(22).symbol("OGC").askPrice(BigDecimal.valueOf(113.03)).build();
    public Ask inputAsk5 = Ask.builder().id(23).symbol("OGC").askPrice(BigDecimal.valueOf(113.08)).build();

}
