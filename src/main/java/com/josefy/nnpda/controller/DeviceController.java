package com.josefy.nnpda.controller;

import com.josefy.nnpda.dto.device.CreateDeviceWithSensorSerialsDto;
import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.dto.device.DeviceWithSensorSerialsDto;
import com.josefy.nnpda.dto.device.DeviceWithSensorsDto;
import com.josefy.nnpda.infrastructure.security.RoleExpressions;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.service.IDeviceService;
import com.josefy.nnpda.annotation.SerialNumber;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Tag(name = "Devices", description = "Manages operations concerning the registered devices.")
@Controller
@RequestMapping("/devices")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize(RoleExpressions.IS_ADMIN)
public class DeviceController {
    private final IDeviceService deviceService;
    @GetMapping
    @Operation(
            summary = "Get all devices",
            description = "Returns all registered devices",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of devices, " +
                                    "if `withSensors` is `true`, each device also includes information about its " +
                                    "sensors.",
                            content = @Content(
                                    schema = @Schema(
                                            type = "array",
                                            oneOf = {
                                                    DeviceWithSensorsDto[].class,
                                                    DeviceDto[].class
                                            }
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> getAll(
            @Parameter(description = "Include sensors in the response")
            @RequestParam(defaultValue = "false") boolean withSensors,
            @AuthenticationPrincipal User user) {
        var devices = deviceService.findAll(withSensors);
        final Function<Device, ?> mapping = withSensors
                ? DeviceWithSensorsDto::fromEntity
                : DeviceDto::fromEntity;
        return ResponseEntity.ok(StreamSupport.stream(devices.spliterator(), false)
                .map(mapping)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{serialNumber}")
    @Operation(
            summary = "Get a device by serial number",
            description = "Returns a device by its serial number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Device data, " +
                                    "if `withSensors` is `true`, device includes its sensors.",
                            content = @Content(
                                    schema = @Schema(
                                            oneOf = {DeviceWithSensorsDto.class, DeviceDto.class}
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Device not found"
                    )
            }
    )
    public ResponseEntity<?> getOne(
            @Parameter(description = "Include sensors in the response")
            @RequestParam(defaultValue = "false") boolean withSensors,
            @PathVariable @Valid @SerialNumber String serialNumber,
            @AuthenticationPrincipal User user) {
        var device = deviceService.findBySerialNumber(serialNumber, withSensors);
        final Function<Device, ?> mapping = withSensors
                ? DeviceWithSensorsDto::fromEntity
                : DeviceDto::fromEntity;
        return device.fold(
                Status::toResponseEntity,
                d -> ResponseEntity.ok(mapping.apply(d)));
    }

    @PostMapping
    @Operation(
            summary = "Create a new device",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Device data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateDeviceWithSensorSerialsDto.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = DeviceWithSensorsDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Device not found"
                    )
            }
    )
    public ResponseEntity<?> create(
            @RequestBody @Valid CreateDeviceWithSensorSerialsDto device,
            @AuthenticationPrincipal User user) {
        return deviceService.create(device)
                .fold(Status::toResponseEntity,
                      success -> ResponseEntity.status(201).body(DeviceWithSensorsDto.fromEntity(success)));
    }

    @PutMapping("/{serialNumber}")
    @Operation(
            summary = "Update a device",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Device data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateDeviceWithSensorSerialsDto.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = DeviceWithSensorsDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Device not found"
                    )
            }
    )
    public ResponseEntity<?> update(
            @PathVariable @Valid @SerialNumber String serialNumber,
            @RequestBody @Valid CreateDeviceWithSensorSerialsDto device,
            @AuthenticationPrincipal User user) {
        return deviceService.update(serialNumber, device)
                .fold(Status::toResponseEntity,
                      success -> ResponseEntity.ok(DeviceWithSensorsDto.fromEntity(success)));
    }

    @DeleteMapping("/{serialNumber}")
    @Operation(
            summary = "Delete a device",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Device deleted"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Device not found"
                    )
            }
    )
    public ResponseEntity<?> delete(
            @PathVariable @Valid @SerialNumber String serialNumber,
            @AuthenticationPrincipal User user) {
        return deviceService.delete(serialNumber).fold(Status::toResponseEntity, ResponseEntity::ok);
    }
}
