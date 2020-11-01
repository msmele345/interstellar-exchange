package com.mitchmele.interstellarexchange.errorlogs;

import com.mitchmele.interstellarexchange.errorlogs.repository.ErrorLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
class ErrorLogServiceTestIT {

    @Autowired
    ErrorLogRepository errorLogRepository;

    @Autowired
    ErrorLogService errorLogService;

    @BeforeEach
    void setUp() {
        errorLogRepository.deleteAll();
    }

    @Test
    public void contextLoads() { }

    @Test
    public void saveError_serviceTakesIncomingEntityAndSavesToRepo() {
        ErrorLogEntity expectedErrorEntity = ErrorLogEntity.builder()
                .errorMessage("some error")
                .errorType("routing")
                .serviceLocation("jms")
                .build();

        errorLogService.saveError(expectedErrorEntity);
        //setup queueBrowser to test errorQueue?
        List<ErrorLogEntity> actualErrorInDb = errorLogRepository.findAll();
        assertThat(actualErrorInDb.get(0)).isEqualToIgnoringGivenFields(expectedErrorEntity, "timeStamp");
    }
}