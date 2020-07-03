package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import com.mitchmele.interstellarexchange.model.Ask;
import com.mitchmele.interstellarexchange.model.Bid;
import com.mitchmele.interstellarexchange.model.QuoteUpdateResult;
import com.mitchmele.interstellarexchange.repository.AskRepository;
import com.mitchmele.interstellarexchange.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateQuoteSystemService {

    private final BidRepository bidRepository;
    private final AskRepository askRepository;
    private final ErrorLogService errorLogService;

    public QuoteUpdateResult updateMarket(Bid bidToBeDeleted, Ask askToBeDeleted) {

        try {
            bidRepository.delete(bidToBeDeleted);
            askRepository.delete(askToBeDeleted);
            log.info("SUCCESSFULLY DELETED BID AND OFFER FOR TRADE. BID: " + bidToBeDeleted.toString() + " ," + "ASK: " + askToBeDeleted.toString());

            return QuoteUpdateResult.builder().isSuccess(true).build();
        } catch (Exception e) {
            log.error("QUOTE SYSTEM SERVICE DELETE FAILURE");

            ErrorLogEntity errorEntity = ErrorLogEntity.builder()
                    .serviceLocation(ServiceLocation.DATABASE.value)
                    .errorMessage(e.getLocalizedMessage())
                    .errorType(ErrorType.PROCESSING.value)
                    .build();

            errorLogService.saveError(errorEntity);
            return QuoteUpdateResult.builder()
                    .isSuccess(false)
                    .lazyMessage(e.getLocalizedMessage())
                    .build();
        }
    }
}
