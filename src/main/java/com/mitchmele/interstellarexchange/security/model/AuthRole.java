package com.mitchmele.interstellarexchange.security.model;

import com.mitchmele.interstellarexchange.exchangeuser.ExchangeUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AUTH_ROLE")
public class AuthRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToMany(mappedBy = "roles")
    private Set<ExchangeUser> exchangeUsers;

    //setup joins
    //user has many roles
}