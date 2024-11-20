package com.josefy.nnpda.infrastructure.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

// todo: this sucks, way too many heap allocations
public record Status(String message, HttpStatusCode code)
{
    public static Status SUCCESS = new Status("Success", HttpStatus.OK);

    public static Status ok(String message) {
        return new Status(message, HttpStatus.OK);
    }

    public ResponseEntity<?> toResponseEntity() {
        return ResponseEntity.status(code).body(message);
    }
}
