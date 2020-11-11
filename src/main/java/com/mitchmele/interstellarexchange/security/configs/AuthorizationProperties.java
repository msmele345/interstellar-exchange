package com.mitchmele.interstellarexchange.security.configs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@ConfigurationProperties(prefix = "authorization")
public class AuthorizationProperties {



}
