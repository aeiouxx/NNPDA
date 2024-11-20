package com.josefy.nnpda.controller;

import com.josefy.nnpda.dto.sensor.SensorDto;
import com.josefy.nnpda.dto.sensor.SensorWithDeviceDto;
import com.josefy.nnpda.dto.sensor.SensorWithDeviceResponseDto;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.service.ISensorService;
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
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Tag(name = "Sensors", description = "Manages operations concerning the registered sensors.")
@Controller
@RequestMapping("/sensors")
@RequiredArgsConstructor
@Slf4j
public class SensorController {
    private final ISensorService sensorService;

    @GetMapping
    @Operation(
            summary = "Get all sensors",
            description = "Returns all registered sensors"
    )
    public ResponseEntity<?> getAll(
            @Parameter(description = "Include sensors in the response")
            @RequestParam(defaultValue = "false") boolean omitDevices,
            @AuthenticationPrincipal User user) {
        var sensors = sensorService.findAll(omitDevices);
        final Function<Sensor, ?> mapping = omitDevices
                ? SensorDto::fromEntity
                : SensorWithDeviceResponseDto::fromEntity;
        var result = StreamSupport.stream(sensors.spliterator(), false)
                        .map(mapping)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{serialNumber}")
    @Operation(
            summary = "Get sensor by serial number",
            description = "Returns sensor with specified serial number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SensorDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sensor with specified serial number not found"
                    )
            }
    )
    public ResponseEntity<?> getOne(
            @PathVariable String serialNumber,
            @AuthenticationPrincipal User user) {
        var sensor = sensorService.findBySerialNumber(serialNumber);
        return sensor.fold(Status::toResponseEntity,
                success -> ResponseEntity.ok(SensorDto.fromEntity(success)));
    }

    @PostMapping
    @Operation(
            summary = "Create sensor",
            description = "Creates a new sensor",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = SensorWithDeviceDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Sensor created",
                            content = @Content(schema = @Schema(implementation = SensorWithDeviceResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid sensor data"
                    )
            }
    )
    public ResponseEntity<?> create(@RequestBody @Valid SensorWithDeviceDto sensorDto,
                                    @AuthenticationPrincipal User user) {
        return sensorService.create(sensorDto)
                .fold(Status::toResponseEntity,
                        success -> ResponseEntity.status(201).body(
                                SensorWithDeviceResponseDto.fromEntity(success)
                        ));
    }

    @PutMapping("/{serialNumber}")
    @Operation(
            summary = "Update sensor",
            description = "Updates sensor with specified serial number",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = SensorWithDeviceDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sensor updated",
                            content = @Content(schema = @Schema(implementation = SensorWithDeviceResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid sensor data"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sensor with specified serial number not found"
                    )
            }
    )
    public ResponseEntity<?> update(
            @PathVariable String serialNumber,
            @RequestBody @Valid SensorWithDeviceDto sensorDto,
            @AuthenticationPrincipal User user) {
        return sensorService.update(serialNumber, sensorDto)
                .fold(Status::toResponseEntity,
                        success -> ResponseEntity.ok(SensorWithDeviceResponseDto.fromEntity(success)));
    }

    @DeleteMapping("/{serialNumber}")
    @Operation(
            summary = "Delete sensor",
            description = "Deletes sensor with specified serial number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sensor deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sensor with specified serial number not found"
                    )
            }
    )
    public ResponseEntity<?> delete(
            @PathVariable String serialNumber,
            @AuthenticationPrincipal User user) {
        return sensorService.delete(serialNumber).fold(Status::toResponseEntity,ResponseEntity::ok);
    }
}
