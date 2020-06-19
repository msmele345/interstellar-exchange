package com.mitchmele.interstellarexchange.errorlogs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.interstellarexchange.common.ErrorType;
import com.mitchmele.interstellarexchange.errorlogs.repository.ErrorLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorLogServiceTest {

    @Mock
    private ErrorLogRepository errorLogRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private ErrorLogService errorLogService;

    @Test
    public void saveError_callsRepository() {
        ErrorLogEntity incomingError = ErrorLogEntity.builder()
                .errorType(ErrorType.PROCESSING.value)
                .errorMessage("Failed to process")
                .build();
        errorLogService.saveError(incomingError);
        verify(errorLogRepository).save(incomingError);
    }

    @Test
    public void saveError_sendsToJmsErrorQueue_afterSavingEntity() throws JsonProcessingException {
        ErrorLogEntity incomingError = ErrorLogEntity.builder()
                .id(1L)
                .errorType(ErrorType.PROCESSING.value)
                .errorMessage("Failed to process")
                .build();

        when(errorLogRepository.save(any())).thenReturn(incomingError);

        ObjectMapper mapper = new ObjectMapper();
        String convertedErrorForQueue = mapper.writeValueAsString(incomingError);

        errorLogService.saveError(incomingError);
        verify(jmsTemplate).convertAndSend("errors", convertedErrorForQueue);
    }

    @Test
    public void saveError_throwsRuntimeExceptionIfSaveFails() {
        ErrorLogEntity incomingError = ErrorLogEntity.builder()
                .id(1L)
                .errorType(ErrorType.PROCESSING.value)
                .errorMessage("Failed to process")
                .build();

        when(errorLogRepository.save(any())).thenThrow(new RuntimeException("bad news"));

        assertThatThrownBy(() -> errorLogService.saveError(incomingError))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("bad news");
    }

    @Test
    public void saveError_throwsRuntimeExceptionIfSendToErrorQueueFails() {
        ErrorLogEntity incomingError = ErrorLogEntity.builder()
                .id(1L)
                .errorType(ErrorType.PROCESSING.value)
                .errorMessage("Failed to process")
                .build();

        when(errorLogRepository.save(any())).thenReturn(incomingError);

        doThrow(new RuntimeException("routing failed")).when(jmsTemplate).convertAndSend(anyString(), any(Object.class));

        assertThatThrownBy(() -> errorLogService.saveError(incomingError))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("routing failed");
    }
}