package com.mitchmele.interstellarexchange.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class QuoteUpdateResult {

    private boolean isSuccess;
    private String lazyMessage;
}
