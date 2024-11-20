package com.josefy.nnpda.controller;


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

@Tag(name = "User Sensors", description = "Operations with sensors of devices assigned to user.")
@Controller
@RequestMapping("{username}/sensors")
@RequiredArgsConstructor
@Slf4j
public class UserSensorController {
    private final IUserDeviceService userDeviceService;

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/{serialNumber}")
    public ResponseEntity<?> getAssigned(@PathVariable String username,
                                         @PathVariable String serialNumber,
                                         @AuthenticationPrincipal User user) {
        log.info("User {} requested sensors for device {}", username, serialNumber);
        var sensors = userDeviceService.findSensorsByUserDevice(username, serialNumber);
        log.info("User {} has {} sensors for device {}", username, sensors.size(), serialNumber);
        return ResponseEntity.ok( sensors );
    }
}
