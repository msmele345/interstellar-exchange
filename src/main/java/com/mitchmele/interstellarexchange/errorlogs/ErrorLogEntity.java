package com.mitchmele.interstellarexchange.errorlogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ERROR_ENTITY")
public class ErrorLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ERROR_LOG_ENTITY_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "ERROR_TYPE")
    private String errorType;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "SERVICE_LOCATION")
    private String serviceLocation;

    @Column(name = "CREATED_TS", updatable = false)
    @CreationTimestamp
    private Date timeStamp;
}
