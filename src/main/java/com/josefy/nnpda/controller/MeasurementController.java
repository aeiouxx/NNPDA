package com.josefy.nnpda.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Measurements", description = "Manages ingestion and retrieval of measurements.")
@Controller
@RequestMapping("/measurements")
@RequiredArgsConstructor
@Slf4j
public class MeasurementController {
}
