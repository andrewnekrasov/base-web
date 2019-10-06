package ru.ithex.baseweb.controller.advice;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ithex.baseweb.model.dto.response.ResponseWrapperDTO;
import ru.ithex.baseweb.model.dto.response.error.InternalServerError;

@RestControllerAdvice
public class BaseControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseWrapperDTO baseHandle(Exception e, HttpRequest request) {
        return ResponseWrapperDTO.error(new InternalServerError(e.getMessage()));
    }
}
