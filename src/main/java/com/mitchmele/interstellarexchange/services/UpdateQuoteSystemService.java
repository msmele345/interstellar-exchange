package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.ask.Ask;
import com.mitchmele.interstellarexchange.bid.Bid;
import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import com.mitchmele.interstellarexchange.quote.*;
import com.mitchmele.interstellarexchange.ask.repository.AskRepository;
import com.mitchmele.interstellarexchange.bid.repository.BidRepository;
import com.mitchmele.interstellarexchange.trade.Trade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateQuoteSystemService {

    private final BidRepository bidRepository;
    private final AskRepository askRepository;
    private final ErrorLogService errorLogService;
    private final UsedQuoteHelper usedQuoteHelper;

    public void updateMarket(List<QuotePrice> allQuotes, List<Trade> actualTrades) {

        List<QuotePrice> bidsAndAsksUsedInTrades = usedQuoteHelper
                .fetchUsedQuotes(allQuotes, actualTrades);
        try {
            bidsAndAsksUsedInTrades.forEach(quotePrice -> {
                if (quotePrice.getClass().equals(Bid.class)) {
                    bidRepository.delete((Bid) quotePrice);
                }
                if (quotePrice.getClass().equals(Ask.class)) {
                    askRepository.delete((Ask) quotePrice);
                }
            });
            log.info("SUCCESSFULLY DELETED BID AND OFFER FOR TRADE");
        } catch (Exception e) {
            log.error("QUOTE SYSTEM SERVICE DELETE FAILURE");
            ErrorLogEntity errorEntity = ErrorLogEntity.builder()
                    .serviceLocation(ServiceLocation.DATABASE.value)
                    .errorMessage(e.getLocalizedMessage())
                    .errorType(ErrorType.PROCESSING.value)
                    .build();

            errorLogService.saveError(errorEntity);
        }
    }
}
