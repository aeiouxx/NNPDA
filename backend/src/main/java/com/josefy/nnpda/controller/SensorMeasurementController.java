package com.josefy.nnpda.controller;

import com.josefy.nnpda.service.IMeasurementService;
import com.josefy.nnpda.service.impl.MeasurementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Sensor measurements", description = "Manages the fetching of sensor measurements")
@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("#username == authentication.principal.username")
public class SensorMeasurementController {
    private final IMeasurementService measurementService;



}
