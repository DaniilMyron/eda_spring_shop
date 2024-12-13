package com.miron.carting.handler;

import com.miron.carting.exceptions.*;
import com.miron.security_lib.handler.BusinessErrorCodes;
import com.miron.security_lib.handler.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartingExceptionHandler {
    @ExceptionHandler({CartNotFoundException.class, UserNotFoundException.class, ProductNotFoundException.class, ProductInCartNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(CartingEntityNotFoundException exp) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ENTITY_NOT_FOUND.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ENTITY_NOT_FOUND.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }
}
