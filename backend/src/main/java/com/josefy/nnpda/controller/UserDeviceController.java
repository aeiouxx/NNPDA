package com.josefy.nnpda.controller;

import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.infrastructure.security.RoleExpressions;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.service.IUserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User Devices", description = "Manages operations concerning the user devices.")
@Controller
@RequestMapping("assign/{username}/devices")
@RequiredArgsConstructor
@PreAuthorize(RoleExpressions.IS_ADMIN)
public class UserDeviceController {
    private final IUserDeviceService userDeviceService;

    @PostMapping("/{serialNumber}")
    @Operation(
            summary = "Assign device to user",
            description = "Assign device to user by serial number.",
            responses = {
                    @ApiResponse(
                            description = "Device assigned to user successfully.",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = DeviceDto.class))),
                    @ApiResponse(
                            description = "Device assignment failed because of invalid authorization.",
                            responseCode = "403"),
                    @ApiResponse(
                            description = "Device assignment failed because of invalid device.",
                            responseCode = "404"),
                    @ApiResponse(
                            description = "Device assignment failed because of device already assigned.",
                            responseCode = "409")
            }
    )
    public ResponseEntity<?> assignDeviceToUser(
            @PathVariable String username,
            @PathVariable String serialNumber,
            @AuthenticationPrincipal User user)
    {
        var result = userDeviceService.assignDeviceToUser(serialNumber, username);
        return result.fold(Status::toResponseEntity,
                success -> {
                    var response = DeviceDto.fromEntity(success.getDevice());
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                });
    }

    @DeleteMapping("/{serialNumber}")
    @Operation(
            summary = "Unassign device from user",
            description = "Unassign device from user by serial number.",
            responses = {
                    @ApiResponse(
                            description = "Device unassigned from user successfully.",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Device unassignment failed because of invalid authorization.",
                            responseCode = "403"),
                    @ApiResponse(
                            description = "Device unassignment failed because of invalid device.",
                            responseCode = "404"),
            }
    )
    public ResponseEntity<?> unassignDeviceFromUser(
            @PathVariable String username,
            @PathVariable String serialNumber,
            @AuthenticationPrincipal User user)
    {
        return userDeviceService.unassignDeviceFromUser(serialNumber, username)
                .fold(Status::toResponseEntity, ResponseEntity::ok);
    }
}
