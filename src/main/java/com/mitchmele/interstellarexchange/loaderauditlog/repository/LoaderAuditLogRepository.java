package com.mitchmele.interstellarexchange.loaderauditlog.repository;

import com.mitchmele.interstellarexchange.loaderauditlog.LoaderAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaderAuditLogRepository extends JpaRepository<LoaderAuditLog, Integer> {

    LoaderAuditLog findFirstByOrderByCreatedAtDesc();

}
