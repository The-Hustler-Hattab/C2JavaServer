package com.mtattab.c2cServer.controller.rest;

import com.mtattab.c2cServer.model.json.RestOutputModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@Order(0)
public class GlobalExceptionRestController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestOutputModel> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        // Construct a concise error message
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed.");

        // Create an ErrorResponse object
        RestOutputModel errorResponse = new RestOutputModel();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMsg(errorMessage);

        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<RestOutputModel> exceptionHandler(Exception exception){
        exception.printStackTrace();
        RestOutputModel response = new RestOutputModel();

        response.setMsg( exception.getMessage());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
