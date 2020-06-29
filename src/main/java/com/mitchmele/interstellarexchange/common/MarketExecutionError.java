package com.mitchmele.interstellarexchange.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketExecutionError {

    Throwable throwable;
    String message;
    ServiceLocation serviceLocation;
    ErrorType errorType;
}
