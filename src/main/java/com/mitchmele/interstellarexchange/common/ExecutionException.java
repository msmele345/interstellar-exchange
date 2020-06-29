package com.mitchmele.interstellarexchange.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionException extends RuntimeException {

    String message;
    List<MarketExecutionError> errors = new ArrayList<>();

    public ExecutionException(MarketExecutionError...error) {
        this.errors.addAll(Arrays.stream(error).collect(Collectors.toList()));
        this.message = errors.stream()
                .map(e -> String.format("Error Type: %s, At Location: %s,  With Message: %s", e.errorType, e.serviceLocation, e.message))
                .collect(Collectors.joining());

    }

    @Override
    public String getMessage() {
        return message;
    }
}
