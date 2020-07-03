package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuoteUpdateResult;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateQuoteSystemServiceTestIT {

    @Autowired
    private UpdateQuoteSystemService updateQuoteSystemService;

    @Autowired
    private BidRepository bidRepository;


    @Autowired
    private AskRepository askRepository;


    @Test
    void updateQuotes() {

        Ask askToBeDeleted = Ask.builder()
                .id(900)
                .askPrice(BigDecimal.valueOf(10.00))
                .build();

        Bid bidToBeDeleted = Bid.builder()
                .id(901)
                .bidPrice(BigDecimal.valueOf(11.00))
                .build();

        bidRepository.save(bidToBeDeleted);
        askRepository.save(askToBeDeleted);

        QuoteUpdateResult expected = QuoteUpdateResult.builder()
                .isSuccess(true)
                .build();
        QuoteUpdateResult actual = updateQuoteSystemService.updateMarket(bidToBeDeleted, askToBeDeleted);

        assertThat(actual).isEqualTo(expected);
    }
}