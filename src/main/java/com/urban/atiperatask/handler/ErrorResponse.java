package com.urban.atiperatask.handler;


public record ErrorResponse(
        int status,
        String message
) {
}
