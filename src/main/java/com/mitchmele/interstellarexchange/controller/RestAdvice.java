package com.mitchmele.interstellarexchange.controller;

import com.mitchmele.interstellarexchange.common.InvalidDateRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RestAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ InvalidDateRequestException.class })
    public ErrorResponse handleBadRequest(InvalidDateRequestException e) {
        return defaultHandler(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ EntityNotFoundException.class })
    public ErrorResponse handleNotFoundException(EntityNotFoundException e) {
        return defaultHandler(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ Exception.class })
    public ErrorResponse handleAllExceptions(Exception e) {
        return defaultHandler(e);
    }

    private ErrorResponse defaultHandler(Throwable t) {
        return ErrorResponse.builder()
                .exceptionMsg("There was an error processing the request")
                .exception(t.getClass().getSimpleName())
                .build();
    }
}
