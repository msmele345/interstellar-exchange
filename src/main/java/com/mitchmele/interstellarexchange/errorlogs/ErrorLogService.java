package com.mitchmele.interstellarexchange.errorlogs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.interstellarexchange.errorlogs.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final JmsTemplate jmsTemplate;

    @Value("${destination.jms.errors}")
    private String errors;

    public void saveError(ErrorLogEntity errorLogEntity) {
        ErrorLogEntity error = null;
        try {
            error = errorLogRepository.save(errorLogEntity);
            log.info("SUCCESSFULLY SAVED ERROR ENTITY WITH MESSAGE: " + errorLogEntity.getErrorMessage());
            jmsTemplate.convertAndSend("errors", Objects.requireNonNull(serializeError(error)));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private String serializeError(ErrorLogEntity errorLogEntity) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(errorLogEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
