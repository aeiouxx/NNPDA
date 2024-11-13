package com.josefy.nnpda.infrastructure.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

// todo: this sucks, way too many heap allocations
public record Status(String message, HttpStatusCode code)
{
    public ResponseEntity<?> toResponseEntity() {
        return ResponseEntity.status(code).body(message);
    }
}
