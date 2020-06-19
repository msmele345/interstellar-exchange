package com.mitchmele.interstellarexchange.services;

import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import com.mitchmele.interstellarexchange.errorlogs.ErrorLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JmsErrorHandlerTest {

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private JmsErrorHandler jmsErrorHandler;

    @Test
    public void handleError_callsErrorLogServiceWithNewErrorLogEntity() {
        Exception error = new Exception("some error");

        ErrorLogEntity expectedErrorEntity = ErrorLogEntity.builder()
                .errorMessage("some error")
                .errorType("ROUTING")
                .serviceLocation("JMS")
                .build();

        jmsErrorHandler.handleError(error);
        verify(errorLogService).saveError(expectedErrorEntity);
    }
}