package com.josefy.nnpda.controller;

import com.josefy.nnpda.dto.measurement.DeviceMeasurementDto;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.service.IMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Measurements", description = "Manages the ingestion of sensor measurements sent by devices")
@Controller
@RequiredArgsConstructor
@RequestMapping("/devices/measurements")
@Slf4j
public class DeviceMeasurementController {
    private final IMeasurementService measurementService;

    @PostMapping
    @Operation(
            summary = "Ingest sensor measurements sent by a specific device",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = DeviceMeasurementDto.class))
            ),
            parameters = {
                    @Parameter(
                            name = "X-DERIVED-ID",
                            description = "Derived ID header, used to identify the device",
                            required = true,
                            schema = @Schema(type = "string")),
                    @Parameter(
                            name = "X-HMAC-SIG",
                            description = "HMAC signature header, used to verify the authenticity of the data",
                            required = true,
                            schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Measurements ingested successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data"
                    )
            }
    )
    public ResponseEntity<?> ingest(
            @RequestBody @Valid DeviceMeasurementDto measurements,
            @AuthenticationPrincipal Device sender) {
        log.info("Received measurements from device {}", sender.getSerialNumber());
        return measurementService.ingestMeasurements(sender, measurements).toResponseEntity();
    }
}
