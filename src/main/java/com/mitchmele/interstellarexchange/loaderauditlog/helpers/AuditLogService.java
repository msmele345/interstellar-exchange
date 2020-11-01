package com.mitchmele.interstellarexchange.loaderauditlog.helpers;

import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import com.mitchmele.interstellarexchange.loaderauditlog.repository.LoaderAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogService {

    private final LoaderAuditLogRepository loaderAuditLogRepository;

//    public LoaderAuditLog getLastLoadSize() {
//
//    }
}
