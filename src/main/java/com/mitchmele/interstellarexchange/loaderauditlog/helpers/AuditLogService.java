package com.mitchmele.interstellarexchange.loaderauditlog.helpers;

import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogService {

    private final LoaderAuditLogRepository loaderAuditLogRepository;

    public List<LoaderAuditLog> findAllStatuses() {
        return loaderAuditLogRepository.findAll();
    }

    public void save(LoaderAuditLog loaderAuditLog) {
        loaderAuditLogRepository.save(loaderAuditLog);
    }
}
