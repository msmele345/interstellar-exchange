package com.mitchmele.interstellarexchange.errorlogs.repository;

import com.mitchmele.interstellarexchange.errorlogs.ErrorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLogEntity, Long> {
}
