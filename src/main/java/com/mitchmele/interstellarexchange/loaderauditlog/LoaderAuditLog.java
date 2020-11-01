package com.mitchmele.interstellarexchange.loaderauditlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LOADER_AUDIT_LOG")
public class LoaderAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "integer default 0")
    private Integer loadCount;

    @Column(name = "CREATED_TS", updatable = false)
    @CreationTimestamp
    private Date createdAt;
}
