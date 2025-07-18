package com.urban.atiperatask.handler;


import com.urban.atiperatask.excetion.GithubUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(GithubUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(GithubUserNotFoundException e) {
        var body = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMsg());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }
}
