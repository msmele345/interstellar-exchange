package com.mitchmele.interstellarexchange.loaderauditlog.helpers;

import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private LoaderAuditLogRepository loaderAuditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    void getLastLoadSize_returnsLastCountOfTransferredTrades() {

        LoaderAuditLog loaderAuditLog = LoaderAuditLog.builder().loadCount(4).createdAt(new Date(30)).build();
        LoaderAuditLog loaderAuditLog2 = LoaderAuditLog.builder().loadCount(6).createdAt(new Date(50)).build();


        when(loaderAuditLogRepository.findAll()).thenReturn(asList(loaderAuditLog, loaderAuditLog2));

        List<LoaderAuditLog> actual = auditLogService.findAllStatuses();

        assertThat(actual).containsExactlyInAnyOrderElementsOf(asList(loaderAuditLog, loaderAuditLog2));

        verify(loaderAuditLogRepository).findAll();
    }

    @Test
    void save_persistsGiveAuditLog() {

        LoaderAuditLog loaderAuditLog = LoaderAuditLog.builder().loadCount(4).createdAt(new Date(30)).build();

        when(loaderAuditLogRepository.save(any()))
                .thenReturn(loaderAuditLog);

        auditLogService.save(loaderAuditLog);

        verify(loaderAuditLogRepository).save(loaderAuditLog);
    }
}