package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.InvalidDateRequestException;
import com.mitchmele.interstellarexchange.helpers.DateHelper;
import com.mitchmele.interstellarexchange.helpers.TradesForDatesRequest;
import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import com.mitchmele.interstellarexchange.trade.Trade;
import com.mitchmele.interstellarexchange.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeLoaderService {

    private final TradeRepository tradeRepository;
    private final DateHelper dateHelper;
    private final LoaderAuditLogRepository loaderAuditLogRepository;

    public List<Trade> fetchTradesForSymbol(String symbol) {
        return tradeRepository.findAllBySymbol(symbol)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Trade> fetchTrades() {
        List<Trade> allTrades = tradeRepository.findAll();
        loaderAuditLogRepository.save(LoaderAuditLog.builder().loadCount(allTrades.size()).build());
        return allTrades;
    }

    public Trade fetchTradeById(Integer id) {
        return tradeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Trade> fetchTradesForDates(TradesForDatesRequest tradesForDatesRequest) throws Exception {

        Date startDate;
        Date endDate;
        try {
            startDate = dateHelper.createDateFromString(tradesForDatesRequest.getStartDate());
            endDate = dateHelper.createDateFromString(tradesForDatesRequest.getEndDate());
        } catch (Exception e) {
            throw new InvalidDateRequestException("Bad Request");
        }
        return tradeRepository.findAllByTimeStampBetween(startDate, endDate);
    }
}
