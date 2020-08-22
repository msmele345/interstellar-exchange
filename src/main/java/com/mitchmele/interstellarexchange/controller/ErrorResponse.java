package com.mitchmele.interstellarexchange.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private String exceptionMsg;
    private String exception;
}
