package com.josefy.nnpda.controller;

import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.service.IUserDeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Tag(name = "User Devices", description = "Operations with devices assigned to user.")
@Controller
@RequestMapping("{username}/devices")
@RequiredArgsConstructor
@Slf4j
public class UserDeviceController {
    private final IUserDeviceService userDeviceService;

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAssigned(@PathVariable String username,
                                         @AuthenticationPrincipal User user) {
        log.info("User {} requested devices", username);
        var devices = userDeviceService.findAssignedDevicesByUser(username);
        log.info("User {} has {} devices", username, devices.size());
        return ResponseEntity.ok(StreamSupport.stream(devices.spliterator(), false)
                .map(DeviceDto::fromEntity)
                .collect(Collectors.toList()));
    }
}
