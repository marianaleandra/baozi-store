package br.com.baozistore.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrosApi {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarConflito(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "A operação conflita com um vínculo entre pedidos, clientes e produtos.");
    }
}
