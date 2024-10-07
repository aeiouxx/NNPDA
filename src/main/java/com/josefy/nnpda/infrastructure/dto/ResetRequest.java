package com.josefy.nnpda.infrastructure.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record ResetRequest(@Schema(description = "Username", example = "User") String username) {}
