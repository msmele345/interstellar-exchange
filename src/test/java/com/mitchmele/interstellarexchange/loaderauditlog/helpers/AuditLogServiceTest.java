package com.mitchmele.interstellarexchange.loaderauditlog.helpers;

import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

class AuditLogServiceTest {

    @Mock
    private LoaderAuditLogRepository loaderAuditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @Test
    void getLastLoadSize_returnsLastCountOfTransferredTrades() {

        LoaderAuditLog loaderAuditLog = LoaderAuditLog.builder().loadCount(4).createdAt(new Date(30)).build();
        LoaderAuditLog loaderAuditLog2 = LoaderAuditLog.builder().loadCount(6).createdAt(new Date(50)).build();


//        when(loaderAuditLogRepository.findAll()).thenReturn(asList())
    }
}