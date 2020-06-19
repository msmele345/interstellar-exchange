package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.common.ServiceLocation;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class JmsErrorHandler implements ErrorHandler {

    private final ErrorLogService errorLogService;

    @Override
    public void handleError(Throwable t) {
        log.info("ERROR HANDLER RECEIVED ERROR: " + t);
        ErrorLogEntity errorLogEntity = ErrorLogEntity.builder()
                .errorMessage(t.getLocalizedMessage())
                .errorType(ErrorType.ROUTING.value)
                .serviceLocation(ServiceLocation.JMS.value)
                .build();

        errorLogService.saveError(errorLogEntity);
    }
}


