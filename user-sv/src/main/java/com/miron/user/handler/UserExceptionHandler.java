package com.miron.user.handler;

import com.miron.security_lib.handler.BusinessErrorCodes;
import com.miron.security_lib.handler.ExceptionResponse;
import com.miron.security_lib.handler.ServiceEntityNotFoundException;
import com.miron.user.exceptions.UserNotFoundException;
import com.miron.user.exceptions.UserRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(ServiceEntityNotFoundException exp) {
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

    @ExceptionHandler(UserRegisteredException.class)
    public ResponseEntity<ExceptionResponse> handleUserRegisteredException(UserRegisteredException exp) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                                .businessErrorDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }
}
